package datatables.server;

import org.apache.commons.lang.ClassUtils;

public final class ColumnDefinition {
    private String name;
    private Class<?> type;
    private String source;
    private String format;

    public ColumnDefinition(String name, Class<?> type, String source, String format) {
        this.name = name;
        this.type = type;
        this.source = source;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Class<?> getWrapperType() {
        return ClassUtils.primitiveToWrapper(this.type);
    }

    public String getSource() {
        return source;
    }

    public String getFormat() {
        return format;
    }
}
