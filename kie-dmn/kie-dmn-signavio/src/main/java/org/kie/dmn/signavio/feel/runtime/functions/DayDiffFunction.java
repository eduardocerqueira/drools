package org.kie.dmn.signavio.feel.runtime.functions;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.BuiltInFunctions;
import org.kie.dmn.feel.runtime.functions.DateAndTimeFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;
import org.kie.dmn.feel.util.EvalHelper;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

public class DayDiffFunction
        extends BaseFEELFunction {

    public DayDiffFunction() {
        super( "dayDiff" );
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName("datetime1") TemporalAccessor datetime1, @ParameterName("datetime2") TemporalAccessor datetime2) {
        if ( datetime1 == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime1", "cannot be null" ) );
        }
        if ( datetime2 == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime2", "cannot be null" ) );
        }
    
        try {
            return FEELFnResult.ofResult(EvalHelper.getBigDecimalOrNull(
                    Duration.between(
                            convertToInstant(datetime1),
                            convertToInstant(datetime2)
                    ).toDays()));
        } catch (DateTimeException e) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "datetime",
                    "invalid 'date' or 'date and time' parameter", e));
        }
    }
    
    private Instant convertToInstant(TemporalAccessor temporal) {
        return supportsSeconds(temporal) ?
                Instant.from(temporal) :
                LocalDate.from(temporal)
                        .atStartOfDay()
                        .toInstant(UTC);
    }
    private boolean supportsSeconds(TemporalAccessor temporal) {
        return temporal.isSupported(INSTANT_SECONDS) && temporal.isSupported(NANO_OF_SECOND);
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName("datetime1") String datetime1, @ParameterName("datetime2") String datetime2) {
        if ( datetime1 == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime1", "cannot be null" ) );
        }
        if ( datetime2 == null ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime2", "cannot be null" ) );
        }

        try {
            TemporalAccessor dt1 =  BuiltInFunctions.getFunction( DateAndTimeFunction.class ).invoke( datetime1 ).getOrElseThrow(e-> new IllegalArgumentException("Invalid parameter datetime1", e.getSourceException()));
            TemporalAccessor dt2 =  BuiltInFunctions.getFunction( DateAndTimeFunction.class ).invoke( datetime2 ).getOrElseThrow(e-> new IllegalArgumentException("Invalid parameter datetime2", e.getSourceException()));
            return invoke( dt1, dt2 );
        } catch ( Exception e ) {
            return FEELFnResult.ofError( new InvalidParametersEvent( Severity.ERROR, "datetime", "invalid 'date' or 'date and time' parameter", e ) );
        }
    }

}
