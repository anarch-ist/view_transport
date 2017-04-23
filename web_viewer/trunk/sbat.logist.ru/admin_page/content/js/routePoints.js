//Client-to-server
/*
 Client-to-server
 action: create, edit, remove
 data: An object containing the row ids to act upon and edited data for those rows.
 preSubmit: hook to add additional parameters before send to the server
 postSubmit: hook to manupulate submitted data before draw
 */
// TODO remove code duplicates
$(document).ready(function () {
    // load all required data
    {
        // when page is loading make request and get all points
        $.post(
            "content/getData.php",
            {status: "getAllPointIdPointNamePairs", format: "json"},
            // server returns array of pairs [{pointID:1, pointName:"somePoint1"}, {pointID:2, pointName:"somePoint2"}]
            function (data) {
                var options = [];

                var selectizePointsOptions = [];
                data = JSON.parse(data);
                data.forEach(function (entry) {
                    var option = "<option value=" + entry.pointID + ">" + entry.pointName + "</option>";
                    options.push(option);
                    var selectizeOption = {"label": entry.pointName, "value": entry.pointID};
                    selectizePointsOptions.push(selectizeOption);
                });


                var selectize1 = routePointsEditor.field('pointName').inst();
                selectize1.clear();
                selectize1.clearOptions();
                selectize1.load(function (callback) {
                    callback(selectizePointsOptions);
                });

                var selectize2 = usersEditor.field('pointName').inst();
                selectize2.clear();
                selectize2.clearOptions();
                selectize2.load(function (callback) {
                    callback(selectizePointsOptions);
                });
            }
        );

        var $routeSelectSelectize;
        // when page is loading make request and get all routes
        $.post(
            "content/getData.php",
            {status: "getAllRouteIdDirectionPairs", format: "json"},
            // server returns array of pairs [{routeID:1, directionName:"SomeDir1"}, {routeID:2, directionName:"SomeDir2"}]
            function (data) {
                var options = [];
                data = JSON.parse(data);
                data.forEach(function (entry) {
                    // console.log(entry.routeID);
                    // console.log(entry.directionName);
                    var option = "<option value=" + entry.routeID + ">" + entry.directionName + "</option>";
                    options.push(option);
                });
                $routeSelectSelectize = $("#routeSelect").html(options.join("")).selectize(
                    {
                        diacritics: true,
                        maxOptions: 10000,
                        maxItems: 1,
                        dropdownParent: null, // body or null
                        selectOnTab: true,
                        onChange: function (value) {
                            onRouteChanged(value);
                        }
                    });
                // console.log($routeSelectSelectize);
                onRouteChanged(getCurrentRouteId());
            }
        );

        $.post( "content/getData.php",
            {status: "getClients", format: "json"},
            function (clientsData) {
                var options = [];
                var selectizeOptions = [];
                clientsData = JSON.parse(clientsData);
                clientsData.forEach(function (entry) {
                    var option = "<option value=" + entry.clientID + ">" + entry.INN + "</option>";
                    options.push(option);
                    var selectizeOption = {"label": entry.INN, "value": entry.clientID};
                    selectizeOptions.push(selectizeOption);
                });
                var clientSelectize = usersEditor.field('clientID').inst();

                clientSelectize.clear();
                clientSelectize.clearOptions();
                clientSelectize.load(function (callback) {
                    callback(selectizeOptions);
                });
            }
        );

        // get all user roles from server
        $.post( "content/getData.php",
            {status: "getAllUserRoles", format:"json"},
            // server returns array of pairs [{userRoleID:'ADMIN', userRoleRusName:'Администратор'}, {userRoleID:'W_DISPATCHER', userRoleRusName:'Диспетчер_склада'}]
            function(userRolesData) {
                var options = [];
                var selectizeOptions = [];
                userRolesData = JSON.parse(userRolesData);
                userRolesData.forEach(function(entry) {
                    var option = "<option value=" + entry.userRoleID+">" + entry.userRoleRusName + "</option>";
                    options.push(option);
                    var selectizeOption = { "label": entry.userRoleRusName, "value": entry.userRoleID };
                    selectizeOptions.push(selectizeOption);
                });
                var userRoleSelectize = usersEditor.field('userRoleRusName').inst();

                userRoleSelectize.clear();
                userRoleSelectize.clearOptions();
                userRoleSelectize.load(function(callback) {
                    callback(selectizeOptions);
                });
            });

        // TODO load distances between points
    }


    $( "#tabs" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
    $( "#tabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );

    $("#daysOfWeekSelect").buttonset();
    $("#updateDaysOfWeek").button().click(function (e) {

        var daysOfWeek = [];
        $("#daysOfWeekSelect :checkbox:checked").each(function () {
            daysOfWeek.push($(this).attr('id'));
        });
        $("#ajaxLoaderGif").show();
        $.post(
            'content/getData.php',
            {
                status: 'updateDaysOfWeek',
                routeID: $routeSelectSelectize[0].selectize.items[0],
                daysOfWeek: daysOfWeek
            },
            function (serverData) {
                setDaysOfWeekData(JSON.parse(serverData));
                $("#ajaxLoaderGif").hide();
            }
        );
    });

    $("#startRouteTimeInput").mask('00:00', {clearIfNotMatch: true, placeholder: "чч:мм"});
    $("#updateStartRouteTime").button().click(function (e) {
        $("#ajaxLoaderGif").show();
        $.post(
            'content/getData.php',
            {
                status: 'updateStartRouteTime',
                routeID: $routeSelectSelectize[0].selectize.items[0],
                firstPointArrivalTime: $('#startRouteTimeInput').val()
            },
            function (serverData) {
                setFirstPointArrivalTime(serverData);
                $("#ajaxLoaderGif").hide();
            }
        );
    });


    // create Editor Internatialization
    $.extend(true, $.fn.dataTable.Editor.defaults, {
        "i18n": {
            "create": {
                "button": "Новая",
                "title": "Добавить новую запись",
                "submit": "Добавить"
            },
            "edit": {
                "button": "Изменить",
                "title": "Изменить запись",
                "submit": "Обновить"
            },
            "remove": {
                "button": "Удалить",
                "title": "Удалить",
                "submit": "Удалить",
                "confirm": {
                    "_": "Вы уверены, что хотите удалить %d записей?",
                    "1": "Вы уверены, что хотите удалить запись?"
                }
            },
            "error": {
                "system": "Возникла системная ошибка"
            },
            "multi": {
                "title": "Множество значений",
                "info": "The selected items contain different values for this input. To edit and set all items for this input to the same value, click or tap here, otherwise they will retain their individual values.",
                "restore": "Обратить изменения"
            },
            "datetime": {
                "previous": "Предыдущая",
                "next": "Следующая",
                "months": ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
                "weekdays": ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
                "amPm": ["am", "pm"],
                "unknown": "-"
            }
        }
    });

    // routePointsDataTable and routePointsEditor
    {
        var routeEditor = new $.fn.dataTable.Editor( {
            ajax: 'content/getData.php',
            table: '#routeTable',
            idSrc: 'routeID',

            fields: [
                { label: 'Название маршрута', name: 'routeName', type: 'text'},
                {
                    label: 'Стоимость за точку',
                    name: 'cost_per_point',
                    type: 'mask',
                    mask: "999999999999.99",
                    maskOptions: { clearIfNotMatch: true},
                    placeholder: "1000.00"
                },
                {
                    label: 'Стоимость за час',
                    name: 'cost_per_hour',
                    type: 'mask',
                    mask: "999999999999.99",
                    maskOptions: { clearIfNotMatch: true},
                    placeholder: "1000.00"
                },
                {
                    label: 'Стоимость за маршрут',
                    name: 'cost',
                    type: 'mask',
                    mask: "999999999999.99",
                    maskOptions: { clearIfNotMatch: true},
                    placeholder: "1000.00"
                }
            ]
        });

        routeEditor.on('preSubmit', function (e, data, action) {
            data.status = 'routeEditingOnly';
        });

        var $routeDataTable =  $("#routeTable").DataTable({
                processing: true,
                serverSide: true,
                ajax: {
                    url: "content/getData.php", // json datasource
                    type: "post",  // method  , by default get
                    data: {"status": "getRoutesData"}
                },
                dom: 'Bfrtip',
                // language: {
                //     url:'/localization/dataTablesRus.json'
                // },
                select: {
                    style: 'single'
                },
                "buttons": [
                    {
                        extend: "create",
                        editor: routeEditor,
                        text: 'добавить запись'
                    },
                    {
                        extend: "remove",
                        editor: routeEditor,
                        text: 'удалить запись'
                    }
                ],
                "paging": 10,
                "columnDefs": [
                    {"name": "routeID", "data": "routeID", "targets": 0, visible: false},
                    {"name": "routeName", "data": "routeName", "targets": 1},
                    {"name": "tariffID", "data": "tariffID", "targets": 2, visible: false},
                    {"name": "cost", "data": "cost", targets: 3},
                    {"name": "cost_per_point", "data": "cost_per_point", "targets": 4},
                    {"name": "cost_per_hour", "data": "cost_per_hour", "targets": 5}
                ]
            }
        );

        var routePointsEditor = new $.fn.dataTable.Editor({
            ajax: 'content/getData.php',
            table: '#routePointsTable',
            idSrc: 'routePointID',

            fields: [
                {
                    label: 'Порядковый номер',
                    name: 'sortOrder',
                    type: 'mask',
                    mask: "0",
                    maskOptions: {clearIfNotMatch: true, translation:{'0': {pattern: /[1-9]/}}},
                    placeholder: "1-9"
                },
                {
                    label: 'Пункт', name: 'pointName', type: 'selectize',
                    options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'label',
                        labelField: 'label',
                        dropdownParent: "body"
                    }
                },
                {
                    label: 'Продолжительность разгрузочных работ',
                    name: 'tLoading',
                    type: 'mask',
                    mask: '00:00',
                    maskOptions: {clearIfNotMatch: true},
                    placeholder: "чч:мм"
                }
                //{ label: 'Продолжительность разгрузочных работ',  name: 'tLoading', type: 'mask', mask:"00ч.00м.", maskOptions: {clearIfNotMatch: true}, placeholder:"__ч.__м."}
            ]
        });

        // transfrom string like 18ч.30м. to 18*60+30
        routePointsEditor.on('preSubmit', function (e, data, action) {
            data.status = 'routeEditing';
            if (action === 'create' || action === 'edit') {
                for (var i in data.data) {
                    data.data[i].routeID = $("#routeSelect option")[0].value;
                    data.data[i].tLoading = stringToMinutes(data.data[i].tLoading);
                }
            }
        });

        routePointsEditor.on('postSubmit', function (e, json, data, action) {

            // refresh relations between points dataTable
            $.post(
                "content/getData.php",
                {status: "getRelationsBetweenRoutePointsDataForRouteID", routeID: getCurrentRouteId(), format: "json"},
                function (data) {
                    data = JSON.parse(data);
                    setRelationsBetweenRoutePointsData(data);
                }
            );

            var dataObject;
            if (action === 'create') {
                dataObject = json.data[0];
                dataObject.tLoading = minutesToString(dataObject.tLoading);
            } else if (action === 'edit') {
                dataObject = json.data[0];
                dataObject.tLoading = minutesToString(dataObject.tLoading);
                $routePointsDataTable.row(".selected").data(dataObject).draw(false);
            } else if (action == 'remove') {

            }
        });

        // set current selected value to pointName
        routePointsEditor.on('open', function (e , mode, action) {
            if (action === "edit") {
                setSelectizeValueFromTable($routePointsDataTable, routePointsEditor, 'pointName', 'pointName');
            }
        });

        var $routePointsDataTable = $("#routePointsTable").DataTable({
                "dom": 'Bt', // show only buttons and table with no decorations
                "idSrc": 'routePointID',
                //ajax: 'content/getData.php',
                language: {
                    url: '/localization/dataTablesRus.json'
                },
                select: {
                    style: 'single'
                },
                "buttons": [
                    {
                        extend: "create",
                        editor: routePointsEditor,
                        text: 'добавить запись'
                    },
                    {
                        extend: "remove",
                        editor: routePointsEditor,
                        text: 'удалить запись'
                    }
                    // TODO временно блокируется возможность менять, пока можно только удалять и создавать новые пункты
                    // {
                    //
                    //    extend: "edit",
                    //    editor: routePointsEditor,
                    //    text: "изменить"
                    // }
                ],
                "paging": false, // no pagination
                "columnDefs": [
                    {"name": "sortOrder", "data": "sortOrder", "targets": 0},
                    {"name": "pointName", "data": "pointName", "targets": 1},
                    {"name": "tLoading", "data": "tLoading", "targets": 2}
                ]
            }
        );
    }

    // $relationsBetweenRoutePointsDataTable and relationsBetweenRoutePointsEditor
    {
        var relationsBetweenRoutePointsEditor = new $.fn.dataTable.Editor({
            ajax: {
                edit: {
                    type: 'POST',
                    url: 'content/getData.php',
                    data: {"status": "relationsBetweenRoutePoints"}
                }
            },
            table: '#relationsBetweenRoutePointsTable',
            idSrc: 'relationID',

            fields: [
                {label: 'Начальный пункт', name: 'pointNameFirst', type: 'readonly'},
                {label: 'Конечный пункт', name: 'pointNameSecond', type: 'readonly'},
                {label: 'Расстояние', name: 'distance', type: 'readonly'},
                {
                    label: 'Время в пути',
                    name: 'timeForDistance',
                    type: 'mask',
                    mask: '00:00',
                    maskOptions: {clearIfNotMatch: true},
                    placeholder: "чч:мм"
                }
            ]
        });

        relationsBetweenRoutePointsEditor.on('preSubmit', function (e, data, action) {
            data.routeID = $routeSelectSelectize[0].selectize.items[0];
            if (action === 'edit') {
                for (i in data.data) {
                    data.data[i].timeForDistance = stringToMinutes(data.data[i].timeForDistance);
                }
            }
        });

        //manually edit data in table
        relationsBetweenRoutePointsEditor.on('postSubmit', function (e, json, data, action) {
            if (action === "edit") {
                //var dataObject = json.data[0];
                setRelationsBetweenRoutePointsData(json.data);
                //dataObject.timeForDistance = minutesToString(dataObject.timeForDistance);
                //$relationsBetweenRoutePointsDataTable.row(".selected").data(dataObject).draw(false);
            }
        });

        var $relationsBetweenRoutePointsDataTable = $("#relationsBetweenRoutePointsTable").DataTable({
                "dom": 'Bt', // show only buttons and table with no decorations
                language: {
                    url: '/localization/dataTablesRus.json'
                },
                select: {
                    style: 'single'
                },
                "buttons": [
                    {
                        extend: "edit",
                        editor: relationsBetweenRoutePointsEditor,
                        text: "изменить"
                    }
                ],
                "paging": false, // no pagination
                "ordering": false,
                "columnDefs": [
                    {"name": "pointNameFirst", "data": "pointNameFirst", "targets": 0},
                    {"name": "pointNameSecond", "data": "pointNameSecond", "targets": 1},
                    {"name": "distance", "data": "distance", "targets": 2},
                    {"name": "timeForDistance", "data": "timeForDistance", "targets": 3}
                ]
            }
        );

    }

    // helper functions
    {
        function stringToMinutes(string) {
            var houres = string.substring(0, 2);
            var minutes = string.substr(2, 2);
            var result = 60 * parseInt(houres) + parseInt(minutes);
            return result;
        }

        function minutesToString(intMinutes) {
            var minutes = intMinutes % 60;
            var strMinutes = "";
            if (minutes <= 9) strMinutes = "0" + minutes;
            else strMinutes = minutes + "";

            var houres = Math.floor(intMinutes / 60);
            var strHoures = "";
            if (houres <= 9) strHoures = "0" + houres;
            else strHoures = houres + "";

            return strHoures + ":" + strMinutes;
        }

        function setDaysOfWeekData(daysOfWeek) {
            if (!$.isArray(daysOfWeek)) throw "illegalArgumentException: input arg should be array";

            $("#daysOfWeekSelect").find("input").each(function () {
                $(this).prop('checked', false).button("refresh");
            });

            daysOfWeek.forEach(function (dayOfWeek) {
                $('#' + dayOfWeek).prop('checked', true).button("refresh");
            });
        }

        function setFirstPointArrivalTime(firstPointArrivalTime) {
            $('#startRouteTimeInput').val(firstPointArrivalTime).trigger('keyup');
        }

        function setRoutePointsData(routePointsData) {
            $routePointsDataTable.rows().remove();
            routePointsData.forEach(function (entry) {
                entry.tLoading = minutesToString(entry.tLoading);
            });
            $routePointsDataTable.rows.add(routePointsData).draw(false);
        }

        function setRelationsBetweenRoutePointsData(relationsBetweenRoutePointsData) {
            $relationsBetweenRoutePointsDataTable.rows().remove();
            relationsBetweenRoutePointsData.forEach(function (entry) {
                entry.timeForDistance = minutesToString(entry.timeForDistance);
            });
            $relationsBetweenRoutePointsDataTable.rows.add(relationsBetweenRoutePointsData).draw(false);
        }

        function getCurrentRouteId() {
            return $("#routeSelect option")[0].value;
        }

        // initial loading of all data
        function onRouteChanged(value) {
            $.post(
                "content/getData.php",
                {status: "getAllRoutePointsDataForRouteID", routeID: value, format: "json"},
                function (data) {
                    data = JSON.parse(data);
                    setDaysOfWeekData(data.daysOfWeek);
                    setFirstPointArrivalTime(data.firstPointArrivalTime);
                    setRoutePointsData(data.routePoints);
                    setRelationsBetweenRoutePointsData(data.relationsBetweenRoutePoints);
                }
            );
        }

        function setSelectizeValueFromTable(dataTable, editor, selectizeFieldName, dataTableDataName) {
            var selectedRowData = dataTable.row().data();
            var selectizeInstance = editor.field(selectizeFieldName).inst();
            selectizeInstance.setValue(selectizeInstance.search(selectedRowData[dataTableDataName]).items[0].id, true);
        }
    }

    // $usersDataTable and usersEditor
    {
        var usersEditor = new $.fn.dataTable.Editor( {
            ajax: 'content/getData.php',
            table: '#usersTable',
            idSrc: 'userId',

            fields: [
                { label: 'ФИО', name: 'userName', type: 'text'},
                { label: 'Логин', name: 'login', type: 'text'},
                { label: 'Должность',  name: 'position', type: 'text'},
                {
                    label: 'Номер телефона',
                    name: 'phoneNumber',
                    type: 'mask',
                    mask:"(000) 000-00-00",
                    maskOptions: {clearIfNotMatch: true},
                    placeholder: "(999) 999-99-99"
                },
                { label: 'Почта',  name: 'email', type: 'text', visible: false},
                { label: 'Пароль',  name: 'password', type: 'password'},
                { label: 'Роль',  name: 'userRoleRusName', type: 'selectize', options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'label',
                        labelField: 'label',
                        dropdownParent: "body"
                    }
                },
                { label: 'Пункт',  name: 'pointName', type: 'selectize', options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'label',
                        labelField: 'label',
                        dropdownParent: "body"
                    }
                },
                {
                    label: 'ИНН Клиента',
                    name: 'clientID',
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
        } );

        // set current selected value to pointName and userRoleRusName
        usersEditor.on('open', function (e , mode, action) {
            usersEditor.field('pointName').disable();
            usersEditor.field('clientID').disable();
            if (action === "edit") {
                // я не знаю что оно должно было делать, но работает с багами, так что я просто отключил
                // код я пока оставлю
                // setSelectizeValueFromTable($usersDataTable, usersEditor, 'pointName', 'pointName');
                // setSelectizeValueFromTable($usersDataTable, usersEditor, 'userRoleRusName', 'userRoleRusName');
                // setSelectizeValueFromTable($usersDataTable, usersEditor, 'clientID', 'clientID');
            }
        });

        // transform password to md5
        usersEditor.on('preSubmit', function (e, data, action) {
            data.status = 'userEditing';
            if (action === 'create' || action === 'edit') {
                for (i in data.data) {
                    data.data[i].password = calcMD5(data.data[i].password);
                }
            }
        });

        usersEditor.field('userRoleRusName').input().on('change', function (e, d) {
            if ( d && d.editorSet ) return;

            var currentRole = $(this).val();
            if ( currentRole === "CLIENT_MANAGER") {
                usersEditor.field('pointName').disable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').enable();
                usersEditor.field('clientID').set('');
            }

            if ( currentRole === "TEMP_REMOVED" ) {
                usersEditor.field('pointName').enable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').enable();
                usersEditor.field('clientID').set('');
            }

            if ( currentRole === "ADMIN" || currentRole === "MARKET_AGENT") {
                usersEditor.field('pointName').disable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').disable();
                usersEditor.field('clientID').set('');
            }

            if ( currentRole === "DISPATCHER" || currentRole === "W_DISPATCHER") {
                usersEditor.field('pointName').enable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').disable();
                usersEditor.field('clientID').set('');
            }
        });

        // example data for exchange with server
        //var exampleData = [{userID: 1, userName:"wefwfe", position: "efewerfw", patronymic:"ergerge", phoneNumber: "9055487552",
        //    email: "qwe@qwe.ru", password:"lewrhbwueu23232", userRoleRusName:"Диспетчер", pointName:"point1"}];

        var $usersDataTable =  $("#usersTable").DataTable({
                processing: true,
                serverSide: true,
                ajax: {
                    url: "content/getData.php", // json datasource
                    type: "post",  // method  , by default get
                    data: {"status": "getUsersData"}
                },
                dom: 'Bfrtip',
                language: {
                    url:'/localization/dataTablesRus.json'
                },
                select: {
                    style: 'single'
                },
                "buttons": [
                    {
                        extend: "create",
                        editor: usersEditor,
                        text: 'добавить запись'
                    },
                    {
                        extend: "remove",
                        editor: usersEditor,
                        text: 'удалить запись'
                    },
                    {
                        extend: "edit",
                        editor: usersEditor,
                        text: "изменить"
                    }
                ],
                "paging": 10,
                "columnDefs": [
                    {"name": "userId", "data": "userId", "targets": 0, visible: false},
                    {"name": "userName", "data": "userName", "targets": 1},
                    {"name": "login", "data": "login", "targets": 2},
                    {"name": "position", "data": "position", "targets": 3},
                    {"name": "phoneNumber", "data": "phoneNumber", "targets": 4},
                    {"name": "email", "data": "email", "targets": 5},
                    {"name": "password", "data": "password", "targets": 6, visible: false},
                    {"name": "userRoleRusName", "data": "userRoleRusName", "targets": 7},
                    {"name": "pointName", "data": "pointName", "targets": 8},
                    {"name": "clientID", "data": "clientID", "targets": 9, visible: false}
                ]
            }
        );
    }

    // TODO create distances between points dataTable and editor

    {
        var transportCompaniesEditor = new $.fn.dataTable.Editor( {
            ajax: 'content/getData.php',
            table: '#transportCompaniesTable',
            idSrc: 'id',

            fields: [
                { label: 'Полное название', name: 'name', type: 'text'},
                { label: 'Название', name: 'short_name', type: 'text'},
                {
                    label: 'ИНН',
                    name: 'inn',
                    type: 'mask',
                    mask: "0000000000",
                    maskOptions: {clearIfNotMatch: true},
                    placeholder: "1234567890"
                },
                { label: 'КПП',  name: 'KPP', type: 'text'},
                { label: 'БИК',  name: 'BIK', type: 'text'},
                { label: 'Кор. счет',  name: 'cor_account', type: 'text'},
                { label: 'Кар. счет',  name: 'cur_account', type: 'text'},
                { label: 'Название банка',  name: 'bank_name', type: 'text'},
                { label: 'Адрес',  name: 'legal_address', type: 'text'},
                { label: 'Почтовый адрес',  name: 'post_address', type: 'text'},
                { label: 'keywords',  name: 'keywords', type: 'text'},
                { label: 'ФИО Директора',  name: 'director_fullname', type: 'text'},
                { label: 'ФИО глав.бухгалтера',  name: 'chief_acc_fullname', type: 'text'}
            ]
        });

        transportCompaniesEditor.on('preSubmit', function (e, data, action) {
            data.status = 'transportCompaniesEditing';
        });

        var $transportCompaniesTable =  $("#transportCompaniesTable").DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                url: "content/getData.php", // json datasource
                type: "post",  // method  , by default get
                data: {"status": "getTransportCompaniesData"}
            },
            dom: 'Bfrtip',
            language: {
                url:'/localization/dataTablesRus.json'
            },
            select: {
                style: 'single'
            },
            "buttons": [
                {
                    extend: "create",
                    editor: transportCompaniesEditor,
                    text: 'добавить запись'
                },
                {
                    extend: "remove",
                    editor: transportCompaniesEditor,
                    text: 'удалить запись'
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "id", "data": "id", "targets": 0},
                {"name": "name", "data": "name", "targets": 1},
                {"name": "short_name", "data": "short_name", "targets": 2},
                {"name": "inn", "data": "inn", "targets": 3},
                {"name": "KPP", "data": "KPP", "targets": 4},
                {"name": "BIK", "data": "BIK", "targets": 5},
                {"name": "cor_account", "data": "cor_account", "targets": 6},
                {"name": "cur_account", "data": "cur_account", "targets": 7},
                {"name": "bank_name", "data": "bank_name", "targets": 8},
                {"name": "legal_address", "data": "legal_address", "targets": 9},
                {"name": "post_address", "data": "post_address", "targets": 10},
                {"name": "keywords", "data": "keywords", "targets": 11},
                {"name": "director_fullname", "data": "director_fullname", "targets": 12},
                {"name": "chief_acc_fullname", "data": "chief_acc_fullname", "targets": 13}
            ]
        });
    }

    {
        var vehiclesEditor = new $.fn.dataTable.Editor({
            ajax: 'content/getData.php',
            table: '#vehiclesTable',
            idSrc: 'id',

            fields: [
                {label: 'Номер ТК', name: 'transport_company_id', type: 'text'},
                {label: 'Номер лицензии', name: 'license_number', type: 'text'},
                {label: 'Модель', name: 'model', type: 'text'},
                {label: 'Грузоподъемность', name: 'carrying_capacity', type: 'text'},
                {label: 'Объем', name: 'volume', type: 'text'},
                {label: 'Тип погрузки', name: 'loading_type', type: 'text'},
                {label: 'Количество палетов', name: 'pallets_quantity', type: 'text'},
                {label: 'Тип', name: 'type', type: 'text'}
            ]
        });

        vehiclesEditor.on('preSubmit', function (e, data, action) {
            data.status = 'vehiclesEditing';
        });

        var $vehiclesTable = $("#vehiclesTable").DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                url: "content/getData.php", // json datasource
                type: "post",  // method  , by default get
                data: {"status": "getVehiclesData"}
            },
            dom: 'Bfrtip',
            language: {
                url: '/localization/dataTablesRus.json'
            },
            select: {
                style: 'single'
            },
            "buttons": [
                {
                    extend: "create",
                    editor: vehiclesEditor,
                    text: 'добавить запись'
                },
                {
                    extend: "remove",
                    editor: vehiclesEditor,
                    text: 'удалить запись'
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "id", "data": "id", "targets": 0},
                {"name": "transport_company_id", "data": "transport_company_id", "targets": 1},
                {"name": "license_number", "data": "license_number", "targets": 2},
                {"name": "model", "data": "model", "targets": 3},
                {"name": "carrying_capacity", "data": "carrying_capacity", "targets": 4},
                {"name": "volume", "data": "volume", "targets": 5},
                {"name": "loading_type", "data": "loading_type", "targets": 6},
                {"name": "pallets_quantity", "data": "pallets_quantity", "targets": 7},
                {"name": "type", "data": "type", "targets": 8}
            ]
        });
    }

    {
        var driversEditor = new $.fn.dataTable.Editor({
            ajax: 'content/getData.php',
            table: '#driversTable',
            idSrc: 'id',

            fields: [
                {label: 'Номер ТС', name: 'vehicle_id', type: 'text'},
                {label: 'Номер ТК', name: 'transport_company_id', type: 'text'},
                {label: 'Полное имя', name: 'full_name', type: 'text'},
                {label: 'Пасспорт', name: 'passport', type: 'text'},
                {
                    label: 'Номер телефона',
                    name: 'phone',
                    type: 'mask',
                    mask:"(000) 000-00-00",
                    maskOptions: {clearIfNotMatch: true},
                    placeholder: "(999) 999-99-99"
                },
                {label: 'Лицензия', name: 'license', type: 'text'}
            ]
        });

        driversEditor.on('preSubmit', function (e, data, action) {
            data.status = 'driversEditing';
        });

        var $driversTable = $("#driversTable").DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                url: "content/getData.php", // json datasource
                type: "post",  // method  , by default get
                data: {"status": "getDriversData"}
            },
            dom: 'Bfrtip',
            language: {
                url: '/localization/dataTablesRus.json'
            },
            select: {
                style: 'single'
            },
            "buttons": [
                {
                    extend: "create",
                    editor: driversEditor,
                    text: 'добавить запись'
                },
                {
                    extend: "remove",
                    editor: driversEditor,
                    text: 'удалить запись'
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "id", "data": "id", "targets": 0, visible: false},
                {"name": "vehicle_id", "data": "vehicle_id", "targets": 1},
                {"name": "transport_company_id", "data": "transport_company_id", "targets": 2},
                {"name": "full_name", "data": "full_name", "targets": 3},
                {"name": "passport", "data": "passport", "targets": 4},
                {"name": "phone", "data": "phone", "targets": 5},
                {"name": "license", "data": "license", "targets": 6}
            ]
        });
    }
});