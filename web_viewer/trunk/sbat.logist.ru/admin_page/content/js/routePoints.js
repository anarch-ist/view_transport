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
                $("#routeSelect").html(options.join("")).selectize(
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

    //$("#daysOfWeekSelect").buttonset();
    //$("#startRouteTimeInput").mask('00:00', {clearIfNotMatch: true, placeholder: "чч:мм"});

    <!-- EXAMPLE DATA-->
    // data must have ID column
    //var exampleData = [
    //    {"routePointID":101, "sortOrder":0, "pointName":"пункт1", "tLoading": "02ч.30м.", "timeToNextPoint": "10ч.00м.", "distanceToNextPoint": 350},
    //    {"routePointID":102, "sortOrder":1, "pointName":"пункт2", "tLoading": "01ч.20м.", "timeToNextPoint": "12ч.30м.", "distanceToNextPoint": 340}
    //];

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

    // create editor for days of week and start route time
    {
        var fieldsEditor = new $.fn.dataTable.Editor({
            ajax: {
                edit: {
                    type: 'PUT',
                    url: 'content/getData.php'
                }
            },

            fields: [{
                label: "daysOfWeekSelect:",
                name: "daysOfWeekSelect",
                type: 'selectize',
                options: [
                    {label: 'ПН', value: 'monday'},
                    {label: 'ВТ', value: 'tuesday'},
                    {label: 'СР', value: 'wednesday'},
                    {label: 'ЧТ', value: 'thursday'},
                    {label: 'ПТ', value: 'friday'},
                    {label: 'СБ', value: 'saturday'},
                    {label: 'ВС', value: 'sunday'}
                ],
                opts: {
                    labelField: 'label',
                    maxItems:7,
                    dropdownParent: null
                }
            }, {
                label: "startRouteTimeInput:",
                name: "startRouteTimeInput"
                //type: mask
            }
            ]
        });

        //fieldsEditor.inline( $('[data-editor-field]'), {
        //
        //} );


        // inline, submit

        $('[data-editor-field]').on( 'click', function (e) {
            fieldsEditor.inline( this, {
                buttons: '_basic'
                //onBlur: 'none'
            } );
        } );
    }


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
                { label: 'Порядковый номер', name: 'sortOrder', type: 'readonly'},
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
                { label: 'Расстояние между пунктами',  name: 'distance', type: 'readonly'},
                { label: 'Время разгрузки',  name: 'timeForDistance', type: 'mask', mask:"00ч.00м.", maskOptions: {clearIfNotMatch: true}, placeholder:"__ч.__м."}
                // etc
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
            var minutes = string.substr(4, 2);
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

        function onRouteChanged(value) {
            $.post(
                "content/getData.php",
                {status: "getAllRoutePointsDataForRouteID", routeID: value, format:"json"},
                function(data) {
                    // example result data
                    data = {
                        directionName:"SomeDir1",
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
                    $("[data-editor-field*='daysOfWeekSelect']").text(data.daysOfWeek.join(" "));
                    $("[data-editor-field*='startRouteTimeInput']").text(data.firstPointArrivalTime);

                    $routePointsDataTable.rows().remove();
                    $routePointsDataTable.rows.add(data.routePoints).draw(false);

                    $relationsBetweenRoutePointsDataTable.rows().remove();
                    $relationsBetweenRoutePointsDataTable.rows.add(data.relationsBetweenRoutePoints).draw(false);

                    //for(var i in data) {
                    //    data[i].tLoading = minutesToString(data[i].tLoading);
                    //    data[i].timeToNextPoint = minutesToString(data[i].timeToNextPoint);
                    //}
                }
            );
        }
    }
});