package org.drools.verifier.report.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

import org.drools.drl.parser.impl.Operator;
import org.drools.verifier.components.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.drools.util.Config.getConfig;

public class MissingNumberPattern extends MissingRange
    implements
    Comparable<MissingRange> {

    private static final Logger LOG = LoggerFactory.getLogger(MissingNumberPattern.class);

    private final String valueType;

    private final String value;

    public int compareTo(MissingRange another) {
        return super.compareTo( another );
    }

    public MissingNumberPattern(Field field,
                                Operator operator,
                                String valueType,
                                String value) {
        super( field,
               operator );

        this.valueType = valueType;
        this.value = value;
    }

    /**
     * Returns alway null, because there is no rule that this is related to.
     */
    public String getRuleName() {
        return null;
    }

    public String getValueAsString() {
        return value;
    }

    public Object getValueAsObject() {
        if ( valueType.equals(Field.BOOLEAN) ) {
            return Boolean.valueOf( value );
        } else if ( valueType.equals(Field.DATE) ) {
            try {
                String fmt = getConfig( "drools.dateformat" );
                if ( fmt == null ) {
                    fmt = "dd-MMM-yyyy";
                }

                return new SimpleDateFormat( fmt,
                                             Locale.ENGLISH ).parse( value );
            } catch ( ParseException e ) {
                LOG.error("Exception", e);
            }
        } else if ( valueType.equals(Field.DOUBLE) ) {
            return Double.valueOf( value );
        } else if ( valueType.equals(Field.INT) ) {
            return Integer.valueOf( value );
        }

        return value;
    }

    public String getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return "Missing restriction " + operator + " " + value;
    }

    public Collection<Cause> getCauses() {
        return null;
    }
}
