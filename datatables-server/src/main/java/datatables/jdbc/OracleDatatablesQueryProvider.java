package datatables.jdbc;

import datatables.model.DatatablesSearch;
import datatables.server.ColumnDefinition;
import datatables.model.DatatablesColumn;
import datatables.model.DatatablesOrder;
import datatables.model.DatatablesRequest;
import datatables.server.TableDefinition;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

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

        final Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        // Filter
        this.appendGlobalFilter(datatablesRequest, sqlBuilder, parameters);

        // Columns Filter
        this.appendColumnsFilter(datatablesRequest, sqlBuilder, parameters);

        // Order
        this.appendOrder(datatablesRequest, sqlBuilder);

        final OracleDatatablesQuery oracleDatatablesQuery = new OracleDatatablesQuery(this.dataSource, sqlBuilder.toString());
        oracleDatatablesQuery.setParameters(parameters);

        return oracleDatatablesQuery;
    }

    private void appendGlobalFilter(DatatablesRequest datatablesRequest, SqlBuilder sqlBuilder, Map<String, Object> parameters) {
        if (!datatablesRequest.hasSearch()) {
            return;
        }

        SqlBuilder searchQuery = new SqlBuilder();

        for (DatatablesColumn column : datatablesRequest.getColumns()) {
            final String columnName = column.getData();

            if (column.isSearchable() && this.tableDefinition.containsColumn(columnName)) {

                final ColumnDefinition columnDefinition = this.tableDefinition.getColumn(columnName);

                if (columnDefinition.getType().isAssignableFrom(String.class) || columnDefinition.getWrapperType().isAssignableFrom(Integer.class) || columnDefinition.getWrapperType().isAssignableFrom(Long.class)) {
                    if (searchQuery.length() > 0) {
                        searchQuery.append(" OR ");
                    }

                    if (columnDefinition.getType().isAssignableFrom(String.class)) {
                        searchQuery.append("UPPER(").appendNS(columnDefinition.getSource()).appendNS(")").append("LIKE UPPER(").appendParameter(DatatablesQuery.DATATABLES_SEARCH_PARAM_NAME).appendNS(")");
                    } else if(columnDefinition.getWrapperType().isAssignableFrom(Integer.class) || columnDefinition.getWrapperType().isAssignableFrom(Long.class)) {
                        searchQuery.append("TO_CHAR(").appendNS(columnDefinition.getSource()).appendNS(")").append("LIKE UPPER(").appendParameter(DatatablesQuery.DATATABLES_SEARCH_PARAM_NAME).appendNS(")");
                    }
                }
            }
        }

        if (searchQuery.length() > 0) {
            sqlBuilder.append("AND");
            sqlBuilder.appendInner(searchQuery.toString());
            parameters.put(DatatablesQuery.DATATABLES_SEARCH_PARAM_NAME, likeValue(datatablesRequest.getSearch().getValue()));
        }
    }

    private void appendColumnsFilter(DatatablesRequest datatablesRequest, SqlBuilder sqlBuilder, Map<String, Object> parameters) {
        for (DatatablesColumn column : datatablesRequest.getColumns()) {
            final String columnName = column.getData();

            if(column.hasSearch() && column.isSearchable() && this.tableDefinition.containsColumn(columnName)) {

                final ColumnDefinition columnDefinition = this.tableDefinition.getColumn(columnName);
                final DatatablesSearch columnSearch = column.getSearch();

                if (columnDefinition.getType().isAssignableFrom(String.class)) {
                    sqlBuilder.append("AND UPPER(").append(columnDefinition.getSource()).appendNS(")").append("LIKE UPPER(").appendParameter(columnName).appendNS(")");
                    parameters.put(columnName, likeValue(columnSearch.getValue()));
                } else if(columnDefinition.getWrapperType().isAssignableFrom(Integer.class) || columnDefinition.getWrapperType().isAssignableFrom(Long.class)) {
                    sqlBuilder.append("AND TO_CHAR(").append(columnDefinition.getSource()).appendNS(")").append("LIKE UPPER(").appendParameter(columnName).appendNS(")");
                    parameters.put(columnName, likeValue(columnSearch.getValue()));
                }
            }
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

    private static String likeValue(String value) {
        return "%" + value + "%";
    }
}
