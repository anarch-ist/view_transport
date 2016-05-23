$(document).ready(function(){
    "use strict";

    var $docAndDateSelector = $("#docAndDateSelector");
    var docDateSelector = $docAndDateSelector.docAndDateSelector({
        useWarehouseRole:true,
        data: $docAndDateSelector.data("component_data")
    });

    //docDateSelector.setSelectedDate(new Date(2015, 10, 30));
    //docDateSelector.setSelectedWarehouseAndDoc(1, 3);

    docDateSelector.setOnSelected(function(event, date, warehouseId, docId) {
        $.ajax({
            url: "getTableData",
            method: "POST",
            data: {date: date, warehouseId: warehouseId, docId: docId},
            dataType: "json"
        }).done(function (tableData) {
        }).fail(function () {
            window.alert("error");
        });
    });
});
