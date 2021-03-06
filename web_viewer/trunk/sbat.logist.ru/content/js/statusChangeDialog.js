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
        '<tr valign="top" id="currentStatusTR" ><td width="200" padding="10px"><label for="statusCurrent">Текущий статус: </label></td><td><strong id="statusCurrent"></strong></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="statusSelect">Новый статус: </label></td><td><select id="statusSelect"></select></td></tr>' +
        '<tr id="companyTr" valign="top" ><td width="200"><label for="companyInput">Транспортная компания: </label></td><td><input id="companyInput" /></td></tr>' +
        '<tr id="vehicleNumberTr" valign="top" ><td width="200"><label for="vehicleNumberInput">Транспортное средство #1: </label></td><td><input id="vehicleNumberInput"/></td></tr>' +
        '<tr id="vehicleNumber2Tr" valign="top" ><td width="200"><label for="vehicleNumber2Input">Транспортное средство #2: </label></td><td><input id="vehicleNumber2Input"/></td></tr>' +
        '<tr id="vehicleNumber3Tr" valign="top" ><td width="200"><label for="vehicleNumber3Input">Транспортное средство #3: </label></td><td><input id="vehicleNumber3Input"/></td></tr>' +
        '<tr id="driverTr" valign="top" ><td width="200"><label for="driverInput">Водитель: </label></td><td><input id="driverInput" /></td></tr>' +
        '<tr id="linkTr"><td></td><td><a href="../../admin_page/#tabs-3">Добавить компанию/ТС/Водителя</a></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="dateTimePickerInput">Дата и время: </label></td><td><input id="dateTimePicker" type="text"></td></tr>' +
        '<tr id="goodCostTr" valign="top" ><td width="200"><label for="goodCost"> Стоймость </label></td><td><input id="goodCost" type="text"></td></tr>' +
        '<tr id="hoursAmountTr" valign="top" ><td width="200"><label for="hoursAmount">Кол-во часов: </label></td><td><input id="hoursAmount" type="text"></td></tr>' +
        '<tr id="palletsQtyTr" valign="top" ><td width="200"><label for="palletsQtyInput">Количество паллет: </label></td><td><input id="palletsQtyInput" type="text"/></td></tr>' +
        '<tr id="boxQtyTr" valign="top" ><td width="200"><label for="boxQtyInput">Количество коробок: </label></td><td><input id="boxQtyInput" type="text"/></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="commentInput">Комментарий: </label></td><td><textarea id="commentInput" maxlength="500"/></td></tr>' +
        '<tr id="selectRequestsTr" valign="top"><td width="200"><label for="statusSelect">Накладные: </label></td><td><div id="requestCheckBoxes2"><table id="requestCheckBoxes"></table></div></td></tr>' +
        // '<tr id="selectNumbersRequestsTr" valign="top"><td width="200"><label for="statusSelect">Номера накладных: </label></td><td><div id="numberRequestCheckBoxes"></div></td></tr>' +
        '</table>' +
        '</div>'
    );
    if ($("#data-role").html().trim() == "Пользователь_клиента") {
        $('#vehicleNumberTr').hide();
        $('#linkTr').hide();
        $('#companyTr').hide();
        $('#driverTr').hide();
    }

    //populate TC select


    $.post("content/getData.php",
        {status: "getCompanies", format: "json"},
        function (companiesData) {

            // var options = [];
            var selectizeOptions = [];
            companiesData = JSON.parse(companiesData);

            companiesData.forEach(function (entry) {

                // var option = "<option value=" + entry.id + ">" + entry.name + "</option>";
                // options.push(option);
                // console.log("id:"+entry.id+" name:"+entry.name+"\n");
                var selectizeOption = {text: entry.name, value: entry.id};
                selectizeOptions.push(selectizeOption);
            });
            $('#driverInput').selectize({
                sortField: "text",
                maxItems: 1,
                onChange: function (value) {
                    if (!value.length) return;
                }
            });
            var driverInput = $('#driverInput')[0].selectize;
            driverInput.disable();

            $('#vehicleNumberInput').selectize({
                sortField: "text",
                maxItems: 1,
                onChange: function (value) {
                    if (!value.length) return;
                    driverInput.disable();
                    $.post(
                        'content/getData.php',
                        {
                            status: 'getDrivers',
                            vehicleId: Number(value)
                        }, function (driversData) {
                            var driversOptions = [];
                            driversData = JSON.parse(driversData);

                            driversData.forEach(function (entry) {
                                var selectizeOption = {text: entry.full_name, value: entry.id};
                                driversOptions.push(selectizeOption);
                            });
                            driverInput.clear();
                            driverInput.clearOptions();
                            driverInput.load(function (callback) {
                                callback(driversOptions)
                            });
                            driverInput.enable();
                        })

                }
            });

            $('#vehicleNumber2Input').selectize({
                sortField: "text",
                maxItems: 1,
                onChange: function (value) {
                    if (!value.length) return;
                }
            });

            $('#vehicleNumber3Input').selectize({
                sortField: "text",
                maxItems: 1,
                onChange: function (value) {
                    if (!value.length) return;
                }
            });

            var vehicleInput = $('#vehicleNumberInput')[0].selectize;
            var vehicleInput2 = $('#vehicleNumber2Input')[0].selectize;
            var vehicleInput3 = $('#vehicleNumber3Input')[0].selectize;
            vehicleInput.disable();
            vehicleInput2.disable();
            vehicleInput3.disable();

            $('#companyInput').selectize({
                placeholder: "Нажмите, чтобы изменить",
                sortField: "text",
                maxItems: 1,
                onChange: function (value) {
                    if (!value.length) return;
                    vehicleInput.disable();
                    $.post(
                        'content/getData.php',
                        {
                            status: 'getVehicles',
                            companyId: Number(value)
                        }, function (vehiclesData) {
                            vehicleOptions=[];
                            vehiclesData=JSON.parse(vehiclesData);
                            vehiclesData.forEach(function (entry) {
                                var selectizeOption = {
                                    text: entry.license_number + " / " + entry.model,
                                    value: entry.id
                                };
                                vehicleOptions.push(selectizeOption);
                            });
                            vehicleInput.clear();
                            vehicleInput2.clear();
                            vehicleInput3.clear();
                            vehicleInput.clearOptions();
                            vehicleInput2.clearOptions();
                            vehicleInput3.clearOptions();
                            vehicleInput.load(function (callback) {
                                callback(vehicleOptions)
                            });
                            vehicleInput2.load(function (callback) {
                                callback(vehicleOptions)
                            });
                            vehicleInput3.load(function (callback) {
                                callback(vehicleOptions)
                            });
                            vehicleInput.enable();
                            vehicleInput2.enable();
                            vehicleInput3.enable();

                            driverInput.clear();
                            driverInput.clearOptions();
                            driverInput.disable();
                        })

                }
            });
            var companySelectize = $('#companyInput')[0].selectize;
            // var clientSelectize = usersEditor.field('clientID').inst();

            companySelectize.clear();
            companySelectize.clearOptions();
            companySelectize.load(function (callback) {
                callback(selectizeOptions);
            });
        }
    );

    // create status select menu
    var $statusSelect = $("#statusSelect");
    var $requestCheckBoxes = $("#requestCheckBoxes");
    var $statusesRequest = $("#statusesRequest");
    var $numberRequestCheckBoxes = $("#numberRequestCheckBoxes");
    var $selectRequestsTr = $("#selectRequestsTr");
    var $selectNumbersRequestsTr = $("#selectNumbersRequestsTr");

    function populateVehicleSelectMenu() {
        var options = [];
    }


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
        // console.log(JSON.stringify(storedStatuses));
    }

    // create palletsQty input
    $("#palletsQtyInput").mask("00", {
        placeholder: "0-99"
});

    // create boxQty input
    $("#boxQtyInput").mask("0000", {
        placeholder: "0-9999"
    });

    // create comment input
    $("#commentInput")
        .addClass("ui-widget ui-state-default ui-corner-all")
        .attr("rows", "5")
        .attr("placeholder", "Введите комментарий")
        .width(COMMENT_WIDTH)
        .css("resize", "none");


    // create and init dateTimePicker
    // createDateTimePickerLocalization();
    // var $dateTimePicker = $("#dateTimePicker");
    // $dateTimePicker.datetimepicker();
    $('#dateTimePicker').mask('00.00.0000 00:00', {placeholder: '31.12.2016 12:36', clearIfNotMatch: true});
    if ($('#dateTimePicker').val() == '') {

        $('#dateTimePicker').val(new Date().toLocaleString('ru-RU', {
            day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', hour12: false
        }).replace(',', '').replace('/', '.').replace('/', '.'));
    }
    $('#hoursAmount').mask('00000', {placeholder: '0-9999'});
    $('#goodCost').mask('00000', {placeholder: '0-9999'});

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


            // if ($statusSelect[0][$statusSelect[0].selectedIndex].value === "DELIVERED"){
            //     $('#hoursAmountTr').hide();
            // } else {
            //     $('#hoursAmountTr').show();
            // }

            $statusSelect.on("selectmenuchange", function (e, ui) {

                if ($statusSelect[0][$statusSelect[0].selectedIndex].value === "DELIVERED" && $("#data-role").html().trim() != "Пользователь_клиента") {
                    $('#hoursAmountTr').show();
                } else {
                    $('#hoursAmountTr').hide();
                }
            });

            $("#palletsQtyTr").hide();
            $("#boxQtyTr").hide();

    switch (dialogType) {
        case "changeStatusForRequest":
            $selectRequestsTr.hide();
            // $statusSelect.off("selectmenuchange");

            $.post(
                "content/getData.php",
                {
                    status: "getRequestsForRouteList",
                    routeListID: dataTable.row($('#user-grid .selected')).data().routeListID
                },
                function (data) {
                    // console.log(data);


                    $('#statusCurrent').html(dataTable.row($('#user-grid .selected')).data().requestStatusRusName);


                    var requestsArray = JSON.parse(data);
                    $statusChangeDialog.data('requestsForSelectedRouteList', requestsArray);
                    $requestCheckBoxes.html("");
                    $numberRequestCheckBoxes.html("");
                    // console.log(requestsArray[0]['requestStatusRusName']);
                    $('#statusCurrent').html(requestsArray[0]['requestStatusRusName']);

                    $('#selectNumbersRequestsTr').hide();
                    $selectRequestsTr.hide();
                    // requestsArray.forEach(function(request){
                    //
                    //     $statusesRequest.html('<span style="font-weight:bold;">'+request.requestStatusRusName+'</span>'+'&nbsp;&nbsp;');
                    //     /*$requestCheckBoxes.append('<label>'+'<input type="checkbox" value='+request.requestID+' checked>'+request.requestIDExternal+'</label>'+'&nbsp;&nbsp;');
                    //     $numberRequestCheckBoxes.append('<span style="font-weight:bold;">'+request.invoiceNumber+'</span>'+'&nbsp;&nbsp;');*/
                    //     // $requestCheckBoxes.append('<span style="font-weight:bold;">'+request.invoiceNumber+'</span><br>');
                    //     $requestCheckBoxes.append('<label style="font-weight:bold;">'+'<input type="checkbox" value='+request.requestIDExternal+' checked>'+request.invoiceNumber+'</label><br>');
                    //     //<label>'+'<input type="checkbox" value='+request.requestIDExternal+' checked>'+request.requestIDExternal+'</label>'+'&nbsp;&nbsp;
                    //     $selectRequestsTr.hide();
                    //     $selectNumbersRequestsTr.hide();
                    //
                    //
                    // });
                }
            );
            break;
        case "changeStatusForSeveralRequests":
            $selectRequestsTr.show();
            $('#palletsQtyTr').show();
            $('#boxQtyTr').show();
            $statusSelect.on("selectmenuchange", function (e, ui) {
                // console.log(($statusSelect[0][$statusSelect[0].selectedIndex].value));
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
                    $('#currentStatusTR').hide();
                    requestsArray.forEach(function (request) {

                        $('#selectNumbersRequestsTr').show();
                        $statusesRequest.html('<span style="font-weight:bold;">' + request.requestStatusRusName + '</span>' + '&nbsp;&nbsp;');
                        $requestCheckBoxes.append('<tr><td><label><span style="font-weight:bold;">' + '<input type="checkbox" value=' + request.requestIDExternal + ' checked>  ' + request.invoiceNumber + '</span></label></td><td>&nbsp;&nbsp;<span>' + request.requestStatusRusName + '</span></td></tr>');
                        // $requestCheckBoxes.append('<label style="font-weight:bold;">'+'<input type="checkbox" value='+request.requestIDExternal+' checked>'+request.invoiceNumber+'</label><br>');
                        //$numberRequestCheckBoxes.append('<span style="font-weight:bold;">'+request.invoiceNumber+'</span>'+'&nbsp;&nbsp;');
                        //<label>'+'<input type="checkbox" value='+request.requestIDExternal+' checked>'+request.requestIDExternal+'</label></td><td>

                    });
                    $selectRequestsTr.show();
                    $selectNumbersRequestsTr.show();
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
                var hoursAmount = $("#hoursAmount").val();
                var goodCost = $("#goodCost").val();
                var companyId = $("#companyInput")[0].selectize.getValue();
                var vehicleId = $("#vehicleNumberInput")[0].selectize.getValue();
                var vehicle2Id = $("#vehicleNumber2Input")[0].selectize.getValue();
                var vehicle3Id = $("#vehicleNumber3Input")[0].selectize.getValue();
                var driverID = $('#driverInput')[0].selectize.getValue();

                // console.log(companyId+' '+vehicleId+' '+driverID);
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
                                vehicleNumber: vehicleNumber,
                                hoursAmount: Number(hoursAmount),
                                goodCost: Number(goodCost),
                                // companyId: companyId,
                                // vehicleId: vehicleId,
                                // driverId: driverID

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
                    var boxQty = $("#boxQtyInput").cleanVal();

                    var requests = [];
                    $requestCheckBoxes.find("input[type='checkbox']:checked").each(function (index) {
                        requests.push($(this).attr('value'));
                    });

                    if ((newStatusID !== DEPARTURE_STATUS && date) || (newStatusID === DEPARTURE_STATUS && date && palletsQty) || (newStatusID === DEPARTURE_STATUS && date && boxQty))
                        $.post(
                            "content/getData.php",
                            {
                                status: dialogType,
                                newStatusID: newStatusID,
                                date: date,
                                comment: comment,
                                vehicleNumber: vehicleNumber,
                                palletsQty: palletsQty,
                                boxQty: boxQty,
                                requestIDExternalArray: requests,
                                routeListID: dataTable.row($('#user-grid .selected')).data().routeListID,
                                hoursAmount: Number(hoursAmount),
                                goodCost:Number(goodCost),
                                companyId: companyId,
                                vehicleId: vehicleId,
                                vehicle2Id: vehicle2Id,
                                vehicle3Id: vehicle3Id,
                                driverId: driverID
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
                        alert("date and boxQty should not be empty"); // TODO
                    }
                }
            },
            "Претензия": function () {
                var dataTable = $statusChangeDialog.data('dataTable');
                var clientID = dataTable.row($('#user-grid .selected')).data().clientIDExternal,
                    invoiceNumber = dataTable.row($('#user-grid .selected')).data().invoiceNumber;
                var url =
                    "?clientId=" +
                    clientID +
                    "&invoiceNumber=" +
                    invoiceNumber +
                    "&pretensionModal=1";
                url = encodeURI(url);
                pretensionWindow = window.open(url, "width=400, height=700");
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