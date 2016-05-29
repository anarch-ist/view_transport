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
    var disableElement;
    var selectedElements = [];
    var serialNumber = 0;

    main.generate = function(){
        var tableSize = main.findTableSizes(startupParameters.windowSize / startupParameters.periodSize);
        var x = tableSize.x;
        var y = tableSize.y;
        var firstPart = "00:00";
        var periodSize= startupParameters.periodSize;
        var secondPart = "";

        var parentElem = document.getElementById(startupParameters.parentId);
        //diableElement
        disableElement = document.createElement("div");
        parentElem.appendChild(disableElement);
        disableElement.classList.add("tablePlugin-disable");
        //tableElement
        tableElement = document.createElement("table");
        tableElement.classList.add("mainTable");

        parentElem.appendChild(tableElement);

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
                    // apply selection constraint
                    if (startupParameters.selectionConstraint) {
                        if (startupParameters.selectionConstraint(
                                +_this.dataset.serialnumber,
                                selectedElementsAsSerialNumbers(selectedElements),
                                _this.classList.contains("highlight"))) {
                            return;
                        }
                    }
                    toggleSelection(_this);
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
                divElement.setAttribute("id", "companyDiv");
                cellElement.appendChild(labelElement);
                cellElement.appendChild(divElement);
                rowElement.appendChild(cellElement);
            }
        }

        // generate any selected event
        var anySelected = false;
        main.setOnSelectionChanged(function() {
            if (selectedElements.length === 0) {
                generateAnySelectedEvent(false);
                anySelected = false;
            } else if (selectedElements.length !== 0 && !anySelected) {
                generateAnySelectedEvent(true);
                anySelected = true;
            }
        });
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

    main.setNotFreeState = function (timeStampStart, timeStampEnd, state, company) {
        var periodNumberStart = definedStartTimeSerialNumberByMinutes(timeStampStart);
        var periodNumberEnd = definedEndTimeSerialNumberByMinutes(timeStampEnd);
        for(var i = periodNumberStart; i <= periodNumberEnd; i++){
            findCellBySerialNumber(i, state, company);
        }
    };

    main.isAnySelected = function() {
        return (selectedElements.length !== 0);
    };

    main.setOnSelectionChanged = function(handler) {
        tableElement.addEventListener("selected", handler);
    };



    main.setOnAnySelected = function(handler) {
        tableElement.addEventListener("anySelected", handler);
    };

    main.setOnDisableChanged = function(handler) {
        tableElement.addEventListener("disableChanged", handler);
    };

    // FIXME make it work with different states
    main.getSelectedPeriod = function() {
        if (selectedElements.length === 0) {
            return {};
        }
        var periodBegin = (+selectedElements[0].dataset.serialnumber -1) * startupParameters.periodSize;
        var periodEnd = (+selectedElements[selectedElements.length - 1].dataset.serialnumber) * startupParameters.periodSize;
        return {periodBegin:periodBegin, periodEnd: periodEnd};
    };

    main.clear = function() {
        if (selectedElements.length === 0) {
            generateAnySelectedEvent(false);
        } else {
            var copy = selectedElements.slice();
            copy.forEach(function(cellElement) {
                removeSelectionStyle(cellElement);
                modifySelectedElementsArray(cellElement, false);
                generateSelectedEvent(cellElement, false);
            });
        }

        [].forEach.call(tableElement.getElementsByTagName("td"), function(element) {
            element.children[1].classList.remove('occupied', 'disabled');
            element.children[1].innerHTML = "";
        });
    };

    // TODO implement this method
    /**
     * @return array of periods with identical state
     */
    main.getSelectedPeriods = function() {
    };

    main.setDisabled = function(disabled) {
        if (disabled) {
            disableElement.style.width = getComputedStyle(tableElement).width;
            disableElement.style.height = getComputedStyle(tableElement).height;
            disableElement.style.position = "absolute";
            disableElement.style.zIndex = getComputedStyle(tableElement).zIndex + 1;
        } else {
            disableElement.style.width = "0%";
            disableElement.style.height = "0%";
            //disableElement.style.zIndex = 0;
        }

        var newEvent = new CustomEvent("disableChanged", {
            detail: disabled,
            bubbles: true,
            cancelable: false
        });
        tableElement.dispatchEvent(newEvent);
    };


    // ----------------------------------HELPER FUNCTIONS------------------------------------------
    function modifySelectedElementsArray(cellElement, isSelected) {
        if (isSelected) {
            selectedElements.push(cellElement);
        } else {
            var index = selectedElements.indexOf(cellElement);
            selectedElements.splice(index, 1);
        }
        selectedElements.sort(compareNum);
    }

    function generateSelectedEvent(cellElement, isSelected) {
        var newEvent = new CustomEvent("selected", {
            detail: {
                x: cellElement.dataset.x,
                y: cellElement.dataset.y,
                isSelected: isSelected
            },
            bubbles: true,
            cancelable: false
        });
        tableElement.dispatchEvent(newEvent);
    }

    function generateAnySelectedEvent(isSelected) {
        var newEvent = new CustomEvent("anySelected", {
            detail: isSelected,
            bubbles: true,
            cancelable: false
        });
        tableElement.dispatchEvent(newEvent);
    }

    function removeSelectionStyle(cellElement) {
        cellElement.classList.remove("highlight");
    }
    function addSelectionStyle(cellElement) {
        cellElement.classList.add("highlight");
    }

    function toggleSelection(cellElement) {
        var wasSelected = cellElement.classList.contains("highlight");
        if (wasSelected) {
            removeSelectionStyle(cellElement);
        } else {
            addSelectionStyle(cellElement);
        }
        var isSelected = !wasSelected;
        modifySelectedElementsArray(cellElement, isSelected);
        generateSelectedEvent(cellElement, isSelected);
    }

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

    function getCellById(x, y) {
            var result = document.getElementById("cell_" + x + "_" + y);
            return result;
    }

    window.tablePlugin = main;

})();
