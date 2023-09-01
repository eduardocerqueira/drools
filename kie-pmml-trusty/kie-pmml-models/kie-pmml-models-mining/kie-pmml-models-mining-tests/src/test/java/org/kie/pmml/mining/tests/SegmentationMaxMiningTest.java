package org.kie.pmml.mining.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.api.runtime.PMMLRuntime;
import org.kie.pmml.models.tests.AbstractPMMLTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SegmentationMaxMiningTest extends AbstractPMMLTest {

    private static final String FILE_NAME_NO_SUFFIX = "segmentationMaxMining";

    private static final String MODEL_NAME = "SegmentationMaxMining";
    private static final String TARGET_FIELD = "result";
    private PMMLRuntime pmmlRuntime;

    private double x;
    private double y;
    private double result;

    public void initSegmentationMaxMiningTest(double x, double y, double result) {
        this.x = x;
        this.y = y;
        this.result = result;
    }

    @BeforeEach
    public void setupClass() {
        pmmlRuntime = getPMMLRuntime(FILE_NAME_NO_SUFFIX);
    }

    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, 0, 50},
                {1, 1, 55},
                {20, 30, 121004},
                {25, 31, 167502},
                {5, 5, 1004}
        });
    }

    @MethodSource("data")
    @ParameterizedTest
    void testSegmentationMedianMiningTest(double x, double y, double result) {
        initSegmentationMaxMiningTest(x, y, result);
        final Map<String, Object> inputData = new HashMap<>();
        inputData.put("x", x);
        inputData.put("y", y);
        PMML4Result pmml4Result = evaluate(pmmlRuntime, inputData, FILE_NAME_NO_SUFFIX, MODEL_NAME);

        assertThat(pmml4Result.getResultVariables().get(TARGET_FIELD)).isNotNull();
        assertThat(pmml4Result.getResultVariables().get(TARGET_FIELD)).isEqualTo(result);
    }
}
