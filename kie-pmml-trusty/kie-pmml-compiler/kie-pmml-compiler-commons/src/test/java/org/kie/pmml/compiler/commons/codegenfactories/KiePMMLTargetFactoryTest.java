package org.kie.pmml.compiler.commons.codegenfactories;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.junit.jupiter.api.Test;
import org.kie.pmml.api.enums.CAST_INTEGER;
import org.kie.pmml.api.enums.OP_TYPE;
import org.kie.pmml.api.models.TargetField;
import org.kie.pmml.api.models.TargetValue;
import org.kie.pmml.commons.model.KiePMMLTarget;
import org.kie.pmml.commons.model.KiePMMLTargetValue;
import org.kie.pmml.compiler.api.utils.ModelUtils;
import org.kie.pmml.compiler.commons.utils.JavaParserUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.pmml.compiler.api.testutils.PMMLModelTestUtils.getRandomTarget;
import static org.kie.pmml.compiler.commons.testutils.CodegenTestUtils.commonValidateCompilationWithImports;
import static org.drools.util.FileUtils.getFileContent;

public class KiePMMLTargetFactoryTest {

    private static final String TEST_01_SOURCE = "KiePMMLTargetFactoryTest_01.txt";

    @Test
    void getKiePMMLTargetValueVariableInitializer() throws IOException {
        TargetField kieTargetField = ModelUtils.convertToKieTargetField(getRandomTarget());
        MethodCallExpr retrieved = KiePMMLTargetFactory.getKiePMMLTargetVariableInitializer(kieTargetField);
        String text = getFileContent(TEST_01_SOURCE);
        List<TargetValue> kieTargetValues = kieTargetField.getTargetValues();
        String opType = OP_TYPE.class.getCanonicalName() + "." + kieTargetField.getOpType().toString();
        String castInteger = CAST_INTEGER.class.getCanonicalName() + "." + kieTargetField.getCastInteger().toString();
        Expression expected = JavaParserUtils.parseExpression(String.format(text,
                                                                            kieTargetField.getName(),
                                                                            kieTargetValues.get(0).getValue(),
                                                                            kieTargetValues.get(0).getDisplayValue(),
                                                                            kieTargetValues.get(0).getPriorProbability(),
                                                                            kieTargetValues.get(0).getDefaultValue(),
                                                                            kieTargetValues.get(1).getValue(),
                                                                            kieTargetValues.get(1).getDisplayValue(),
                                                                            kieTargetValues.get(1).getPriorProbability(),
                                                                            kieTargetValues.get(1).getDefaultValue(),
                                                                            kieTargetValues.get(2).getValue(),
                                                                            kieTargetValues.get(2).getDisplayValue(),
                                                                            kieTargetValues.get(2).getPriorProbability(),
                                                                            kieTargetValues.get(2).getDefaultValue(),
                                                                            opType,
                                                                            kieTargetField.getField(),
                                                                            castInteger,
                                                                            kieTargetField.getMin(),
                                                                            kieTargetField.getMax(),
                                                                            kieTargetField.getRescaleConstant(),
                                                                            kieTargetField.getRescaleFactor()));
        assertThat(JavaParserUtils.equalsNode(expected, retrieved)).isTrue();
        List<Class<?>> imports = Arrays.asList(Arrays.class, Collections.class, KiePMMLTarget.class,
                                               KiePMMLTargetValue.class, TargetField.class, TargetValue.class);
        commonValidateCompilationWithImports(retrieved, imports);
    }
}