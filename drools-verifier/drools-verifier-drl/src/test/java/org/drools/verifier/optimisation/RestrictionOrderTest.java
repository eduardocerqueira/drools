package org.drools.verifier.optimisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.verifier.TestBaseOld;
import org.drools.verifier.components.Restriction;
import org.drools.verifier.components.RuleComponent;
import org.drools.verifier.data.VerifierReport;
import org.drools.verifier.data.VerifierReportFactory;
import org.drools.verifier.report.components.Severity;
import org.drools.verifier.report.components.VerifierMessage;
import org.drools.verifier.report.components.VerifierMessageBase;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RestrictionOrderTest extends TestBaseOld {

    @Test
    void testRestrictionOrderInsideOperator() throws Exception {
        KieSession session = getStatelessKieSession(this.getClass().getResourceAsStream("RestrictionOrder.drl"));

        VerifierReport result = VerifierReportFactory.newVerifierReport();
        Collection<? extends Object> testData = getTestData(this.getClass().getResourceAsStream("OptimisationRestrictionOrderTest.drl"),
                result.getVerifierData());

        //        for ( Object object : testData ) {
        //            if ( object instanceof SubPattern ) {
        //                SubPattern s = (SubPattern) object;
        //                System.out.println( " - " + s );
        //                for ( PatternComponent o : s.getItems() ) {
        //                    System.out.println( " -- " + o + " : " + o.getOrderNumber() );
        //                }
        //            }
        //        }

        session.setGlobal("result",
                result);

        for (Object o : testData) {
            session.insert(o);
        }
        session.fireAllRules(new RuleNameMatchesAgendaFilter("Optimise restrictions inside operator"));

        Iterator<VerifierMessageBase> iter = result.getBySeverity(Severity.NOTE).iterator();

        Collection<String> ruleNames = new ArrayList<String>();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof VerifierMessage) {
                String name = ((VerifierMessage) o).getCauses().toArray(new Restriction[2])[0].getRuleName();

                ruleNames.add(name);
            }
        }

        assertThat(ruleNames.remove("Wrong descr order 1")).isTrue();

        if (!ruleNames.isEmpty()) {
            for (String string : ruleNames) {
                fail("Rule " + string + " caused an error.");
            }
        }
    }

    @Test
    void testPredicateOrderInsideOperator() throws Exception {
        KieSession session = getStatelessKieSession(this.getClass().getResourceAsStream("RestrictionOrder.drl"));

        VerifierReport result = VerifierReportFactory.newVerifierReport();
        Collection<? extends Object> testData = getTestData(this.getClass().getResourceAsStream("OptimisationRestrictionOrderTest.drl"),
                result.getVerifierData());

        session.setGlobal("result",
                result);

        for (Object o : testData) {
            session.insert(o);
        }
        session.fireAllRules(new RuleNameMatchesAgendaFilter("Optimise predicates inside operator"));

        Iterator<VerifierMessageBase> iter = result.getBySeverity(Severity.NOTE).iterator();

        Collection<String> ruleNames = new ArrayList<String>();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof VerifierMessage) {
                String name = ((VerifierMessage) o).getCauses().toArray(new RuleComponent[2])[0].getRuleName();

                ruleNames.add(name);
            }
        }

        assertThat(ruleNames.remove("Wrong eval order 1")).isTrue();

        if (!ruleNames.isEmpty()) {
            for (String string : ruleNames) {
                fail("Rule " + string + " caused an error.");
            }
        }
    }
}
