package datatables.model;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class DefaultDatatablesRequest implements DatatablesRequest {
    private long draw;
    private long start;
    private long length;
    private DatatablesSearch search;
    private List<DatatablesColumn> columns;
    private List<DatatablesOrder> orders;

    public long getDraw() {
        return draw;
    }

    public void setDraw(long draw) {
        this.draw = draw;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public DatatablesSearch getSearch() {
        return search;
    }

    public void setSearch(DatatablesSearch search) {
        this.search = search;
    }

    public boolean hasSearch() {
        if(this.search == null) {
            return false;
        }

        return StringUtils.isNotBlank(this.search.getValue());
    }

    public List<DatatablesColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DatatablesColumn> columns) {
        this.columns = columns;
    }

    public DatatablesColumn getColumnAt(int index) {
        return this.columns.get(index);
    }

    public List<DatatablesOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DatatablesOrder> orders) {
        this.orders = orders;
    }

    public boolean hasOrder() {
        return this.orders.size() > 0;
    }
}
