function linkRenderer(data, type, row) {
    var $link = $("<a />");
    $link.attr("href", data);
    $link.attr("target", "_blank");
    $link.html(data);

    return $("<div />").append($link).remove().html();
}

$(function() {
    var $airportsTab = $("#airports-tab");
    var datatable = $airportsTab.DataTable({
        processing: true,
        serverSide: true,
        paging: true,
        pagingType: "simple_numbers",
        ajax: "/datatables-demo/datatables/airports",
        columnDefs: [
            {
                data: null,
                defaultContent: "",
                targets: -1
            }
        ],
        columns: [
            {data: "id", title: "Id"},
            {data: "ident", title: "Ident" },
            {data: "type", title: "Type" },
            {data: "name", title: "Name" },
            {data: "wikipediaLink", title: "Link", render: linkRenderer },
            {data: "latitude", title: "Latitude", render: {_: "display"}},
            {data: "longitude", title: "Longitude", render: {_: "display"}},
            {data: "lastUpdate", title: "Last Update", render: {_: "display"}}
        ],
        order: [[0, "asc"], [1, "asc"]]
    });

    $(datatable.table().header()).find("th").each( function(index) {
        var $this = $(this);
        $this.off("click.DT");

        var title = $this.text();
        var $search = $('<input type="text" />');
        $search.attr("placeholder", "Search " + title);
        $search.addClass("column-filter");
        $this.append($search);

        $this.click(function(e){
            if(!$(e.target).hasClass("column-filter")) {
                $this.blur(); // Remove Focus

                var orderArray = datatable.order();
                var foundIndex = false;

                for(var i=0;i<orderArray.length;i++) {
                    if(orderArray[i][0] === index) {
                        foundIndex = true;

                        if(orderArray[i][1] == "asc") {
                            datatable.column(index).order("desc");
                        } else {
                            datatable.column(index).order("asc");
                        }
                    }
                }

                if(!foundIndex) {
                    datatable.column(index).order("asc");
                }

                datatable.draw();
            }
        })
    });

    datatable.columns().every( function () {
        var that = this;

        $('input', this.header() ).on( 'keyup change', function () {
            if ( that.search() !== this.value ) {
                that.search( this.value ).draw();
            }
        } );
    });
});