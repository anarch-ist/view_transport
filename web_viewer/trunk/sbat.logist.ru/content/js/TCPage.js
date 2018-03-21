$   (document).ready(function () {
    // $.post(
    //     "content/getData.php",
    //     {
    //         status: "getTCPageRouteLists"
    //     },
    //     function (data) {
    //         alert(data);
    //     });


    var dataTable = $('#TCRouteListsTable').DataTable({
        processing: true,
        serverSide: false,
        search: {
            caseInsensitive: true
        },
        fixedColumns: {
            leftColumns: 1
        },
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getTCPageRouteLists"},
            // success: function(data){
            //     console.log(data);
            // },
            // error: function () {
            //     console.log("An error occured");
            // }
        },
        columnDefs: [

            {"name": "routeListNumber", "data": "routeListNumber", "targets": 0},
            {"name": "routeListStatusRusName", "data": "routeListStatusRusName", "targets": 1},
            {"name": "departureDate", "data": "departureDate", "targets": 2},
            {"name": "creationDate", "data": "creationDate", "targets":3}

        ],
        language: {
            url: '/localization/dataTablesRus.json'
        },
        dom: 'Bfrtip',
        responsive: true,
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

    var dataTableVehicles =$('#TCPageVehiclesTable').DataTable({
        processing: true,
        serverSide: false,
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getTCPageVehicles"},
            // success: function(data){
            //     console.log(data);
            // },
            // error: function () {
            //     console.log("An error occured");
            // }
        },
        columnDefs: [

            {"name": "license_number", "data": "license_number", "targets": 0},
            {"name": "model", "data": "model", "targets": 1},
            {"name": "carrying_capacity", "data": "carrying_capacity", "targets": 2},
            {"name": "volume", "data": "volume", "targets": 3},
            {"name": "loading_type", "data": "loading_type", "targets": 4},
            {"name": "pallets_quantity", "data": "pallets_quantity", "targets": 5},
            {"name": "type", "data": "type", "targets": 6},
            {"name": "wialon_id", "data": "wialon_id", "targets": 7},

        ],
        language: {
            url: '/localization/dataTablesRus.json'
        },
        dom: 'Bfrtip',
        responsive: true,
        select: {
            style: 'single'
        },
        buttons: [
            {
                text: 'Select all',
                action: function () {
                    dataTableVehicles.rows().select();
                }
            },
            {
                text: 'Select none',
                action: function () {
                    dataTableVehicles.rows().deselect();
                }
            }
        ]
    });
    var dataTableDrivers =$('#TCPageDriversTable').DataTable({
        processing: true,
        serverSide: false,
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getTCPageDrivers"},
            // success: function(data){
            //     console.log(data);
            // },
            // error: function () {
            //     console.log("An error occured");
            // }
        },
        columnDefs: [


            {"name": "full_name", "data": "full_name", "targets": 0},
            {"name": "passport", "data": "passport", "targets": 1},
            {"name": "phone", "data": "phone", "targets": 2},
            {"name": "license", "data": "license", "targets": 3}

        ],
        language: {
            url: '/localization/dataTablesRus.json'
        },
        dom: 'Bfrtip',
        responsive: true,
        select: {
            style: 'single'
        },
        buttons: [
        {
            text: 'Select all',
            action: function () {
                dataTableDrivers.rows().select();
            }
        },
        {
            text: 'Select none',
            action: function () {
                dataTableDrivers.rows().deselect();
            }
        }
    ]
    });
});