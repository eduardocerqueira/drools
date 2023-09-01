package org.kie.dmn.validation;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.junit.Test;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNMessageType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_COMPILATION;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_MODEL;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_SCHEMA;

public class ValidatorKnowledgeSourceTest extends AbstractValidatorTest {

    @Test
    public void testKNOW_SOURCE_MISSING_OWNER_ReaderInput() throws IOException {
        try (final Reader reader = getReader( "knowledgesource/KNOW_SOURCE_MISSING_OWNER.dmn" )) {
            final List<DMNMessage> validate = validator.validate(
                    reader,
                    VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }

    @Test
    public void testKNOW_SOURCE_MISSING_OWNER_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile( "knowledgesource/KNOW_SOURCE_MISSING_OWNER.dmn" ),
                VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testKNOW_SOURCE_MISSING_OWNER_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions( "knowledgesource/KNOW_SOURCE_MISSING_OWNER.dmn",
                                "https://github.com/kiegroup/kie-dmn",
                                "KNOW_SOURCE_MISSING_OWNER"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testKNOW_SOURCE_OWNER_NOT_ORG_UNIT_ReaderInput() throws IOException {
        try (final Reader reader = getReader( "knowledgesource/KNOW_SOURCE_OWNER_NOT_ORG_UNIT.dmn" )) {
            final List<DMNMessage> validate = validator.validate(
                    reader,
                    VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(3);
            assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
        }
    }

    @Test
    public void testKNOW_SOURCE_OWNER_NOT_ORG_UNIT_FileInput() {
        final List<DMNMessage> validate = validator.validate(
                getFile( "knowledgesource/KNOW_SOURCE_OWNER_NOT_ORG_UNIT.dmn" ),
                VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(3);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }

    @Test
    public void testKNOW_SOURCE_OWNER_NOT_ORG_UNIT_DefinitionsInput() {
        final List<DMNMessage> validate = validator.validate(
                getDefinitions( "knowledgesource/KNOW_SOURCE_OWNER_NOT_ORG_UNIT.dmn",
                                "https://github.com/kiegroup/kie-dmn",
                                "KNOW_SOURCE_OWNER_NOT_ORG_UNIT"),
                VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(3);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND))).isTrue();
    }
}
