package org.kie.dmn.legacy.tests.validation.v1_1;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNMessageType;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.marshalling.DMNMarshaller;
import org.kie.dmn.backend.marshalling.v1x.DMNMarshallerFactory;
import org.kie.dmn.core.DMNInputRuntimeTest;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.kie.dmn.model.api.Definitions;
import org.kie.dmn.validation.AbstractValidatorTest;
import org.kie.dmn.validation.DMNValidator;
import org.kie.dmn.validation.DMNValidatorFactory;
import org.kie.dmn.validation.ValidatorUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_COMPILATION;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_MODEL;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_SCHEMA;

public class ValidatorTest extends AbstractValidatorTest {

    @Test
    public void testDryRun() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "0001-input-data-string.dmn", DMNInputRuntimeTest.class );
        DMNModel dmnModel = runtime.getModel( "https://github.com/kiegroup/drools/kie-dmn", "_0001-input-data-string" );
        assertThat(dmnModel).isNotNull();
        

        Definitions definitions = dmnModel.getDefinitions();
        assertThat(definitions).isNotNull();
        
        DMNValidatorFactory.newValidator().validate(definitions);
    }

    @Test
    public void testMACDInputDefinitions() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "MACD-enhanced_iteration.dmn", DMNInputRuntimeTest.class );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/definitions/_6cfe7d88-6741-45d1-968c-b61a597d0964", "MACD-enhanced iteration" );
        assertThat(dmnModel).isNotNull();

        Definitions definitions = dmnModel.getDefinitions();
        assertThat(definitions).isNotNull();

        List<DMNMessage> messages = DMNValidatorFactory.newValidator().validate(definitions, VALIDATE_MODEL, VALIDATE_COMPILATION);

        assertThat(messages).as(messages.toString()).hasSize(0);
    }

    @Test
    public void testMACDInputReader() throws IOException {
        try (final Reader reader = new InputStreamReader(getClass().getResourceAsStream("/org/kie/dmn/core/MACD-enhanced_iteration.dmn") )) {
            List<DMNMessage> messages = DMNValidatorFactory.newValidator().validate(reader, VALIDATE_MODEL, VALIDATE_COMPILATION);
            assertThat(messages).as(messages.toString()).hasSize(0);
        }
    }

    private Definitions utilDefinitions(String filename, String modelName) {
//        List<DMNMessage> validateXML;
//        try {
//            validateXML = validator.validate( new File(this.getClass().getResource(filename).toURI()), DMNValidator.Validation.VALIDATE_SCHEMA );
//            assertThat( "using unit test method utilDefinitions must received a XML valid DMN file", validateXML, IsEmptyCollection.empty() );
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            fail("Unable for the test suite to locate the file for XML validation.");
//        }
        
        DMNMarshaller marshaller = DMNMarshallerFactory.newDefaultMarshaller();
        try( InputStreamReader isr = new InputStreamReader( getClass().getResourceAsStream( filename ) ) ) {
            Definitions definitions = marshaller.unmarshal( isr );
            assertThat(definitions).isNotNull();
            return definitions;
        } catch ( IOException e ) {
            e.printStackTrace();
            fail("Unable for the test suite to locate the file for validation.");
        }
        return null;
    }
        
    @Test
    public void testInvalidXml() throws URISyntaxException {
        List<DMNMessage> validateXML = validator.validate( new File(this.getClass().getResource( "invalidXml.dmn" ).toURI()), DMNValidator.Validation.VALIDATE_SCHEMA);
        assertThat(validateXML).as(ValidatorUtil.formatMessages( validateXML )).hasSize(1);
        assertThat(validateXML.get(0).getMessageType()).as(validateXML.get(0).toString()).isEqualTo(DMNMessageType.FAILED_XML_VALIDATION);
    }

    @Test
    public void testINVOCATION_MISSING_EXPR() {
        List<DMNMessage> validate = validator.validate( getReader( "INVOCATION_MISSING_EXPR.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSizeGreaterThan(0);
        assertThat(validate.get(0).getMessageType()).as(validate.get(0).toString()).isEqualTo(DMNMessageType.MISSING_EXPRESSION);
    }

    @Test
    public void testNAME_IS_VALID() {
        List<DMNMessage> validate = validator.validate( getReader( "NAME_IS_VALID.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testNAME_INVALID_empty_name() {
        List<DMNMessage> validate = validator.validate( getReader( "DROOLS-1447.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(4);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.FAILED_XML_VALIDATION))).isTrue();
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.VARIABLE_NAME_MISMATCH))).isTrue();
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.INVALID_NAME) && p.getSourceId().equals("_5e43b55c-888e-443c-b1b9-80e4aa6746bd"))).isTrue();
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.INVALID_NAME) && p.getSourceId().equals("_b1e4588e-9ce1-4474-8e4e-48dbcdb7524b"))).isTrue();
    }
    
    @Test
    public void testDRGELEM_NOT_UNIQUE() {
        List<DMNMessage> validate = validator.validate( getReader( "DRGELEM_NOT_UNIQUE.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.DUPLICATE_NAME))).isTrue();
    }
    
    @Test
    public void testFORMAL_PARAM_DUPLICATED() {
        List<DMNMessage> validate = validator.validate( getReader( "FORMAL_PARAM_DUPLICATED.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(3);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.DUPLICATED_PARAM))).isTrue();
    }
    
    @Test
    public void testINVOCATION_INCONSISTENT_PARAM_NAMES() {
        List<DMNMessage> validate = validator.validate( getReader( "INVOCATION_INCONSISTENT_PARAM_NAMES.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSizeGreaterThan(0);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.PARAMETER_MISMATCH))).isTrue();
    }
    
    @Test @Ignore( "Needs to be improved as invocations can be used to invoke functions node defined in BKMs. E.g., FEEL built in functions, etc.")
    public void testINVOCATION_MISSING_TARGET() {
        Definitions definitions = utilDefinitions( "INVOCATION_MISSING_TARGET.dmn", "INVOCATION_MISSING_TARGET" );
        List<DMNMessage> validate = validator.validate(definitions);
        
//        assertTrue( validate.stream().anyMatch( p -> p.getMessageType().equals( DMNMessageType.INVOCATION_MISSING_TARGET ) ) );
    }
    
    @Ignore("known current limitation")
    @Test
    public void testINVOCATION_MISSING_TARGETRbis() {
        Definitions definitions = utilDefinitions( "INVOCATION_MISSING_TARGETbis.dmn", "INVOCATION_MISSING_TARGETbis" );
        List<DMNMessage> validate = validator.validate(definitions);
        
//        assertTrue( validate.stream().anyMatch( p -> p.getMessageType().equals( DMNMessageType.INVOCATION_MISSING_TARGET ) ) );
    }
    
    @Test
    public void testINVOCATION_WRONG_PARAM_COUNT() {
        List<DMNMessage> validate = validator.validate( getReader( "INVOCATION_WRONG_PARAM_COUNT.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSizeGreaterThan(0);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.PARAMETER_MISMATCH))).isTrue();
    }
    
    @Test
    public void testITEMCOMP_DUPLICATED() {
        List<DMNMessage> validate = validator.validate( getReader( "ITEMCOMP_DUPLICATED.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.DUPLICATED_ITEM_DEF))).isTrue();
    }
    
    @Test
    public void testITEMDEF_NOT_UNIQUE() {
        List<DMNMessage> validate = validator.validate( getReader( "ITEMDEF_NOT_UNIQUE.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.DUPLICATED_ITEM_DEF))).isTrue();
    }
    
    @Test
    public void testITEMDEF_NOT_UNIQUE_DROOLS_1450() {
        // DROOLS-1450
        List<DMNMessage> validate = validator.validate( getReader( "ITEMDEF_NOT_UNIQUE_DROOLS-1450.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }
    
    @Test
    public void testRELATION_DUP_COLUMN() {
        List<DMNMessage> validate = validator.validate( getReader( "RELATION_DUP_COLUMN.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.DUPLICATED_RELATION_COLUMN))).isTrue();
    }
    
    @Test
    public void testRELATION_ROW_CELL_NOTLITERAL() {
        List<DMNMessage> validate = validator.validate( getReader( "RELATION_ROW_CELL_NOTLITERAL.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(2);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.RELATION_CELL_NOT_LITERAL))).isTrue();
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_EXPRESSION))).isTrue();
    }
    
    @Test
    public void testRELATION_ROW_CELLCOUNTMISMATCH() {
        List<DMNMessage> validate = validator.validate( getReader( "RELATION_ROW_CELLCOUNTMISMATCH.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.RELATION_CELL_COUNT_MISMATCH))).isTrue();
    }

    @Test
    public void testMortgageRecommender() {
        // This file has a gazillion errors. The goal of this test is simply check that the validator itself is not blowing up
        // and raising an exception. The errors in the file itself are irrelevant.
        List<DMNMessage> validate = validator.validate( getReader( "MortgageRecommender.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).isNotEmpty();
    }

    @Test
    public void testREQAUTH_NOT_KNOWLEDGESOURCEbis() {
        // DROOLS-1435
        List<DMNMessage> validate = validator.validate( getReader( "REQAUTH_NOT_KNOWLEDGESOURCEbis.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
    }

    @Test
    public void testVARIABLE_LEADING_TRAILING_SPACES() {
        List<DMNMessage> validate = validator.validate( getReader( "VARIABLE_LEADING_TRAILING_SPACES.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).withFailMessage(ValidatorUtil.formatMessages(validate)).isNotEmpty();
        assertThat(validate).anySatisfy(p -> {
            assertThat(p.getMessageType()).isEqualTo(DMNMessageType.INVALID_NAME);
            assertThat(p.getSourceId()).isEqualTo("_dd662d27-7896-42cb-9d14-bd74203bdbec");
        });
        assertThat(validate).anySatisfy(p -> {
            assertThat(p.getMessageType()).isEqualTo(DMNMessageType.INVALID_NAME);
            assertThat(p.getSourceId()).isEqualTo("_1f54fd51-6805-4280-b576-607450f85edd");
        });
    }

    @Test
    public void testUNKNOWN_VARIABLE() {
        List<DMNMessage> validate = validator.validate( getReader( "UNKNOWN_VARIABLE.dmn" ), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(1);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.ERR_COMPILING_FEEL))).isTrue();
    }

    @Test
    public void testVALIDATION() {
        List<DMNMessage> validate = validator.validate( getReader( "validation.dmn" ), VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(5);
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.INVALID_NAME))).isTrue();
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_TYPE_REF))).isTrue();
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.MISSING_EXPRESSION))).isTrue();
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.ERR_COMPILING_FEEL))).isTrue();
        // on node DTI the `Loan Payment` is of type `tLoanPayment` hence the property is `monthlyAmount`, NOT `amount` as reported in the model FEEL expression: (Loan Payment.amount+...
        assertThat(validate.stream().anyMatch(p -> p.getMessageType().equals(DMNMessageType.ERR_COMPILING_FEEL))).isTrue();
    }

    @Test
    public void testUsingSemanticNamespacePrefix() {
        // DROOLS-2419
        List<DMNMessage> validate = validator.validate(getReader("UsingSemanticNS.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testUsingSemanticNamespacePrefixAndExtensions() {
        // DROOLS-2447
        List<DMNMessage> validate = validator.validate(getReader("Hello_World_semantic_namespace_with_extensions.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testNoPrefixAndExtensions() {
        // DROOLS-2447
        List<DMNMessage> validate = validator.validate(getReader("Hello_World_no_prefix_with_extensions.dmn"), VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION);
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);
    }

    @Test
    public void testDecisionService20181008() {
        // DROOLS-3087 DMN Validation of DecisionService referencing a missing import
        List<DMNMessage> validate = validator.validateUsing(VALIDATE_MODEL, VALIDATE_COMPILATION)
                                             .theseModels(getReader("DSWithImport20181008-ModelA.dmn"),
                                                          getReader("DSWithImport20181008-ModelB.dmn"));
        assertThat(validate).as(ValidatorUtil.formatMessages(validate)).hasSize(0);

        List<DMNMessage> missingDMNImport = validator.validateUsing(VALIDATE_MODEL)
                                                     .theseModels(getReader("DSWithImport20181008-ModelA.dmn"),
                                                                  getReader("DSWithImport20181008-ModelB-missingDMNImport.dmn"));
        assertThat(missingDMNImport.stream().filter(p -> p.getMessageType().equals(DMNMessageType.REQ_NOT_FOUND)).count()).isEqualTo(2L); // on Decision and Decision Service missing to locate the dependency given Import is omitted.
    }
}
