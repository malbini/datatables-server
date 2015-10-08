package datatables.server;

import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DefaultTableDefinition implements TableDefinition {
    private Map<String, ColumnDefinition> columns;

    public DefaultTableDefinition(Map<String, ColumnDefinition> columns) {
        Validate.notNull(columns, "columns cannot be null");
        this.columns = new LinkedHashMap<String, ColumnDefinition>(columns);
    }

    public boolean containsColumn(String columnName) {
        return this.columns.containsKey(columnName);
    }

    public ColumnDefinition getColumn(String columnName) {
        return this.columns.get(columnName);
    }

    public List<ColumnDefinition> getColumns() {
        return Collections.unmodifiableList(new ArrayList<ColumnDefinition>(this.columns.values()));
    }
}
