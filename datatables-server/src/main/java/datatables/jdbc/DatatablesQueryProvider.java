package datatables.jdbc;

import datatables.model.DatatablesRequest;

public interface DatatablesQueryProvider {
    DatatablesQuery createQueryForRequest(DatatablesRequest datatablesRequest);
}
