package org.kie.dmn.core;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNMessageType;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class DMNMessagesAPITest {

    public static final Logger LOG = LoggerFactory.getLogger(DMNMessagesAPITest.class);

    @Test
    public void testAPIUsage() {
        // DROOLS-3335 Broken DMN resource should inhibit KJAR and report KieBuilder message
        final KieServices ks = KieServices.Factory.get();
        final KieContainer kieContainer = DMNRuntimeUtil.getKieContainerIgnoringErrors(ks.newReleaseId("org.kie", "dmn-test-" + UUID.randomUUID(), "1.0"),
                                                                                       ks.getResources().newClassPathResource("incomplete_expression.dmn", this.getClass()),
                                                                                       ks.getResources().newClassPathResource("duff.drl", this.getClass()));

        Results verify = kieContainer.verify();
        List<Message> kie_messages = verify.getMessages();
        kie_messages.forEach(m -> LOG.info("{}", m));
        assertThat(kie_messages).hasSize(3);
        assertThat(kie_messages.stream().filter(m -> m.getPath().equals("duff.drl")).count()).isEqualTo(2L);

        List<DMNMessage> dmnMessages = kie_messages.stream()
                                                   .filter(DMNMessage.class::isInstance)
                                                   .map(DMNMessage.class::cast)
                                                   .collect(Collectors.toList());
        assertThat(dmnMessages).hasSize(1);

        DMNMessage dmnMessage = dmnMessages.get(0);
        assertThat(dmnMessage.getSourceId()).isEqualTo("_c990c3b2-e322-4ef9-931d-79bcdac99686");
        assertThat(dmnMessage.getMessageType()).isEqualTo(DMNMessageType.ERR_COMPILING_FEEL);
        assertThat(dmnMessage.getPath()).isEqualTo("incomplete_expression.dmn");
    }

    @Test(expected = IllegalStateException.class)
    public void testAPIUsageSnippetForDocumentation() {
        KieServices ks = KieServices.Factory.get();
        
        ReleaseId releaseId = ks.newReleaseId("org.kie", "dmn-test-" + UUID.randomUUID(), "1.0");
        Resource dmnResource = ks.getResources().newClassPathResource("incomplete_expression.dmn", this.getClass());
        
        KieFileSystem kfs = ks.newKieFileSystem()
                              .generateAndWritePomXML(releaseId)
                              .write(dmnResource);
        KieBuilder kieBuilder = ks.newKieBuilder(kfs)
                                  .buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException(results.getMessages(Message.Level.ERROR).toString());
        }
    }
}
