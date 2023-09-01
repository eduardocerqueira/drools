package org.kie.dmn.core.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.event.DMNRuntimeEventManager;
import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.api.feel.runtime.events.FEELEventListener;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.core.api.EvaluatorResult;
import org.kie.dmn.core.api.EvaluatorResult.ResultType;
import org.kie.dmn.core.impl.DMNResultImpl;
import org.kie.dmn.core.util.Msg;
import org.kie.dmn.core.util.MsgUtil;
import org.kie.dmn.feel.FEEL;
import org.kie.dmn.feel.codegen.feel11.ProcessedExpression;
import org.kie.dmn.feel.lang.CompiledExpression;
import org.kie.dmn.feel.lang.impl.CompiledExpressionImpl;
import org.kie.dmn.feel.lang.impl.EvaluationContextImpl;
import org.kie.dmn.feel.lang.impl.FEELImpl;
import org.kie.dmn.model.api.LiteralExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An evaluator for DMN Literal Expressions
 */
public class DMNLiteralExpressionEvaluator
        implements DMNExpressionEvaluator {

    private static final Logger LOG = LoggerFactory.getLogger(DMNLiteralExpressionEvaluator.class);

    private LiteralExpression expressionNode;
    private CompiledExpression expression;
    private boolean isFunctionDef;
    final FEELImpl feelInstance;

    public DMNLiteralExpressionEvaluator(CompiledExpression expression, LiteralExpression expressionNode, FEEL feel) {
        this.expressionNode = expressionNode;
        this.expression = expression;
        if (expression instanceof CompiledExpressionImpl) {
            this.isFunctionDef = ((CompiledExpressionImpl) expression).isFunctionDef();
        } else if (expression instanceof ProcessedExpression) {
            this.isFunctionDef = ((ProcessedExpression) expression).getInterpreted().isFunctionDef();
        } else {
            throw new IllegalArgumentException(
                    "Cannot create DMNLiteralExpressionEvaluator: unsupported type " + expression.getClass());
        }
        this.feelInstance = (FEELImpl) feel;
    }

    public boolean isFunctionDefinition() {
        return isFunctionDef;
    }

    public CompiledExpression getExpression() {
        return this.expression;
    }

    @Override
    public EvaluatorResult evaluate(DMNRuntimeEventManager dmrem, DMNResult dmnr) {
        DMNResultImpl result = (DMNResultImpl) dmnr;
        LiteralInvocationListener liListener = new LiteralInvocationListener();
        EvaluationContextImpl ectx = feelInstance.newEvaluationContext(List.of(liListener), result.getContext().getAll());
        ectx.setDMNRuntime(dmrem.getRuntime());
        // in case an exception is thrown, the parent node will report it
        Object val = feelInstance.evaluate(expression, ectx);
        ResultType resultType = ResultType.SUCCESS;
        for (FEELEvent captured : liListener.events) {
            MsgUtil.reportMessage(LOG,
                                  dmnSeverityFromFEELSeverity(captured.getSeverity()),
                                  expressionNode,
                                  result,
                                  null,
                                  captured,
                                  Msg.FEEL_EVENT_EVAL_LITERAL_EXPRESSION,
                                  captured.getSeverity().toString(),
                                  MsgUtil.clipString(expressionNode.getText(), 50),
                                  captured.getMessage());
            if (captured.getSeverity() == Severity.ERROR) { // as FEEL events are being cycled, compute it here.
                resultType = ResultType.FAILURE;
            }
        }
        return new EvaluatorResultImpl(val, resultType);
    }

    private static DMNMessage.Severity dmnSeverityFromFEELSeverity(FEELEvent.Severity severity) {
        switch (severity) {
            case ERROR:
                return DMNMessage.Severity.ERROR;
            case TRACE:
                return DMNMessage.Severity.TRACE;
            case WARN:
                return DMNMessage.Severity.WARN;
            case INFO:
            default:
                return DMNMessage.Severity.INFO;
        }
    }

    static class LiteralInvocationListener implements FEELEventListener {

        public final List<FEELEvent> events = new ArrayList<>();

        @Override
        public void onEvent(FEELEvent event) {
            events.add(event);
        }
    }
}
