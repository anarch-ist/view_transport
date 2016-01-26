$(document).ready(function () {

    // --------LOGOUT-----------------
    $("#logout").button().on("click", function () {
        // delete auth cookies
        $.cookie('SESSION_CHECK_STRING', null, -1, '/');
        // make redirect to login page
        window.location.reload();
    });

    // --------DATATABLE INIT--------------
    $('#user-grid tfoot th').each(function () {
        var title = $(this).text();
        $(this).html('<input type="text" placeholder="Поиск ' + title + '" />');
    });

    var dataTable = $('#user-grid').DataTable({
        processing: true,
        serverSide: true,
        select: {
            style: 'single'
        },
        dom: 'Brtip',
        buttons: [
            {
                extend: 'selectedSingle',
                text: 'изменить статус накладной',
                action: function (e, dt, node, config) {
                    $.showInvoiceStatusDialog("changeStatusForInvoice", dataTable);
                }
            },
            {
                extend: 'selectedSingle',
                text: 'изменить статус МЛ',
                action: function (e, dt, node, config) {
                    $.showInvoiceStatusDialog("changeStatusForSeveralInvoices", dataTable);
                }
            },
            {
                extend: 'selectedSingle',
                text: 'история статусов',
                action: function (e, dt, node, config) {
                    $.post("content/getData.php", {
                            status: 'getStatusHistory',
                            invoiceNumber: dataTable.row($('#user-grid .selected')).data().invoiceNumber
                        },
                        function (data) {
                            $.showInvoiceHistoryDialog(data);
                        }
                    );
                }
            },
            {
                text: 'сброс фильтров',
                action: function (e, dt, node, config) {
                    dataTable.columns().every(function () {
                        $('input', this.footer())[0].value = '';
                        this.search('');
                        dataTable.columns().draw();
                    });
                }
            }
        ],
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getInvoicesForUser"},    // post-parameter for determining type of query
            error: function () {  // error handling
                $(".user-grid-error").html("");
                $("#user-grid").append('<tbody class="user-grid-error"><tr><th colspan="3">No data found in the server</th></tr></tbody>');
                $("#user-grid_processing").css("display", "none");
            }
        },
        columns: [
            {"data": "requestNumber"},
            {"data": "insiderRequestNumber"},
            {"data": "invoiceNumber"},
            {"data": "INN"},
            {"data": "deliveryPoint"},
            {"data": "warehousePoint"},
            {"data": "lastName"},
            {"data": "invoiceStatusRusName"},
            {"data": "boxQty"},
            {"data": "driver"},
            {"data": "licensePlate"},
            {"data": "palletsQty"},
            {"data": "routeListNumber"},
            {"data": "directionName"},
            {"data": "currentPoint"},
            {"data": "nextPoint"},
            {"data": "arrivalTime"},
            {"data": "invoiceStatusID"},
            {"data": "routeListID"}
        ],
        columnDefs: [
            {"name": "requestNumber", "searchable": true, "targets": 0},
            {"name": "insiderRequestNumber", "searchable": true, "targets": 1},
            {"name": "invoiceNumber", "searchable": true, "targets": 2},
            {"name": "INN", "searchable": true, "targets": 3},
            {"name": "deliveryPoint", "searchable": true, "targets": 4},
            {"name": "warehousePoint", "searchable": true, "targets": 5},
            {"name": "lastName", "searchable": true, "targets": 6},
            {"name": "invoiceStatusRusName", "searchable": true, "targets": 7},
            {"name": "boxQty", "searchable": true, "targets": 8},
            {"name": "driver", "searchable": true, "targets": 9},
            {"name": "licensePlate", "searchable": true, "targets": 10},
            {"name": "palletsQty", "searchable": true, "targets": 11},
            {"name": "routeListNumber", "searchable": true, "targets": 12},
            {"name": "directionName", "searchable": true, "targets": 13},
            {"name": "currentPoint", "searchable": true, "targets": 14},
            {"name": "nextPoint", "searchable": true, "targets": 15},
            {"name": "arrivalTime", "searchable": true, "targets": 16},
            {"name": "invoiceStatusID", "searchable": false, "visible": false, "targets": 17},
            {"name": "routeListID", "searchable": false, "visible": false, "targets": 18}
        ],
        language: {
            url: '/localization/dataTablesRus.json'
        }
    });
    // set padding for dataTable
    $('#user-grid_wrapper').css('padding-top', '40px');

    // Apply the search
    dataTable.columns().every(function () {
        var that = this;

        $('input', this.footer()).on('keyup change', function () {
            if (that.search() !== this.value) {
                that
                    .search(this.value)
                    .draw();
            }
        });
    });
});

