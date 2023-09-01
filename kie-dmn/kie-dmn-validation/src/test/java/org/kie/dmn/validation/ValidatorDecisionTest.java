package org.kie.dmn.validation;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.junit.Test;
import org.kie.api.builder.Message.Level;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNMessageType;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.DMNRuntimeTest;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.kie.dmn.model.api.Definitions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_COMPILATION;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_MODEL;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_SCHEMA;

public class ValidatorDecisionTest extends AbstractValidatorTest {

    @Test
    public void testDECISION_MISSING_EXPR_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_MISSING_EXPR.dmn")) {
            final List<DMNMessage> validate = validator.validate(
                    reader,
                    VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.get(0).getMessageType()).as(validate.get(0).toString()).isEqualTo(DMNMessageType.MISSING_EXPRESSION);
        }
    }

    @Test
    public void testDECISION_MISSING_EXPR_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_MISSING_EXPR.dmn"),
                VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.get(0).getMessageType()).as(validate.get(0).toString()).isEqualTo(DMNMessageType.MISSING_EXPRESSION);
    }

    @Test
    public void testDECISION_MISSING_EXPR_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_MISSING_EXPR.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_MISSING_EXPR"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.get(0).getMessageType()).as(validate.get(0).toString()).isEqualTo(DMNMessageType.MISSING_EXPRESSION);
    }

    @Test
    public void testDECISION_MISSING_VAR_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_MISSING_VAR.dmn")) {
            final List<DMNMessage> validate = validator.validate(
                    reader,
                    VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_VARIABLE))).isTrue();
        }
    }

    @Test
    public void testDECISION_MISSING_VAR_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_MISSING_VAR.dmn"),
                VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_VARIABLE))).isTrue();
    }

    @Test
    public void testDECISION_MISSING_VAR_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_MISSING_VAR.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_MISSING_VAR"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_VARIABLE))).isTrue();
    }

    @Test
    public void testDECISION_MISSING_VARbis_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_MISSING_VARbis.dmn")) {
            final List<DMNMessage> validate = validator.validate(reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_VARIABLE))).isTrue();
        }
    }

    @Test
    public void testDECISION_MISSING_VARbis_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_MISSING_VARbis.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_VARIABLE))).isTrue();
    }

    @Test
    public void testDECISION_MISSING_VARbis_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_MISSING_VARbis.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_MISSING_VARbis"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_VARIABLE))).isTrue();
    }

    @Test
    public void testDECISION_MISMATCH_VAR_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_MISMATCH_VAR.dmn")) {
            final List<DMNMessage> validate = validator.validate(reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.VARIABLE_NAME_MISMATCH))).isTrue();
        }
    }

    @Test
    public void testDECISION_MISMATCH_VAR_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_MISMATCH_VAR.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.VARIABLE_NAME_MISMATCH))).isTrue();
    }

    @Test
    public void testDECISION_MISMATCH_VAR_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_MISMATCH_VAR.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_MISSING_VAR"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.VARIABLE_NAME_MISMATCH))).isTrue();
    }

    @Test
    public void testDECISION_MULTIPLE_EXPRESSIONS_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_MULTIPLE_EXPRESSIONS.dmn")) {
            final List<DMNMessage> validate = validator.validate(reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.FAILED_XML_VALIDATION))).isTrue();
        }
    }

    @Test
    public void testDECISION_MULTIPLE_EXPRESSIONS_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_MULTIPLE_EXPRESSIONS.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.FAILED_XML_VALIDATION))).isTrue();
    }

    @Test
    public void testDECISION_MULTIPLE_EXPRESSIONS_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_MULTIPLE_EXPRESSIONS.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_MULTIPLE_EXPRESSIONS"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testDECISION_PERF_INDICATOR_WRONG_TYPE_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_PERF_INDICATOR_WRONG_TYPE.dmn")) {
            final List<DMNMessage> validate = validator.validate(reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }

    @Test
    public void testDECISION_PERF_INDICATOR_WRONG_TYPE_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_PERF_INDICATOR_WRONG_TYPE.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_PERF_INDICATOR_WRONG_TYPE_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_PERF_INDICATOR_WRONG_TYPE.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_PERF_INDICATOR_WRONG_TYPE"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_DECISION_MAKER_WRONG_TYPE_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_DECISION_MAKER_WRONG_TYPE.dmn")) {
            final List<DMNMessage> validate = validator.validate(reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }

    @Test
    public void testDECISION_DECISION_MAKER_WRONG_TYPE_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_DECISION_MAKER_WRONG_TYPE.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_DECISION_MAKER_WRONG_TYPE_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_DECISION_MAKER_WRONG_TYPE.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_DECISION_MAKER_WRONG_TYPE"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_DECISION_OWNER_WRONG_TYPE_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_DECISION_OWNER_WRONG_TYPE.dmn")) {
            final List<DMNMessage> validate = validator.validate(reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }

    @Test
    public void testDECISION_DECISION_OWNER_WRONG_TYPE_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile("decision/DECISION_DECISION_OWNER_WRONG_TYPE.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_DECISION_OWNER_WRONG_TYPE_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_DECISION_OWNER_WRONG_TYPE.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_DECISION_MAKER_WRONG_TYPE"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_CYCLIC_DEPENDENCY_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_CYCLIC_DEPENDENCY.dmn")) {
            final List<DMNMessage> validate = validator.validate( reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }

    @Test
    public void testDECISION_CYCLIC_DEPENDENCY_FileInput() {
        final List<DMNMessage> validate = validator.validate( getFile("decision/DECISION_CYCLIC_DEPENDENCY.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_CYCLIC_DEPENDENCY_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_CYCLIC_DEPENDENCY.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_CYCLIC_DEPENDENCY"),
                VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_CYCLIC_DEPENDENCY_SELF_REFERENCE_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_CYCLIC_DEPENDENCY_SELF_REFERENCE.dmn")) {
            final List<DMNMessage> validate = validator.validate( reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }

    @Test
    public void testDECISION_CYCLIC_DEPENDENCY_SELF_REFERENCE_FileInput() {
        final List<DMNMessage> validate = validator.validate( getFile("decision/DECISION_CYCLIC_DEPENDENCY_SELF_REFERENCE.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_CYCLIC_DEPENDENCY_SELF_REFERENCE_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_CYCLIC_DEPENDENCY_SELF_REFERENCE.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_CYCLIC_DEPENDENCY_SELF_REFERENCE"),
                VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_DEADLY_DIAMOND_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_DEADLY_DIAMOND.dmn")) {
            final List<DMNMessage> validate = validator.validate( reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
        }
    }

    @Test
    public void testDECISION_DEADLY_DIAMOND_FileInput() {
        final List<DMNMessage> validate = validator.validate( getFile("decision/DECISION_DEADLY_DIAMOND.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testDECISION_DEADLY_DIAMOND_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_DEADLY_DIAMOND.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_DEADLY_DIAMOND"),
                VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testDECISION_DEADLY_KITE_ReaderInput() throws IOException {
        try (final Reader reader = getReader("decision/DECISION_DEADLY_KITE.dmn")) {
            final List<DMNMessage> validate = validator.validate( reader, VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
        }
    }

    @Test
    public void testDECISION_DEADLY_KITE_FileInput() {
        final List<DMNMessage> validate = validator.validate( getFile("decision/DECISION_DEADLY_KITE.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testDECISION_DEADLY_KITE_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_DEADLY_KITE.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_DEADLY_KITE"),
                VALIDATE_MODEL, VALIDATE_COMPILATION );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testDECISION_MISSING_REQ_ReaderInput() throws IOException { // ELEMREF_MISSING
        try (final Reader reader = getReader("decision/DECISION_MISSING_REQ.dmn")) {
            final List<DMNMessage> validate = validator.validate(reader, VALIDATE_SCHEMA, VALIDATE_MODEL);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }
    
    @Test
    public void testDECISION_MISSING_REQ_FileInput() {
        final List<DMNMessage> validate = validator.validate( getFile("decision/DECISION_MISSING_REQ.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testDECISION_MISSING_REQ_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions("decision/DECISION_MISSING_REQ.dmn",
                               "https://github.com/kiegroup/kie-dmn",
                               "DECISION_MISSING_REQ"),
                VALIDATE_MODEL );
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }
    
    @Test
    public void testDTCollectOperatorsMultipleOutputs() {
        // DROOLS-6590 DMN composite output on DT Collect with operators - this is beyond the spec.
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime("multipleOutputsCollectDT.dmn", DMNRuntimeTest.class);
        DMNModel dmnModel = runtime.getModel("https://kiegroup.org/dmn/_943A3581-5FD1-4BCF-9A52-AC7242CC451C", "multipleOutputsCollectDT");
        assertThat(dmnModel).isNotNull();

        Definitions definitions = dmnModel.getDefinitions();
        assertThat(definitions).isNotNull();

        List<DMNMessage> validate = DMNValidatorFactory.newValidator().validate(definitions, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream()
                .allMatch(p -> p.getLevel() == Level.WARNING &&
                        p.getText().contains("Collect with Operator for compound outputs"))).as(ValidatorUtil.formatMessages(validate)).isTrue();
    }
}
