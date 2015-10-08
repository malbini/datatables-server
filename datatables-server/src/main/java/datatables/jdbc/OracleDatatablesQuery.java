package datatables.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OracleDatatablesQuery implements DatatablesQuery {

    private static final String NUM_COLUMN_NAME = "DATATABLES_NUM";
    private static final String START_PARAMETER_NAME = "datatables_start";
    private static final String END_PARAMETER_NAME = "datatables_end";

    private NamedParameterJdbcOperations jdbcOperations;
    private String query;
    private Map<String, Object> parameters = new LinkedHashMap<String, Object>();

    public OracleDatatablesQuery(DataSource dataSource, String query) {
        this(new NamedParameterJdbcTemplate(dataSource), query);
    }

    public OracleDatatablesQuery(NamedParameterJdbcOperations jdbcOperations, String query) {
        this.jdbcOperations = jdbcOperations;
        this.query = query;
    }

    public void setParameter(String name, Object value) {
        this.parameters.put(name, value);
    }

    @Override
    public <T> List<T> data(RowMapper<T> rowMapper) {
        return this.jdbcOperations.query(this.query, rowMapper);
    }

    @Override
    public <T> List<T> limitedData(RowMapper<T> rowMapper, long start, long end) {
        SqlBuilder dataQueryBuilder = new SqlBuilder();
        dataQueryBuilder.append("SELECT").append("ROWNUM AS").append(NUM_COLUMN_NAME).append(",").append("DATA.*");
        dataQueryBuilder.append("FROM");
        dataQueryBuilder.appendQuery(this.query, "DATA");

        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.append("SELECT *");
        sqlBuilder.append("FROM");
        sqlBuilder.appendQuery(dataQueryBuilder.toString());
        sqlBuilder.append("WHERE").append(NUM_COLUMN_NAME).append(">=").appendParameter(START_PARAMETER_NAME)
                  .append("AND").append().append(NUM_COLUMN_NAME).append("<=").appendParameter(END_PARAMETER_NAME);
        sqlBuilder.append("ORDER BY").append(NUM_COLUMN_NAME);

        Map<String,Object> params = new LinkedHashMap<String, Object>();
        params.putAll(this.parameters);
        params.put(START_PARAMETER_NAME, start);
        params.put(END_PARAMETER_NAME, end);

        return this.jdbcOperations.query(sqlBuilder.toString(), params, rowMapper);
    }

    @Override
    public long count() {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.append("SELECT COUNT(*)");
        sqlBuilder.append("FROM");
        sqlBuilder.appendQuery(this.query);

        Map<String,Object> params = new LinkedHashMap<String, Object>();
        params.putAll(this.parameters);

        return this.jdbcOperations.queryForObject(sqlBuilder.toString(), params, Long.class);
    }
}
