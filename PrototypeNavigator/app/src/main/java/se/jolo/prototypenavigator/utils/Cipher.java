package se.jolo.prototypenavigator.utils;

import java.util.ArrayList;
import java.util.Collections;
/* The Cipher class decodes the encrypted xml file. */
public final class Cipher {

    private Cipher() {}

    public static byte[] decrypt(byte[] data, byte[] key) {

		/* Create a table with the given key */
        Table table = new Table(key, data);

		/* Sort the columns by the order of the corresponding key characters */
        table.sortColumnsByKey();

		/* Write data into the table, row-by-row */
        table.write(data, false);

		/* Restore the order of the columns to match the key */
        table.restoreColumns();

		/*  Read back the data, column-by-column */
        byte[] decryptedData = table.read(true);

		/* Trim trailing zeros in encryptedData */
        return table.trim(decryptedData);
    }

    private static class Table {

        byte[] key;
        ArrayList<Column> columns;
        ArrayList<Column> savedColumns;
        int numRows;

        public Table(byte[] key, byte[] data) {
            this.key = key;
            this.columns = new ArrayList<Column>();
            this.savedColumns = new ArrayList<Column>();

            int numCols = key.length;
            numRows = (int) Math.ceil((float) data.length / (float) numCols);

            // Pre-allocate table wth numColums Columns holding numRows bytes
            for (int i = 0; i < numCols; i++) {
                columns.add(new Column(key[i], numRows));
            }
        }

        /**
         * Rearrange columns by sorting them by their key characters
         */
        public void sortColumnsByKey() {
            savedColumns.clear();
            for (int i = 0; i < columns.size(); i++) {
                savedColumns.add(columns.get(i));
            }
            Collections.sort(columns);
        }

        /**
         * Rearrange columns to their original order given by the order of the key characters in the key
         */
        public void restoreColumns() {
            columns.clear();
            for (int i = 0; i < savedColumns.size(); i++) {
                columns.add(savedColumns.get(i));
            }
            savedColumns.clear();
        }

        /**
         * Read data from table, column by column or row by row
         */
        public byte[] read(boolean columnByColumn) {

            int numCols = columns.size();

            byte[] tableData = new byte[numRows * numCols];
            int tableDataPos = 0;

            if (columnByColumn) {
                for (int c = 0; c < numCols; c++) {
                    System.arraycopy(columns.get(c).getBytes(), 0, tableData, tableDataPos, numRows);
                    tableDataPos += numRows;
                }
            } else {
                for (int r = 0; r < numRows; r++) {
                    for (int c = 0; c < numCols; c++) {
                        tableDataPos = r * numCols + c;
                        tableData[tableDataPos] = columns.get(c).getBytes()[r];
                    }
                }
            }

            return tableData;
        }

        /**
         * Write data to table, column by column or row by row
         */
        public void write(byte[] data, boolean columnByColumn) {

            int numCols = columns.size();

            byte[] tableData = new byte[numRows * numCols];
            System.arraycopy(data, 0, tableData, 0, data.length);

            int tableDataPos = 0;

            // Write to table
            if (columnByColumn) {
                for (int c = 0; c < numCols; c++) {
                    System.arraycopy(tableData, tableDataPos, columns.get(c).getBytes(), 0, numRows);
                    tableDataPos += numRows;
                }
            } else {
                for (int r = 0; r < numRows; r++) {
                    for (int c = 0; c < numCols; c++) {
                        tableDataPos = r * numCols + c;
                        columns.get(c).getBytes()[r] = tableData[tableDataPos];
                    }
                }
            }
        }

        public byte[] trim(byte[] data) {

            int lastNonZeroByte = data.length - 1;
            while (data[lastNonZeroByte] == 0) {
                lastNonZeroByte--;
            }

            // If zeros where found, trim data
            if (lastNonZeroByte != data.length - 1) {
                byte[] trimmedData = new byte[lastNonZeroByte + 1];
                System.arraycopy(data, 0, trimmedData, 0, lastNonZeroByte + 1);
                return trimmedData;
            } else {
                return data;
            }
        }

        private class Column implements Comparable<Column> {

            public Column(byte keyChar, int size) {
                this.keyChar = keyChar;
                this.data = new byte[size];
            }

            public byte[] getBytes() {
                return data;
            }

            public int compareTo(Column column) {
                if (keyChar < column.keyChar)
                    return -1;
                else if (keyChar > column.keyChar)
                    return 1;
                else
                    return 0;
            }

            byte keyChar;
            byte[] data;
        }
    }
}