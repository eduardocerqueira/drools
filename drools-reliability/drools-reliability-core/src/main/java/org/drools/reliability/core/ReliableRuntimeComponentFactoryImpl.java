package org.drools.reliability.core;

import java.util.concurrent.atomic.AtomicLong;

import org.drools.base.RuleBase;
import org.drools.base.rule.accessor.GlobalResolver;
import org.drools.core.SessionConfiguration;
import org.drools.core.common.AgendaFactory;
import org.drools.core.common.EntryPointFactory;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.common.ReteEvaluator;
import org.drools.core.common.Storage;
import org.drools.core.time.TimerService;
import org.drools.kiesession.factory.RuntimeComponentFactoryImpl;
import org.drools.kiesession.factory.WorkingMemoryFactory;
import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.conf.PersistedSessionOption;

import static org.drools.reliability.core.ReliableSessionInitializer.initReliableSession;

public class ReliableRuntimeComponentFactoryImpl extends RuntimeComponentFactoryImpl {

    private static final AtomicLong RELIABLE_SESSIONS_COUNTER = new AtomicLong(0);
    private static final String NEXT_SESSION_ID = "nextSessionId";

    private final transient WorkingMemoryFactory wmFactory = ReliablePhreakWorkingMemoryFactory.getInstance();
    private final transient AgendaFactory agendaFactory = ReliableAgendaFactory.getInstance();

    public ReliableRuntimeComponentFactoryImpl() {
        refreshReliableSessionsCounterUsingStorage();
    }

    private static void refreshReliableSessionsCounterUsingStorage() {
        Storage<String, Long> sessionsCounter = StorageManagerFactory.get().getStorageManager().getOrCreateSharedStorage("sessionsCounter");
        if (sessionsCounter.containsKey(NEXT_SESSION_ID)) {
            RELIABLE_SESSIONS_COUNTER.set(sessionsCounter.get(NEXT_SESSION_ID));
        } else {
            sessionsCounter.put(NEXT_SESSION_ID, RELIABLE_SESSIONS_COUNTER.get());
        }
    }

    @Override
    public EntryPointFactory getEntryPointFactory() {
        return new ReliableNamedEntryPointFactory();
    }

    @Override
    public InternalWorkingMemory createStatefulSession(RuleBase ruleBase, Environment environment, SessionConfiguration sessionConfig, boolean fromPool) {
        if (!sessionConfig.hasPersistedSessionOption()) {
            return super.createStatefulSession(ruleBase, environment, sessionConfig, fromPool);
        }
        InternalKnowledgeBase kbase = (InternalKnowledgeBase) ruleBase;
        if (fromPool || kbase.getSessionPool() == null) {
            InternalWorkingMemory session = wmFactory.createWorkingMemory(RELIABLE_SESSIONS_COUNTER.getAndIncrement(), kbase, sessionConfig, environment);
            updateSessionsCounter();
            return internalInitSession(kbase, sessionConfig, session);
        }
        return (InternalWorkingMemory) kbase.getSessionPool().newKieSession(sessionConfig);
    }

    private void updateSessionsCounter() {
        Storage<String, Long> sessionsCounter = StorageManagerFactory.get().getStorageManager().getOrCreateSharedStorage("sessionsCounter");
        sessionsCounter.put(NEXT_SESSION_ID, RELIABLE_SESSIONS_COUNTER.get());
    }

    @Override
    public GlobalResolver createGlobalResolver(ReteEvaluator reteEvaluator, Environment environment) {
        if (!reteEvaluator.getSessionConfiguration().hasPersistedSessionOption()) {
            return super.createGlobalResolver(reteEvaluator, environment);
        }
        return ReliableGlobalResolverFactory.get().createReliableGlobalResolver(StorageManagerFactory.get().getStorageManager().getOrCreateStorageForSession(reteEvaluator, PersistedSessionOption.SafepointStrategy.ALWAYS, "globals"));
    }

    @Override
    public TimerService createTimerService(ReteEvaluator reteEvaluator) {
        if (!reteEvaluator.getSessionConfiguration().hasPersistedSessionOption()) {
            return super.createTimerService(reteEvaluator);
        }
        return new ReliablePseudoClockScheduler(StorageManagerFactory.get().getStorageManager().getOrCreateStorageForSession(reteEvaluator, PersistedSessionOption.SafepointStrategy.ALWAYS, "timer"));
    }

    private InternalWorkingMemory internalInitSession(InternalKnowledgeBase kbase, SessionConfiguration sessionConfig, InternalWorkingMemory session) {
        if (sessionConfig.isKeepReference()) {
            kbase.addStatefulSession(session);
        }
        return initReliableSession(sessionConfig, session);
    }

    @Override
    public AgendaFactory getAgendaFactory(SessionConfiguration sessionConfig) {
        if (!sessionConfig.hasPersistedSessionOption() || sessionConfig.getPersistedSessionOption().getPersistenceStrategy() == PersistedSessionOption.PersistenceStrategy.STORES_ONLY) {
            return super.getAgendaFactory(sessionConfig);
        }
        return agendaFactory;
    }

    // test purpose to completely reset the counter
    public static void resetCounter() {
        RELIABLE_SESSIONS_COUNTER.set(0);
    }

    // test purpose to simulate fail-over
    public static void refreshCounterUsingStorage() {
        refreshReliableSessionsCounterUsingStorage();
    }

    @Override
    public int servicePriority() {
        return 1;
    }
}
