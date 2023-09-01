package org.drools.base.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import org.drools.base.time.TimeUtils;

/**
 * A parameters parser that uses JodaTime for time units parsing.
 */
public class TimeIntervalParser {

    private TimeIntervalParser() { }

    public static long[] parse(String paramText) {
        if ( paramText == null || paramText.trim().length() == 0 ) {
            return new long[0];
        }
        String[] params = paramText.split( "," );
        long[] result = new long[params.length];
        for ( int i = 0; i < params.length; i++ ) {
            result[i] = parseSingle( params[i] );
        }
        return result;
    }

    public static long parseSingle(String param) {
        param = param.trim();
        if ( param.length() > 0 ) {
            return TimeUtils.parseTimeString( param );
        }
        throw new RuntimeException( "Empty parameters not allowed in: [" + param + "]" );
    }

    public static long getTimestampFromDate( Object obj ) {
        if (obj instanceof Long ) {
            return ( Long ) obj;
        }
        if (obj instanceof Date) {
            return ( (Date) obj ).getTime();
        }
        try {
            if (obj instanceof LocalDate) {
                return ((LocalDate) obj).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            if (obj instanceof LocalDateTime) {
                return ((LocalDateTime) obj).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            if (obj instanceof ZonedDateTime) {
                return ((ZonedDateTime) obj).toInstant().toEpochMilli();
            }
            if (obj instanceof Instant) {
                return ((Instant) obj).toEpochMilli();
            }
        } catch (ArithmeticException ae) {
            throw new RuntimeException("Cannot convert " + obj.getClass().getSimpleName() + " '" + obj + "' into a long value");
        }
        throw new RuntimeException("Cannot extract timestamp from " + obj);
    }
}
