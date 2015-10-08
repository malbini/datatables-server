package datatables.server;

import datatables.jdbc.DatatablesQuery;
import datatables.jdbc.DatatablesQueryProvider;
import datatables.model.DatatablesRequestParser;
import datatables.model.DatatablesResponse;
import datatables.model.DefaultDatatablesRequest;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JdbcHttpDatatablesHandlerTest {
    @Test
    public void testHandleWithoutFilters() {
        Mockery context = new Mockery();

        final int start = 10;
        final int length = 30;
        final long count = 20;
        final long draw = 999;

        final HttpServletRequest httpRequest = context.mock(HttpServletRequest.class);
        final DatatablesRequestParser datatablesRequestParser = context.mock(DatatablesRequestParser.class);
        final DatatablesQueryProvider datatablesQueryProvider = context.mock(DatatablesQueryProvider.class);

        @SuppressWarnings("unchecked")
        final RowMapper<TestObject> rowMapper = (RowMapper<TestObject>) context.mock(RowMapper.class);

        @SuppressWarnings("unchecked")
        final ResponseDataMapper<TestObject> responseDataMapper = (ResponseDataMapper<TestObject>) context.mock(ResponseDataMapper.class);

        final DefaultDatatablesRequest datatablesRequest = new DefaultDatatablesRequest();
        datatablesRequest.setDraw(draw);
        datatablesRequest.setStart(start);
        datatablesRequest.setLength(length);

        final DatatablesQuery datatablesQuery = context.mock(DatatablesQuery.class);

        final TestObject testObject = new TestObject("NAME-1", "SURNAME-1", new Date());
        final List<TestObject> queryResult = Arrays.asList(testObject);

        final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        context.checking(new Expectations() {{
            oneOf(datatablesRequestParser).parse(httpRequest);
            will(returnValue(datatablesRequest));

            oneOf(datatablesQueryProvider).createQueryForRequest(datatablesRequest);
            will(returnValue(datatablesQuery));

            oneOf(datatablesQuery).limitedData(rowMapper, start, start + length);
            will(returnValue(queryResult));

            oneOf(datatablesQuery).count();
            will(returnValue(count));

            oneOf(responseDataMapper).map(queryResult);
            will(returnValue(result));

        }});

        JdbcHttpDatatablesHandler<TestObject> handler = new JdbcHttpDatatablesHandler<TestObject>();
        handler.setDatatablesRequestParser(datatablesRequestParser);
        handler.setDatatablesQueryProvider(datatablesQueryProvider);
        handler.setRowMapper(rowMapper);
        handler.setResponseDataMapper(responseDataMapper);

        DatatablesResponse datatablesResponse = handler.handle(httpRequest);
        Assert.assertEquals(draw, datatablesResponse.getDraw());
        Assert.assertEquals(count, datatablesResponse.getRecordsFiltered());
        Assert.assertEquals(count, datatablesResponse.getRecordsTotal());

        List<Map<String, Object>> responseData = datatablesResponse.getData();
        Assert.assertNotNull(responseData);
        Assert.assertSame(result, responseData);

        context.assertIsSatisfied();
    }
}
