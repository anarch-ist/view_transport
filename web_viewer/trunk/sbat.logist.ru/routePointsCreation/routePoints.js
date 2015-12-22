$(document).ready(function() {

    $("#routeSelect").selectmenu();

    // create table
    var $invoiceHistoryDialogTable = $("#routePointsTable");
    var $invoiceHistoryDialogTable = $invoiceHistoryDialogTable.DataTable({
            "dom": 'Bt', // show only table with no decorations
            "buttons": [
                {
                    text: 'добавить запись',
                    action: function (e, dt, node, config) {

                    }
                },
                {
                    extend: 'selectedSingle',
                    text: 'удалить запись',
                    action: function (e, dt, node, config) {
                        alert("delete");
                    }
                },
                {
                    text: 'сохранить',
                    action: function (e, dt, node, config) {
                        alert("save");
                    }
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

    var editor = new $.fn.dataTable.Editor( {
        ajax:  '/api/staff',
        table: '#routePointsTable',
        fields: [
            { label: 'First name', name: 'first_name' },
            { label: 'Last name',  name: 'last_name'  },
            // etc
        ]
    } );

});