(function() {
    "use strict";
    var DEFAULT_PARAMETERS = {
        parentId: '',
        periodLength: 30, // in minutes!!!
        windowSize: 60 * 24
    };
    var startupParameters = {};

    // value is an object
    var main = function (initParams) {
        for(var key in DEFAULT_PARAMETERS) {
            if (DEFAULT_PARAMETERS.hasOwnProperty(key)) {
                startupParameters[key] = DEFAULT_PARAMETERS[key];
                if (initParams[key]) {
                    startupParameters[key] = initParams[key];
                }
            }
        }
        return main;
    };

    main.findTableSizes = function() {
        var periodsCount = startupParameters.windowSize / startupParameters.periodLength;

        var result = {
           x: 1,
           y: periodsCount
        };

        for(var i = 2; i <= (periodsCount / 2); i++) {
            if(!(periodsCount % i)) {
                var j = periodsCount / i;
                if(((result.y - result.x) > (j - i)) && ((j - i) >= 0)) {
                    result.x = i;
                    result.y = j;
                }
            }
        }
        return result;
    }


    main.defaultParameters = DEFAULT_PARAMETERS;

    var tableElement;

    main.generate = function(){
        var tableSize = main.findTableSizes(startupParameters.windowSize / startupParameters.periodLength);
        var x = tableSize.x;
        var y = tableSize.y;
        var firstPart = "00:00";
        var periodLength = startupParameters.periodLength;
        var secondPart = "";

        //tableTag
        var elem = document.getElementById(startupParameters.parentId);
        tableElement = document.createElement("table");

        elem.appendChild(tableElement);

        // rows and cells
        for(var i = 1; i <= y; i++) {
            var rowElement = document.createElement("tr");
            tableElement.appendChild(rowElement);

            for(var j = 1; j <= x; j++) {
                var cellElement = document.createElement("td");
                cellElement.id = "cell_" + i + "_" + j;
                cellElement.setAttribute("data-x", i);
                cellElement.setAttribute("data-y", j);
                cellElement.onclick = function(e) {
                    var _this = this;
                    var newEvent = new CustomEvent("selected", {
                            detail: {
                                x: _this.dataset.x,
                                y: _this.dataset.y
                            },
                            bubbles: true,
                            cancelable: false
                        });
                    tableElement.dispatchEvent(newEvent);
                };

                var labelElement = document.createElement("label");

                var time = firstPart.split(':');
                var newTime = +time[0] * 60 + (+time[1]) + periodLength;
                var hours = Math.floor(newTime / 60);
                var minutes = newTime - (hours * 60);
                if(hours < 10) {
                    hours = "0" + hours;
                }
                if(minutes < 10) {
                    minutes = "0" + minutes;
                }
                if(hours == 24){
                    hours = "00";
                }

                secondPart = hours + ":" + minutes;
                labelElement.innerHTML = firstPart + "-" + secondPart;
                firstPart = secondPart;

                var divElement = document.createElement("div");
                cellElement.appendChild(labelElement);
                cellElement.appendChild(divElement);
                rowElement.appendChild(cellElement);
            }
        }
    };

    main.findById = function(x, y) {
        var result = {};
        var tdElement = document.getElementById("cell_" + x + "_" + y);
        var labelElement = tdElement.children[0];
        var divElement = tdElement.children[1];
        result.tdElem = tdElement;
        result.labelElem = labelElement;
        result.divElem = divElement;
        return result;
    }

    main.setString = function(str, x, y) {
        var divElement = main.findById(x, y).divElem;
        divElement.innerHTML = str;
    }

    main.setOnClicked = function(handler) {
        tableElement.addEventListener("selected", handler);
    }

    window.tablePlugin = main;

})();
