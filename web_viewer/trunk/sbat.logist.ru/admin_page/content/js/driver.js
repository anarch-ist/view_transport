$(document).ready(function () {
    var driversEditor = new $.fn.dataTable.Editor({
        ajax: 'content/getData.php',
        table: '#driversTable',
        idSrc: 'id',

        fields: [
            {label: 'Номер ТС', name: 'vehicle_id', type: 'text'},
            {label: 'Номер ТК', name: 'transport_company_id', type: 'text'},
            {label: 'Полное имя', name: 'full_name', type: 'text'},
            {label: 'Пасспорт', name: 'passport', type: 'text'},
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

    driversEditor.on('preSubmit', function (e, data) {
        data.status = 'driversEditing';
    });

    $("#driversTable").DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getDriversData"}
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
                editor: driversEditor,
                text: 'добавить запись'
            },
            {
                extend: "edit",
                editor: driversEditor,
                text: "изменить"
            },
            {
                extend: "remove",
                editor: driversEditor,
                text: 'удалить запись'
            }
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0, visible: false},
            {"name": "vehicle_id", "data": "vehicle_id", "targets": 1},
            {"name": "transport_company_id", "data": "transport_company_id", "targets": 2},
            {"name": "full_name", "data": "full_name", "targets": 3},
            {"name": "passport", "data": "passport", "targets": 4},
            {"name": "phone", "data": "phone", "targets": 5},
            {"name": "license", "data": "license", "targets": 6}
        ]
    });
});