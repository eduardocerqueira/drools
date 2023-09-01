package org.drools.mvel.accessors;

import java.lang.reflect.Method;

import org.drools.core.base.BaseClassFieldWriter;
import org.drools.base.base.ValueType;

public abstract class BaseShortClassFieldWriter extends BaseClassFieldWriter {

    private static final long serialVersionUID = 510l;

    public BaseShortClassFieldWriter(final Class< ? > clazz,
                                     final String fieldName) {
        super( clazz,
               fieldName );
    }

    /**
     * This constructor is not supposed to be used from outside the class hierarchy
     * 
     * @param index
     * @param fieldType
     * @param valueType
     */
    protected BaseShortClassFieldWriter(final int index,
                                        final Class< ? > fieldType,
                                        final ValueType valueType) {
        super( index,
               fieldType,
               valueType );
    }

    public BaseShortClassFieldWriter() {
    }

    public void setValue(final Object bean,
                         final Object value) {
        setShortValue( bean,
                       value == null ? 0 : ((Number) value).shortValue() );
    }

    public void setBooleanValue(final Object bean,
                                final boolean value) {
        throw new RuntimeException( "Conversion to short not supported from boolean" );
    }

    public void setByteValue(final Object bean,
                             final byte value) {
        setShortValue( bean,
                       value);

    }

    public void setCharValue(final Object bean,
                             final char value) {
        throw new RuntimeException( "Conversion to short not supported from char" );
    }

    public void setDoubleValue(final Object bean,
                               final double value) {
        setShortValue( bean,
                       (short) value );
    }

    public void setFloatValue(final Object bean,
                              final float value) {
        setShortValue( bean,
                       (short) value );
    }

    public void setIntValue(final Object bean,
                            final int value) {
        setShortValue( bean,
                       (short) value );
    }

    public void setLongValue(final Object bean,
                             final long value) {
        setShortValue( bean,
                       (short) value );
    }

    public abstract void setShortValue(final Object object,
                                       final short value);

    public Method getNativeWriteMethod() {
        try {
            return this.getClass().getDeclaredMethod("setShortValue",
                                                     Object.class, short.class);
        } catch ( final Exception e ) {
            throw new RuntimeException( "This is a bug. Please report to development team: " + e.getMessage(),
                                        e );
        }
    }

}
