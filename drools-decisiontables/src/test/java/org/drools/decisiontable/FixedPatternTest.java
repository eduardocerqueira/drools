package org.drools.decisiontable;

import java.util.ArrayList;
import java.util.List;

import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.drools.kiesession.rulebase.KnowledgeBaseFactory;
import org.junit.After;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class FixedPatternTest {

    private KieSession ksession;

    @After
    public void tearDown() {
        if (ksession != null) {
            ksession.dispose();
        }
    }
    
    @Test
    public void testFixedPattern() {

        DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dtconf.setInputType(DecisionTableInputType.XLS);
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("fixedPattern.drl.xls", getClass()), ResourceType.DTABLE, dtconf);
        if (kbuilder.hasErrors()) {
            fail( kbuilder.getErrors().toString() );
        }
        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addPackages(kbuilder.getKnowledgePackages());
        
        ksession = kbase.newKieSession();

        List<Long> list = new ArrayList<>();
        ksession.setGlobal("list", list);

        ksession.insert(1L);
        ksession.insert(2);
        
        ksession.fireAllRules();

        assertThat(list).hasSize(1).containsExactly(1L);
    }
}
