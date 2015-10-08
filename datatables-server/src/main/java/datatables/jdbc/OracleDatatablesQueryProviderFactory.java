package datatables.jdbc;

import datatables.server.TableDefinition;

import javax.sql.DataSource;

public class OracleDatatablesQueryProviderFactory implements DatatablesQueryProviderFactory {
    private DataSource dataSource;

    public OracleDatatablesQueryProviderFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DatatablesQueryProvider createInstance(String sqlQuery, TableDefinition tableDefinition) {
        return new OracleDatatablesQueryProvider(this.dataSource, sqlQuery, tableDefinition);
    }
}
