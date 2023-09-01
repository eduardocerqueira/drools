package org.kie.dmn.feel.runtime.functions.interval;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.Range;
import org.kie.dmn.feel.runtime.Range.RangeBoundary;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

public class IncludesFunction
        extends BaseFEELFunction {

    public static final IncludesFunction INSTANCE = new IncludesFunction();

    public IncludesFunction() {
        super( "includes" );
    }

    public FEELFnResult<Boolean> invoke(@ParameterName( "range" ) Range range, @ParameterName( "point" ) Comparable point) {
        if ( point == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "point", "cannot be null"));
        }
        if ( range == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "range", "cannot be null"));
        }
        try {
            boolean result = (range.getLowEndPoint().compareTo(point) < 0 && range.getHighEndPoint().compareTo(point) > 0) ||
                             (range.getLowEndPoint().compareTo(point) == 0 && range.getLowBoundary() == RangeBoundary.CLOSED) ||
                             (range.getHighEndPoint().compareTo(point) == 0 && range.getHighBoundary() == RangeBoundary.CLOSED);
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
            boolean result = (range1.getLowEndPoint().compareTo(range2.getLowEndPoint()) < 0 ||
                              (range1.getLowEndPoint().compareTo(range2.getLowEndPoint()) == 0 &&
                               (range1.getLowBoundary() == RangeBoundary.CLOSED ||
                                range2.getLowBoundary() == RangeBoundary.OPEN))) &&
                             (range1.getHighEndPoint().compareTo(range2.getHighEndPoint()) > 0 ||
                              (range1.getHighEndPoint().compareTo(range2.getHighEndPoint()) == 0 &&
                               (range1.getHighBoundary() == RangeBoundary.CLOSED ||
                                range2.getHighBoundary() == RangeBoundary.OPEN)));
            return FEELFnResult.ofResult(result);
        } catch( Exception e ) {
            // points are not comparable
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "range1", "cannot be compared to range2"));
        }
    }

}
