package org.kie.dmn.validation;

import java.util.List;

import org.junit.Test;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNMessageType;
import org.kie.dmn.core.DMNInputRuntimeTest;
import org.kie.dmn.core.decisionservices.DMNDecisionServicesTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_COMPILATION;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_MODEL;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_SCHEMA;

public class UsingResourceValidatorTest extends AbstractValidatorTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(UsingResourceValidatorTest.class);

    @Test
    public void testInvalidXml_single() {
        List<DMNMessage> validateXML = validator.validate( getResource( "invalidXml.dmn" ), VALIDATE_SCHEMA);
        assertThat(validateXML).as(ValidatorUtil.formatMessages(validateXML)).hasSize(1);
        assertThat(validateXML.get(0).getMessageType()).as(validateXML.get(0).toString()).isEqualTo(DMNMessageType.FAILED_XML_VALIDATION);
        assertThat(validateXML.get(0).getPath()).containsSequence("invalidXml.dmn");
    }
    
    @Test
    public void testInvalidXml_builder() {
        List<DMNMessage> validateXML = validator.validateUsing(VALIDATE_SCHEMA).theseModels(getResource( "invalidXml.dmn" ), getResource( "0001-input-data-string.dmn", DMNInputRuntimeTest.class ));
        assertThat(validateXML).as(ValidatorUtil.formatMessages(validateXML)).hasSize(1);
        assertThat(validateXML.get(0).getMessageType()).as(validateXML.get(0).toString()).isEqualTo(DMNMessageType.FAILED_XML_VALIDATION);
        assertThat(validateXML.get(0).getPath()).containsSequence("invalidXml.dmn");
    }
    
    @Test
    public void testFailingModelValidation_single() {
        final List<DMNMessage> messages = validator.validate( getResource("import/importingMyHelloDSbkmBoxedInvocation_wrongDoubleImportName.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL );
        LOG.debug("{}", messages);
        assertThat(messages.stream().anyMatch(p -> p.getPath().endsWith("importingMyHelloDSbkmBoxedInvocation_wrongDoubleImportName.dmn") && p.getText().contains("myHelloDS") && p.getMessageType().equals(DMNMessageType.DUPLICATE_NAME))).isTrue();
    }
    
    @Test
    public void testFailingModelValidation_builder() {
        final List<DMNMessage> messages = validator.validateUsing(VALIDATE_SCHEMA, VALIDATE_MODEL)
                                                   .theseModels(getResource("myHelloDS.dmn", DMNDecisionServicesTest.class),
                                                                getResource("import/importingMyHelloDSbkmBoxedInvocation_wrongDoubleImportName.dmn"));
        LOG.debug("{}", messages);
        assertThat(messages.stream().anyMatch(p -> p.getPath().endsWith("importingMyHelloDSbkmBoxedInvocation_wrongDoubleImportName.dmn") && p.getText().contains("myHelloDS") && p.getMessageType().equals(DMNMessageType.DUPLICATE_NAME))).isTrue();
    }
    
    @Test
    public void testFailingCompilation_single() {
        final List<DMNMessage> messages = validator.validate( getResource("invalidFEEL.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
        LOG.debug("{}", messages);
        assertThat(messages.stream().anyMatch(p -> p.getPath().endsWith("invalidFEEL.dmn") && p.getText().contains("Error compiling FEEL expression") && p.getMessageType().equals(DMNMessageType.ERR_COMPILING_FEEL))).isTrue();
    }
    
    @Test
    public void testFailingCompilation_builder() {
        final List<DMNMessage> messages = validator.validateUsing(VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION)
                .theseModels(getResource("invalidFEEL.dmn"));
        LOG.debug("{}", messages);
        assertThat(messages.stream().anyMatch(p -> p.getPath().endsWith("invalidFEEL.dmn") && p.getText().contains("Error compiling FEEL expression") && p.getMessageType().equals(DMNMessageType.ERR_COMPILING_FEEL))).isTrue();
    }

}
