(function () {
    "use strict";

    var DEFAULT_PARAMETERS = {
        parentId: 'overviewTableContainer',
        cellSize: 30
        //windowSize: 60 * 24
        //Maybe we don't need this just yet.
        //Table/cell sizes are calculated in CSS with percents, in case you're wondering
    };
    // CONSTANTS
    var CLOSED = "CLOSED",
        OCCUPIED = "OCCUPIED",
        OPENED = "OPENED";

    var overviewTableContainer;
    var startupParams;

    var main = function (initParams) {
        startupParams = merge(DEFAULT_PARAMETERS, initParams);
        overviewTableContainer = document.getElementById(startupParams.parentId);
        return main;
    };

    main.pushTable = function(data){
        var cellStatuses = new Array(Number(startupParams.cellSize)); //In case if Array initializer takes cellSize not as number
        data.docPeriods.forEach(function(dataElem){
            for (var i = Number(dataElem.periodBegin/startupParams.cellSize); i < Number(dataElem.periodEnd/startupParams.cellSize); i++){
                if (dataElem.state===OCCUPIED){
                    if (dataElem.occupiedStatus==="IN_PROCESS"){
                        cellStatuses[i] = "tp_in_process";
                    } else if (dataElem.occupiedStatus==="ERROR"){
                        cellStatuses[i] = "tp_error";
                    } else if (dataElem.occupiedStatus==="DELIVERED"){
                        cellStatuses[i] = "tp_delivered";
                    } else if (dataElem.occupiedStatus==="ARRIVED"){
                        cellStatuses[i] = "tp_arrived";
                    }
                } else if (dataElem.state === CLOSED){
                    cellStatuses[i] = "tp_closed";
                }
            }
        });

        var serialNumber = 0;
        var tableElement = overviewTableContainer.appendChild(document.createElement("table"));
        tableElement.classList.add("overview-table");
        var caption = document.createElement("caption");
        tableElement.appendChild(caption);
        caption.classList.add("overview-table-caption");
        caption.innerHTML = data.docName;

        for (var i = 1; i <= 8; i++) {
            var rowElement = document.createElement("tr");
            tableElement.appendChild(rowElement);

            for (var j = 1; j <= Number((1440/startupParams.cellSize)/8); j++) {


                var cellElement = document.createElement("td");
                if(typeof cellStatuses[serialNumber] === 'undefined') {
                    cellElement.classList.add("available");
                }
                else {
                    cellElement.classList.add(cellStatuses[serialNumber]);
                }


                cellElement.setAttribute("data-serialnumber", serialNumber);
                rowElement.appendChild(cellElement);
                serialNumber++;
            }
        }
    };

    function merge() {
        var obj = {},
            i = 0,
            il = arguments.length,
            key;
        for (; i < il; i++) {
            for (key in arguments[i]) {
                if (arguments[i].hasOwnProperty(key)) {
                    obj[key] = arguments[i][key];
                }
            }
        }

        return obj;
    }

    function clearAll(){
        //it's slightly faster than innerHTML=''

        while(overviewTableContainer.firstChild) {
            overviewTableContainer.removeChild(overviewTableContainer.firstChild);
        }
    }
    main.clearAll = clearAll;
    window.tableOverviewPlugin = main;
})();