package datatables.jdbc;

import datatables.server.ColumnDefinition;
import datatables.model.DatatablesColumn;
import datatables.model.DatatablesOrder;
import datatables.model.DatatablesRequest;
import datatables.server.TableDefinition;

import javax.sql.DataSource;

public class OracleDatatablesQueryProvider implements DatatablesQueryProvider {

    private DataSource dataSource;
    private String sqlQuery;
    private TableDefinition tableDefinition;

    public OracleDatatablesQueryProvider(DataSource dataSource, String sqlQuery, TableDefinition tableDefinition) {
        this.dataSource = dataSource;
        this.sqlQuery = sqlQuery;
        this.tableDefinition = tableDefinition;
    }

    @Override
    public DatatablesQuery createQueryForRequest(DatatablesRequest datatablesRequest) {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.append("SELECT *");
        sqlBuilder.append("FROM");
        sqlBuilder.appendQuery(this.sqlQuery);
        sqlBuilder.append("WHERE 1=1");

        // Filter
        this.appendFilter(datatablesRequest, sqlBuilder);

        // Order
        this.appendOrder(datatablesRequest, sqlBuilder);

        return new OracleDatatablesQuery(this.dataSource, sqlBuilder.toString());
    }

    private void appendFilter(DatatablesRequest datatablesRequest, SqlBuilder sqlBuilder) {
        if (!datatablesRequest.hasSearch()) {
            return;
        }

        StringBuilder searchQuery = new StringBuilder();

        for (DatatablesColumn column : datatablesRequest.getColumns()) {
            final String columnName = column.getData();

            if (column.isSearchable() && this.tableDefinition.containsColumn(columnName)) {

                final ColumnDefinition columnDefinition = this.tableDefinition.getColumn(columnName);

                if (columnDefinition.getType().isAssignableFrom(String.class)) {
                    if (searchQuery.length() > 0) {
                        searchQuery.append(" OR ");
                    }

                    searchQuery.append("UPPER(").append(columnDefinition.getSource()).append(")").append(" LIKE UPPER(:").append(DatatablesQuery.DATATABLES_SEARCH_PARAM_NAME).append(")");
                }
            }
        }

        if (searchQuery.length() > 0) {
            sqlBuilder.append("AND");
            sqlBuilder.appendInner(searchQuery.toString());
        }
    }

    private void appendOrder(DatatablesRequest datatablesRequest, SqlBuilder sqlBuilder) {
        if (!datatablesRequest.hasOrder()) {
            return;
        }

        StringBuilder orderQuery = new StringBuilder();

        for (DatatablesOrder order : datatablesRequest.getOrders()) {
            DatatablesColumn column = datatablesRequest.getColumnAt(order.getColumn());
            final String columnName = column.getData();

            if (column.isOrderable() && this.tableDefinition.containsColumn(columnName)) {
                if (orderQuery.length() > 0) {
                    orderQuery.append(" , ");
                }

                final ColumnDefinition columnDefinition = this.tableDefinition.getColumn(columnName);
                orderQuery.append(columnDefinition.getSource()).append(" ").append(order.getDir().toString());
            }
        }

        if (orderQuery.length() > 0) {
            sqlBuilder.append("ORDER BY").append(orderQuery.toString());
        }
    }
}
