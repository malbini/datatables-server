package datatables.server;

import java.util.List;
import java.util.Map;

public interface ResponseDataMapper<T> {
    List<Map<String, Object>> map(List<T> data);
}
