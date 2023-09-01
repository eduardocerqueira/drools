package org.drools.core.concurrent;

import org.drools.core.common.ActivationsManager;
import org.drools.core.common.InternalAgendaGroup;
import org.drools.core.phreak.RuleAgendaItem;
import org.drools.core.rule.consequence.KnowledgeHelper;
import org.kie.api.runtime.rule.AgendaFilter;

public abstract class AbstractGroupEvaluator implements GroupEvaluator {
    protected final ActivationsManager activationsManager;

    private final boolean sequential;

    private KnowledgeHelper knowledgeHelper;

    private boolean haltEvaluation;

    public AbstractGroupEvaluator(ActivationsManager activationsManager) {
        this.activationsManager = activationsManager;
        this.sequential = activationsManager.getReteEvaluator().getKnowledgeBase().getRuleBaseConfiguration().isSequential();
        this.knowledgeHelper = newKnowledgeHelper();
    }

    public final int evaluateAndFire( InternalAgendaGroup group, AgendaFilter filter, int fireCount, int fireLimit ) {
        startEvaluation(group);
        RuleAgendaItem item = nextActivation(group);
        int loopFireCount = 0;
        while (item != null && !haltEvaluation && (fireLimit < 0 || (fireCount + loopFireCount) < fireLimit)) {
            activationsManager.evaluateQueriesForRule( item );
            loopFireCount += item.getRuleExecutor().evaluateNetworkAndFire(activationsManager, filter, fireCount, fireLimit);
            activationsManager.flushPropagations();
            item = nextActivation(group);
        }
        return loopFireCount;
    }

    private KnowledgeHelper newKnowledgeHelper() {
        return activationsManager.getReteEvaluator().createKnowledgeHelper();
    }

    private RuleAgendaItem nextActivation(InternalAgendaGroup group) {
        return sequential ? group.remove() : group.peek();
    }

    @Override
    public KnowledgeHelper getKnowledgeHelper() {
        return knowledgeHelper;
    }

    @Override
    public void resetKnowledgeHelper() {
        knowledgeHelper = newKnowledgeHelper();
    }

    @Override
    public void haltEvaluation() {
        haltEvaluation = true;
    }

    protected void startEvaluation(InternalAgendaGroup group) {
        haltEvaluation = false;
    }
}
