//Client-to-server
/*
Client-to-server
action: create, edit, remove
data: An object containing the row ids to act upon and edited data for those rows.
preSubmit: hook to add additional parameters before send to the server
postSubmit: hook to manupulate submitted data before draw
*/

$(document).ready(function() {

    // load all required data
    {
        // when page is loading make request and get all points. data is loading into
        $.post(
            "content/getData.php",
            {status: "getAllPointIdPointNamePairs", format:"json"},
            // server returns array of pairs [{pointID:1, pointName:"somePoint1"}, {pointID:2, pointName:"somePoint2"}]
            function(data) {
                var options = [];
                var selectizeOptions = [];
                data = JSON.parse(data);
                data.forEach(function(entry) {
                    var option = "<option value=" + entry.pointID+">" + entry.pointName + "</option>";
                    options.push(option);
                    var selectizeOption = { "label": entry.pointName, "value": entry.pointID };
                    selectizeOptions.push(selectizeOption);
                });

                var allOptions = options.join("");
                var selectize = routePointsEditor.field('pointName').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function(callback) {
                    callback(selectizeOptions);
                });

                $("#pointSelect").html(allOptions);
                //editor.field('pointName').update(options);
            }
        );
        var $routeSelectSelectize;
        // when page is loading make request and get all routes
        $.post(
            "content/getData.php",
            {status: "getAllRouteIdDirectionPairs", format:"json"},
            // server returns array of pairs [{routeID:1, directionName:"SomeDir1"}, {routeID:2, directionName:"SomeDir2"}]
            function(data) {
                var options = [];
                data = JSON.parse(data);
                data.forEach(function(entry) {
                    var option = "<option value=" + entry.routeID+">" + entry.directionName + "</option>";
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
                firstPointArrivalTime:$('#startRouteTimeInput').val()
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
        var routePointsEditor = new $.fn.dataTable.Editor( {
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
            table: '#routePointsTable',
            idSrc: 'routePointID',

            fields: [
                { label: 'Порядковый номер', name: 'sortOrder', type: 'mask', mask:"0", maskOptions: {clearIfNotMatch: true}, placeholder:"0-9"},
                { label: 'Пункт',  name: 'pointName', type: 'selectize',
                    options: [
                    ],
                    opts: {
                        diacritics: true,
                        searchField: 'label',
                        labelField: 'label',
                        dropdownParent: null
                    }
                },
                { label: 'Продолжительность разгрузочных работ',  name: 'tLoading', type: 'mask', mask:"00ч.00м.", maskOptions: {clearIfNotMatch: true}, placeholder:"__ч.__м."}
            ]
        } );

        // transfrom string like 18ч.30м. to 18*60+30
        routePointsEditor.on( 'preSubmit', function (e, data, action) {
            if (action === 'create' || action === 'edit') {
                console.log(data.data[0].tLoading);
                data.data[0].tLoading = stringToMinutes(data.data[0].tLoading);
            }
        } );

        routePointsEditor.on( 'postSubmit', function (e, json, data, action) {
            if (action === 'create' || action === 'edit') {
                //TODO use minutes toString
                //console.log(data);
                //console.log(json);
                //console.log(action);
            }
        } );

        var $routePointsDataTable =  $("#routePointsTable").DataTable({
                "dom": 'Bt', // show only buttons and table with no decorations
                language: {
                    url:'/localization/dataTablesRus.json'
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
        var relationsBetweenRoutePointsEditor = new $.fn.dataTable.Editor( {
            ajax: {
                edit: {
                    type: 'PUT',
                    url: 'content/getData.php'
                }
            },
            table: '#relationsBetweenRoutePointsTable',
            idSrc: 'relationID',

            fields: [
                { label: 'Начальный пункт', name: 'pointNameFirst', type: 'readonly'},
                { label: 'Конечный пункт',  name: 'pointNameSecond', type: 'readonly'},
                { label: 'Расстояние',  name: 'distance', type: 'readonly'},
                { label: 'Время в пути',  name: 'timeForDistance', type: 'mask', mask:"00ч.00м.", maskOptions: {clearIfNotMatch: true}, placeholder:"__ч.__м."}
            ]
        } );

        var $relationsBetweenRoutePointsDataTable =  $("#relationsBetweenRoutePointsTable").DataTable({
                "dom": 'Bt', // show only buttons and table with no decorations
                language: {
                    url:'/localization/dataTablesRus.json'
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
            var minutes = string.substr(2, 3);
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

            return strHoures + "ч." + strMinutes + "м.";
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
                {status: "getAllRoutePointsDataForRouteID", routeID: value, format:"json"},
                function(data) {

                    // example result data
                    data = {
                        daysOfWeek:["monday", "wednesday", "friday"],
                        firstPointArrivalTime: "18:00",
                        routePoints: [
                            {routePointID: 10, sortOrder:0, pointName:"point1", tLoading: 180},
                            {routePointID: 11, sortOrder:1, pointName:"point2", tLoading: 90},
                            {routePointID: 12, sortOrder:2, pointName:"point3", tLoading: 110}
                        ],
                        relationsBetweenRoutePoints: [
                            {relationID:"10_11", pointNameFirst:"point1", pointNameSecond:"point2", distance: 300, timeForDistance: 450},
                            {relationID:"11_12", pointNameFirst:"point2", pointNameSecond:"point3", distance: 500, timeForDistance: 780}
                        ]
                    };
                    data = JSON.stringify(data);

                    data = JSON.parse(data);

                    setDaysOfWeekData(data.daysOfWeek);

                    setFirstPointArrivalTime(data.firstPointArrivalTime);

                    // set routePoints table data
                    $routePointsDataTable.rows().remove();
                    data.routePoints.forEach(function(entry) {
                       entry.tLoading = minutesToString(entry.tLoading);
                    });
                    $routePointsDataTable.rows.add(data.routePoints).draw(false);

                    // set relationsBetweenRoutePoints table data
                    $relationsBetweenRoutePointsDataTable.rows().remove();
                    data.relationsBetweenRoutePoints.forEach(function(entry) {
                        entry.timeForDistance = minutesToString(entry.timeForDistance);
                    });
                    $relationsBetweenRoutePointsDataTable.rows.add(data.relationsBetweenRoutePoints).draw(false);

                }
            );
        }
    }
});