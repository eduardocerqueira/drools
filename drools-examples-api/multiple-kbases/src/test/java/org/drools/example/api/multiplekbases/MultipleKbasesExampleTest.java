package org.drools.example.api.multiplekbases;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.time.impl.JDKTimerService;
import org.drools.core.time.impl.PseudoClockScheduler;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultipleKbasesExampleTest {

    @Test
    public void testSimpleKieBase() {
        List<Integer> list = useKieSession("ksession1");
        // no packages imported means import everything
        assertEquals(4, list.size());
        assertTrue( list.containsAll( asList(0, 1, 2, 3) ) );
    }

    @Test
    public void testKieBaseWithPackage() {
        List<Integer> list = useKieSession("ksession2");
        // import package org.some.pkg
        assertEquals(1, list.size());
        assertTrue(list.containsAll(List.of(1)));
    }

    @Test
    public void testKieBaseWithInclusion() {
        List<Integer> list = useKieSession("ksession3");
        // include ksession2 + import package org.some.pkg2
        assertEquals(2, list.size());
        assertTrue(list.containsAll(asList(1, 2)));
    }

    @Test
    public void testKieBaseWith2Packages() {
        List<Integer> list = useKieSession("ksession4");
        // import package org.some.pkg, org.other.pkg
        assertEquals(2, list.size());
        assertTrue( list.containsAll( asList(1, 3) ) );
    }

    @Test
    public void testKieBaseWithPackageAndTransitiveInclusion() {
        List<Integer> list = useKieSession("ksession5");
        // import package org.*
        assertEquals(3, list.size());
        assertTrue(list.containsAll(asList(1, 2, 3)));
    }

    @Test
    public void testKieBaseWithAllPackages() {
        List<Integer> list = useKieSession("ksession6");
        // import package org.some.*
        assertEquals(2, list.size());
        assertTrue(list.containsAll(asList(1, 2)));
    }

    private List<Integer> useKieSession(String name) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession(name);

        List<Integer> list = new ArrayList<Integer>();
        kSession.setGlobal("list", list);
        kSession.insert(1);
        kSession.fireAllRules();

        return list;
    }

    @Test
    public void testEditSessionModel() {
        String name = "ksession6";
        ClockTypeOption pseudoClock = ClockTypeOption.PSEUDO;
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.newKieClasspathContainer();

        KieSessionModel kieSessionModel = kContainer.getKieSessionModel(name);
        ClockTypeOption clockType = kieSessionModel.getClockType();

        // clockType realtime
        Assert.assertNotEquals(clockType, pseudoClock);

        // change model to pseudo
        kieSessionModel.setClockType(pseudoClock);
        Assert.assertEquals(kieSessionModel.getClockType(), pseudoClock);

        // new pseudo session
        KieSession kSession = kContainer.newKieSession(name);
        Assert.assertEquals(kSession.getSessionClock().getClass(), PseudoClockScheduler.class);
    }

    @Test
    public void testEditSessionModelAfterFirstCreatedKieSession() {
        String name = "ksession6";
        ClockTypeOption pseudoClock = ClockTypeOption.PSEUDO;
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.newKieClasspathContainer();

        KieSessionModel kieSessionModel = kContainer.getKieSessionModel(name);
        ClockTypeOption clockType = kieSessionModel.getClockType();

        // clockType realtime
        Assert.assertEquals(clockType, ClockTypeOption.REALTIME);
        Assert.assertNotEquals(clockType, pseudoClock);

        // session is realtime
        KieSession kSession = kContainer.newKieSession(name);
        Assert.assertEquals(kSession.getSessionClock().getClass(), JDKTimerService.class);

        // change model to pseudo
        kieSessionModel.setClockType(pseudoClock);

        // new session still realtime
        KieSession kSessionPseudo = kContainer.newKieSession(name);
        Assert.assertEquals(kSessionPseudo.getSessionClock().getClass(), PseudoClockScheduler.class);
    }
}