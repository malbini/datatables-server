package datatables.server;

import java.util.List;

public interface TableDefinition {
    boolean containsColumn(String columnName);

    ColumnDefinition getColumn(String columnName);

    List<ColumnDefinition> getColumns();
}
