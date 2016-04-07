$(document).ready(function () {

    // --------LOGOUT-----------------
    $("#logout").button().on("click", function () {
        // delete auth cookies
        $.cookie('SESSION_CHECK_STRING', null, -1, '/');
        // make redirect to login page
        window.location.reload();
    });

    // ------SEARCH INPUTS-------------
    $('#user-grid tfoot th').each(function () {
        var title = $(this).text();
        $(this).html('<input type="text" placeholder="Поиск ' + title + '" />');
    });
    var filterInputs = $('#user-grid tfoot th input');
    filterInputs.attr("currentFilter", "");

    // --------DATATABLE INIT--------------
    var dataTable = $('#user-grid').DataTable({
        processing: true,
        serverSide: true,
        colReorder: true,
       ScrollXInner: "100%",
        scrollY:        "500px",
       // scrollX:        true,
       autoWidth: false,
       scrollCollapse: true,
      //  jQueryUI: true,
     //   paging:         true,
      //  paginate: false,

        fixedColumns: false,
       // fixedHeader: {
        //  header: true,
         //   footer: false
      //  },


        stateSave: true,
        stateDuration: 0, // 0 a special value as it indicates that the state can be stored and retrieved indefinitely with no time limit
        // format for data object: https://datatables.net/reference/option/stateSaveCallback
        // save all but paging
        stateSaveParams: function (settings, data) {
            data.start = 0;
            data.length = dataTable.page.len();
        },
        // manually load filters data into filter inputs
        stateLoaded: function (settings, data) {
            for (var i = 0; i < data.columns.length; i++) {
                var column = data.columns[i];
                var search = column.search.search;
                if (search) {
                    $(filterInputs[i]).val(search);
                    $(filterInputs[i]).attr("currentFilter", search);
                }
            }
        },

        //  autoWidth: true,
        pageLength: 40,
        select: {
            style: 'single'
        },
        dom: 'Brtip',
        language: {
            url: '/localization/dataTablesRus.json'
        },
        // Note that when language url parameter is set, DataTables' initialisation will be asynchronous due to the Ajax data load.
        // That is to say that the table will not be drawn until the Ajax request as completed.
        // As such, any actions that require the table to have completed its initialisation should be placed into the initComplete callback.
        initComplete: function(settings, json) {

            // Apply the search
            dataTable.columns().every(function () {
                var that = this;
                $('input', this.footer()).on('keyup change', function (e) {
                    var enterPressed = (e.keyCode == '13');
                    if (enterPressed && (that.search() !== this.value)) {
                        that
                            .search(this.value)
                            .draw();
                        $(this)
                            .attr("currentFilter", this.value);
                    }
                }).blur(function() {
                    var filterValue = $(this).attr("currentFilter");
                    $(this).val(filterValue);
                });
            });

            //TODO fix it
            // if user role is CLIENT_MANAGER then delete 'изменить статус МЛ' button
            if ($("#userRoleContainer").html().trim() === "Пользователь_клиента")
                dataTable.buttons(2).remove();
        },
        buttons: [
            {
                text: 'выбрать столбцы',
                action: function (e, dt, node, config) {
                    $.showColumnSelectDialog(dataTable);
                }
            },
            {
                extend: 'selectedSingle',
                text: 'изменить статус накладной',
                action: function (e, dt, node, config) {
                    $.showRequestStatusDialog("changeStatusForRequest", dataTable);
                }
            },
            {
                name: 'changeRouteListStatus',
                extend: 'selectedSingle',
                text: 'изменить статус МЛ',
                action: function (e, dt, node, config) {
                    $.showRequestStatusDialog("changeStatusForSeveralRequests", dataTable);
                }
            },
            {
                extend: 'selectedSingle',
                text: 'история статусов',
                action: function (e, dt, node, config) {
                    $.post("content/getData.php", {
                            status: 'getStatusHistory',
                            requestIDExternal: dataTable.row($('#user-grid .selected')).data().requestIDExternal
                        },
                        function (data) {
                            $.showRequestHistoryDialog(data);
                        }
                    );
                }
            },
            {
                text: 'сброс фильтров',
                action: function (e, dt, node, config) {
                    dataTable.columns().every(function () {
                        var $input = $('input', this.footer());
                        $input.val("");
                        $input.attr("currentFilter", "");
                        this.search("");
                    });
                    dataTable.columns().draw();
                }
            }

          //  {
          //      extend: 'pdfHtml5',
          //      download: 'open'
           // }
        ],
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getRequestsForUser"}    // post-parameter for determining type of query

            //error: function () {  // error handling
            //    //$(".user-grid-error").html("");
            //    //$("#user-grid").append('<tbody class="user-grid-error"><tr><th colspan="3">No data found in the server</th></tr></tbody>');
            //    //$("#user-grid_processing").css("display", "none");
            //}
        },
        columns: [
            {"data": "requestIDExternal"},
            {"data": "requestNumber"},
            {"data": "requestDate"},
            {"data": "invoiceNumber"},
            {"data": "invoiceDate"},
            {"data": "documentNumber"},
            {"data": "documentDate"},
            {"data": "firma"},
            {"data": "storage"},
            {"data": "commentForStatus"},
            {"data": "boxQty"},
            {"data": "requestStatusRusName"},
            {"data": "clientIDExternal"},
            {"data": "INN"},
            {"data": "clientName"},
            {"data": "marketAgentUserName"},
            {"data": "deliveryPointName"},
            {"data": "warehousePointName"},
            {"data": "lastVisitedPointName"},
            {"data": "nextPointName"},
            {"data": "routeName"},
            {"data": "driverUserName"},
            {"data": "licensePlate"},
            {"data": "palletsQty"},
            {"data": "routeListNumber"},
            {"data": "arrivalTimeToNextRoutePoint"},

            {"data": "requestStatusID"},
            {"data": "routeListID"}
        ],
        columnDefs: [
            {"name": "requestIDExternal", "searchable": true, "targets": 0},
            {"name": "requestNumber", "searchable": true, "targets": 1},
            {"name": "requestDate", "searchable": true, "targets": 2},
            {"name": "invoiceNumber", "searchable": true, "targets": 3},
            {"name": "invoiceDate", "searchable": true, "targets": 4},
            {"name": "documentNumber", "searchable": true, "targets": 5},
            {"name": "documentDate", "searchable": true, "targets": 6},
            {"name": "firma", "searchable": true, "targets": 7},
            {"name": "storage", "searchable": true, "targets": 8},
            {"name": "commentForStatus", "searchable": true, "targets": 9},
            {"name": "boxQty", "searchable": true, "targets": 10},
            {"name": "requestStatusRusName", "searchable": true, "targets": 11},
            {"name": "clientIDExternal", "searchable": true, "targets": 12},
            {"name": "INN", "searchable": true, "targets": 13},
            {"name": "clientName", "searchable": true, "targets": 14},
            {"name": "marketAgentUserName", "searchable": true, "targets": 15},
            {"name": "deliveryPointName", "searchable": true, "targets": 16},
            {"name": "warehousePointName", "searchable": true, "targets": 17},
            {"name": "lastVisitedPointName", "searchable": true, "targets": 18},
            {"name": "nextPointName", "searchable": true, "targets": 19},
            {"name": "routeName", "searchable": true, "targets": 20},
            {"name": "driverUserName", "searchable": true, "targets": 21},
            {"name": "licensePlate", "searchable": true, "targets": 22},
            {"name": "palletsQty", "searchable": true, "targets": 23},
            {"name": "routeListNumber", "searchable": true, "targets": 24},
            {"name": "arrivalTimeToNextRoutePoint", "searchable": true, "targets": 25},

            {"name": "requestStatusID", "searchable": false, "visible": false, "targets": 26},
            {"name": "routeListID", "searchable": false, "visible": false, "targets": 27}
        ]
    });
    // set padding for dataTable
    $('#user-grid_wrapper').css('padding-top', '40px');
    $(".dataTables_scrollHeadInner").css({"width":"100%"});

    $(".dataTable ").css({"width":"100%"});
});

