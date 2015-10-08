package datatables.server;

import datatables.annotations.Column;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AnnotationTableDefinitionFactory {
    private AnnotationTableDefinitionFactory() {
    }

    public static TableDefinition forClass(Class<?> clazz) {
        Map<String, ColumnDefinition> columns = parseColumns(clazz);
        return new DefaultTableDefinition(columns);
    }

    private static Map<String, ColumnDefinition> parseColumns(Class<?> clazz) {
        Map<String, ColumnDefinition> columnsMap = new LinkedHashMap<String, ColumnDefinition>();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);

                final String columnName = field.getName();
                final Class<?> columnType = field.getType();
                final String source = column.value();
                final String format = StringUtils.trimToNull(column.format());

                final ColumnDefinition columnDefinition = new ColumnDefinition(columnName, columnType, source, format);
                columnsMap.put(columnName, columnDefinition);
            }
        }

        return Collections.unmodifiableMap(columnsMap);
    }
}
