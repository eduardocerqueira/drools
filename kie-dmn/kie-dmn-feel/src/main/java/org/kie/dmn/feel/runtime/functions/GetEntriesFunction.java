package org.kie.dmn.feel.runtime.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.extended.ContextPutFunction;

public class GetEntriesFunction extends BaseFEELFunction {

    public GetEntriesFunction() {
        super("get entries");
    }

    public FEELFnResult<List<Object>> invoke(@ParameterName("m") Object m) {
        if (m == null) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "m", "cannot be null"));
        }
        return ContextPutFunction.toMap(m).map(GetEntriesFunction::toEntries);
    }

    private static List<Object> toEntries(Map<?, ?> m) {
        List<Object> result = m.entrySet().stream()
                                             .map(kv -> {
                                                 Map<Object, Object> entry = new HashMap<>();
                                                 entry.put("key", kv.getKey());
                                                 entry.put("value", kv.getValue());
                                                 return entry;
                                             })
                                             .collect(Collectors.toList());
        return result;
    }
}
