package uk.co.informaticslab.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by tom on 08/05/2015.
 */
public class DataRow {

    private static final String DEFAULT_DELIMITER = ",";

    private final List<Serializable> data;
    private final String delimiter;

    public DataRow() {
        this(DEFAULT_DELIMITER);
    }

    public DataRow(String delimiter) {
        this.data = new ArrayList<>();
        this.delimiter = delimiter;
    }

    /**
     * Adds an element to the given DataRow
     * @param serializable element to add to this data row
     */
    public void addData(Serializable serializable) {
        data.add(serializable);
    }

    /**
     * @return the row of data as single string separated by the specified delimited
     */
    public String getDataAsRow() {
        StringBuilder sb = new StringBuilder();
        for (Serializable s : data) {
            sb.append(s);
            sb.append(delimiter);
        }
        sb.deleteCharAt(sb.lastIndexOf(delimiter));
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRow dataRow = (DataRow) o;
        return Objects.equals(data, dataRow.data) &&
                Objects.equals(delimiter, dataRow.delimiter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, delimiter);
    }

    @Override
    public String toString() {
        return "DataRow{" +
                "data=" + data +
                ", delimiter='" + delimiter + '\'' +
                '}';
    }

}
