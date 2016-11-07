$(document).ready(function () {

    $('button').addClass('ui-state-active ui-state focus');

    // --------LOGOUT-----------------
    $("#logout").button().on("click", function () {
        // delete auth cookies
        $.cookie('SESSION_CHECK_STRING', null, -1, '/');
        // make redirect to login page
        window.location.reload();
    });

    // --------DATATABLE INIT--------------
    var dataTable = $('#user-grid').DataTable({
        processing: true,
        serverSide: true,
        colReorder: true,
        ScrollXInner: "100%",
        scrollY: "500px",
        scrollX: true,
        autoWidth: true,
        scrollCollapse: true,
        //"order": [],
      //  jQueryUI: true,
     //   paging:         true,
      //  paginate: false,
        fixedColumns: {
            leftColumns: 2
        },
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

            if(json['recordsFiltered'] == 0){
                alert('Данных не найдено');
            }

            // ------SEARCH INPUTS-------------
            var state = dataTable.state();


            dataTable.columns().every(function () {

                var $footer = $(this.footer());

                var title = $footer.text();
                var searchInput = $('<input type="text" class="searchColumn" style="display: none; position: absolute" placeholder="Поиск ' + title + '" />');

                $("body").append(searchInput);

                var searchDiv = $('<div>');
                searchDiv.height(20);
                var search = $('<i class="fa fa-search">');
                searchDiv.html(search);
                $footer.html(searchDiv);

                searchDiv.on("click", function() {
                    console.log("CLICKED");
                    var position = $(this).offset();
                    searchInput.offset(position);
                    searchInput.css({top: position.top, left: position.left, display: 'inline-block'});
                    searchInput.focus();
                });

                var that = this;
                searchInput.on('keyup change', function (e) {
                    var enterPressed = (e.keyCode == '13');
                    if (enterPressed && (that.search() !== this.value)) {
                        that
                            .search(this.value)
                            .draw();
                        $(this).attr("currentFilter", this.value);
                        searchInput.css({'display': 'none'});
                    }
                }).blur(function() {
                    var filterValue = $(this).attr("currentFilter");
                    $(this).val(filterValue);
                    searchInput.css({'display': 'none'});
                });

                // manually load filters data into filter inputs
                var column = state.columns[this.index()];
                var historySearch = column.search.search;
                if (historySearch) {
                    searchInput.val(historySearch);
                    searchInput.attr("currentFilter", historySearch);
                }
            });


            //TODO fix it
            // if user role is CLIENT_MANAGER then delete 'изменить статус МЛ' button
            if ($("#userRoleContainer").html().trim() === "Пользователь_клиента")
                dataTable.buttons(2).remove();
        },
        buttons: [
            {
                text: 'Выбрать столбцы',
                action: function (e, dt, node, config) {
                    $.showColumnSelectDialog(dataTable);
                }
            },
            {
                extend: 'selectedSingle',
                className: 'changeStatusForRequest',
                text: 'Изменить статус накладной',
                action: function (e, dt, node, config) {
                    $.showRequestStatusDialog("changeStatusForRequest", dataTable);
                }
            },
            {
                name: 'changeRouteListStatus',
                extend: 'selectedSingle',
                className: 'changeStatusForSeveralRequests',
                text: 'Изменить статус МЛ',
                action: function (e, dt, node, config) {
                    $.showRequestStatusDialog("changeStatusForSeveralRequests", dataTable);
                }
            },
            {
                extend: 'selectedSingle',
                className: 'statusHistory',
                text: 'История статусов',
                action: function (e, dt, node, config) {
                    var url =
                        "?clientId=" +
                        dataTable.row($('#user-grid .selected')).data().clientIDExternal +
                        "&invoiceNumber=" +
                        dataTable.row($('#user-grid .selected')).data().invoiceNumber;
                    url = encodeURI(url);
                    
                    window.open(url, "width=400, height=700");
                    // $.post("content/getData.php", {
                    //         status: 'getStatusHistory',
                    //         requestIDExternal: dataTable.row($('#user-grid .selected')).data().requestIDExternal
                    //     },
                    //     function (data) {
                    //         $.showRequestHistoryDialog(data);
                    //     }
                    // );
                }
            },
            {
                text: 'Сброс фильтров',
                action: function (e, dt, node, config) {
                    $('.searchColumn').each(function() {
                        $(this).val("").attr("currentFilter", "");
                    });
                    dataTable.columns().every(function () {
                        this.search("");
                    });
                    dataTable.columns().draw();
                }
            }
        ],
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getRequestsForUser"},
            
            /*success: function (data) {
                console.log(data);
            }*/
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

        ],
        /*success: function (data) {
                console.log(data);
        }*/
    });
    // set padding for dataTable
    $('#user-grid_wrapper').css('padding-top', '40px');
    $(".dataTables_scrollHeadInner").css({"width":"100%"});

    $(".dataTable ").css({"width":"100%"});
    var disabled = 0;
    //var buttons = dataTable.buttons(['.changeStatusForRequest', '.changeStatusForSeveralRequests', '.statusHistory']);
    dataTable.on( 'select', function ( e, dt, type, indexes ) {
        var routeListID = dataTable.row($('#user-grid .selected')).data().routeListNumber;
        if(routeListID == null && disabled != 1){
            dataTable.buttons(2).remove();
            disabled = 1;
            //buttons.disable();
        }else if(disabled == 1 && routeListID != null){
            dataTable.button().add( 2, {
                name: 'changeRouteListStatus',
                extend: 'selectedSingle',
                className: 'changeStatusForSeveralRequests',
                text: 'Изменить статус МЛ',
                action: function (e, dt, node, config) {
                    $.showRequestStatusDialog("changeStatusForSeveralRequests", dataTable);
                }
            });
            disabled = 0;
        }
    });

    //$(dataTable.table().container()).on( 'click', 'td', function () {
    //    var cell = table.cell( this );
    //    console.log( cell.index() );
    //} );

    //setTimeout(function(){
    //    var firstSearchDiv = $("#user-grid tfoot tr th.col1 div:not(.dataTables_sizing)");
    //    var secondSearchDiv = $("#user-grid tfoot tr th.col2 div:not(.dataTables_sizing)");
    //
    //    var firstClonedSearchDiv = $(".DTFC_Cloned tfoot tr th.col1 div");
    //    var secondClonedSearchDiv = $(".DTFC_Cloned tfoot tr th.col2 div");
    //
    //    firstClonedSearchDiv.on("click", function() {
    //        firstSearchDiv.click();
    //    });
    //
    //    secondClonedSearchDiv.on("click", function() {
    //        secondSearchDiv.click();
    //    });
    //
    //    console.log(firstSearchDiv);
    //    console.log(secondSearchDiv);
    //    console.log(firstClonedSearchDiv);
    //    console.log(secondClonedSearchDiv);
    //}, 2000);


});

