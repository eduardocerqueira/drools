package org.kie.dmn.feel.runtime.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;

public class IndexOfFunction
        extends BaseFEELFunction {

    public IndexOfFunction() {
        super( "index of" );
    }

    public FEELFnResult<List<BigDecimal>> invoke(@ParameterName( "list" ) List list, @ParameterName( "match" ) Object match) {
        if ( list == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "cannot be null"));
        }

        final List<BigDecimal> result = new ArrayList<>();
        for( int i = 0; i < list.size(); i++ ) {
            Object o = list.get( i );
            if ( o == null && match == null) {
                result.add( BigDecimal.valueOf( i+1L ) );
            } else if ( o != null && match != null ) {
                if ( equalsAsBigDecimals(o, match) || o.equals(match) ) {
                    result.add( BigDecimal.valueOf( i+1L ) );
                }
            }
        }
        return FEELFnResult.ofResult( result );
    }

    private boolean equalsAsBigDecimals(final Object object, final Object match) {
        return (object instanceof BigDecimal)
                && (match instanceof BigDecimal)
                && ((BigDecimal) object).compareTo((BigDecimal) match) == 0;
    }
}
