package datatables.server;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class ColumnValueMappers {
    private ColumnValueMappers(){}

    public static class DateColumnValueMapper extends AbstractColumnMapper<Date> {
        private String pattern;

        public DateColumnValueMapper(String pattern) {
            super(Date.class);
            this.pattern = pattern;
        }

        @Override
        protected Object doMap(Date value) {
            String displayValue = null;
            Long timestamp = null;

            if(value != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(this.pattern);
                displayValue = sdf.format(value);
                timestamp = value.getTime();
            }

            Map<String, Object> dateMap = new LinkedHashMap<String, Object>();
            dateMap.put("display", displayValue);
            dateMap.put("timestamp", timestamp);

            return dateMap;
        }
    }

    public static class BigDecimalColumnValueMapper extends AbstractColumnMapper<BigDecimal> {
        private String pattern;
        private Locale locale;

        public BigDecimalColumnValueMapper(String pattern, Locale locale) {
            super(BigDecimal.class);
            this.pattern = pattern;
            this.locale = locale;
        }

        @Override
        protected Object doMap(BigDecimal value) {
            String displayValue = null;
            String decimalValue = null;

            if(value != null) {
                DecimalFormat df = new DecimalFormat(this.pattern, DecimalFormatSymbols.getInstance(this.locale));
                displayValue = df.format(value);
                decimalValue = value.toString();
            }

            Map<String, String> dateMap = new LinkedHashMap<String, String>();
            dateMap.put("display", displayValue);
            dateMap.put("value", decimalValue);

            return dateMap;
        }
    }
}
