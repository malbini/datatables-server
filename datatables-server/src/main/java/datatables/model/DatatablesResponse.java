package datatables.model;

import java.util.List;
import java.util.Map;

public interface DatatablesResponse {
    long getDraw();

    long getRecordsTotal();

    long getRecordsFiltered();

    List<Map<String, Object>> getData();
}
