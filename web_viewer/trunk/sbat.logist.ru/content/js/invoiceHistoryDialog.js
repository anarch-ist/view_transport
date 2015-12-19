
$(document).ready(function () {

    var html =
    '<div id="invoiceHistoryDialog" title="История статусов">' +
        '<table id="invoiceHistoryDialogTable" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">'+
            '<thead>'+
            '<tr>'+
                '<th>Пункт</th>'+
                '<th>ФИО</th>'+
                '<th>Статус</th>'+
                '<th>Время</th>'+
                '<th>Маршрутный лист</th>'+
                '<th>Количество паллет</th>'+
                '<th>Количество коробок</th>'+
            '</tr>'+
            '</thead>'+
        '</table>'+
    '</div>';
    $("body").append(html);

    var $invoiceHistoryDialog = $("#invoiceHistoryDialog");

    $invoiceHistoryDialog.dialog({
        autoOpen: false,
        resizable: true,
        height: 600,
        width: 900,
        modal: true,
        close: function( event, ui ) {
        }
    });

    var $invoiceHistoryDialogTable = $("#invoiceHistoryDialogTable");
    var invoiceHistoryDialogTable = $invoiceHistoryDialogTable.DataTable( {
            "dom":'t', // show only table with no decorations
            "paging": false, // no pagination
            "columnDefs": [
                { "name": "pointName", "data": "pointName", "targets": 0 },
                { "name": "fullName",  "data": "fullName", "targets": 1 },
                { "name": "invoiceStatusRusName", "data": "invoiceStatusRusName", "targets": 2 },
                { "name": "lastStatusUpdated", "data": "lastStatusUpdated", "targets": 3 },
                { "name": "routListNumber", "data": "routListNumber", "targets": 4 },
                { "name": "palletsQty", "data": "palletsQty", "targets": 5 },
                { "name": "boxQty", "data": "boxQty", "targets": 6 }
            ]
        }
    );

    //$invoiceHistoryDialogTable.css("table.dataTable thead .sorting, table.dataTable thead .sorting_asc, table.dataTable thead .sorting_desc {background : none;}");

    // data is an array of objects, each object is a string
    $.showInvoiceHistoryDialog = function(data) {
        $invoiceHistoryDialog.dialog("open");
        data = JSON.parse(data);

        for (var i=0;i<data.length;i++) {
            data[i].fullName = data[i].firstName+" "+ data[i].lastName+" "+ " "+data[i].patronymic;
            delete data[i].firstName;
            delete data[i].lastName;
            delete data[i].patronymic;
        }
        invoiceHistoryDialogTable.rows().remove();
        invoiceHistoryDialogTable.rows.add(data).draw(false);
    };

});