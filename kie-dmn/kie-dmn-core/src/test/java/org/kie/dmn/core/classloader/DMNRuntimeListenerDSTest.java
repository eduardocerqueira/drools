package org.kie.dmn.core.classloader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionServiceEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateDecisionServiceEvent;
import org.kie.dmn.core.api.DMNFactory;
import org.kie.dmn.core.api.event.DefaultDMNRuntimeEventListener;
import org.kie.dmn.core.impl.DMNEventUtils;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class DMNRuntimeListenerDSTest {

    public static final Logger LOG = LoggerFactory.getLogger(DMNRuntimeListenerDSTest.class);

    public static class DSListener extends DefaultDMNRuntimeEventListener {

        private List<Map<String, Object>> invParams = new ArrayList<>();
        private List<Map<String, Object>> outputDecisionsResults = new ArrayList<>();

        @Override
        public void beforeEvaluateDecisionService(BeforeEvaluateDecisionServiceEvent event) {
            Map<String, Object> dsParams = DMNEventUtils.extractDSParameters(event);
            invParams.add(dsParams);
        }

        @Override
        public void afterEvaluateDecisionService(AfterEvaluateDecisionServiceEvent event) {
            Map<String, Object> outs = DMNEventUtils.extractDSOutputDecisionsValues(event);
            outputDecisionsResults.add(outs);
        }

        public List<Map<String, Object>> getInvParams() {
            return invParams;
        }

        public List<Map<String, Object>> getOutputDecisionsResults() {
            return outputDecisionsResults;
        }

    }

    @Test
    public void testWholeModel() {
        final DSListener listenerUT = new DSListener();

        final DMNRuntime runtime = DMNRuntimeUtil.createRuntime("helloDS.dmn", this.getClass());
        runtime.addListener(listenerUT);
        final DMNModel dmnModel = runtime.getModel("https://kiegroup.org/dmn/_4D937A56-2648-4AA8-A252-EBD405CFC6A8", "helloDS");
        assertThat(dmnModel).isNotNull();
        assertThat(dmnModel.hasErrors()).as(DMNRuntimeUtil.formatMessages(dmnModel.getMessages())).isFalse();

        final DMNContext context = DMNFactory.newContext();
        context.set("my name", "John Doe");

        final DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        LOG.debug("{}", dmnResult);
        assertThat(dmnResult.hasErrors()).as(DMNRuntimeUtil.formatMessages(dmnResult.getMessages())).isFalse();
        assertThat(dmnResult.getDecisionResultByName("greeting").getResult()).isEqualTo("Hello, John Doe");
        assertThat(dmnResult.getDecisionResultByName("hardcoded").getResult()).isEqualTo("Hello, hc");

        assertThat(listenerUT.getInvParams()).hasSize(1);

        Map<String, Object> expectedParameters = new LinkedHashMap<String, Object>();
        expectedParameters.put("my name", "hc");
        Map<String, Object> dsParams = listenerUT.getInvParams().get(0);
        assertThat(dsParams).containsAllEntriesOf(expectedParameters);

        assertThat(listenerUT.getOutputDecisionsResults()).hasSize(1);

        Map<String, Object> expectedOuts = new LinkedHashMap<String, Object>();
        expectedOuts.put("greeting", "Hello, hc");
        Map<String, Object> outs = listenerUT.getOutputDecisionsResults().get(0);
        assertThat(outs).containsAllEntriesOf(expectedOuts);
    }

    @Test
    public void testDS() {
        final DSListener listenerUT = new DSListener();

        final DMNRuntime runtime = DMNRuntimeUtil.createRuntime("helloDS.dmn", this.getClass());
        runtime.addListener(listenerUT);
        final DMNModel dmnModel = runtime.getModel("https://kiegroup.org/dmn/_4D937A56-2648-4AA8-A252-EBD405CFC6A8", "helloDS");
        assertThat(dmnModel).isNotNull();
        assertThat(dmnModel.hasErrors()).as(DMNRuntimeUtil.formatMessages(dmnModel.getMessages())).isFalse();

        final DMNContext context = DMNFactory.newContext();
        context.set("my name", "Alice");

        final DMNResult dmnResult = runtime.evaluateDecisionService(dmnModel, context, "myDS");
        LOG.debug("{}", dmnResult);
        assertThat(dmnResult.hasErrors()).as(DMNRuntimeUtil.formatMessages(dmnResult.getMessages())).isFalse();
        assertThat(dmnResult.getDecisionResultByName("greeting").getResult()).isEqualTo("Hello, Alice");

        assertThat(listenerUT.getInvParams()).hasSize(1);

        Map<String, Object> expectedParameters = new LinkedHashMap<String, Object>();
        expectedParameters.put("my name", "Alice");
        Map<String, Object> dsParams = listenerUT.getInvParams().get(0);
        assertThat(dsParams).containsAllEntriesOf(expectedParameters);

        assertThat(listenerUT.getOutputDecisionsResults()).hasSize(1);

        Map<String, Object> expectedOuts = new LinkedHashMap<String, Object>();
        expectedOuts.put("greeting", "Hello, Alice");
        Map<String, Object> outs = listenerUT.getOutputDecisionsResults().get(0);
        assertThat(outs).containsAllEntriesOf(expectedOuts);
    }
}