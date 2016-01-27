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
                onRouteChanged($("#routeSelect option")[0].value);
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
                var userRoleSelectize = usersEditor.field('role').inst();

                userRoleSelectize.clear();
                userRoleSelectize.clearOptions();
                userRoleSelectize.load(function(callback) {
                    callback(selectizeOptions);
                });
            });

        // TODO load distances between points
    }

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
                routeID: $routeSelectSelectize[0].selectize.items[0],
                daysOfWeek: daysOfWeek
            },
            // example serverData : ["monday", "friday", "saturday"]
            function (serverData) {
                setDaysOfWeekData(serverData);
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
                routeID: $routeSelectSelectize[0].selectize.items[0],
                firstPointArrivalTime: $('#startRouteTimeInput').val()
            },
            // example serverData : "17:00"
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


    // PROTOCOL DESCRIPTION
    // https://editor.datatables.net/manual/server
    //Client-to-server
    //action:
    //On create: create
    //On edit: edit
    //On remove: remove
    //data:
    //contains routePointID and other columns with data
    //EXAMPLE:
    //action:"create"
    //data[0][sortOrder]:"4"
    //data[0][pointName]:"2"
    //data[0][tLoading]:"743"
    //Server-to-client
    //{
    //    "data": [
    //    {
    //        "routePointID":   "row_29",
    //        "sortOrder": "4",
    //        "pointName":  "point5",
    //        "tLoading":   "564"
    //    }
    //    ]
    //}

    // routePointsDataTable and routePointsEditor
    {
        var routePointsEditor = new $.fn.dataTable.Editor({
            ajax: {
                create: {
                    type: 'POST',
                    url: 'content/getData.php'
                },
                edit: {
                    type: 'POST',
                    url: 'content/getData.php'
                },
                remove: {
                    type: 'POST',
                    url: 'content/getData.php'
                }
            },
            table: '#routePointsTable',
            idSrc: 'routePointID',

            fields: [
                {
                    label: 'Порядковый номер',
                    name: 'sortOrder',
                    type: 'mask',
                    mask: "0",
                    maskOptions: {clearIfNotMatch: true},
                    placeholder: "0-9"
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
            if (action === 'create' || action === 'edit') {
                //
                //console.log(data.data[0].tLoading);
                //data.data[0].tLoading = stringToMinutes(data.data[0].tLoading);
                for (var i in data.data) {
                    data = data.data[i];
                    break;
                }
                data.tLoading = stringToMinutes(data.tLoading);
            }
        });

        routePointsEditor.on('postSubmit', function (e, json, data, action) {
            if (action === 'create' || action === 'edit') {
                //TODO use minutes toString
                //console.log(data);
                //console.log(json);
                //console.log(action);
            }
        });

        var $routePointsDataTable = $("#routePointsTable").DataTable({
                "dom": 'Bt', // show only buttons and table with no decorations
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
                    },
                    {
                        extend: "edit",
                        editor: routePointsEditor,
                        text: "изменить"
                    }
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
                    type: 'PUT',
                    url: 'content/getData.php'
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
                //{ label: 'Время в пути',  name: 'timeForDistance', type: 'mask', mask:"00ч.00м.", maskOptions: {clearIfNotMatch: true}, placeholder:"__ч.__м."}
            ]
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
            console.log(houres);
            var minutes = string.substr(3, 2);
            console.log(minutes);
            return houres * 60 + parseInt(minutes);
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
            //return strHoures + "ч." + strMinutes + "м.";
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

        // initial loading of all data
        function onRouteChanged(value) {
            $.post(
                "content/getData.php",
                {status: "getAllRoutePointsDataForRouteID", routeID: value, format: "json"},
                function (data) {

                    //// example result data
                    //data = {
                    //    daysOfWeek:["monday", "wednesday", "friday"],
                    //    firstPointArrivalTime: "18:00",
                    //    routePoints: [
                    //        {routePointID: 10, sortOrder:0, pointName:"point1", tLoading: 180},
                    //        {routePointID: 11, sortOrder:1, pointName:"point2", tLoading: 90},
                    //        {routePointID: 12, sortOrder:2, pointName:"point3", tLoading: 110}
                    //    ],
                    //    relationsBetweenRoutePoints: [
                    //        {relationID:"10_11", pointNameFirst:"point1", pointNameSecond:"point2", distance: 300, timeForDistance: 450},
                    //        {relationID:"11_12", pointNameFirst:"point2", pointNameSecond:"point3", distance: 500, timeForDistance: 780}
                    //    ]
                    //};
                    //data = JSON.stringify(data);

                    data = JSON.parse(data);

                    setDaysOfWeekData(data.daysOfWeek);

                    setFirstPointArrivalTime(data.firstPointArrivalTime);

                    // set routePoints table data
                    $routePointsDataTable.rows().remove();
                    data.routePoints.forEach(function (entry) {
                        entry.tLoading = minutesToString(entry.tLoading);
                    });
                    $routePointsDataTable.rows.add(data.routePoints).draw(false);

                    // set relationsBetweenRoutePoints table data
                    $relationsBetweenRoutePointsDataTable.rows().remove();
                    data.relationsBetweenRoutePoints.forEach(function (entry) {
                        entry.timeForDistance = minutesToString(entry.timeForDistance);
                    });
                    $relationsBetweenRoutePointsDataTable.rows.add(data.relationsBetweenRoutePoints).draw(false);

                }
            );
        }
    }

    // $usersDataTable and usersEditor
    {
        var usersEditor = new $.fn.dataTable.Editor( {
            ajax: {
                create: {
                    type: 'POST',
                    url: 'content/getData.php'
                },
                edit: {
                    type: 'PUT',
                    url: 'content/getData.php'
                },
                remove: {
                    type: 'DELETE',
                    url: 'content/getData.php'
                }
            },
            table: '#usersTable',
            idSrc: 'userID',

            fields: [
                { label: 'Имя', name: 'firstName', type: 'text'},
                { label: 'Фамилия',  name: 'lastName', type: 'text'},
                { label: 'Отчество',  name: 'patronymic', type: 'text'},
                { label: 'Должность',  name: 'position', type: 'text'},
                { label: 'Номер телефона',  name: 'phoneNumber', type: 'mask', mask:"(000) 000-00-00", maskOptions: {clearIfNotMatch: true}, placeholder:"(999) 999-99-99"},
                { label: 'Почта',  name: 'email', type: 'text'},
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
                }
            ]
        } );

        // TODO add same type of code to other selectize fields for editors
        // set current selected value to pointName and userRoleRusName
        usersEditor.on('open', function (e , mode, action) {
            if (action === "edit") {
                var selectedRowData = $usersDataTable.row().data();
                var usersPointNameSelectize = usersEditor.field('pointName').inst();
                usersPointNameSelectize.setValue(usersPointNameSelectize.search(selectedRowData.pointName).items[0].id, true);
                var usersRoleSelectize = usersEditor.field('userRoleRusName').inst();
                usersRoleSelectize.setValue(usersPointNameSelectize.search(selectedRowData.userRoleRusName).items[0].id, true);
            }
        });

        // transform password to md5
        usersEditor.on('preSubmit', function (e, data, action) {
            if (action === 'create' || action === 'edit') {
                data.data[0].password = calcMD5(data.data[0].password);
            }
        });

        // example data for exchange with server
        var exampleData = [{userID: 1, firstName:"wefwfe", lastName:"ewrkbfif", position: "efewerfw", patronymic:"ergerge", phoneNumber: "9055487552",
            email: "qwe@qwe.ru", password:"lewrhbwueu23232", userRoleRusName:"Диспетчер", pointName:"point1"}];

        var $usersDataTable =  $("#usersTable").DataTable({
                ajax: 'content/getData.php',
                dom: 'Bfrtip',
                language: {
                    url:'/localization/dataTablesRus.json'
                },
                data: exampleData,
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
                    {"name": "firstName", "data": "firstName", "targets": 0},
                    {"name": "lastName", "data": "lastName", "targets": 1},
                    {"name": "patronymic", "data": "patronymic", "targets": 2},
                    {"name": "position", "data": "position", "targets": 3},
                    {"name": "phoneNumber", "data": "phoneNumber", "targets": 4},
                    {"name": "email", "data": "email", "targets": 5},
                    {"name": "password", "data": "password", "targets": 6, visible:false},
                    {"name": "userRoleRusName", "data": "userRoleRusName", "targets": 7},
                    {"name": "pointName", "data": "pointName", "targets": 8}
                ]
            }
        );
    }

});