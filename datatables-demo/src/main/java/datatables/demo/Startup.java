package datatables.demo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class Startup implements InitializingBean {
    private static final QueryLoader QUERY_LOADER = new QueryLoader();

    private JdbcOperations jdbcOperations;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcOperations = new JdbcTemplate(dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String createSchemaQuery = QUERY_LOADER.load("/schema.sql");
        this.jdbcOperations.execute(createSchemaQuery);
    }
}
