package org.kie.pmml.compiler.commons.codegenfactories;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.dmg.pmml.LinearNorm;
import org.dmg.pmml.NormContinuous;
import org.junit.jupiter.api.Test;
import org.kie.pmml.api.enums.OUTLIER_TREATMENT_METHOD;
import org.kie.pmml.commons.model.expressions.KiePMMLLinearNorm;
import org.kie.pmml.commons.model.expressions.KiePMMLNormContinuous;
import org.kie.pmml.compiler.commons.utils.JavaParserUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.pmml.compiler.api.testutils.PMMLModelTestUtils.getRandomLinearNorm;
import static org.kie.pmml.compiler.api.testutils.PMMLModelTestUtils.getRandomNormContinuous;
import static org.kie.pmml.compiler.commons.testutils.CodegenTestUtils.commonValidateCompilationWithImports;
import static org.drools.util.FileUtils.getFileContent;

public class KiePMMLNormContinuousFactoryTest {

    private static final String TEST_01_SOURCE = "KiePMMLNormContinuousFactoryTest_01.txt";
    private static final String TEST_02_SOURCE = "KiePMMLNormContinuousFactoryTest_02.txt";

    @Test
    void getNormContinuousVariableDeclaration() throws IOException {
        String variableName = "variableName";
        NormContinuous normContinuous = getRandomNormContinuous();
        List<LinearNorm> linearNorms = normContinuous.getLinearNorms();

        BlockStmt retrieved =
                org.kie.pmml.compiler.commons.codegenfactories.KiePMMLNormContinuousFactory.getNormContinuousVariableDeclaration(variableName,
                                                                                                                                               normContinuous);
        String outlierString =
                OUTLIER_TREATMENT_METHOD.class.getName() + "." + OUTLIER_TREATMENT_METHOD.byName(normContinuous.getOutliers().value()).name();
        String text = getFileContent(TEST_01_SOURCE);
        Statement expected = JavaParserUtils.parseBlock(String.format(text, variableName,
                                                                      normContinuous.getField().getValue(),
                                                                      linearNorms.get(0).getOrig(),
                                                                      linearNorms.get(0).getNorm(),
                                                                      linearNorms.get(1).getOrig(),
                                                                      linearNorms.get(1).getNorm(),
                                                                      linearNorms.get(2).getOrig(),
                                                                      linearNorms.get(2).getNorm(),
                                                                      outlierString, normContinuous.getMapMissingTo()));
        assertThat(JavaParserUtils.equalsNode(expected, retrieved)).isTrue();
        List<Class<?>> imports = Arrays.asList(Arrays.class, Collections.class, KiePMMLLinearNorm.class,
                                               KiePMMLNormContinuous.class, OUTLIER_TREATMENT_METHOD.class);
        commonValidateCompilationWithImports(retrieved, imports);
    }

    @Test
    void getNewKiePMMLLinearNormExpression() throws IOException {
        String name = "name";
        LinearNorm linearNorm = getRandomLinearNorm();
        Expression retrieved = KiePMMLNormContinuousFactory.getNewKiePMMLLinearNormExpression(linearNorm, name);
        String text = getFileContent(TEST_02_SOURCE);
        Expression expected = JavaParserUtils.parseExpression(String.format(text, name,
                                                                            linearNorm.getOrig(),
                                                                            linearNorm.getNorm()));
        assertThat(JavaParserUtils.equalsNode(expected, retrieved)).isTrue();
    }
}