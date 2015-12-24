$(document).ready(function() {
    // custom plugin
    // https://editor.datatables.net/examples/plug-ins/fieldPlugin.html
    // plugin for autocomplete fields, add autoComplete field type to Editor
    // https://editor.datatables.net/plug-ins/field-type/editor.autoComplete
    // list of all field types: https://editor.datatables.net/reference/field/
    <!-- EXAMPLE DATA-->
    // data must have ID column
    var exampleData = [
        {"routePointID":101, "sortOrder":0, "pointName":"point1", "tLoading": 100, "timeToNextPoint": 200, "distanceToNextPoint": 350},
        {"routePointID":102, "sortOrder":1, "pointName":"point2", "tLoading": 50, "timeToNextPoint": 130, "distanceToNextPoint": 340}
    ];

    $("#routeSelect").selectmenu({
        change: function( event, ui ) {
            alert("routeID = " + ui.item.value);
            // do ajax request to route_points table and fill $invoiceHistoryDialogTable
            //TODO
            //$.getJSON(
            //    "../content/getData.php",
            //    {status: "getAllFromRoutePointsTable", format:"json"},
            //    //{"routePointID":101, "sortOrder":0, "pointName":"point1", "tLoading": 100, "timeToNextPoint": 200, "distanceToNextPoint": 350},
            //    //{"routePointID":102, "sortOrder":1, "pointName":"point2", "tLoading": 50, "timeToNextPoint": 130, "distanceToNextPoint": 340}
            //    function(data) {
            //
            //        $invoiceHistoryDialogTable
            //        var options = [];
            //        data.forEach(function(entry) {
            //            // create <option> from this object
            //            var option = "<option value=" + entry.routeID+">" + entry.directionName + "</option>";
            //            options.push(option);
            //        });
            //        $("#routeSelect").html(options.join(""));
            //    }
            //);

        }
    });

    // when page is loading make request and get all points
    $.getJSON(
        "../content/getData.php",
        {status: "getAllRouteIdDirectionPairs", format:"json"},
        // server returns array of pairs [{routeID:1, directionName:"SomeDir1"}, {routeID:2, directionName:"SomeDir2"}]
        function(data) {
            var options = [];
            data.forEach(function(entry) {
                // create <option> from this object
                var option = "<option value=" + entry.routeID+">" + entry.directionName + "</option>";
                options.push(option);
            });
            $("#routeSelect").html(options.join(""));
        }
    );

    // create table
    var $invoiceHistoryDialogTable = $("#routePointsTable");

    var editor = new $.fn.dataTable.Editor( {
        ajax:  '../content/getData.php',
        table: '#routePointsTable',
        idSrc: 'routePointID',
        fields: [
            { label: 'Порядковый номер', name: 'sortOrder' },
            { label: 'Пункт',  name: 'pointName'  },
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

    var $invoiceHistoryDialogTable = $invoiceHistoryDialogTable.DataTable({
            "dom": 'Bt', // show only table with no decorations
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