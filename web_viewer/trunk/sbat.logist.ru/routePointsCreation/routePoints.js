$(document).ready(function() {
    // custom plugin
    // https://editor.datatables.net/examples/plug-ins/fieldPlugin.html
    // plugin for autocomplete fields, add autoComplete field type to Editor
    // https://editor.datatables.net/plug-ins/field-type/editor.selectize
    // list of all field types: https://editor.datatables.net/reference/field/
    <!-- EXAMPLE DATA-->
    // data must have ID column
    var exampleData = [
        {"routePointID":101, "sortOrder":0, "pointName":"point1", "tLoading": 100, "timeToNextPoint": 200, "distanceToNextPoint": 350},
        {"routePointID":102, "sortOrder":1, "pointName":"point2", "tLoading": 50, "timeToNextPoint": 130, "distanceToNextPoint": 340}
    ];

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
            { label: 'Порядковый номер', name: 'sortOrder' },
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
            { label: 'Время разгрузки',  name: 'tLoading'  },
            { label: 'Время следующего пункта',  name: 'timeToNextPoint'  },
            { label: 'Расстояние до следующего пункта',  name: 'distanceToNextPoint'  }
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
    //editor.field('pointName')

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