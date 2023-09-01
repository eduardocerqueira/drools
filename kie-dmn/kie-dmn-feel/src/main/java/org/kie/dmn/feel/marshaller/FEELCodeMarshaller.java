package org.kie.dmn.feel.marshaller;

import java.util.function.Function;

import org.kie.dmn.feel.FEEL;
import org.kie.dmn.feel.lang.Type;
import org.kie.dmn.feel.runtime.functions.extended.CodeFunction;
import org.kie.dmn.feel.runtime.functions.extended.KieExtendedDMNFunctions;

import static org.kie.dmn.feel.lang.types.BuiltInType.justNull;

/**
 * An implementation of the FEEL marshaller interface
 * that converts FEEL objects into it's string representation
 * and vice versa
 */
public class FEELCodeMarshaller
        implements FEELMarshaller<String> {

    public static final FEELCodeMarshaller INSTANCE = new FEELCodeMarshaller();
    private FEEL feel = FEEL.newInstance();

    private FEELCodeMarshaller() {}

    /**
     * Marshalls the given value into FEEL code that can be executed to
     * reconstruct the value. For instance, here are some examples of the marshalling process:
     *
     * * number 10 marshalls as: 10
     * * string foo marshalls as: "foo"
     * * duration P1D marshalls as: duration( "P1D" )
     * * context { x : 10, y : foo } marshalls as: { x : 10, y : "foo" }
     *
     * @param value the FEEL value to be marshalled
     *
     * @return a string representing the FEEL code that needs to be executed to reconstruct the value
     */
    @Override
    public String marshall(Object value) {
        if( value == null ) {
            return "null";
        }
        return KieExtendedDMNFunctions.getFunction(CodeFunction.class).invoke(value).cata(justNull(), Function.identity());
    }

    /**
     * Unmarshalls the string into a FEEL value by executing it.
     *
     * @param feelType this parameter is ignored by this marshaller and can be set to null
     * @param value the FEEL code to execute for unmarshalling
     *
     * @return the value resulting from executing the code
     */
    @Override
    public Object unmarshall(Type feelType, String value) {
        return feel.evaluate( value );
    }
}
