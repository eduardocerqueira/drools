package org.kie.pmml.regression.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.api.runtime.PMMLRuntime;
import org.kie.pmml.models.tests.AbstractPMMLTest;

import static org.assertj.core.api.Assertions.assertThat;

public class PredictorTermRegressionTest extends AbstractPMMLTest {

    private static final String FILE_NAME_NO_SUFFIX = "PredictorTermRegression";
    private static final String MODEL_NAME = "PredictorTermRegression";
    private static final String TARGET_FIELD = "result";
    private static PMMLRuntime pmmlRuntime;

    private double x;
    private double y;
    private double z;

    public void initPredictorTermRegressionTest(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @BeforeAll
    public static void setupClass() {
        pmmlRuntime = getPMMLRuntime(FILE_NAME_NO_SUFFIX);
    }

    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, 0, 0}, {-1, 2, 3}, {0.5, -2.5, 4}, {3, 1, 2}, {25, 50, 15},
                {-100, 250, -10}, {-100.1, 800, 105}, {-8, 12.5, 230}, {-1001, -500, 8}, {-1701, 508, 9}
        });
    }

    private static double regressionFunction(double x, double y, double z) {
        return 2 * x + y + 5 * z * z + 4 * y * z - 2.5 * x * y * z + 5;
    }

    @MethodSource("data")
    @ParameterizedTest
    void testPredictorTermRegression(double x, double y, double z) {
        initPredictorTermRegressionTest(x, y, z);
        final Map<String, Object> inputData = new HashMap<>();
        inputData.put("x", x);
        inputData.put("y", y);
        inputData.put("z", z);
        PMML4Result pmml4Result = evaluate(pmmlRuntime, inputData, FILE_NAME_NO_SUFFIX, MODEL_NAME);

        assertThat(pmml4Result).isNotNull();
        assertThat(pmml4Result.getResultVariables()).containsKey(TARGET_FIELD);
        assertThat((Double) pmml4Result.getResultVariables().get(TARGET_FIELD))
                .isEqualTo(regressionFunction(x, y, z));
    }
}
