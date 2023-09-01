package org.kie.dmn.feel.lang.impl;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kie.dmn.feel.runtime.FEELFunction;
import org.kie.dmn.feel.runtime.functions.BuiltInFunctions;
import org.kie.dmn.feel.util.EvalHelper;

/**
 * This is a thread safe implementation of a root
 * execution frame that automatically registers all
 * the built in functions.
 */
public class RootExecutionFrame implements ExecutionFrame {

    public static final ExecutionFrame INSTANCE = new RootExecutionFrame();

    private final Map<String, Object> functions;

    private RootExecutionFrame() {
        Map<String, Object> builtIn = new ConcurrentHashMap<>(  );
        for( FEELFunction f : BuiltInFunctions.getFunctions() ) {
            builtIn.put( EvalHelper.normalizeVariableName( f.getName() ), f );
        }
        functions = Collections.unmodifiableMap( builtIn );
    }

    public Object getValue(String symbol) {
        symbol = EvalHelper.normalizeVariableName( symbol );
        if ( functions.containsKey( symbol ) ) {
            return functions.get( symbol );
        }
        return null;
    }

    public boolean isDefined( String symbol ) {
        symbol = EvalHelper.normalizeVariableName( symbol );
        return functions.containsKey( symbol );
    }

    public void setValue(String symbol, Object value) {
        throw new UnsupportedOperationException( "No value or variable can be set on the RootExecutionFrame" );
    }

    public Map<String, Object> getAllValues() {
        return this.functions;
    }

    @Override
    public void setRootObject(Object v) {
        throw new UnsupportedOperationException("Setting root object is not supported on the Root frame");
    }

    @Override
    public Object getRootObject() {
        return null;
    }
}
