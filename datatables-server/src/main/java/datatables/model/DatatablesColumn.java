package datatables.model;

public interface DatatablesColumn {
    String getData();

    String getName();

    boolean isSearchable();

    boolean isOrderable();

    DatatablesSearch getSearch();
}
