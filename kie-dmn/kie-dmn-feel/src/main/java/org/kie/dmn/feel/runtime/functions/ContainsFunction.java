package org.kie.dmn.feel.runtime.functions;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;

public class ContainsFunction
        extends BaseFEELFunction {

    public ContainsFunction() {
        super( "contains" );
    }

    public FEELFnResult<Boolean> invoke(@ParameterName("string") String string, @ParameterName("match") String match) {
        if ( string == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "string", "cannot be null"));
        }
        if ( match == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "match", "cannot be null"));
        }
        
        return FEELFnResult.ofResult(string.contains(match));
    }
}
