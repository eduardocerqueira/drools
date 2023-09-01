package org.kie.dmn.signavio.feel.runtime.functions;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

import java.math.BigDecimal;
import java.math.MathContext;

public class ModuloFunction
        extends BaseFEELFunction {
    public static final ModuloFunction INSTANCE = new ModuloFunction();

    ModuloFunction() {
        super( "modulo" );
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName( "dividend" ) BigDecimal divident, @ParameterName( "divisor" ) BigDecimal divisor) {
        if ( divident == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "divident", "cannot be null"));
        }
        if ( divisor == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "divisor", "cannot be null"));
        }
        return FEELFnResult.ofResult( divident.remainder( divisor, MathContext.DECIMAL128 ) );
    }
}
