$(document).ready(function () {
    var dataTable = $('#routeListsTable').DataTable({
        processing: true,
        search: {
            caseInsensitive: true
        },
        fixedColumns: {
            leftColumns: 1
        },
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getRouteListsData"}
        },
        columnDefs: [
            {"name": "routeListIDExternal", "data": "routeListIDExternal", "targets": 0},
            {"name": "routeListNumber", "data": "routeListNumber", "targets": 1},
            {"name": "dataSourceName", "data": "dataSourceName", "targets": 2},
            {"name": "routeListStatusRusName", "data": "routeListStatusRusName", "targets": 3},
            {"name": "departureDate", "data": "departureDate", "targets": 4},
            {"name": "creationDate", "data": "creationDate", "targets":5}
            // {"name": "routeListNumber", "data": "routeListNumber", "targets": 4, visible: false},
            // {"name": "boxQty", "data": "boxQty", "targets": 5, visible: false}
        ],
        language: {
            url: '/localization/dataTablesRus.json'
        },
        dom: 'Bfrtip',
        // responsive: true,
        select: {
            style: 'single'
        },
        buttons: [
            {

                text: "На главную",
                action: function (e, dt, node, config) {
                    var url =
                        "index.php";
                    url = encodeURI(url);
                    window.open(url);
                }
            },
            {

                text: "История статусов",
                extend: 'selectedSingle',
                action: function (e, dt, node, config) {
                    var url =
                        "?routeListHistory="+dataTable.row($('.selected')).data().routeListIDExternal;;
                    url = encodeURI(url);
                    window.open(url);
                }
            },
            {

                text: "Заявки на этом МЛ",
                extend: 'selectedSingle',
                action: function (e, dt, node, config) {
                    var url =
                        "?routeListId=" +
                        dataTable.row($('.selected')).data().routeListID;
                    url = encodeURI(url);
                    window.open(url);
                }
            }


        ]
    });

});