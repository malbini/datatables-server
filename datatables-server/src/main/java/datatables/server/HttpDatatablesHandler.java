package datatables.server;

import datatables.model.DatatablesResponse;

import javax.servlet.http.HttpServletRequest;

public interface HttpDatatablesHandler extends DatatablesHandler {
    DatatablesResponse handle(HttpServletRequest request);
}
