package org.drools.decisiontable;

import java.util.List;

import org.drools.decisiontable.parser.DecisionTableParser;
import org.drools.decisiontable.parser.csv.CsvLineParser;
import org.drools.decisiontable.parser.csv.CsvParser;
import org.drools.decisiontable.parser.xls.ExcelParser;
import org.drools.template.parser.DataListener;
import org.kie.internal.builder.DecisionTableInputType;

/**
 * Provides valid input types for decision tables.
 * (which also serve as parser factories).
 */
public abstract class InputType {
    public static final InputType XLS = new XlsInput();
    public static final InputType CSV = new CsvInput();

    protected InputType() {

    }

    /**
     * @param listener
     * @return The appropriate Parser. 
     */
    public abstract DecisionTableParser createParser(DataListener listener);
    public abstract DecisionTableParser createParser(List<DataListener> listeners);

    /**
     * Converts DecisionTableInputType to InputType.
     *
     * @param decisionTableInputType DecisionTableInputType to convert
     * @return the appropriate InputType based on the specified DecisionTableInputType
     */
    public static InputType getInputTypeFromDecisionTableInputType(final DecisionTableInputType decisionTableInputType) {
        switch (decisionTableInputType) {
            case CSV:
                return InputType.CSV;
            case XLS:
            case XLSX:
                return InputType.XLS;
            default: throw new IllegalArgumentException("Unsupported DecisionTableInputType: " + decisionTableInputType + "!");
        }
    }
}

class XlsInput extends InputType {

    public DecisionTableParser createParser(final DataListener listener) {
        return new ExcelParser( listener );
    }
    public DecisionTableParser createParser(final List<DataListener> listeners) {
        return new ExcelParser( listeners );
    }

}

class CsvInput extends InputType {

    public DecisionTableParser createParser(final DataListener listener) {
        return new CsvParser( listener,
                              new CsvLineParser() );
    }

    public DecisionTableParser createParser(final List<DataListener> listeners) {
        return new CsvParser( listeners,
                new CsvLineParser() );
    }
    
}
