$(document).ready(function () {
    const $_GET = {};

    document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
        function decode(s) {
            return decodeURIComponent(s.split("+").join(" "));
        }

        $_GET[decode(arguments[1])] = decode(arguments[2]);
    });


    var dataTable = $('#requestsForRouteListTable').DataTable({
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
            data: {
                status: "getRouteListData",
                routeListID: $_GET['routeListId']
            }
        },
        columnDefs: [
            {"name": "requestIDExternal", "data": "requestIDExternal", "targets": 0},
            {"name": "invoiceNumber", "data": "invoiceNumber", "targets": 1},
            {"name": "requestStatusRusName", "data": "requestStatusRusName", "targets": 2},
            {"name": "deliveryDate", "data": "deliveryDate", "targets": 3},
            {"name": "documentNumber", "data": "documentNumber", "targets": 4},
            {"name": "boxQty", "data": "boxQty", "targets": 5}
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
                text: 'Документы',
                extend: 'selectedSingle',
                action: function (e, dt, node, config) {
                    var url =
                        "?reqIdExt=" +
                        dataTable.row($('.selected')).data().requestIDExternal;
                    url = encodeURI(url);
                    window.open(url);

                    // console.log(JSON.stringify(dataTable.row($('.selected')).data()));
                }
            }
        ]
    });

    $.post("content/getData.php", {
            status: 'getRouteListById',
            routeListID: $_GET['routeListId']
        },
        function (data) {
            console.log(data);
            data=JSON.parse(data);
            routeListIDExternal = data[0].routeListIDExternal;
            $('#routeListNumber').html(data[0].routeListNumber);
            document.title = 'МЛ ' + data[0].routeListNumber;
        }
    );
});