$(document).ready(function () {
    var vehiclesEditor = new $.fn.dataTable.Editor({
        ajax: 'content/getData.php',
        table: '#vehiclesTable',
        idSrc: 'id',

        fields: [
            {label: 'Номер ТК', name: 'transport_company_id', type: 'text'},
            {label: 'Номер лицензии', name: 'license_number', type: 'text'},
            {label: 'Модель', name: 'model', type: 'text'},
            {label: 'Грузоподъемность', name: 'carrying_capacity', type: 'text'},
            {label: 'Объем', name: 'volume', type: 'text'},
            {label: 'Тип погрузки', name: 'loading_type', type: 'selectize', options: [{label:"Задняя", value:"Задняя"}, {label:"Верхняя", value:"Верхняя"},{label: "Боковая", value:"Боковая"}]},
            {label: 'Количество палетов', name: 'pallets_quantity', type: 'mask', mask: "#" },
            {label: 'Тип ТС', name: 'type', type: 'selectize', options: [{label:"Тент", value:"Тент"}, {label:"Термос", value:"Термос"},{label: "Рефрижератор", value:"Рефрижератор"}]}
        ]
    });

    vehiclesEditor.on('preSubmit', function (e, data, action) {
        data.status = 'vehiclesEditing';
    });

    var $vehiclesTable = $("#vehiclesTable").DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getVehiclesData"}
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
                editor: vehiclesEditor,
                text: 'добавить запись'
            },
            {
                extend: "edit",
                editor: vehiclesEditor,
                text: "изменить"
            },
            {
                extend: "remove",
                editor: vehiclesEditor,
                text: 'удалить запись',
                formMessage: function (e, dt) {
                    return "Вы уверены, что вы хотите удалить это ТС?</br> Все водители, привязанные к этому ТС, так же будут удалены."
                }
            }
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0},
            {"name": "transport_company_id", "data": "transport_company_id", "targets": 1},
            {"name": "license_number", "data": "license_number", "targets": 2},
            {"name": "model", "data": "model", "targets": 3},
            {"name": "carrying_capacity", "data": "carrying_capacity", "targets": 4},
            {"name": "volume", "data": "volume", "targets": 5},
            {"name": "loading_type", "data": "loading_type", "targets": 6},
            {"name": "pallets_quantity", "data": "pallets_quantity", "targets": 7},
            {"name": "type", "data": "type", "targets": 8}
        ]
    });
});