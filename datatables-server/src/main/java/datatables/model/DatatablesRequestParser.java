package datatables.model;

import javax.servlet.http.HttpServletRequest;

public interface DatatablesRequestParser {
    DatatablesRequest parse(HttpServletRequest request);
}
