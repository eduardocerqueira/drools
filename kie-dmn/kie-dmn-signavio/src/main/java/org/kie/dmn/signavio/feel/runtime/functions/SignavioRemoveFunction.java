package org.kie.dmn.signavio.feel.runtime.functions;

import java.util.ArrayList;
import java.util.List;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

public class SignavioRemoveFunction
        extends BaseFEELFunction {

    public SignavioRemoveFunction() {
        super( "remove" );
    }

    public FEELFnResult<List> invoke(@ParameterName("list") List list, @ParameterName("element") Object element) {
        if ( list == null ) { 
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "cannot be null"));
        }
        if (element == null) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "element", "cannot be null"));
        }

        // spec requires us to return a new list
        List<Object> result = new ArrayList<>( list );

        result.remove(element);

        return FEELFnResult.ofResult( result );
    }
}
