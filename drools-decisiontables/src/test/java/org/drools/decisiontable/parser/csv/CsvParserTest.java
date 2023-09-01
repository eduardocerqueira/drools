package org.drools.decisiontable.parser.csv;

import java.util.HashMap;
import java.util.Map;

import org.drools.template.parser.DataListener;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.drools.template.parser.DataListener.NON_MERGED;

public class CsvParserTest {

    @Test
    public void testCsv() {
        final MockSheetListener listener = new MockSheetListener();
        final CsvLineParser lineParser = new CsvLineParser();
        final CsvParser parser = new CsvParser(listener, lineParser);

        parser.parseFile(getClass().getResourceAsStream("/data/TestCsv.drl.csv"));
        
        assertThat(listener.getCell(0, 0)).isEqualTo("A");
        assertThat(listener.getCell(0, 1)).isEqualTo("B");
        assertThat(listener.getCell(2, 0)).isEqualTo("");
        assertThat(listener.getCell(1, 0)).isEqualTo("C");
        assertThat(listener.getCell(1, 1)).isEqualTo("D");
        assertThat(listener.getCell(1, 3)).isEqualTo("E");

    }

    /**
     * Test the handling of merged cells.
     */
    @Test
    public void testCellMergeHandling() {
        CsvParser parser = new CsvParser((DataListener) null, null);
        assertThat(parser.calcStartMerge(NON_MERGED, 1, "foo")).isEqualTo(NON_MERGED);
        assertThat(parser.calcStartMerge(NON_MERGED, 42, "...")).isEqualTo(42);

        assertThat(parser.calcStartMerge(42, 43, "...")).isEqualTo(42);

        assertThat(parser.calcStartMerge(42, 44, "VanHalen")).isEqualTo(NON_MERGED);

        assertThat(parser.calcCellText(NON_MERGED, "VanHalen")).isEqualTo("VanHalen");
        assertThat(parser.calcCellText(42, "VanHalen...")).isEqualTo("VanHalen");
        assertThat(parser.calcCellText(42, "...")).isEqualTo("");
    }

    static class MockSheetListener implements DataListener {

        Map<String, String> data = new HashMap<>();

        public String getCell(final int row,
                              final int col) {
            return this.data.get(cellKey(row, col));
        }

        public void startSheet(final String name) {
        }

        public void finishSheet() {
        }

        public void newRow(final int rowNumber,
                           final int columns) {

        }

        public void newCell(final int row,
                            final int column,
                            final String value,
                            final int mergeCellStart) {

            this.data.put(cellKey(row, column), value);
        }

        String cellKey(final int row, final int column) {
            return "R" + row + "C" + column;
        }
    }
}
