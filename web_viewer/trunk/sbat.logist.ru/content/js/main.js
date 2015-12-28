$(document).ready(function () {

    // create selectColumnsControl
    $("#selectColumnsControl").on("click", function () {
        // create dialog here with selection content
    });
    $("#logout").button().on("click", function () {
        // delete auth cookies
        $.cookie('SESSION_CHECK_STRING', null, -1, '/');
        // make redirect to login page
        window.location.reload();
        //location.reload(true);
    });

    // create div that will be dialog container
    $("body").append(
        '<div id="statusChangeDialog" title="Выбор нового статуса">' +
        '<label for="statusSelect">Новый статус: </label>' +
        '<select id="statusSelect"></select>' + '<br><br><br>' +
        '<label for="dateTimePickerInput">Дата и время: </label>' +
        '<input id="dateTimePicker" type="text">' +
        '</div>'
    );

    createDateTimePickerLocalization();
    var $dateTimePicker = $("#dateTimePicker");
    $dateTimePicker.datetimepicker();

    var $statusChangeDialog = $("#statusChangeDialog");
    $statusChangeDialog.dialog({
        autoOpen: false,
        resizable: true,
        height: 300,
        width: 400,
        modal: true,
        close: function (event, ui) {
            document.location.reload();
        },
        buttons: {
            "Сохранить": function () {
                //TODO get new invoice status ID for request
                // возможно это пригодится для
                //var table = $('#user-grid').DataTable();
                //table.rows( { selected: true } ).data();
                newStatusID = $('#statusSelect')[0][$('#statusSelect')[0].selectedIndex].value;
                date = $('#dateTimePicker')[0].value;
                var action = $('#statusSelect').attr("action");
                if (action === "changeStatusForInvoice") {
                    // получение ИД выделенной в таблице накладной
                    invoiceNumber = dataTable.row($('#user-grid .selected')).data().invoiceNumber;
                    $.post("content/getData.php",
                        {
                            status: action,
                            invoiceNumber: invoiceNumber,
                            newStatusID: newStatusID,
                            date: date
                        },
                        function (data) {
                            if (data === '1') {
                                document.location.reload();
                            }
                        }
                    );
                } else if (action === "changeStatusForSeveralInvoices") {
                    // получение ИД маршрутного листа
                    routeListID = dataTable.row($('#user-grid .selected')).data().routeListID;
                    $.post(
                        "content/getData.php",
                        {status: action, routeListID: routeListID, newStatusID: newStatusID, date: date},
                        function (data) {
                            if (data === '1') {
                                dataTable.columns().draw();
                                $statusChangeDialog.dialog("close");
                            }
                        }
                    );
                }
            },
            "Отмена": function () {
                $(this).dialog("close");
            }
        }
    });


    function showInvoiceStatusDialog(action) {
        $statusChangeDialog.dialog("open");

        var options = [];
        var storedStatuses = '';
        if (window.localStorage["USER_STATUSES"]) {
            storedStatuses = JSON.parse(window.localStorage["USER_STATUSES"]);
        }
        for (var i = 0; i < storedStatuses.length; i++) {
            console.log(storedStatuses[i]);
            options.push("<option value='" + storedStatuses[i].invoiceStatusID + "'>" + storedStatuses[i].invoiceStatusRusName + "</option>");
        }
        //append after populating all options
        var $statusSelect = $("#statusSelect");
        $statusSelect.attr("action", action);
        $statusSelect.html(options.join("")).selectmenu();
    }

    function createDateTimePickerLocalization() {
        $.datepicker.regional['ru'] = {
            closeText: 'Закрыть',
            prevText: '<Пред',
            nextText: 'След>',
            currentText: 'Сегодня',
            monthNames: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
            monthNamesShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
            dayNames: ['воскресенье', 'понедельник', 'вторник', 'среда', 'четверг', 'пятница', 'суббота'],
            dayNamesShort: ['вск', 'пнд', 'втр', 'срд', 'чтв', 'птн', 'сбт'],
            dayNamesMin: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
            weekHeader: 'Не',
            dateFormat: 'dd.mm.yy',
            firstDay: 1,
            isRTL: false,
            showMonthAfterYear: false,
            yearSuffix: ''
        };
        $.datepicker.setDefaults($.datepicker.regional['ru']);
        $.timepicker.regional['ru'] = {
            timeOnlyTitle: 'Выберите время',
            timeText: 'Время',
            hourText: 'Часы',
            minuteText: 'Минуты',
            secondText: 'Секунды',
            millisecText: 'Миллисекунды',
            timezoneText: 'Часовой пояс',
            currentText: 'Сейчас',
            closeText: 'Закрыть',
            timeFormat: 'HH:mm',
            amNames: ['AM', 'A'],
            pmNames: ['PM', 'P'],
            isRTL: false
        };
        $.timepicker.setDefaults($.timepicker.regional['ru']);
    }

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

                    showInvoiceStatusDialog("changeStatusForInvoice");
                }
            },
            {
                extend: 'selectedSingle',
                text: 'изменить статус МЛ',
                action: function (e, dt, node, config) {
                    showInvoiceStatusDialog("changeStatusForSeveralInvoices");
                }
            },
            {
                extend: 'selectedSingle',
                text: 'история статусов',
                action: function (e, dt, node, config) {
                    // TODO: status history
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
            select: {
                rows: {
                    0: "Выделите запись",
                    1: ""
                }
            },
            "processing": "Подождите...",
            "search": "Поиск:",
            "lengthMenu": "Показать _MENU_ записей",
            "info": "Записи с _START_ до _END_ из _TOTAL_ записей",
            "infoEmpty": "Записи с 0 до 0 из 0 записей",
            "infoFiltered": "(отфильтровано из _MAX_ записей)",
            "infoPostFix": "",
            "loadingRecords": "Загрузка записей...",
            "zeroRecords": "Записи отсутствуют.",
            "emptyTable": "В таблице отсутствуют данные",
            "paginate": {
                "first": "Первая",
                "previous": "Предыдущая",
                "next": "Следующая",
                "last": "Последняя"
            },
            aria: {
                "sortAscending": ": активировать для сортировки столбца по возрастанию",
                "sortDescending": ": активировать для сортировки столбца по убыванию"
            }
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

