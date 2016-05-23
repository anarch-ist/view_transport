$(document).ready(function(){
    "use strict";

    var $docAndDateSelector = $("#docAndDateSelector");
    var docDateSelector = $docAndDateSelector.docAndDateSelector({
        useWarehouseRole:false,
        data: $docAndDateSelector.data("component_data")
    });

    //docDateSelector.setSelectedDate(new Date(2015, 10, 30));
    //docDateSelector.setSelectedDoc(3);

    docDateSelector.setOnSelected(function(event, date, warehouseId, docId) {
        window.console.log("SELECTED EVENT");
        window.console.log(date);
        window.console.log(warehouseId);
        window.console.log(docId);
    });
});
