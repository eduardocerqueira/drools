package org.kie.dmn.feel.runtime.functions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;

public class AllFunctionTest {

    private AllFunction allFunction;

    @Before
    public void setUp() {
        allFunction = new AllFunction();
    }

    @Test
    public void invokeBooleanParamNull() {
        FunctionTestUtil.assertResultNull(allFunction.invoke((Boolean) null));
    }

    @Test
    public void invokeBooleanParamTrue() {
        FunctionTestUtil.assertResult(allFunction.invoke(true), true);
    }

    @Test
    public void invokeBooleanParamFalse() {
        FunctionTestUtil.assertResult(allFunction.invoke(false), false);
    }

    @Test
    public void invokeArrayParamNull() {
        FunctionTestUtil.assertResultError(allFunction.invoke((Object[]) null), InvalidParametersEvent.class);
    }

    @Test
    public void invokeArrayParamEmptyArray() {
        FunctionTestUtil.assertResult(allFunction.invoke(new Object[]{}), true);
    }

    @Test
    public void invokeArrayParamReturnTrue() {
        FunctionTestUtil.assertResult(allFunction.invoke(new Object[]{Boolean.TRUE, Boolean.TRUE}), true);
    }

    @Test
    public void invokeArrayParamReturnFalse() {
        FunctionTestUtil.assertResult(allFunction.invoke(new Object[]{Boolean.TRUE, Boolean.FALSE}), false);
        FunctionTestUtil.assertResult(allFunction.invoke(new Object[]{Boolean.TRUE, null, Boolean.FALSE}), false);
    }

    @Test
    public void invokeArrayParamReturnNull() {
        FunctionTestUtil.assertResultNull(allFunction.invoke(new Object[]{Boolean.TRUE, null, Boolean.TRUE}));
    }

    @Test
    public void invokeArrayParamTypeHeterogenousArray() {
        FunctionTestUtil.assertResultError(allFunction.invoke(new Object[]{Boolean.TRUE, 1}), InvalidParametersEvent.class);
        FunctionTestUtil.assertResultError(allFunction.invoke(new Object[]{Boolean.FALSE, 1}), InvalidParametersEvent.class);
        FunctionTestUtil.assertResultError(allFunction.invoke(new Object[]{Boolean.TRUE, null, 1}), InvalidParametersEvent.class);
    }

    @Test
    public void invokeListParamNull() {
        FunctionTestUtil.assertResultError(allFunction.invoke((List) null), InvalidParametersEvent.class);
    }

    @Test
    public void invokeListParamEmptyList() {
        FunctionTestUtil.assertResult(allFunction.invoke(Collections.emptyList()), true);
    }

    @Test
    public void invokeListParamReturnTrue() {
        FunctionTestUtil.assertResult(allFunction.invoke(Arrays.asList(Boolean.TRUE, Boolean.TRUE)), true);
    }

    @Test
    public void invokeListParamReturnFalse() {
        FunctionTestUtil.assertResult(allFunction.invoke(Arrays.asList(Boolean.TRUE, Boolean.FALSE)), false);
        FunctionTestUtil.assertResult(allFunction.invoke(Arrays.asList(Boolean.TRUE, null, Boolean.FALSE)), false);
    }

    @Test
    public void invokeListParamReturnNull() {
        FunctionTestUtil.assertResultNull(allFunction.invoke(Arrays.asList(Boolean.TRUE, null, Boolean.TRUE)));
    }

    @Test
    public void invokeListParamTypeHeterogenousArray() {
        FunctionTestUtil.assertResultError(allFunction.invoke(Arrays.asList(Boolean.TRUE, 1)), InvalidParametersEvent.class);
        FunctionTestUtil.assertResultError(allFunction.invoke(Arrays.asList(Boolean.FALSE, 1)), InvalidParametersEvent.class);
        FunctionTestUtil.assertResultError(allFunction.invoke(Arrays.asList(Boolean.TRUE, null, 1)), InvalidParametersEvent.class);
    }
}