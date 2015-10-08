package datatables.server;

import datatables.model.DatatablesRequest;
import datatables.model.DatatablesResponse;

public interface DatatablesHandler {
    DatatablesResponse handle(DatatablesRequest datatablesRequest);

    DatatablesResponse handle(DatatablesRequest datatablesRequest, DatatablesQueryFilter filter);
}
