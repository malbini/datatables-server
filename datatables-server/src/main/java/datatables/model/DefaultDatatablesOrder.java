package datatables.model;

public final class DefaultDatatablesOrder implements DatatablesOrder {
    private int column;
    private OrderType dir;

    public DefaultDatatablesOrder(int column, OrderType dir) {
        this.column = column;
        this.dir = dir;
    }

    public int getColumn() {
        return column;
    }

    public OrderType getDir() {
        return dir;
    }
}
