function linkRenderer(data, type, row) {
    var $link = $("<a />");
    $link.attr("href", data);
    $link.attr("target", "_blank");
    $link.html(data);

    return $("<div />").append($link).remove().html();
}

$(function() {
    $("#airports-tab").dataTable({
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
});