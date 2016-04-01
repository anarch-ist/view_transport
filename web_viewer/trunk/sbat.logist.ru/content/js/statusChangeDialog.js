$(document).ready(function () {

    const STATUS_SELECT_MENU_WIDTH = 600,
        DIALOG_HEIGHT = 430,
        DIALOG_WIDTH = 800,
        COMMENT_WIDTH = 595,
        DEPARTURE_STATUS = "DEPARTURE";

    // create divs that will be dialog container
    $("body").append(
        '<div id="statusChangeDialog" title="Выбор нового статуса">' +
        '<table>' +
        '<tr valign="top" ><td width="200" padding="10px"><label for="statusCurrent">Текущий статус: </label></td><td><strong id="statusCurrent"></strong></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="statusSelect">Новый статус: </label></td><td><select id="statusSelect"></select></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="dateTimePickerInput">Дата и время: </label></td><td><input id="dateTimePicker" type="text"></td></tr>' +
        '<tr id="palletsQtyTr" valign="top" ><td width="200"><label for="palletsQtyInput">Количество паллет: </label></td><td><input id="palletsQtyInput" type="text"/></td></tr>' +
        '<tr id="vehicleNumberTr" valign="top" ><td width="200"><label for="vehicleNumberInput">Номер ТС: </label></td><td><input id="vehicleNumberInput" type="text"/></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="commentInput">Комментарий: </label></td><td><textarea id="commentInput" maxlength="500"/></td></tr>' +
        '<tr id="selectRequestsTr" valign="top"><td width="200"><label for="statusSelect">Накладные: </label></td><td><div id="requestCheckBoxes">aaaaaaaaaa</div></td></tr>' +
        '<tr id="selectNumbersRequestsTr" valign="top"><td width="200"><label for="statusSelect">Номера накладных: </label></td><td><div id="numberRequestCheckBoxes"></div></td></tr>' +
        '</table>' +
        '</div>'
    );

    // create status select menu
    var $statusSelect = $("#statusSelect");
    var $requestCheckBoxes = $("#requestCheckBoxes");
    var $statusesRequest = $("#statusesRequest");
    var $numberRequestCheckBoxes = $("#numberRequestCheckBoxes");
    var $selectRequestsTr = $("#selectRequestsTr");


    function populateStatusSelectMenu() {
        var options = [];
        var storedStatuses = '';
        if (window.localStorage["USER_STATUSES"]) {
            storedStatuses = JSON.parse(window.localStorage["USER_STATUSES"]);
        }
        for (var i = 0; i < storedStatuses.length; i++) {
            options.push("<option value='" + storedStatuses[i].requestStatusID + "'>" + storedStatuses[i].requestStatusRusName + "</option>");
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
        .css("resize", "none");


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
        // when dialog open transform it to one of two types
        open: function () {

            var dialogType = $statusChangeDialog.data('dialogType');
            var dataTable = $statusChangeDialog.data('dataTable');

            $("#palletsQtyTr").hide();
            switch (dialogType) {
                case "changeStatusForRequest":
                    $selectRequestsTr.show();
                    $statusSelect.off("selectmenuchange");
                    $.post(
                        "content/getData.php",
                        {
                            status: "getRequestsForRouteList",
                            routeListID: dataTable.row($('#user-grid .selected')).data().routeListID
                        },
                        function (data) {
                            var requestsArray = JSON.parse(data);
                            $statusChangeDialog.data('requestsForSelectedRouteList', requestsArray);
                            $requestCheckBoxes.html("");
                            $numberRequestCheckBoxes.html("");
                            $('#statusCurrent').html(requestsArray[0]['requestStatusRusName']);
                            requestsArray.forEach(function(request){
                                
                                $('#selectNumbersRequestsTr').show();
                                $statusesRequest.html('<span style="font-weight:bold;">'+request.requestStatusRusName+'</span>'+'&nbsp;&nbsp;');
                                $requestCheckBoxes.append('<label>'+'<input type="checkbox" value='+request.requestID+' checked>'+request.requestIDExternal+'</label>'+'&nbsp;&nbsp;');
                                $numberRequestCheckBoxes.append('<span style="font-weight:bold;">'+request.invoiceNumber+'</span>'+'&nbsp;&nbsp;');

                            });
                        }
                    );
                    break;
                case "changeStatusForSeveralRequests":
                    $selectRequestsTr.show();
                    $statusSelect.on("selectmenuchange", function (e, ui) {
                        if (ui.item.value === DEPARTURE_STATUS)
                            $("#palletsQtyTr").show();
                        else
                            $("#palletsQtyTr").hide();
                    });

                    $.post(
                        "content/getData.php",
                        {
                            status: "getRequestsForRouteList",
                            routeListID: dataTable.row($('#user-grid .selected')).data().routeListID
                        },
                        function (data) {
                            var requestsArray = JSON.parse(data);
                            $statusChangeDialog.data('requestsForSelectedRouteList', requestsArray);
                            $requestCheckBoxes.html("");
                            $numberRequestCheckBoxes.html("");
                            $('#statusCurrent').html(requestsArray[0]['requestStatusRusName']);
                            requestsArray.forEach(function(request){
                                
                                $('#selectNumbersRequestsTr').show();
                                $statusesRequest.html('<span style="font-weight:bold;">'+request.requestStatusRusName+'</span>'+'&nbsp;&nbsp;');
                                $requestCheckBoxes.append('<label>'+'<input type="checkbox" value='+request.requestID+' checked>'+request.requestIDExternal+'</label>'+'&nbsp;&nbsp;');
                                $numberRequestCheckBoxes.append('<span style="font-weight:bold;">'+request.invoiceNumber+'</span>'+'&nbsp;&nbsp;');

                            });
                        }
                    );
                    break;
            }
        },
        buttons: {
            "Сохранить": function () {

                // get all common variables
                var dialogType = $statusChangeDialog.data('dialogType');
                var dataTable = $statusChangeDialog.data('dataTable');
                var newStatusID = $statusSelect[0][$statusSelect[0].selectedIndex].value;
                var date = $('#dateTimePicker')[0].value;
                var comment = $("#commentInput").val();
                var vehicleNumber = $("#vehicleNumberInput").val();

                if (dialogType === "changeStatusForRequest") {

                    // get specific vars for "changeStatusForRequest" dialogType

                    var requestIDExternal = dataTable.row($('#user-grid .selected')).data().requestIDExternal;
                    if (date)
                        $.post("content/getData.php",
                            {
                                status: dialogType,
                                requestIDExternal: requestIDExternal,
                                newStatusID: newStatusID,
                                date: date,
                                comment: comment,
                                vehicleNumber: vehicleNumber
                            },
                            function (data) {
                                if (data === '1') {
                                    dataTable.draw(false);
                                    $statusChangeDialog.dialog("close");
                                }
                            }
                        );
                    else {
                        alert("date should not be empty"); // TODO
                    }

                } else if (dialogType === "changeStatusForSeveralRequests") {

                    // get specific vars for "changeStatusForSeveralRequests" dialogType
                    //var routeListID = dataTable.row($('#user-grid .selected')).data().routeListID;
                    var palletsQty = $("#palletsQtyInput").cleanVal();

                    var requests = [];
                    $requestCheckBoxes.find("input[type='checkbox']:checked").each(function(index){
                        requests.push($(this).attr('value'));
                    });

                    if ((newStatusID !== DEPARTURE_STATUS && date) || (newStatusID === DEPARTURE_STATUS && date && palletsQty))
                        $.post(
                            "content/getData.php",
                            {
                                status: dialogType,
                                newStatusID: newStatusID,
                                date: date,
                                comment: comment,
                                vehicleNumber: vehicleNumber,
                                palletsQty: palletsQty,
                                requestIDExternalArray: requests
                            },
                            function (data) {
                                if (data === '1') {
                                    dataTable.draw(false);
                                    $statusChangeDialog.dialog("close");
                                }
                            }
                        );
                    else {
                        alert("date and palletsQty should not be empty"); // TODO
                    }
                }
            },
            "Отмена": function () {
                $(this).dialog("close");
            }
        }
    });

    // external function
    $.showRequestStatusDialog = function (dialogType, dataTable) {
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