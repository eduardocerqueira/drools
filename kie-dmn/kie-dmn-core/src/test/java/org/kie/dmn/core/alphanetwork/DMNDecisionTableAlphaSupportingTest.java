package org.kie.dmn.core.alphanetwork;

import java.math.BigDecimal;

import org.junit.Test;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.BaseInterpretedVsAlphaNetworkTest;
import org.kie.dmn.core.api.DMNFactory;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class DMNDecisionTableAlphaSupportingTest extends BaseInterpretedVsAlphaNetworkTest {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(DMNDecisionTableAlphaSupportingTest.class);

    public DMNDecisionTableAlphaSupportingTest(final boolean useAlphaNetwork ) {
        super( useAlphaNetwork );
    }

    @Test
    public void testSimpleDecision() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime("alphasupport.dmn", this.getClass());
        DMNModel dmnModel = runtime.getModel("http://www.trisotech.com/definitions/_c0cf6e20-0b43-43ce-9def-c759a5f86df2", "DMN Specification Chapter 11 Example Reduced");
        assertThat(dmnModel).isNotNull();

        final DMNContext context = runtime.newContext();
        context.set("Existing Customer", "s");
        context.set("Application Risk Score", new BigDecimal("123"));
        final DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        LOG.debug("{}", dmnResult);
        assertThat(dmnResult.getContext().get("Pre-bureau risk category table")).isEqualTo("LOW");
    }

    @Test
    public void testSimpleTableMultipleTests() {
        final DMNRuntime runtime = DMNRuntimeUtil.createRuntime("an-simpletable-multipletests.dmn", this.getClass());
        final DMNModel dmnModel = runtime.getModel("https://github.com/kiegroup/kie-dmn", "an-simpletable-multipletests");
        assertThat(dmnModel).isNotNull();

        final DMNContext context = DMNFactory.newContext();
        context.set("Age", 21);
        context.set("RiskCategory", "Low");
        context.set("isAffordable", true);
        final DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        final DMNContext result = dmnResult.getContext();
        assertThat(result.get("Approval Status")).isEqualTo("Approved");
    }
}
