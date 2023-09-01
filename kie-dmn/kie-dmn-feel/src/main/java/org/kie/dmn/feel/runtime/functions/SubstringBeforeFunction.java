package org.kie.dmn.feel.runtime.functions;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;

public class SubstringBeforeFunction
        extends BaseFEELFunction {

    public SubstringBeforeFunction() {
        super( "substring before" );
    }

    public FEELFnResult<String> invoke(@ParameterName("string") String string, @ParameterName("match") String match) {
        if ( string == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "string", "cannot be null" ) );
        }
        if ( match == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "match", "cannot be null" ) );
        }

        int index = string.indexOf( match );
        if ( index > 0 ) {
            return FEELFnResult.ofResult( string.substring( 0, index ) );
        } else {
            return FEELFnResult.ofResult( "" );
        }
    }

}
