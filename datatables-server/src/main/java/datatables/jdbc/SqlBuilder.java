package datatables.jdbc;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;

class SqlBuilder {
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";
    public static final String PARAMETER_PREFIX = ":";

    private StringBuilder strBuilder = new StringBuilder();

    public int length() {
        return this.strBuilder.length();
    }

    public SqlBuilder append(String str, boolean addSpace) {
        if(addSpace && this.strBuilder.length() > 0) {
            this.strBuilder.append(SPACE);
        }

        this.strBuilder.append(str);
        return this;
    }

    public SqlBuilder append(String str) {
        return this.append(str, true);
    }

    public SqlBuilder appendNS(String str) {
        return this.append(str, false);
    }

    public SqlBuilder appendParameter(String parameterName) {
        return this.append(PARAMETER_PREFIX + parameterName, false);
    }

    public SqlBuilder append() {
        return this.append(EMPTY_STRING, true);
    }

    public final String toString() {
        return this.strBuilder.toString();
    }

    public SqlBuilder appendCollection(Collection<String> collection) {
        return this.appendCollection(collection, null, null);
    }

    public SqlBuilder appendCollection(Collection<String> collection, String startToken, String endToken) {

        int index = 0;

        if(startToken != null) {
            this.strBuilder.append(startToken);
        }

        for(String value : collection) {
            if(index > 0) {
                this.strBuilder.append(", ");
            }

            this.strBuilder.append(value);
            index ++;
        }

        if(endToken != null) {
            this.strBuilder.append(endToken);
        }

        return this;
    }

    public void appendInner(String query) {
        this.append("(");
        this.append(StringUtils.trim(query));
        this.append(")");
    }

    public void appendQuery(String query) {
        this.appendQuery(query, null);
    }

    public void appendQuery(String query, String alias) {
        this.append("(");
        this.append(StringUtils.trim(query));
        this.append(")");

        if(StringUtils.isNotBlank(alias)) {
            this.append(alias);
        }
    }
}
