package datatables.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultDatatablesResponse implements DatatablesResponse {
    private long draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    public long getDraw() {
        return draw;
    }

    public void setDraw(long draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
