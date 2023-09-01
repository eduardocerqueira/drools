package org.kie.dmn.feel.runtime.functions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.runtime.FEELFunction;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;

public class SortFunction
        extends BaseFEELFunction {

    public SortFunction() {
        super( "sort" );
    }

    public FEELFnResult<List<Object>> invoke(@ParameterName( "ctx" ) EvaluationContext ctx,
                                             @ParameterName("list") List list,
                                             @ParameterName("precedes") FEELFunction function) {
        if ( function == null ) {
            return invoke( list );
        } else {
            return invoke(list, (a, b) -> {
                final Object result = function.invokeReflectively(ctx, new Object[]{a, b});
                if (!(result instanceof Boolean) || ((Boolean) result)) {
                    return -1;
                } else {
                    return 1;
                }
            } );
        }
    }

    public FEELFnResult<List<Object>> invoke(@ParameterName("list") List list) {
        return invoke(list, (a, b) -> {
            if( a instanceof Comparable && b instanceof Comparable ) {
                return ((Comparable) a).compareTo( b );
            } else {
                return 0;
            }
        });
    }

    private FEELFnResult<List<Object>> invoke(final List list, final Comparator<? super Object> comparator) {
        if ( list == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "cannot be null"));
        }
        final List<Object> newList = new ArrayList<>( list );
        try {
            newList.sort(comparator);
        } catch (final Throwable ex) {
            return FEELFnResult.ofError(
                    new InvalidParametersEvent(Severity.ERROR, "list",
                            "raised an exception while sorting by natural order", ex ) );
        }

        return FEELFnResult.ofResult( newList );
    }

}
