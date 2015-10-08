package datatables.server;

import datatables.jdbc.DatatablesQuery;

public interface DatatablesQueryFilter {
    void applyFilter(DatatablesQuery query);
}
