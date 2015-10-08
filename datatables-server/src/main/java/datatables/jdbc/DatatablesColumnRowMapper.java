package datatables.jdbc;

import datatables.annotations.Column;
import org.apache.commons.lang.Validate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatatablesColumnRowMapper<T> implements RowMapper<T> {

    private static final Map<Class<?>, ColumnHandler<?>> DEFAULT_HANDLERS = defaultHandlers();

    private static Map<Class<?>, ColumnHandler<?>> defaultHandlers() {
        Map<Class<?>, ColumnHandler<?>> handlers = new HashMap<Class<?>, ColumnHandler<?>>();

        handlers.put(String.class, new ColumnHandlers.StringColumnHandler());

        handlers.put(int.class, new ColumnHandlers.IntegerColumnHandler());
        handlers.put(Integer.class, new ColumnHandlers.IntegerColumnHandler());

        handlers.put(long.class, new ColumnHandlers.LongColumnHandler());
        handlers.put(Long.class, new ColumnHandlers.LongColumnHandler());

        handlers.put(BigDecimal.class, new ColumnHandlers.BigDecimalColumnHandler());
        handlers.put(Date.class, new ColumnHandlers.DateColumnHandler());

        return Collections.unmodifiableMap(handlers);
    }

    private Map<Class<?>, ColumnHandler<?>> handlerMap = DEFAULT_HANDLERS;

    private Class<T> clazz;
    private Constructor<T> constructor;
    private List<ReadColumnValueCommand<T>> readColumnValueCommands;

    public static <T> DatatablesColumnRowMapper<T> forClass(Class<T> clazz) {
        return new DatatablesColumnRowMapper<T>(clazz);
    }

    private DatatablesColumnRowMapper(Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = this.allowNewInstanceCreation();
        this.readColumnValueCommands = this.generateReadColumnValueCommands();
    }

    public <T> void registerHandler(Class<T> clazz, ColumnHandler<T> columnHandler) {
        Validate.notNull(clazz, "clazz must be not null");
        Validate.notNull(columnHandler, "columnHandler must be not null");
        this.handlerMap.put(clazz, columnHandler);
    }

    @Override
    public final T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T object = newInstance();

        for(ReadColumnValueCommand<T> command : this.readColumnValueCommands) {
            command.read(object, rs);
        }

        return object;
    }

    private List<ReadColumnValueCommand<T>> generateReadColumnValueCommands() {
        List<ReadColumnValueCommand<T>> commands = new ArrayList<ReadColumnValueCommand<T>>();

        Field[] fields = this.clazz.getDeclaredFields();

        for(Field field : fields) {
            if(field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                final Column column = field.getAnnotation(Column.class);
                final String columnName = column.value();
                final ColumnHandler<?> columnHandler = this.handlerMap.get(field.getType());

                if(columnHandler == null) {
                    throw new RuntimeException("No Column Handler found for class: [" + field.getType().getName() + "]");
                }

                commands.add(new ReadColumnValueCommand<T>(field, columnName, columnHandler));
            }
        }

        return Collections.unmodifiableList(commands);
    }

    private Constructor<T> allowNewInstanceCreation() {
        try {
            Constructor<T> constructor = this.clazz.getDeclaredConstructor();

            if(constructor == null) {
                throw new RuntimeException("Cannot find default constructor for class: [" + this.clazz.getName() + "]");
            }

            constructor.setAccessible(true);
            return constructor;
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private T newInstance() {
        try {
            return this.constructor.newInstance();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private static class ReadColumnValueCommand<T> {
        private Field field;
        private String columnName;
        private ColumnHandler<?> columnHandler;

        private ReadColumnValueCommand(Field field, String columnName, ColumnHandler<?> columnHandler) {
            this.field = field;
            this.columnName = columnName;
            this.columnHandler = columnHandler;
        }

        public void read(T object, ResultSet rs) throws SQLException {
            try {
                this.field.set(object, this.columnHandler.readValue(rs, this.columnName));
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}
