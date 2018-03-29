$   (document).ready(function () {
    // $.post(
    //     "content/getData.php",
    //     {
    //         status: "getTCPageRouteLists"
    //     },
    //     function (data) {
    //         alert(data);
    //     });

    let TCPageVehiclesEditor = new $.fn.dataTable.Editor({
        ajax: 'content/getData.php',
        table: '#TCPageVehiclesTable',
        idSrc: 'id',

        fields: [
            {label: 'Номер лицензии', name: 'license_number', type: 'text'},
            {label: 'Модель', name: 'model', type: 'text'},
            {label: 'Грузоподъемность, кг.', name: 'carrying_capacity', type: 'text'},
            {label: 'Объем, м<sup>3</sup>', name: 'volume', type: 'text'},
            {
                label: 'Тип погрузки',
                name: 'loading_type',
                type: 'selectize',
                options: [{label: "Задняя", value: "Задняя"}, {label: "Верхняя", value: "Верхняя"}, {
                    label: "Боковая",
                    value: "Боковая"
                }]
            },
            {label: 'Количество паллет', name: 'pallets_quantity', type: 'text'},
            {
                label: 'Тип ТС',
                name: 'type',
                type: 'selectize',
                options: [{label: "Тент", value: "Тент"}, {label: "Термос", value: "Термос"}, {
                    label: "Рефрижератор",
                    value: "Рефрижератор"
                }]
            },
            {label: 'Wialon ID', name: 'wialon_id', type: 'text'}
        ]
    });
    TCPageVehiclesEditor.on('preSubmit', function (e, data, action) {
        if(action==="edit"){
            data.status = 'TCPageVehiclesEditing';
        }
    });
    let TCPageDriversEditor = new $.fn.dataTable.Editor({
        ajax: 'content/getData.php',
        table: '#TCPageDriversTable',
        idSrc: 'id',

            fields: [
                {label: 'Полное имя', name: 'full_name', type: 'text'},
                {label: 'Паспорт', name: 'passport', type: 'text'},
                {
                    label: 'Номер телефона',
                    name: 'phone',
                    type: 'mask',
                    mask: "(000) 000-00-00",
                    maskOptions: {clearIfNotMatch: true},
                    placeholder: "(999) 999-99-99"
                },
                {label: 'Лицензия', name: 'license', type: 'text'}
            ]

    });
    TCPageDriversEditor.on('preSubmit', function (e, data, action) {
        data.status = 'TCPageDriversEditing';
    });
    TCPageDriversEditor.on('postRemove', function (e, data, action) {
        $("#TCPageDriversTable").DataTable().columns().draw();
    });

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
                extend: "create",
                editor: TCPageVehiclesEditor ,
                text: 'добавить запись'
            },
            {
                extend: "edit",
                editor: TCPageVehiclesEditor ,
                text: "изменить"
            },
            {
                extend: "remove",
                editor: TCPageVehiclesEditor ,
                text: 'удалить запись',
                formMessage: function (e, dt) {
                    return "Вы уверены, что вы хотите удалить это ТС?</br> Все водители, привязанные к этому ТС, так же будут удалены."
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

            {"name": "id", "data": "id", "targets": 0, visible: false},
            {"name": "full_name", "data": "full_name", "targets": 1},
            {"name": "passport", "data": "passport", "targets": 2},
            {"name": "phone", "data": "phone", "targets": 3},
            {"name": "license", "data": "license", "targets": 4}

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
            extend: "create",
            editor: TCPageDriversEditor ,
            text: 'добавить запись'
        },
        {
            extend: "edit",
            editor: TCPageDriversEditor ,
            text: "изменить"
        },
        {
            extend: "remove",
            editor: TCPageDriversEditor ,
            text: 'удалить запись',
            formMessage: function (e, dt) {
                return "Вы уверены, что вы хотите удалить это ТС?</br> Все водители, привязанные к этому ТС, так же будут удалены."
            }
        }
    ]
    });
});