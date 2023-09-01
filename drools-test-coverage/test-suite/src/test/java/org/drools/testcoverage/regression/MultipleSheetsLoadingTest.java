package org.drools.testcoverage.regression;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.drools.compiler.builder.conf.DecisionTableConfigurationImpl;
import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.KieUtil;
import org.drools.testcoverage.common.util.TestParametersUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceConfiguration;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests loading decision tables from several worksheets in a XLS file.
 */
@RunWith(Parameterized.class)
public class MultipleSheetsLoadingTest {

    private final KieBaseTestConfiguration kieBaseTestConfiguration;

    public MultipleSheetsLoadingTest(final KieBaseTestConfiguration kieBaseTestConfiguration) {
        this.kieBaseTestConfiguration = kieBaseTestConfiguration;
    }

    @Parameterized.Parameters(name = "KieBase type={0}")
    public static Collection<Object[]> getParameters() {
        return TestParametersUtil.getKieBaseConfigurations();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleSheetsLoadingTest.class);

    private static final String XLS_EXTENSION = "drl.xls";
    private static final String XLS_FILE_NAME_NO_EXTENSION = "multiple-sheets";
    private static final String XLS_FILE_NAME = XLS_FILE_NAME_NO_EXTENSION + "." + XLS_EXTENSION;

    private static final String WORKSHEET_1_NAME = "first";
    private static final String WORKSHEET_2_NAME = "second";

    @Test
    public void test() {
        final KieBuilder kbuilder = this.buildResources();

        final Collection<Message> results = kbuilder.getResults().getMessages(Level.ERROR, Level.WARNING);
        if (results.size() > 0) {
            LOGGER.error(results.toString());
        }
        assertThat(results).as("Some errors/warnings found").isEmpty();

        final KieBase kbase = KieBaseUtil.getDefaultKieBaseFromKieBuilder(kbuilder);
        final StatelessKieSession ksession = kbase.newStatelessKieSession();

        final Set<String> resultSet = new HashSet<String>();
        ksession.execute((Object) resultSet);

        assertThat(resultSet.size()).as("Wrong number of rules was fired").isEqualTo(2);
        for (String ruleName : new String[] { "rule1", "rule2" }) {
            assertThat(resultSet.contains(ruleName)).as("Rule " + ruleName + " was not fired!").isTrue();
        }
    }

    private KieBuilder buildResources() {
        final Resource resourceXlsFirst = this.createResourceWithConfig(WORKSHEET_1_NAME);
        final Resource resourceXlsSecond = this.createResourceWithConfig(WORKSHEET_2_NAME);
        return KieUtil.getKieBuilderFromResources(kieBaseTestConfiguration, false, resourceXlsFirst, resourceXlsSecond);
    }

    private Resource createResourceWithConfig(final String worksheetName) {
        final Resource resourceXls =
                KieServices.Factory.get().getResources().newClassPathResource(XLS_FILE_NAME, getClass());
        resourceXls.setTargetPath(String.format("%s-%s.%s", XLS_FILE_NAME_NO_EXTENSION, worksheetName, XLS_EXTENSION));
        resourceXls.setConfiguration(this.createXLSResourceConfig(worksheetName));
        return resourceXls;
    }

    private ResourceConfiguration createXLSResourceConfig(final String worksheetName) {
        final DecisionTableConfiguration resourceConfig = new DecisionTableConfigurationImpl();
        resourceConfig.setInputType(DecisionTableInputType.XLS);
        resourceConfig.setWorksheetName(worksheetName);
        return resourceConfig;
    }
}
