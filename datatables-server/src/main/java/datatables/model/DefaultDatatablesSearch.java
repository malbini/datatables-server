package datatables.model;

public final class DefaultDatatablesSearch implements DatatablesSearch {
    private String value;
    private boolean regex;

    public DefaultDatatablesSearch(String value, boolean regex) {
        this.value = value;
        this.regex = regex;
    }

    public String getValue() {
        return value;
    }

    public boolean isRegex() {
        return regex;
    }
}
