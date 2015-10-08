package datatables.server;

public abstract class AbstractColumnMapper<T> implements ColumnValueMapper {
    private Class<T> clazz;

    protected AbstractColumnMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public final Object map(Object value) {
        return this.doMap(this.clazz.cast(value));
    }

    protected abstract Object doMap(T value);
}
