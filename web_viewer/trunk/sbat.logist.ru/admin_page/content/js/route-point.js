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
        );1
        // TODO load distances between points
    }

    // $( "#tabs" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
    // $( "#tabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );

    // $("#daysOfWeekSelect").buttonset();
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
    $("#updateRouteType").button().click(function (e) {
        $("#ajaxLoaderGif").show();
        $.post(
            'content/getData.php',
            {
                status: 'updateRouteType',
                routeID: $routeSelectSelectize[0].selectize.items[0],
                type: $('#routeTypeSelect').val()
            },
            function (serverData) {
                // serverData = JSON.parse(serverData);
                setRouteType(serverData);
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

        routePointsEditor.field('pointName').input().on('keyup', function (e, d) {
            var pointNamePart = $(this).val();
            $.post( "content/getData.php",
                {status: "getPointsByName", format: "json", name: pointNamePart},
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

                    var selectize2 = routePointsEditor.field('pointName').inst();
                    selectize2.clear();
                    selectize2.clearOptions();
                    selectize2.load(function (callback) {
                        callback(selectizePointsOptions);
                    });
                }
            );
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
        
        function setRouteType(typeValue) {
           $("#routeTypeSelect").val(typeValue);

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
                    setRouteType(data.type);
                }
            );
        }

        function setSelectizeValueFromTable(dataTable, editor, selectizeFieldName, dataTableDataName) {
            var selectedRowData = dataTable.row().data();
            var selectizeInstance = editor.field(selectizeFieldName).inst();
            selectizeInstance.setValue(selectizeInstance.search(selectedRowData[dataTableDataName]).items[0].id, true);
        }
    }

    // TODO create distances between points dataTable and editor
});