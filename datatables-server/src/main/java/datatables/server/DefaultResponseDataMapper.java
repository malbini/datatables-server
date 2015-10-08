package datatables.server;

import org.apache.commons.beanutils.BeanMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultResponseDataMapper<T> implements ResponseDataMapper<T> {

    private static final String CLASS_PROPERTY = "class";

    private Map<String, ColumnValueMapper> columnNameMapper = new LinkedHashMap<String, ColumnValueMapper>();

    public void registerColumnValueMapper(String columnName, ColumnValueMapper columnValueMapper) {
        this.columnNameMapper.put(columnName, columnValueMapper);
    }

    public void registerColumnClassMapper(TableDefinition tableDefinition, Class<?> columnClass, ColumnValueMapper columnValueMapper) {
        for(ColumnDefinition columnDefinition : tableDefinition.getColumns()) {
            if(columnDefinition.getType().isAssignableFrom(columnClass)) {
                this.columnNameMapper.put(columnDefinition.getName(), columnValueMapper);
            }
        }
    }

    @Override
    public List<Map<String, Object>> map(List<T> data) {
        List<Map<String, Object>> responseData = new ArrayList<Map<String, Object>>();

        for(T record : data) {
            responseData.add(toBeanMap(record));
        }

        return responseData;
    }

    private Map<String, Object> toBeanMap(T record) {
        BeanMap recordMap = new BeanMap(record);
        Map<String, Object> beanMap = new LinkedHashMap<String, Object>();

        for(Map.Entry<Object, Object> entry : recordMap.entrySet()){
            final String key = (String) entry.getKey();

            if(!CLASS_PROPERTY.equals(key)) {
                final Object value = entry.getValue();
                beanMap.put(key, this.map(key, value));
            }
        }

        return Collections.unmodifiableMap(beanMap);
    }

    private Object map(final String key, final Object value) {
        if(this.columnNameMapper.containsKey(key)) {
            ColumnValueMapper responseMapper = this.columnNameMapper.get(key);
            return responseMapper.map(value);
        }

        return value;
    }
}
