package org.drools.compiler.integrationtests.notms;

import java.util.Collection;
import java.util.List;

import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.KieUtil;
import org.drools.testcoverage.common.util.TestParametersUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.api.KieBase;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class NoTmsTest {
    private final KieBaseTestConfiguration kieBaseTestConfiguration;

    public NoTmsTest(final KieBaseTestConfiguration kieBaseTestConfiguration) {
        this.kieBaseTestConfiguration = kieBaseTestConfiguration;
    }

    @Parameterized.Parameters(name = "KieBase type={0}")
    public static Collection<Object[]> getParameters() {
        return TestParametersUtil.getKieBaseCloudConfigurations(true);
    }

    @Test
    public void testUnsupportedTms() {
        String drl =
                "package org.drools.test; \n" +
                "" +
                "rule A when\n" +
                " $x : Integer() \n" +
                "then\n" +
                " insertLogical( \"\" + $x ); \n" +
                "end\n" +
                "" +
                "rule B when\n" +
                " $x : String() \n" +
                "then\n" +
                "end";

        KieBuilder kieBuilder = KieUtil.getKieBuilderFromDrls(kieBaseTestConfiguration, false, drl);
        List<Message> errors = kieBuilder.getResults().getMessages(org.kie.api.builder.Message.Level.ERROR);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0).getText().contains("drools-tms")).isTrue();
    }

    @Test
    public void testPlainInsert() {
        String drl =
                "package org.drools.test; \n" +
                "" +
                "rule A when\n" +
                " $x : Integer() \n" +
                "then\n" +
                " insert( \"\" + $x ); \n" +
                "end\n" +
                "" +
                "rule B when\n" +
                " $x : String() \n" +
                "then\n" +
                "end";

        KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("accumulate-test", kieBaseTestConfiguration, drl);
        KieSession ksession  = kbase.newKieSession();

        ksession.insert(1);
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }
}
