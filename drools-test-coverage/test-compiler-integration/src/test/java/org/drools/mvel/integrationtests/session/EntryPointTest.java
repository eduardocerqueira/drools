package org.drools.mvel.integrationtests.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class EntryPointTest {

    private final KieBaseTestConfiguration kieBaseTestConfiguration;

    public EntryPointTest(final KieBaseTestConfiguration kieBaseTestConfiguration) {
        this.kieBaseTestConfiguration = kieBaseTestConfiguration;
    }

    @Parameterized.Parameters(name = "KieBase type={0}")
    public static Collection<Object[]> getParameters() {
        return TestParametersUtil.getKieBaseCloudConfigurations(true);
    }

    @Test
    public void testEntryPointWithVarIN() {
        final String str = "package org.drools.mvel.compiler.test;\n" +
                "\n" +
                "global java.util.List list;\n" +
                "\n" +
                "rule \"In\"\n" +
                "when\n" +
                "   $x : Integer()\n " +
                "then\n" +
                "   drools.getEntryPoint(\"inX\").insert( $x );\n" +
                "end\n" +
                "\n" +
                "rule \"Out\"\n" +
                "when\n" +
                "   $i : Integer() from entry-point \"inX\"\n" +
                "then\n" +
                "   list.add( $i );\n" +
                "end";

        KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("test", kieBaseTestConfiguration, str);
        final KieSession ksession = kbase.newKieSession();

        ksession.insert(10);

        final List res = new ArrayList();
        ksession.setGlobal("list", res);

        ksession.fireAllRules();
        ksession.dispose();
        assertThat(res.contains(10)).isTrue();
    }
}
