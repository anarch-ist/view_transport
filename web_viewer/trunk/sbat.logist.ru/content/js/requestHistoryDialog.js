$(document).ready(function () {

    var html =
        '<div id="requestHistoryDialog" title="История статусов">' +
        '<table id="requestHistoryDialogTable" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">' +
        '<thead>' +
        '<tr>' +
        '<th>Время</th>' +
        '<th>Пункт</th>' +
        '<th>ФИО</th>' +
        '<th>Статус</th>' +
        '<th>Маршрутный лист</th>' +
        '<th>Количество коробок</th>' +
        '</tr>' +
        '</thead>' +
        '</table>' +
        '</div>';
    $("body").append(html);

    var $requestHistoryDialog = $("#requestHistoryDialog");

    $requestHistoryDialog.dialog({
        autoOpen: false,
        resizable: true,
        height: 600,
        width: 900,
        modal: true,
        close: function (event, ui) {
        }
    });

    var $requestHistoryDialogTable = $("#requestHistoryDialogTable");
    var requestHistoryDialogTable = $requestHistoryDialogTable.DataTable({
            "dom": 't', // show only table with no decorations
            "paging": false, // no pagination
            "columnDefs": [
                {"name": "timeMarkWhenRequestWasChanged", "data": "timeMarkWhenRequestWasChanged", "targets": 0},
                {"name": "pointWhereStatusWasChanged", "data": "pointWhereStatusWasChanged", "targets": 1},
                {"name": "userNameThatChangedStatus", "data": "userNameThatChangedStatus", "targets": 2},
                {"name": "requestStatusRusName", "data": "requestStatusRusName", "targets": 3},
                {"name": "routeListNumber", "data": "routeListNumber", "targets": 4},
                {"name": "boxQty", "data": "boxQty", "targets": 5}
            ]
        }
    );

    //$requestHistoryDialogTable.css("table.dataTable thead .sorting, table.dataTable thead .sorting_asc, table.dataTable thead .sorting_desc {background : none;}");

    // data is an array of objects, each object is a string
    $.showRequestHistoryDialog = function (data) {
        $requestHistoryDialog.dialog("open");
        data = JSON.parse(data);

        for (var i = 0; i < data.length; i++) {
            data[i].fullName = data[i].userName;
            delete data[i].userName;
        }
        requestHistoryDialogTable.rows().remove();
        requestHistoryDialogTable.rows.add(data).draw(false);
    };

});