package datatables.demo;

import datatables.annotations.Column;

import java.math.BigDecimal;
import java.util.Date;

public class Airport {
    @Column("ID")
    private long id;

    @Column("IDENT")
    private String ident;

    @Column("AIRPORT_TYPE")
    private String type;

    @Column("AIRPORT_NAME")
    private String name;

    @Column("WIKIPEDIA_LINK")
    private String wikipediaLink;

    @Column("LATITUDE_DEG")
    private BigDecimal latitude; // Only for demo purpose

    @Column("LONGITUDE_DEG")
    private BigDecimal longitude; // Only for demo purpose

    @Column("LAST_UPDATE")
    private Date lastUpdate; // Only for demo purpose

    public long getId() {
        return id;
    }

    public String getIdent() {
        return ident;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getWikipediaLink() {
        return wikipediaLink;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }
}
