package datatables.model;

import java.util.List;

public interface DatatablesRequest {
    long getDraw();

    long getStart();

    long getLength();

    DatatablesSearch getSearch();

    boolean hasSearch();

    List<DatatablesColumn> getColumns();

    DatatablesColumn getColumnAt(int index);

    List<DatatablesOrder> getOrders();

    boolean hasOrder();
}
