(function() {
    "use strict";
    var DEFAULT_PARAMETERS = {
        parentId: '',
        periodSize: 30, // in minutes!!!
        windowSize: 60 * 24,
        selectionConstraint: null
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
        var periodsCount = startupParameters.windowSize / startupParameters.periodSize;

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
    };


    main.defaultParameters = DEFAULT_PARAMETERS;

    var tableElement;
    var selectedElements = [];
    var serialNumber = 0;

    main.generate = function(){
        var tableSize = main.findTableSizes(startupParameters.windowSize / startupParameters.periodSize);
        var x = tableSize.x;
        var y = tableSize.y;
        var firstPart = "00:00";
        var periodSize= startupParameters.periodSize;
        var secondPart = "";

        //tableTag
        var elem = document.getElementById(startupParameters.parentId);
        tableElement = document.createElement("table");
        tableElement.classList.add("mainTable");

        elem.appendChild(tableElement);

        // rows and cells
        for(var i = 1; i <= y; i++) {
            var rowElement = document.createElement("tr");
            tableElement.appendChild(rowElement);

            for(var j = 1; j <= x; j++) {
                serialNumber++;
                var cellElement = document.createElement("td");
                cellElement.classList.add("tableCell");
                cellElement.id = "cell_" + i + "_" + j;
                cellElement.setAttribute("data-x", i);
                cellElement.setAttribute("data-y", j);
                cellElement.setAttribute("data-serialnumber", serialNumber);

                cellElement.onclick = function(e) {

                    var _this = this;
                    if (startupParameters.selectionConstraint) {
                        if (startupParameters.selectionConstraint(
                                +_this.dataset.serialnumber,
                                selectedElementsAsSerialNumbers(selectedElements),
                                _this.classList.contains("highlight"))) {
                            return;
                        }
                    }

                    var wasSelected = _this.classList.contains("highlight");
                    if (!wasSelected) {
                        selectedElements.push(_this);
                    } else {
                        var index = selectedElements.indexOf(_this);
                        selectedElements.splice(index, 1);
                    }
                    selectedElements.sort(compareNum);

                    var newEvent = new CustomEvent("selected", {
                        detail: {
                           x: _this.dataset.x,
                           y: _this.dataset.y,
                           isSelected: wasSelected
                        },
                        bubbles: true,
                        cancelable: false
                    });
                    tableElement.dispatchEvent(newEvent);
                    _this.classList.toggle("highlight");
                };

                var labelElement = document.createElement("label");
                labelElement.classList.add("timePeriodLabel");

                var time = firstPart.split(':');
                var newTime = +time[0] * 60 + (+time[1]) + periodSize;
                var hours = Math.floor(newTime / 60);
                var minutes = newTime - (hours * 60);
                if(hours < 10) {
                    hours = "0" + hours;
                }
                if(minutes < 10) {
                    minutes = "0" + minutes;
                }
                if(hours === 24){
                    hours = "00";
                }

                secondPart = hours + ":" + minutes;
                labelElement.innerHTML = firstPart + "-" + secondPart;
                firstPart = secondPart;

                var divElement = document.createElement("div");
                divElement.classList.add("companyDiv");
                cellElement.appendChild(labelElement);
                cellElement.appendChild(divElement);
                rowElement.appendChild(cellElement);
            }
        }
    };


    // ----------------------------------METHODS------------------------------------------
    main.findById = function(x, y) {
        var result = {};
        var tdElement = document.getElementById("cell_" + x + "_" + y);
        var labelElement = tdElement.children[0];
        var divElement = tdElement.children[1];
        result.tdElem = tdElement;
        result.labelElem = labelElement;
        result.divElem = divElement;
        return result;
    };

    main.setString = function(str, x, y) {
        var divElement = main.findById(x, y).divElem;
        divElement.innerHTML = str;
    };

    main.setOnClicked = function(handler) {
        tableElement.addEventListener("selected", handler);
    };

    main.setNotFreeState = function (timeStampStart, timeStampEnd, state, company) {
        var periodNumberStart = definedStartTimeSerialNumberByMinutes(timeStampStart);
        var periodNumberEnd = definedEndTimeSerialNumberByMinutes(timeStampEnd);
        for(var i = periodNumberStart; i <= periodNumberEnd; i++){
            findCellBySerialNumber(i, state, company);
        }
    };

    // TODO implement this method
//    main.setDisabled = function(disabled) {
//        // tableElement.disabled = disabled;
//    };


    // ----------------------------------HELPER FUNCTIONS------------------------------------------
    function selectedElementsAsSerialNumbers() {
        var result = [];
        for (var i = 0; i < selectedElements.length; i++) {
            result.push(+selectedElements[i].dataset.serialnumber);
        }
        return result;
    }

    function compareNum(a, b) {
        return a.dataset.serialnumber - b.dataset.serialnumber;
    }

    function definedStartTimeSerialNumberByMinutes(timeStamp) {
        var periodSize = startupParameters.periodSize;
        var result;
        if(timeStamp === 0) return 1;
        result = timeStamp / periodSize;
        result++;
        return result;
    }

    function definedEndTimeSerialNumberByMinutes(timeStamp) {
        var periodSize = startupParameters.periodSize;
        var result;
        if(timeStamp === 0) return (24 * 60 / periodSize);
        result = (timeStamp / periodSize);
        return result;
    }

    function findCellBySerialNumber(number, state, company) {

        var rows = tableElement.rows;
        var row;
        var cells;
        for(var i = 0; i < rows.length; i++){
            row = rows[i];
            cells = row.cells;
            for(var j = 0; j < cells.length; j++){
                if(number == cells[j].dataset.serialnumber){
                    cells[j].lastElementChild.classList.add(state);
                    if(company){
                        cells[j].lastElementChild.innerHTML = company;
                    }
                }
            }
        }
    }

    main.sendDocPeriods = function() {
        var result = [];

        var previousState;
        var previousSerialNumber;
        var sequentialStates = [];
        for(var i = 0; i < selectedElements.length; i++){
            var cellState = selectedElements[i].lastElementChild.getAttribute('class');
            var cellSerialNumber = selectedElements[i].dataset.serialnumber;
            if (cellState === previousState && (cellSerialNumber - 1) === previousSerialNumber) {
                sequentialStates.push(i);
            } else {

            }


            previousSerialNumber = cellSerialNumber;
            previousState = cellState;
        }

        var formElem = document.createElement("form");
        formElem.setAttribute('Action', 'submit');

        // send result as content????
        //formElem.setAttribute('Content', result);
        var inputElem = document.createElement("input");
        inputElem.setAttribute('Type', 'button');
        inputElem.setAttribute('Value', 'Отправить');
        formElem.appendChild(inputElem);
        document.body.appendChild(formElem);
    }


    window.tablePlugin = main;

})();
