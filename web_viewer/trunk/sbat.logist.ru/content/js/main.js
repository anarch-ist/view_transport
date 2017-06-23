$(document).ready(function () {
    var role_type = "role-type";
    $('button').addClass('ui-state-active ui-state focus');
    // --------LOGOUT-----------------
    $("#logout").button().on("click", function () {
        // delete auth cookies
        $.cookie('SESSION_CHECK_STRING', null, -1, '/');
        // make redirect to login page
        document.location = '/';
        // window.location.reload();
    });


    var requestEditor = new $.fn.dataTable.Editor({
        ajax: "'content/getData.php'",
        table: "#user-grid",
        name: 'Создать новую заявку',
        idSrc: 'requestIDExternal',
        fields: [
            {
                label: 'ИНН Client',
                name: 'clientID',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Пункт склада',
                name: 'warehousePointId',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Торговый представитель',
                name: 'marketAgentUserId',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Маршрутный лист',
                name: 'routeListID',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            }
        ]
    });

    requestEditor.field('clientID').input().on('keyup', function (e, d) {
        var clientINNPart = $(this).val();
        $.post("content/getData.php",
            {status: "getClientsByINN", format: "json", inn: clientINNPart},
            function (clientsData) {
                var options = [];
                var selectizeOptions = [];
                console.log(clientsData);
                clientsData = JSON.parse(clientsData);
                clientsData.forEach(function (entry) {
                    var option = "<option value=" + entry.clientID + ">" + "ИНН: " + entry.INN + ", имя: " + entry.clientName + "</option>";
                    options.push(option);
                    var selectizeOption = {
                        "label": "ИНН: " + entry.INN + ", имя: " + entry.clientName,
                        "value": entry.clientID
                    };
                    selectizeOptions.push(selectizeOption);
                });
                let clientSelectize = requestEditor.field('clientID').inst();

                clientSelectize.clear();
                clientSelectize.clearOptions();
                clientSelectize.load(function (callback) {
                    callback(selectizeOptions);
                });
            }
        );
    });

    requestEditor.field('warehousePointId').input().on('keyup', function (e, d) {
        let pointNamePart = $(this).val();
        $.post("content/getData.php",
            {status: "getPointsByName", format: "json", name: pointNamePart},
            function (data) {
                let options = [];

                let selectizePointsOptions = [];
                data = JSON.parse(data);
                data.forEach(function (entry) {
                    let option = "<option value=" + entry.pointID + ">" + entry.pointName + "</option>";
                    options.push(option);
                    let selectizeOption = {"label": entry.pointName, "value": entry.pointID};
                    selectizePointsOptions.push(selectizeOption);
                });

                let selectize = requestEditor.field('warehousePointId').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function (callback) {
                    callback(selectizePointsOptions);
                });
            }
        );
    });

    requestEditor.field('marketAgentUserId').input().on('keyup', function (e, d) {
        let marketAgentName = $(this).val();
        $.post("content/getData.php",
            {status: "getMarketAgentsByName", format: "json", name: marketAgentName},
            function (data) {
                let options = [];

                let selectizePointsOptions = [];
                data = JSON.parse(data);
                data.forEach(function (entry) {
                    let option = "<option value=" + entry.userID + ">" + entry.userName + "</option>";
                    options.push(option);
                    let selectizeOption = {"label": entry.userName, "value": entry.userID};
                    selectizePointsOptions.push(selectizeOption);
                });

                let selectize = requestEditor.field('marketAgentUserId').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function (callback) {
                    callback(selectizePointsOptions);
                });
            }
        );
    });

    requestEditor.field('routeListID').input().on('keyup', function (e, d) {
        let routeListNumber = $(this).val();
        $.post("content/getData.php",
            {status: "getRouteListsByNumber", format: "json", number: routeListNumber},
            function (data) {
                let options = [];

                let selectizePointsOptions = [];
                console.log(data);
                data = JSON.parse(data);
                data.forEach(function (entry) {
                    let option = "<option value=" + entry.routeListID + ">" + entry.routeListNumber + "</option>";
                    options.push(option);
                    let selectizeOption = {"label": entry.routeListNumber, "value": entry.routeListID};
                    selectizePointsOptions.push(selectizeOption);
                });

                let selectize = requestEditor.field('routeListID').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function (callback) {
                    callback(selectizePointsOptions);
                });
            }
        );
    });


    // --------DATATABLE INIT--------------
    //noinspection JSJQueryEfficiency
    var dataTable = $('#user-grid').DataTable({

        processing: true,
        serverSide: true,
        colReorder: true,
        ScrollXInner: "100%",
        scrollY: "500px",
        scrollX: true,
        autoWidth: true,
        scrollCollapse: true,
        search: {
            caseInsensitive: true
        },
        order: [[2, "desc"]],
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
        initComplete: function (settings, json) {

            if (json['recordsFiltered'] == 0) {
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

                searchDiv.on("click", function () {
                    // console.log("CLICKED");
                    var position = $(this).offset();
                    searchInput.offset(position);
                    searchInput.css({top: position.top, left: position.left, display: 'inline-block'});
                    searchInput.focus();
                });

                var that = this;
                // searchInput.on('keyup change', function (e) {
                //     var enterPressed = (e.keyCode == '13');
                //     if (enterPressed && (that.search() !== this.value)) {
                //         that
                //             .search(this.value)
                //             .draw();
                //         $(this).attr("currentFilter", this.value);
                //         searchInput.css({'display': 'none'});
                //     }
                // }).blur(function() {
                //     var filterValue = $(this).attr("currentFilter");
                //     $(this).val(filterValue);
                //     searchInput.css({'display': 'none'});
                // });

                searchInput.on('keyup change', function (e) {
                    var enterPressed = (e.keyCode == '13');
                    if ((that.search() !== this.value) && (enterPressed || localStorage.getItem('liveSearch') == 'true')) {
                        that
                            .search(this.value)
                            .draw();
                        $(this).attr("currentFilter", this.value);
                        // searchInput.css({'display': 'none'});
                    }
                }).blur(function () {
                    var filterValue = $(this).attr("currentFilter");
                    $(this).val(filterValue);
                    searchInput.css({'display': 'none'});
                    if (this.value != '') {
                        $footer.css("background-color", "#c22929")
                    } else {
                        $footer.css("background-color", "#f6f6f6");
                    }
                });

                // manually load filters data into filter inputs
                var column = state.columns[this.index()];
                var historySearch = column.search.search;
                if (historySearch) {
                    searchInput.val(historySearch);
                    searchInput.attr("currentFilter", historySearch);
                    $footer.css("background-color", "#c22929")
                }

            });


            //TODO fix it
            // if user role is CLIENT_MANAGER then delete 'изменить статус МЛ' button
            if ($("#userRoleContainer").html().trim() === "Пользователь_клиента") {
                dataTable.buttons(2).remove();
            }

            var role = $('#data-role').attr('data-role');
            if (localStorage.getItem(role_type) == undefined) {
                localStorage.setItem(role_type, role);
                $.getDefaultColumns(dataTable, role);
            }
            else if (localStorage.getItem(role_type) != role) {
                localStorage.setItem(role_type, role);
                $.getDefaultColumns(dataTable, role);
            }

            //Button-link to admin page
            if (role == "DISPATCHER" || role == "ADMIN") {
                dataTable.button().add(8, {
                    text: 'Админ. Страница',
                    action: function (e, dt, node, config) {
                        window.location = "/admin_page"
                    }
                });
            }
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

                    window.open(url);
                }
            },
            {
                extend: 'selectedSingle',
                className: 'documents',
                text: 'Документы',
                action: function (e, dt, node, config) {
                    var url =
                        "?reqIdExt=" +
                        dataTable.row($('#user-grid .selected')).data().requestIDExternal;
                    url = encodeURI(url);

                    window.open(url);
                }
            },
            {
                text: 'Сброс фильтров',
                action: function (e, dt, node, config) {
                    $('.searchColumn').each(function () {
                        $(this).val("").attr("currentFilter", "");
                    });
                    dataTable.columns().every(function () {
                        $(this.footer()).css("background-color", "f6f6f6");
                        this.search("");
                    });
                    dataTable.columns().draw();
                }
            },
            // if ($('#data-role').attr('data-role')=="ADMIN"||$('#data-role').attr('data-role')=="DISPATCHER")


            {
                text: (localStorage.getItem("liveSearch") === 'true') ? 'Живой поиск' : 'Стандартный поиск',

                action: function (e, dt, node, config) {

                    (localStorage.getItem("liveSearch") === 'true') ? localStorage.setItem("liveSearch", false) : localStorage.setItem("liveSearch", true);
                    this.text((localStorage.getItem("liveSearch") === 'true') ? 'Живой поиск' : 'Стандартный поиск');
                }
            },
            // Раскомментируй
            // {
            //     extend: "create",
            //     editor: requestEditor,
            //     text: 'Добавить заявку'
            // }
        ],
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getRequestsForUser"},

            // success: function (data) {
            //     // console.log(data);
            //     // alert(JSON.stringify(data));
            // },
            // error: function () {  // error handling
            //     console.log("error");
            //     //$(".user-grid-error").html("");
            //     //$("#user-grid").append('<tbody class="user-grid-error"><tr><th colspan="3">No data found in the server</th></tr></tbody>');
            //     //$("#user-grid_processing").css("display", "none");
            //
            // }
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
    });
    // set padding for dataTable
    $('#user-grid_wrapper').css('padding-top', '40px');
    $(".dataTables_scrollHeadInner").css({"width": "100%"});
    $(".dataTable ").css({"width": "100%"});
    var disabled = 0;
    //var buttons = dataTable.buttons(['.changeStatusForRequest', '.changeStatusForSeveralRequests', '.statusHistory']);
    dataTable.on('select', function (e, dt, type, indexes) {
        var routeListID = dataTable.row($('#user-grid .selected')).data().routeListNumber;
        if ((routeListID == null && disabled != 1) || ($("#data-role").html().trim() == "Пользователь_клиента") && disabled != 1) {
            dataTable.buttons(2).remove();
            disabled = 1;
            // buttons.disable();
        } else if (disabled == 1 && routeListID != null && (($("#data-role").html().trim() != "Пользователь_клиента"))) {
            dataTable.button().add(2, {
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

});
