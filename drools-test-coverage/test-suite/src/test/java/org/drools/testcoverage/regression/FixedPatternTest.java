package org.drools.testcoverage.regression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.KieUtil;
import org.drools.testcoverage.common.util.TestParametersUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for BZ 1150308.
 */
@RunWith(Parameterized.class)
public class FixedPatternTest {

    private KieSession ksession;

    private final KieBaseTestConfiguration kieBaseTestConfiguration;

    public FixedPatternTest(final KieBaseTestConfiguration kieBaseTestConfiguration) {
        this.kieBaseTestConfiguration = kieBaseTestConfiguration;
    }

    @Parameterized.Parameters(name = "KieBase type={0}")
    public static Collection<Object[]> getParameters() {
        return TestParametersUtil.getKieBaseConfigurations();
    }

    @After
    public void cleanup() {
        if (this.ksession != null) {
            this.ksession.dispose();
        }
    }

    /**
     * Tests fixed pattern without constraint in Decision table (BZ 1150308).
     */
    @Test
    public void testFixedPattern() {

        final Resource resource = KieServices.Factory.get().getResources().newClassPathResource("fixedPattern.drl.xls", getClass());
        final KieBuilder kbuilder = KieUtil.getKieBuilderFromResources(kieBaseTestConfiguration, true, resource);

        final KieSession ksession = KieBaseUtil.getDefaultKieBaseFromKieBuilder(kbuilder).newKieSession();

        final List<Long> list = new ArrayList<Long>();
        ksession.setGlobal("list", list);

        ksession.insert(1L);
        ksession.insert(2);
        ksession.fireAllRules();

        assertThat(list).hasSize(1);
        assertThat(list).first().isEqualTo(1L);
    }
}
