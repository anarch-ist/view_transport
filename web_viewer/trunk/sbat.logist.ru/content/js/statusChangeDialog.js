$(document).ready(function () {

    const  STATUS_SELECT_MENU_WIDTH = 600,
           DIALOG_HEIGHT = 430,
           DIALOG_WIDTH = 800,
           COMMENT_WIDTH = 595;

    // create divs that will be dialog container
    $("body").append(
        '<div id="statusChangeDialog" title="Выбор нового статуса">' +
        '<table>' +
        '<tr><td width="200" valign="top"><label for="statusSelect">Новый статус: </label></td><td valign="top"><select id="statusSelect"></select></td></tr>'+
        '<tr><td width="200" valign="top"><label for="dateTimePickerInput">Дата и время: </label></td><td valign="top"><input id="dateTimePicker" type="text"></td></tr>'+
        '<tr><td width="200" valign="top"><label for="palletsQtyInput">Количество паллет: </label></td><td valign="top"><input id="palletsQtyInput" type="text"/></td></tr>'+
        '<tr><td width="200" valign="top"><label for="commentInput">Комментарий: </label></td><td valign="top"><textarea id="commentInput" maxlength="500"/></td></tr>'+
        '</table>'+
        '</div>'
    );

    // create status select menu
    var $statusSelect = $("#statusSelect");
    function populateStatusSelectMenu() {
        var options = [];
        var storedStatuses = '';
        if (window.localStorage["USER_STATUSES"]) {
            storedStatuses = JSON.parse(window.localStorage["USER_STATUSES"]);
        }
        for (var i = 0; i < storedStatuses.length; i++) {
            options.push("<option value='" + storedStatuses[i].invoiceStatusID + "'>" + storedStatuses[i].invoiceStatusRusName + "</option>");
        }
        $statusSelect.html(options.join("")).selectmenu({width: STATUS_SELECT_MENU_WIDTH});
    }

    // create palletsQty input
    $("#palletsQtyInput").mask("00", {
        placeholder: "0-99"
    });

    // create comment input
    $("#commentInput")
        .addClass("ui-widget ui-state-default ui-corner-all")
        .attr("rows", "5")
        .attr("placeholder", "введите комментарий")
        .width(COMMENT_WIDTH)
        .css("resize","none");



    // create and init dateTimePicker
    createDateTimePickerLocalization();
    var $dateTimePicker = $("#dateTimePicker");
    $dateTimePicker.datetimepicker();


    var $statusChangeDialog = $("#statusChangeDialog");
    $statusChangeDialog.dialog({
        autoOpen: false,
        resizable: false,
        minWidth: DIALOG_WIDTH,
        minHeight: DIALOG_HEIGHT,
        maxWidth: DIALOG_WIDTH,
        maxHeight: DIALOG_HEIGHT,
        width: DIALOG_WIDTH,
        height: DIALOG_HEIGHT,
        modal: true,
        close: function (event, ui) {
            document.location.reload();
        },
        buttons: {
            "Сохранить": function () {
                var dialogType = $statusChangeDialog.data('dialogType');
                var dataTable = $statusChangeDialog.data('dataTable');

                // возможно это пригодится для
                //table.rows( { selected: true } ).data();
                var newStatusID = $statusSelect[0][$statusSelect[0].selectedIndex].value;
                var date = $('#dateTimePicker')[0].value;
                if (dialogType === "changeStatusForInvoice") {
                    // получение ИД выделенной в таблице накладной
                    var invoiceNumber = dataTable.row($('#user-grid .selected')).data().invoiceNumber;
                    $.post("content/getData.php",
                        {
                            status: dialogType,
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
                } else if (dialogType === "changeStatusForSeveralInvoices") {
                    // получение ИД маршрутного листа
                    var routeListID = dataTable.row($('#user-grid .selected')).data().routeListID;
                    $.post(
                        "content/getData.php",
                        {status: dialogType, routeListID: routeListID, newStatusID: newStatusID, date: date},
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

    // external function
    $.showInvoiceStatusDialog = function(dialogType, dataTable) {
        $statusChangeDialog
            .data("dialogType", dialogType)
            .data("dataTable", dataTable)
            .dialog("open");
        populateStatusSelectMenu();
    };

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
});