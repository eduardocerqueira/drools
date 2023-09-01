package org.kie.dmn.feel.runtime.functions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.lang.types.BuiltInType;
import org.kie.dmn.feel.runtime.decisiontables.DecisionTableImpl;
import org.kie.dmn.feel.runtime.events.FEELEventBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DTInvokerFunction
        extends BaseFEELFunction {
    private static final Logger logger = LoggerFactory.getLogger( DTInvokerFunction.class );

    private final DecisionTableImpl dt;

    public DTInvokerFunction(DecisionTableImpl dt) {
        super( dt.getName() );
        this.dt = dt;
    }

    public FEELFnResult<Object> invoke(EvaluationContext ctx, Object[] params) {
        FEELEvent capturedException;
        try {
            ctx.enterFrame();
            for( int i = 0; i < params.length; i++ ) {
                ctx.setValue( dt.getParameterNames().get( i ), params[i] );
            }
            return dt.evaluate( ctx, params );
        } catch ( Exception e ) {
            String message = "Error invoking decision table '" + getName() + "': " + e.getClass().getSimpleName();
            capturedException = new FEELEventBase( Severity.ERROR, message, e);
            logger.error( message, e );
        } finally {
            ctx.exitFrame();
        }
        return FEELFnResult.ofError( capturedException );
    }

    @Override
    protected boolean isCustomFunction() {
        return true;
    }

    public DecisionTableImpl getDecisionTable() {
        return dt;
    }

    @Override
    public List<List<Param>> getParameters() {
        return Collections.singletonList(dt.getParameterNames().stream().map(n -> new Param(n, BuiltInType.UNKNOWN)).collect(Collectors.toList()));
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        dt.setName(name);
    }
    
    @Override
    public String toString() {
        return "decision table " + dt.getSignature();
    }
}
