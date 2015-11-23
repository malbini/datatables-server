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

    $airportsTab.find("thead th").each( function () {
        var title = $(this).text();
        var $search = $('<input type="text" />');
        $search.attr("placeholder", "Search " + title);
        $(this).append($search);
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