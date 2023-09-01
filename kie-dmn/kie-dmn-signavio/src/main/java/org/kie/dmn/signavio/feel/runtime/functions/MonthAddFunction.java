package org.kie.dmn.signavio.feel.runtime.functions;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.lang.types.BuiltInType;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.BuiltInFunctions;
import org.kie.dmn.feel.runtime.functions.DateAndTimeFunction;
import org.kie.dmn.feel.runtime.functions.DateFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

public class MonthAddFunction
        extends BaseFEELFunction {

    public MonthAddFunction() {
        super( "monthAdd" );
    }

    public FEELFnResult<TemporalAccessor> invoke(@ParameterName("datestring") String datetime, @ParameterName( "months to add" ) BigDecimal months) {
        if ( datetime == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datestring", "cannot be null" ) );
        }
        if ( months == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "months to add", "cannot be null" ) );
        }

        try {
            TemporalAccessor r = null;
            if( datetime.contains( "T" ) ) {
                r = BuiltInFunctions.getFunction( DateAndTimeFunction.class ).invoke( datetime ).cata( BuiltInType.justNull(), Function.identity() );
            } else {
                r = BuiltInFunctions.getFunction( DateFunction.class ).invoke( datetime ).cata( BuiltInType.justNull(), Function.identity() );
            }

            if (r instanceof TemporalAccessor) {
                return invoke(r, months);
            } else {
                return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datestring", "date-parsing exception" ) );
            }
        } catch ( DateTimeException e ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datestring", "date-parsing exception", e ) );
        }
    }

    public FEELFnResult<TemporalAccessor> invoke(@ParameterName("datetime") TemporalAccessor datetime, @ParameterName( "months to add" ) BigDecimal months) {
        if ( datetime == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime", "cannot be null" ) );
        }
        if ( months == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "months to add", "cannot be null" ) );
        }

        try {
            if( datetime instanceof Temporal ) {
                return FEELFnResult.ofResult( ((Temporal) datetime).plus( months.longValue(), ChronoUnit.MONTHS ) );
            } else {
                return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime", "invalid 'date' or 'date and time' parameter" ) );
            }
        } catch ( DateTimeException e ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime", "invalid 'date' or 'date and time' parameter", e ) );
        }
    }

}
