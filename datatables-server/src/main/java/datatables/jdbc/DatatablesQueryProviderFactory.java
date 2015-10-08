package datatables.jdbc;

import datatables.server.TableDefinition;

public interface DatatablesQueryProviderFactory {
    DatatablesQueryProvider createInstance(String sqlQuery, TableDefinition tableDefinition);
}
