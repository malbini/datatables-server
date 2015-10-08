package datatables.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface DatatablesQuery {

    public static final String DATATABLES_SEARCH_PARAM_NAME = "datatablesSearch";

    public void setParameter(String name, Object value);

    public <T> List<T> data(RowMapper<T> rowMapper);

    public <T> List<T> limitedData(RowMapper<T> rowMapper, long start, long end);

    public long count();
}
