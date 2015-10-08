package datatables.server;

import datatables.model.DatatablesColumn;
import datatables.model.DatatablesOrder;
import datatables.model.DatatablesRequest;
import datatables.model.DatatablesRequestParser;
import datatables.model.DatatablesSearch;
import datatables.model.DefaultDatatablesColumn;
import datatables.model.DefaultDatatablesOrder;
import datatables.model.DefaultDatatablesRequest;
import datatables.model.DefaultDatatablesSearch;
import datatables.model.OrderType;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpDatatablesRequestParser implements DatatablesRequestParser {
    private static final String ORDER_REGEX = "^order\\[([0-9]+)\\].+$";
    private static final Pattern ORDER_REGEX_PATTERN = Pattern.compile(ORDER_REGEX);

    private static final String COLUMNS_REGEX = "^columns\\[([0-9]+)\\].+$";
    private static final Pattern COLUMNS_REGEX_PATTERN = Pattern.compile(COLUMNS_REGEX);

    public DatatablesRequest parse(HttpServletRequest request) {
        final Set<String> parameterNames = readParametersNames(request);

        DefaultDatatablesRequest datatablesRequest = new DefaultDatatablesRequest();

        datatablesRequest.setDraw(requiredLongValue(request, "draw"));
        datatablesRequest.setStart(requiredLongValue(request, "start"));
        datatablesRequest.setLength(requiredLongValue(request, "length"));

        // Global Search Parsing
        datatablesRequest.setSearch(parseGlobalSearch(request));

        // Columns Parsing
        datatablesRequest.setColumns(parseColumns(request, parameterNames));

        // Orders Parsing
        datatablesRequest.setOrders(parseOrders(request, parameterNames));

        return datatablesRequest;
    }

    private static DatatablesSearch parseGlobalSearch(HttpServletRequest request) {
        final String searchValue = stringValue(request, "search[value]");
        final boolean regex = booleanValue(request, "search[regex]", false);

        return new DefaultDatatablesSearch(searchValue, regex);
    }

    private static List<DatatablesColumn> parseColumns(HttpServletRequest request, Set<String> parameterNames) {
        Map<Integer, DatatablesColumn> columnsMap = new HashMap<Integer, DatatablesColumn>();

        for (String parameterName : parameterNames) {
            Matcher matcher = COLUMNS_REGEX_PATTERN.matcher(parameterName);

            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));

                if (!columnsMap.containsKey(index)) {
                    columnsMap.put(index, parseColumn(request, index));
                }
            }
        }

        return keyToList(columnsMap);
    }

    private static DatatablesColumn parseColumn(HttpServletRequest request, int index) {
        final String prefix = "columns[" + index + "]";

        DefaultDatatablesColumn column = new DefaultDatatablesColumn();

        column.setData(requiredStringValue(request, prefix + "[data]"));
        column.setName(stringValue(request, prefix + "[name]"));
        column.setSearchable(booleanValue(request, prefix + "[searchable]", false));
        column.setOrderable(booleanValue(request, prefix + "[orderable]", false));

        final String searchValue = stringValue(request, prefix + "[search][value]");
        final boolean searchRegex = booleanValue(request, prefix + "[search][regex]", false);

        column.setSearch(new DefaultDatatablesSearch(searchValue, searchRegex));

        return column;
    }

    private static List<DatatablesOrder> parseOrders(HttpServletRequest request, Set<String> parameterNames) {
        Map<Integer, DatatablesOrder> orderMap = new HashMap<Integer, DatatablesOrder>();

        for (String parameterName : parameterNames) {
            Matcher matcher = ORDER_REGEX_PATTERN.matcher(parameterName);

            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));

                if (!orderMap.containsKey(index)) {
                    orderMap.put(index, parseOrder(request, index));
                }
            }
        }

        return keyToList(orderMap);
    }

    private static DatatablesOrder parseOrder(HttpServletRequest request, int index) {
        final String prefix = "order[" + index + "]";

        final int column = requiredIntValue(request, prefix + "[column]");
        final String dir = requiredStringValue(request, prefix + "[dir]");

        final OrderType ord = OrderType.valueOf(dir.toUpperCase());
        return new DefaultDatatablesOrder(column, ord);
    }

    private static String requiredStringValue(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);

        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Parameter [" + paramName + "] must be not null");
        }

        return value;
    }

    private static long requiredLongValue(HttpServletRequest request, String paramName) {
        String value = requiredStringValue(request, paramName);
        return Long.parseLong(value);
    }

    private static int requiredIntValue(HttpServletRequest request, String paramName) {
        String value = requiredStringValue(request, paramName);
        return Integer.parseInt(value);
    }

    private static String stringValue(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    private static boolean booleanValue(HttpServletRequest request, String paramName, boolean defaultValue) {
        String value = request.getParameter(paramName);

        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    private static <V> List<V> keyToList(Map<Integer, V> valuesMap) {
        List<V> values = new ArrayList<V>(valuesMap.size());

        List<Integer> indexes = new ArrayList<Integer>(valuesMap.keySet());
        Collections.sort(indexes);

        for (int index : indexes) {
            fillList(values, index);
            values.add(index, valuesMap.get(index));
        }

        return values;
    }

    private static void fillList(List<?> list, int index) {

        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        while (index < list.size()) {
            list.add(list.size(), null);
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<String> readParametersNames(HttpServletRequest request) {
        Enumeration<String> parameterNamesEnum = (Enumeration<String>) request.getParameterNames();
        List<String> parameterNames = new ArrayList<String>();

        while (parameterNamesEnum.hasMoreElements()) {
            parameterNames.add(parameterNamesEnum.nextElement());
        }

        Collections.sort(parameterNames);
        return new LinkedHashSet<String>(parameterNames);
    }
}
