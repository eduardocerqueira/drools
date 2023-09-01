package org.kie.dmn.feel.runtime.functions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;

public class InsertBeforeFunctionTest {

    private InsertBeforeFunction insertBeforeFunction;

    @Before
    public void setUp() {
        insertBeforeFunction = new InsertBeforeFunction();
    }

    @Test
    public void invokeListNull() {
        FunctionTestUtil.assertResultError(insertBeforeFunction.invoke(null, BigDecimal.ZERO, new Object()), InvalidParametersEvent.class);
    }

    @Test
    public void invokePositionNull() {
        FunctionTestUtil.assertResultError(insertBeforeFunction.invoke(Collections.emptyList(), null, new Object()), InvalidParametersEvent.class);
    }

    @Test
    public void invokeListPositionNull() {
        FunctionTestUtil.assertResultError(insertBeforeFunction.invoke(null, null, new Object()), InvalidParametersEvent.class);
    }

    @Test
    public void invokePositionZero() {
        FunctionTestUtil.assertResultError(insertBeforeFunction.invoke(Collections.emptyList(), BigDecimal.ZERO, new Object()), InvalidParametersEvent.class);
    }

    @Test
    public void invokePositionOutsideListBounds() {
        FunctionTestUtil.assertResultError(insertBeforeFunction.invoke(Collections.emptyList(), BigDecimal.ONE, new Object()), InvalidParametersEvent.class);
        FunctionTestUtil.assertResultError(insertBeforeFunction.invoke(Collections.emptyList(), BigDecimal.valueOf(-1), new Object()), InvalidParametersEvent.class);
    }

    @Test
    public void invokeInsertIntoEmptyList() {
        // According to spec, inserting into empty list shouldn't be possible. For inserting into empty list, user should use append() function.
        FunctionTestUtil.assertResultError(insertBeforeFunction.invoke(Collections.emptyList(), BigDecimal.ONE, null), InvalidParametersEvent.class);
    }

    @Test
    public void invokePositionPositive() {
        FunctionTestUtil.assertResult(insertBeforeFunction.invoke(Collections.singletonList("test"), BigDecimal.ONE, null), Arrays.asList(null, "test"));
        FunctionTestUtil.assertResult(
                insertBeforeFunction.invoke(Arrays.asList("test", null, BigDecimal.ZERO), BigDecimal.valueOf(2), "testtt"),
                Arrays.asList("test", "testtt", null, BigDecimal.ZERO));
        FunctionTestUtil.assertResult(
                insertBeforeFunction.invoke(Arrays.asList("test", null, BigDecimal.ZERO), BigDecimal.valueOf(3), "testtt"),
                Arrays.asList("test", null, "testtt", BigDecimal.ZERO));
    }

    @Test
    public void invokePositionNegative() {
        FunctionTestUtil.assertResult(insertBeforeFunction.invoke(Collections.singletonList("test"), BigDecimal.valueOf(-1), null), Arrays.asList(null, "test"));
        FunctionTestUtil.assertResult(
                insertBeforeFunction.invoke(Arrays.asList("test", null, BigDecimal.ZERO), BigDecimal.valueOf(-2), "testtt"),
                Arrays.asList("test", "testtt", null, BigDecimal.ZERO));
        FunctionTestUtil.assertResult(
                insertBeforeFunction.invoke(Arrays.asList("test", null, BigDecimal.ZERO), BigDecimal.valueOf(-3), "testtt"),
                Arrays.asList("testtt", "test", null, BigDecimal.ZERO));
    }
}