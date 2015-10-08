package datatables.server;

import datatables.jdbc.DatatablesQuery;
import datatables.jdbc.DatatablesQueryProvider;
import datatables.model.DatatablesRequest;
import datatables.model.DatatablesRequestParser;
import datatables.model.DatatablesResponse;
import datatables.model.DefaultDatatablesResponse;
import org.springframework.jdbc.core.RowMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class JdbcHttpDatatablesHandler<T> implements HttpDatatablesHandler {

    private static final DatatablesRequestParser DEFAULT_PARSER = new HttpDatatablesRequestParser();
    private static final DatatablesQueryFilter EMPTY_FILTER = new EmptyFilter();

    private DatatablesRequestParser datatablesRequestParser = DEFAULT_PARSER;
    private DatatablesQueryProvider datatablesQueryProvider;
    private RowMapper<T> rowMapper;
    private ResponseDataMapper<T> responseDataMapper;

    public void setDatatablesRequestParser(DatatablesRequestParser datatablesRequestParser) {
        this.datatablesRequestParser = datatablesRequestParser;
    }

    public void setDatatablesQueryProvider(DatatablesQueryProvider datatablesQueryProvider) {
        this.datatablesQueryProvider = datatablesQueryProvider;
    }

    public void setRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public void setResponseDataMapper(ResponseDataMapper<T> responseDataMapper) {
        this.responseDataMapper = responseDataMapper;
    }

    public DatatablesResponse handle(HttpServletRequest request) {
        DatatablesRequest datatablesRequest = this.datatablesRequestParser.parse(request);
        return this.handle(datatablesRequest);
    }

    @Override
    public DatatablesResponse handle(DatatablesRequest datatablesRequest) {
        return this.handle(datatablesRequest, EMPTY_FILTER);
    }

    @Override
    public DatatablesResponse handle(DatatablesRequest datatablesRequest, DatatablesQueryFilter filter) {
        final DatatablesQuery searchQuery = this.datatablesQueryProvider.createQueryForRequest(datatablesRequest);

        filter.applyFilter(searchQuery);

        if (datatablesRequest.hasSearch()) {
            searchQuery.setParameter(DatatablesQuery.DATATABLES_SEARCH_PARAM_NAME, "%" + datatablesRequest.getSearch().getValue() + "%");
        }

        List<T> result = searchQuery.limitedData(this.rowMapper,
                                                 datatablesRequest.getStart(),
                                                 datatablesRequest.getStart() + datatablesRequest.getLength());

        final long count = searchQuery.count();

        DefaultDatatablesResponse datatablesResponse = new DefaultDatatablesResponse();
        datatablesResponse.setDraw(datatablesRequest.getDraw());
        datatablesResponse.setRecordsTotal(count);
        datatablesResponse.setRecordsFiltered(count);
        datatablesResponse.setData(this.responseDataMapper.map(result));

        return datatablesResponse;
    }

    private static final class EmptyFilter implements DatatablesQueryFilter {
        @Override
        public void applyFilter(DatatablesQuery query) {
            // Do nothing
        }
    }
}
