$(document).ready(function () {
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
            {label: 'Количество паллет', name: 'pallets_quantity', type: 'mask', mask: "#"},
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
        data.status = 'TCPageVehiclesEditing';
    });

    let $TCPageVehiclesTable = $("#TCPageVehiclesTable").DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getTCpageVehicles"}
        },
        dom: 'Bfrtip',
        language: {
            url: '/localization/dataTablesRus.json'
        },
        select: {
            style: 'single'
        },
        "buttons": [
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
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0,visible:false},
            {"name": "license_number", "data": "license_number", "targets": 1},
            {"name": "model", "data": "model", "targets": 2},
            {"name": "carrying_capacity", "data": "carrying_capacity", "targets": 3},
            {"name": "volume", "data": "volume", "targets": 4},
            {"name": "loading_type", "data": "loading_type", "targets": 5},
            {"name": "pallets_quantity", "data": "pallets_quantity", "targets": 6},
            {"name": "type", "data": "type", "targets": 7},
            {"name": "wialon_id", "data": "wialon_id", "targets": 8},

        ]
    });
});