package datatables.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ColumnHandler<T> {
    T readValue(ResultSet rs, String columnName) throws SQLException;
}
