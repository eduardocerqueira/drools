package org.kie.dmn.feel.runtime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.runners.Parameterized;

public class TCFoldNotTCFoldTest extends BaseFEELTest {

    @Parameterized.Parameters(name = "{3}: {0} ({1}) = {2},   Kie-Extended: {4}")
    public static Collection<Object[]> data() {
        final Object[][] cases =
                new Object[][]{
                               {"date(\"2021-02-12\")", LocalDate.of(2021, 2, 12), null},
                               {"{date : function(s) s, r: date(\"2021-02-12\")}.r", "2021-02-12", null},
                               {"{fs : [function(s) s], r: for date in fs return date(\"2021-02-12\")}.r[1]", "2021-02-12", null},
                               {"{fs : [function(s) s], r: every date in fs satisfies date(\"2021-02-12\") = \"2021-02-12\"}.r", Boolean.TRUE, null},
                               {"{f : function(date) date(\"2021-02-12\"), id : function(x) x, r: f(id)}.r", "2021-02-12", null},
                               {"date and time(\"2021-02-12T12:34:56\")", LocalDateTime.of(2021, 2, 12, 12, 34, 56), null},
                               {"{date and time : function(s) s, r: date and time(\"2021-02-12T12:34:56\")}.r", "2021-02-12T12:34:56", null},
                               {"{fs : [function(s) s], r: for date and time in fs return date and time(\"2021-02-12T12:34:56\")}.r[1]", "2021-02-12T12:34:56", null},
                               {"{fs : [function(s) s], r: every date and time in fs satisfies date and time(\"2021-02-12T12:34:56\") = \"2021-02-12T12:34:56\"}.r", Boolean.TRUE, null},
                               {"{f : function(date and time) date and time(\"2021-02-12T12:34:56\"), id : function(x) x, r: f(id)}.r", "2021-02-12T12:34:56", null},
        };
        final Collection<Object[]> c = addAdditionalParameters(cases, false);
        c.addAll(addAdditionalParameters(cases, true));
        return c;
    }
}
