package datatables.model;

public class DefaultDatatablesColumn implements DatatablesColumn {
    private String data;
    private String name;
    private boolean searchable;
    private boolean orderable;
    private DatatablesSearch search;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public DatatablesSearch getSearch() {
        return search;
    }

    public void setSearch(DatatablesSearch search) {
        this.search = search;
    }
}
