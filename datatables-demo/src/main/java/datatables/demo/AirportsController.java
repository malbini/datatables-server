package datatables.demo;

import datatables.jdbc.DatatablesColumnRowMapper;
import datatables.jdbc.DatatablesQueryProviderFactory;
import datatables.jdbc.OracleDatatablesQueryProvider;
import datatables.model.DatatablesResponse;
import datatables.server.AnnotationTableDefinitionFactory;
import datatables.server.ColumnValueMappers;
import datatables.server.DefaultResponseDataMapper;
import datatables.server.HttpDatatablesHandler;
import datatables.server.JdbcHttpDatatablesHandler;
import datatables.server.TableDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

@Controller
public class AirportsController {
    private static final QueryLoader QUERY_LOADER = new QueryLoader();

    @Autowired
    private DatatablesQueryProviderFactory datatablesQueryProviderFactory;

    private HttpDatatablesHandler airportDatatablesHandler;

    @PostConstruct
    public void init() {
        final String airportsQuery = QUERY_LOADER.load("/select_airports.sql");
        final TableDefinition airportsTableDefinition = AnnotationTableDefinitionFactory.forClass(Airport.class);

        DefaultResponseDataMapper<Airport> airportsResponseDataMapper = new DefaultResponseDataMapper<Airport>();
        airportsResponseDataMapper.registerColumnClassMapper(airportsTableDefinition, Date.class, new ColumnValueMappers.DateColumnValueMapper("dd/MM/yyyy HH:mm:ss"));
        airportsResponseDataMapper.registerColumnClassMapper(airportsTableDefinition, BigDecimal.class, new ColumnValueMappers.BigDecimalColumnValueMapper("#.00", Locale.getDefault()));

        JdbcHttpDatatablesHandler<Airport> jdbcHttpDatatablesHandler = new JdbcHttpDatatablesHandler<Airport>();
        jdbcHttpDatatablesHandler.setDatatablesQueryProvider(this.datatablesQueryProviderFactory.createInstance(airportsQuery, airportsTableDefinition));
        jdbcHttpDatatablesHandler.setRowMapper(DatatablesColumnRowMapper.forClass(Airport.class));
        jdbcHttpDatatablesHandler.setResponseDataMapper(airportsResponseDataMapper);

        this.airportDatatablesHandler = jdbcHttpDatatablesHandler;
    }

    @RequestMapping("/airports")
    public
    @ResponseBody
    DatatablesResponse airports(HttpServletRequest request) {
        return this.airportDatatablesHandler.handle(request);
    }
}
