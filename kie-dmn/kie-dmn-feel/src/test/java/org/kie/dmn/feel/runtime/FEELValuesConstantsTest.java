package org.kie.dmn.feel.runtime;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.runners.Parameterized;
import org.kie.dmn.api.feel.runtime.events.FEELEvent;

public class FEELValuesConstantsTest extends BaseFEELTest {

    @Parameterized.Parameters(name = "{3}: {0} ({1}) = {2}")
    public static Collection<Object[]> data() {
        final Object[][] cases = new Object[][] {
                // constants
                { "null", null , null},
                {"true", Boolean.TRUE , null},
                { "false", Boolean.FALSE , null},
                // dash is an unary test that always matches, so for now, returning true.
                // have to double check to know if this is not the case
                { "-", null, FEELEvent.Severity.ERROR },
                { ".872", new BigDecimal( "0.872" ) , null},
                { ".872e+21", new BigDecimal( "0.872e+21" ) , null},
                { ".872E+21", new BigDecimal( "0.872e+21" ) , null},
                { ".872e-21", new BigDecimal( "0.872e-21" ) , null},
                { ".872E-21", new BigDecimal( "0.872e-21" ) , null},
                { "-.872", new BigDecimal( "-0.872" ) , null},
                { "-.872e+21", new BigDecimal( "-0.872e+21" ) , null},
                { "-.872E+21", new BigDecimal( "-0.872e+21" ) , null},
                { "-.872e-21", new BigDecimal( "-0.872e-21" ) , null},
                { "-.872E-21", new BigDecimal( "-0.872e-21" ) , null},
                { "+.872", new BigDecimal( "0.872" ) , null},
                { "+.872e+21", new BigDecimal( "0.872e+21" ) , null},
                { "+.872E+21", new BigDecimal( "0.872e+21" ) , null},
                { "+.872e-21", new BigDecimal( "0.872e-21" ) , null},
                { "+.872E-21", new BigDecimal( "0.872e-21" ) , null},

                { "50", new BigDecimal( "50" ) , null},
                { "50e+21", new BigDecimal( "50e+21" ) , null},
                { "50E+21", new BigDecimal( "50e+21" ) , null},
                { "50e-21", new BigDecimal( "50e-21" ) , null},
                { "50E-21", new BigDecimal( "50e-21" ) , null},
                { "-50", new BigDecimal( "-50" ) , null},
                { "-50e+21", new BigDecimal( "-50e+21" ) , null},
                { "-50E+21", new BigDecimal( "-50e+21" ) , null},
                { "-50e-21", new BigDecimal( "-50e-21" ) , null},
                { "-50E-21", new BigDecimal( "-50e-21" ) , null},
                { "+50", new BigDecimal( "50" ) , null},
                { "+50e+21", new BigDecimal( "50e+21" ) , null},
                { "+50E+21", new BigDecimal( "50e+21" ) , null},
                { "+50e-21", new BigDecimal( "50e-21" ) , null},
                { "+50E-21", new BigDecimal( "50e-21" ) , null},
                { "50.872", new BigDecimal( "50.872" ) , null},
                { "50.872e+21", new BigDecimal( "50.872e+21" ) , null},
                { "50.872E+21", new BigDecimal( "50.872e+21" ) , null},
                { "50.872e-21", new BigDecimal( "50.872e-21" ) , null},
                { "50.872E-21", new BigDecimal( "50.872e-21" ) , null},
                { "-50.567", new BigDecimal( "-50.567" ) , null},
                { "-50.872e+21", new BigDecimal( "-50.872e+21" ) , null},
                { "-50.872E+21", new BigDecimal( "-50.872e+21" ) , null},
                { "-50.872e-21", new BigDecimal( "-50.872e-21" ) , null},
                { "-50.872E-21", new BigDecimal( "-50.872e-21" ) , null},
                { "+50.567", new BigDecimal( "50.567" ) , null},
                { "+50.872e+21", new BigDecimal( "50.872e+21" ) , null},
                { "+50.872E+21", new BigDecimal( "50.872e+21" ) , null},
                { "+50.872e-21", new BigDecimal( "50.872e-21" ) , null},
                { "+50.872E-21", new BigDecimal( "50.872e-21" ) , null},
                // quotes are a syntactical markup character for strings, so they disappear when the expression is evaluated
                { "\"foo bar\"", "foo bar" , null},
                { "\"šomeÚnicodeŠtriňg\"", "šomeÚnicodeŠtriňg" , null},
                { "\"横綱\"", "横綱" , null},
                { "\"thisIsSomeLongStringThatMustBeProcessedSoHopefullyThisTestPassWithItAndIMustWriteSomethingMoreSoItIsLongerAndLongerAndLongerAndLongerAndLongerTillItIsReallyLong\"", "thisIsSomeLongStringThatMustBeProcessedSoHopefullyThisTestPassWithItAndIMustWriteSomethingMoreSoItIsLongerAndLongerAndLongerAndLongerAndLongerTillItIsReallyLong" , null},
                { "\"\"", "" , null},
                { "-\"10\"", null , FEELEvent.Severity.ERROR},
                { "-string(\"10\")", null , FEELEvent.Severity.ERROR},
                { "+\"10\"", null , FEELEvent.Severity.ERROR},
        };
        return addAdditionalParameters(cases, false);
    }
}
