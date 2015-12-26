//Client-to-server
/*
Client-to-server
action: create, edit, remove
data: An object containing the row ids to act upon and edited data for those rows.
preSubmit: hook to add additional parameters before send to the server
postSubmit: hook to manupulate submitted data before draw
*/




$(document).ready(function() {
    // custom plugin
    // https://editor.datatables.net/examples/plug-ins/fieldPlugin.html
    // plugin for autocomplete fields, add autoComplete field type to Editor
    // https://editor.datatables.net/plug-ins/field-type/editor.selectize
    // list of all field types: https://editor.datatables.net/reference/field/
    <!-- EXAMPLE DATA-->
    // data must have ID column
    var exampleData = [
        {"routePointID":101, "sortOrder":0, "pointName":"пункт1", "tLoading": "02ч.30м.", "timeToNextPoint": "10ч.00м.", "distanceToNextPoint": 350},
        {"routePointID":102, "sortOrder":1, "pointName":"пункт2", "tLoading": "01ч.20м.", "timeToNextPoint": "12ч.30м.", "distanceToNextPoint": 340}
    ];
    // create
    $("#routeSelect").selectize({
        diacritics: true,
        maxOptions: 10000,
        maxItems: 1,
        dropdownParent: null, // body or null
        selectOnTab: true,
        onChange: function(value) {
            $.getJSON(
                "../content/getData.php",
                {status: "getAllRoutePointsDataForRouteID", routeID: value, format:"json"},
                function(data) {
                    $routePointsDataTable.rows().remove();
                    $routePointsDataTable.rows.add(data).draw(false);
                }
            );
        }
    });

    // when page is loading make request and get all points
    $.getJSON(
        "../content/getData.php",
        {status: "getAllPointIdPointNamePairs", format:"json"},
        // server returns array of pairs [{pointID:1, pointName:"somePoint1"}, {pointID:2, pointName:"somePoint2"}]
        function(data) {
            var options = [];
            data.forEach(function(entry) {
                var option = "<option value=" + entry.pointID+">" + entry.pointName + "</option>";
                options.push(option);
            });
            //$("#pointSelect").html(options.join(""));
        }
    );
    // when page is loading make request and get all routes
    $.getJSON(
        "../content/getData.php",
        {status: "getAllRouteIdDirectionPairs", format:"json"},
        // server returns array of pairs [{routeID:1, directionName:"SomeDir1"}, {routeID:2, directionName:"SomeDir2"}]
        function(data) {
            var options = [];
            data.forEach(function(entry) {
                var option = "<option value=" + entry.routeID+">" + entry.directionName + "</option>";
                options.push(option);
            });
            $("#routeSelect").html(options.join(""));
        }
    );

    // create table
    //var $routePointsTable = $("#routePointsTable");

    var editor = new $.fn.dataTable.Editor( {
        ajax: {
            create: {
                type: 'POST',
                url: '../content/getData.php'
            },
            edit: {
                type: 'PUT',
                url: '../content/getData.php'
            },
            remove: {
                type: 'DELETE',
                url: '../content/getData.php'
            }
        },
        table: '#routePointsTable',
        idSrc: 'routePointID',

        fields: [
            { label: 'Порядковый номер', name: 'sortOrder', type: 'mask', mask: "0", placeholder: "0-9" },
            { label: 'Пункт',  name: 'pointName', type: 'selectize',

                options: [
                    { "label": "пункт1", "value": "1" },
                    { "label": "пункт2", "value": "2" },
                    { "label": "пункт3", "value": "3" },
                    { "label": "пункт4", "value": "4" },
                    { "label": "пункт5", "value": "5" }
                ],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: null
                }
            },
            { label: 'Время разгрузки',  name: 'tLoading', type: 'mask', mask:"00ч.00м.", maskOptions: {clearIfNotMatch: true}, placeholder:"__ч.__м."},
            { label: 'Время до следующего пункта',  name: 'timeToNextPoint', type: 'mask', mask:"00ч.00м.", maskOptions: {clearIfNotMatch: true}, placeholder:"__ч.__м."},
            { label: 'Расстояние до следующего пункта',  name: 'distanceToNextPoint', type: 'mask', mask:"9999", placeholder:"____км."}
            // etc
        ],
        i18n: {
            create: {
                button: "Новая",
                title:  "Добавить новую запись",
                submit: "Добавить"
            },
            edit: {
                button: "Изменить",
                title:  "Изменить запись",
                submit: "Обновить"
            },
            remove: {
                button: "Удалить",
                title:  "Удалить",
                submit: "Удалить",
                confirm: {
                    _: "Вы уверены, что хотите удалить %d записей?",
                    1: "Вы уверены, что хотите удалить запись?"
                }
            },
            error: {
                system: "Возникла системная ошибка"
            },
            multi: {
                title: "Множество значений",
                info: "The selected items contain different values for this input. To edit and set all items for this input to the same value, click or tap here, otherwise they will retain their individual values.",
                restore: "Обратить изменения"
            },
            datetime: {
                previous: 'Предыдущая',
                next:     'Следующая',
                months:   [ 'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь' ],
                weekdays: [ 'Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб' ],
                amPm:     [ 'am', 'pm' ],
                unknown:  '-'
            }
        }
    } );

    //$(editor.field('pointName')).on("");
    // transfrom string like 18ч.30м. to 18*60+30
    editor.on( 'preSubmit', function (e, data, action) {
        if (action === 'create' || action === 'edit') {
            data.data[0].tLoading = stringToMinutes(data.data[0].tLoading);
            data.data[0].timeToNextPoint = stringToMinutes(data.data[0].timeToNextPoint);
        }
    } );

    editor.on( 'postSubmit', function (e, json, data, action) {
        if (action === 'create' || action === 'edit') {
        //TODO use minutes toString
            //console.log(data);
            //console.log(json);
            //console.log(action);
        }
    } );

    function stringToMinutes(string) {
        var houres = string.substring(0, 2);
        var minutes = string.substring(2, 4);
        return houres * 60 + minutes * 1;
    }

    function minutesToString(intMinutes) {
        var minutes = intMinutes % 60;
        var strMinutes = "";
        if (minutes <= 9) strMinutes = "0" + minutes;
        else strMinutes = minutes + "";

        var houres = Math.floor(intMinutes / 60);
        var strHoures = "";
        if (houres <= 9) strHoures = "0" + minutes;
        else strHoures = houres + "";

        return strHoures + "ч." + strMinutes + "м.";
    }

    var $routePointsDataTable =  $("#routePointsTable").DataTable({
            "dom": 'Bt', // show only buttons and table with no decorations
            "data": exampleData,
            select: {
                style: 'single'
            },
            "buttons": [
                {
                    extend: "create",
                    editor: editor,
                    text: 'добавить запись'
                },
                {
                    extend: "remove",
                    editor: editor,
                    text: 'удалить запись'
                },
                {
                    extend: "edit",
                    editor: editor,
                    text: "изменить"
                }
            ],
            "paging": false, // no pagination
            "columnDefs": [
                {"name": "sortOrder", "data": "sortOrder", "targets": 0},
                {"name": "pointName", "data": "pointName", "targets": 1},
                {"name": "tLoading", "data": "tLoading", "targets": 2},
                {"name": "timeToNextPoint", "data": "timeToNextPoint", "targets": 3},
                {"name": "distanceToNextPoint", "data": "distanceToNextPoint", "targets": 4}
            ]
        }
    );
});