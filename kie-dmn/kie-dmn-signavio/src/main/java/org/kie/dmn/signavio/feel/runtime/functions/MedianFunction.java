package org.kie.dmn.signavio.feel.runtime.functions;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

public class MedianFunction
        extends BaseFEELFunction {
    public static final MedianFunction INSTANCE = new MedianFunction();

    MedianFunction() {
        super("median");
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName("list") List<?> list) {
        if (list == null) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "cannot be null"));
        }
        if (list.isEmpty()) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "cannot be empty"));
        }

        List<?> sorted = list.stream().sorted().collect(Collectors.toList());

        if (sorted.size() % 2 == 0) {
            // even sized list
            int medianPos0 = (sorted.size() / 2) - 1;
            int medianPos1 = medianPos0 + 1;
            if (!(sorted.get(medianPos0) instanceof BigDecimal) || !(sorted.get(medianPos1) instanceof BigDecimal)) {
                return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "does not contain number"));
            }
            BigDecimal median0 = (BigDecimal) sorted.get(medianPos0);
            BigDecimal median1 = (BigDecimal) sorted.get(medianPos1);
            BigDecimal medianAvg = median0.add(median1).divide(new BigDecimal(2, MathContext.DECIMAL128), MathContext.DECIMAL128);
            return FEELFnResult.ofResult(medianAvg);
        } else {
            int medianPos = sorted.size() / 2;
            Object median = sorted.get(medianPos);
            if (!(median instanceof BigDecimal)) {
                return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "does not contain number"));
            }
            return FEELFnResult.ofResult((BigDecimal) median);
        }
    }
}
