package datatables.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

class ColumnHandlers {
    private ColumnHandlers(){}

    static class StringColumnHandler implements ColumnHandler<String> {
        @Override
        public String readValue(ResultSet rs, String columnName) throws SQLException {
            return rs.getString(columnName);
        }
    }

    abstract static class NumberColumnHandler<T extends Number> implements ColumnHandler<T> {
        @Override
        public final T readValue(ResultSet rs, String columnName) throws SQLException {
            BigDecimal number = rs.getBigDecimal(columnName);

            if(number == null) {
                return null;
            }

            return this.toNumber(number);
        }

        protected abstract T toNumber(BigDecimal value);
    }

    static class BigDecimalColumnHandler extends NumberColumnHandler<BigDecimal> {
        @Override
        protected BigDecimal toNumber(BigDecimal value) {
            return value;
        }
    }

    static class LongColumnHandler extends NumberColumnHandler<Long> {
        @Override
        protected Long toNumber(BigDecimal value) {
            return value.longValue();
        }
    }

    static class IntegerColumnHandler extends NumberColumnHandler<Integer> {
        @Override
        protected Integer toNumber(BigDecimal value) {
            return value.intValue();
        }
    }

    static class DateColumnHandler implements ColumnHandler<Date> {
        @Override
        public Date readValue(ResultSet rs, String columnName) throws SQLException {
            return rs.getTimestamp(columnName);
        }
    }
}
