package org.drools.compiler.integrationtests;

import java.util.Collection;

import org.drools.testcoverage.common.model.Person;
import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.TestParametersUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.api.KieBase;
import org.kie.api.conf.MBeansOption;
import org.kie.api.definition.rule.Rule;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AnnotationsCepTest {

    private final KieBaseTestConfiguration kieBaseTestConfiguration;

    public AnnotationsCepTest(final KieBaseTestConfiguration kieBaseTestConfiguration) {
        this.kieBaseTestConfiguration = kieBaseTestConfiguration;
    }

    @Parameterized.Parameters(name = "KieBase type={0}")
    public static Collection<Object[]> getParameters() {
        return TestParametersUtil.getKieBaseStreamConfigurations(true);
    }

    @Test
    public void testRuleAnnotation() {
        final String drl = "package org.drools.compiler.integrationtests\n" +
                "import " + Person.class.getCanonicalName() + "; \n" +
                "rule X\n" +
                "    @author(\"John Doe\")\n" +
                "    @output(Hello World!)\n" + // backward compatibility
                "    @value( 10 + 10 )\n" +
                "    @alt( \"Hello \"+\"World!\" )\n" +
                "when\n" +
                "    Person()\n" +
                "then\n" +
                "end";

        kieBaseTestConfiguration.setAdditionalKieBaseOptions(MBeansOption.ENABLED);
        final KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("annotations-cep-test", kieBaseTestConfiguration, drl);

        final Rule rule = kbase.getRule("org.drools.compiler.integrationtests",
                                        "X" );

        assertThat(rule.getMetaData().get("author")).isEqualTo("John Doe");
        assertThat(rule.getMetaData().get("output")).isEqualTo("Hello World!");
        assertThat(((Number) rule.getMetaData().get("value")).intValue()).isEqualTo(20);
        assertThat(rule.getMetaData().get("alt")).isEqualTo("Hello World!");

    }

    @Test
    public void testRuleAnnotation2() {
        final String drl = "package org.drools.compiler.integrationtests\n" +
                "import " + Person.class.getCanonicalName() + "; \n" +
                "rule X\n" +
                "    @alt(\" \\\"<- these are supposed to be the only quotes ->\\\" \")\n" +
                "when\n"+
                "    Person()\n" +
                "then\n" +
                "end";
        kieBaseTestConfiguration.setAdditionalKieBaseOptions(MBeansOption.ENABLED);
        final KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("annotations-cep-test", kieBaseTestConfiguration, drl);

        final Rule rule = kbase.getRule("org.drools.compiler.integrationtests",
                                        "X" );

        assertThat(rule.getMetaData().get("alt")).isEqualTo(" \"<- these are supposed to be the only quotes ->\" ");

    }

}
