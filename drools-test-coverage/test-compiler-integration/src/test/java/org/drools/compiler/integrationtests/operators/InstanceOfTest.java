package org.drools.compiler.integrationtests.operators;

import java.util.Collection;

import org.drools.testcoverage.common.model.LongAddress;
import org.drools.testcoverage.common.model.Person;
import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.TestParametersUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class InstanceOfTest {

    private final KieBaseTestConfiguration kieBaseTestConfiguration;

    public InstanceOfTest(final KieBaseTestConfiguration kieBaseTestConfiguration) {
        this.kieBaseTestConfiguration = kieBaseTestConfiguration;
    }

    @Parameterized.Parameters(name = "KieBase type={0}")
    public static Collection<Object[]> getParameters() {
        return TestParametersUtil.getKieBaseCloudConfigurations(true);
    }

    @Test
    public void testInstanceof() {
        // JBRULES-3591
        final String drl = "import " + Person.class.getCanonicalName() + ";\n" +
                "import " + LongAddress.class.getCanonicalName() + ";\n" +
                "rule R1 when\n" +
                "   Person( address instanceof LongAddress )\n" +
                "then\n" +
                "end\n";

        final KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("instance-of-test",
                                                                         kieBaseTestConfiguration,
                                                                         drl);
        final KieSession ksession = kbase.newKieSession();
        try {
            final Person mark = new Person("mark");
            mark.setAddress(new LongAddress("uk"));
            ksession.insert(mark);

            assertThat(ksession.fireAllRules()).isEqualTo(1);
        } finally {
            ksession.dispose();
        }
    }

}
