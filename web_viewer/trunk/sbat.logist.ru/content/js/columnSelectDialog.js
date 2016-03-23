$(document).ready(function () {

    var html =
        '<div id="columnSelectDialogContainer" title="Выбор столбцов">' +
        '<div id="inputsContainer" title="Выбор столбцов"></div>' +
        '</div>';
    $("body").append(html);

    var $columnSelectDialogContainer = $("#columnSelectDialogContainer");
    var $inputsContainer = $("#inputsContainer");

    $.showColumnSelectDialog = function (dataTable) {

        $inputsContainer.html("");


        dataTable.columns().every(function() {
            var column = this;
            var columnIndex = column.index();
            var columnVisibility = column.visible();
            var columnRusName = $(column.header()).attr("aria-label").split(":")[0];

            var input = $("<input>").attr("type", "checkbox").attr("id", columnIndex).prop("checked", columnVisibility);
            input.change(function() {
                column.visible(this.checked);
            });
            var label = $("<label>").attr("for", columnIndex).html(columnRusName);
            $inputsContainer.append(input, label, "<br>");

        });

        $columnSelectDialogContainer.dialog("open");

    };

    $columnSelectDialogContainer.dialog({
        autoOpen: false,
        resizable: false,
        height: 710,
        width: 500,
        modal: true
    });

});