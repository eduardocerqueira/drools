package org.kie.dmn.feel.runtime.functions.interval;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.Range;
import org.kie.dmn.feel.runtime.Range.RangeBoundary;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

public class StartedByFunction
        extends BaseFEELFunction {

    public static final StartedByFunction INSTANCE = new StartedByFunction();

    public StartedByFunction() {
        super( "started by" );
    }

    public FEELFnResult<Boolean> invoke(@ParameterName( "range" ) Range range, @ParameterName( "point" ) Comparable point) {
        if ( point == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "point", "cannot be null"));
        }
        if ( range == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "range", "cannot be null"));
        }
        try {
            boolean result = ( range.getLowBoundary() == Range.RangeBoundary.CLOSED && point.compareTo( range.getLowEndPoint() ) == 0 );
            return FEELFnResult.ofResult( result );
        } catch( Exception e ) {
            // points are not comparable
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "point", "cannot be compared to range"));
        }
    }

    public FEELFnResult<Boolean> invoke(@ParameterName( "range1" ) Range range1, @ParameterName( "range2" ) Range range2) {
        if ( range1 == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "range1", "cannot be null"));
        }
        if ( range2 == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "range2", "cannot be null"));
        }
        try {
            boolean result = range1.getLowEndPoint().compareTo(range2.getLowEndPoint()) == 0 &&
                             range1.getLowBoundary() == range2.getLowBoundary() &&
                             (range2.getHighEndPoint().compareTo(range1.getHighEndPoint()) < 0 ||
                              (range2.getHighEndPoint().compareTo(range1.getHighEndPoint()) == 0 &&
                               (range2.getHighBoundary() == RangeBoundary.OPEN ||
                                range1.getHighBoundary() == RangeBoundary.CLOSED)));
            return FEELFnResult.ofResult(result);
        } catch( Exception e ) {
            // points are not comparable
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "range1", "cannot be compared to range2"));
        }
    }

}
