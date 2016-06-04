(function() {
    "use strict";

    var DEFAULT_PARAMETERS = {
        parentId: '',
        cellSize: 30, // in minutes!!!
        windowSize: 60 * 24,
        selectionConstraint: null,
        allowedStatesForSelection: {isOpenedAllowed: true, isClosedAllowed: true, isOccupiedAllowed: true},
        selectionModel: {
            isSelectAllOccupied: true,
            isSelectAllClosed: true
        }
    };

    const CLOSED = "CLOSED";
    const OCCUPIED = "OCCUPIED";
    const OPENED = "OPENED";

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


    var data = [];
    var dataAndCellsRelation = new Map();


    main.defaultParameters = DEFAULT_PARAMETERS;

    var tableElement;
    var disableElement;
    var selectedElementsInstance = new SelectedElements();
    var generatedCells = [];

    main.generate = function(){
        var tableSizes = findTableSizes(startupParameters.windowSize, startupParameters.cellSize);
        var serialNumber = 0;

        var parentElem = document.getElementById(startupParameters.parentId);
        parentElem.classList.add("tablePlugin");
        disableElement = document.createElement("div");
        parentElem.appendChild(disableElement);
        disableElement.classList.add("tp_disabled");
        tableElement = document.createElement("table");
        tableElement.classList.add("mainTable");
        parentElem.appendChild(tableElement);
        var labelGenerator = createLabelGenerator(startupParameters.cellSize);

        // generate rows and cells
        for(var i = 1; i <= tableSizes.y; i++) {
            var rowElement = document.createElement("tr");
            tableElement.appendChild(rowElement);

            for(var j = 1; j <= tableSizes.x; j++) {
                serialNumber++;
                var cellElement = document.createElement("td");
                generatedCells.push(cellElement);
                dataAndCellsRelation.set(cellElement, null);
                cellElement.setAttribute("data-serialnumber", serialNumber);
                cellElement.onclick = onClickHandlersFactory(cellElement);
                cellElement.appendChild(labelGenerator());
                cellElement.appendChild(document.createElement("div"));
                rowElement.appendChild(cellElement);
            }
        }

        // generate any selected event
        var anySelected = false;
        main.setOnSelectionChanged(function() {
            if (selectedElementsInstance.isEmpty()) {
                generateAnySelectedEvent(false);
                anySelected = false;
            } else if (!anySelected) {
                generateAnySelectedEvent(true);
                anySelected = true;
            }
        });
    };


    // ----------------------------------METHODS------------------------------------------



    /**
     * set data array and creates view for this data
     * @param newData
     */
    main.setData = function(newData) {
        data = newData;
        clearVisual();
        data.forEach(function(dataElem) {
            dataElem.cells = [];
            var cellsForPeriod = getCellsForPeriod(dataElem.periodBegin, dataElem.periodEnd);
            for (var i = 0; i < cellsForPeriod.length; i++) {

                var cellDiv = cellsForPeriod[i].getElementsByTagName('div')[0];

                dataElem.cells.push(cellsForPeriod[i]);
                dataAndCellsRelation.set(cellsForPeriod[i], dataElem);

                if (dataElem.state === OCCUPIED) {
                    cellDiv.innerHTML = dataElem.stateData.supplierName;
                    cellDiv.classList.add("tp_occupied");
                    if (dataElem.owned) {
                        cellDiv.classList.add("tp_owned");
                    }
                } else if (dataElem.state === CLOSED) {
                    cellDiv = cellsForPeriod[i].getElementsByTagName('div')[0];
                    cellDiv.classList.add("tp_closed");
                }
            }
        });
    };

    /**
     * cloned data object
     */
    main.getData = function() {
        return JSON.parse(JSON.stringify(data));
    };

    main.isAnySelected = function() {
        return (!selectedElementsInstance.isEmpty());
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
        //if (selectedElementsInstance.isEmpty()) {
        //    return {};
        //}
        //var periodBegin = (+selectedElementsInstance[0].dataset.serialnumber -1) * startupParameters.cellSize;
        //var periodEnd = (+selectedElementsInstance[selectedElementsInstance.length - 1].dataset.serialnumber) * startupParameters.cellSize;
        //return {periodBegin:periodBegin, periodEnd: periodEnd};
    };


    main.clear = function() {
        if (selectedElementsInstance.isEmpty()) {
            generateAnySelectedEvent(false);
        } else {
            var copy = selectedElementsInstance.slice();
            copy.forEach(function(cellElement) {
                removeSelectionStyle(cellElement);
                modifySelectedElementsArray(cellElement, false);
                generateSelectedEvent(cellElement, false);
            });
        }

        clearVisual();
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

    var previousSelectedState;
    function onClickHandlersFactory(cellElement) {

        return function() {
            var wasSelected = selectedElementsInstance.contains(cellElement);

            // apply selection constraint
            if (startupParameters.selectionConstraint) {
                if (startupParameters.selectionConstraint(
                        +cellElement.dataset.serialnumber,
                        selectedElementsInstance.selectedElementsAsSerialNumbers(),
                        wasSelected)) {
                    return;
                }
            }

            var relatedData = dataAndCellsRelation.get(cellElement);
            var currentSelectedState;
            if (relatedData === null) {
                currentSelectedState = OPENED;
            } else if (relatedData.state === CLOSED) {
                currentSelectedState = CLOSED;
            } else if (relatedData.state === OCCUPIED) {
                currentSelectedState = OCCUPIED;
            }

            if (!startupParameters.allowedStatesForSelection.isOpenedAllowed) {
                if (currentSelectedState === OPENED) {
                    return;
                }
            }

            if (!startupParameters.allowedStatesForSelection.isClosedAllowed) {
                if (currentSelectedState === CLOSED) {
                    return;
                }
            }

            if (!startupParameters.allowedStatesForSelection.isOccupiedAllowed) {
                if (currentSelectedState === OCCUPIED) {
                    return;
                }
            }

            // если не владлец, то всегда запрещать возможность выбора
            if (currentSelectedState !== OPENED && !relatedData.owned) {
                return;
            }

            if (startupParameters.selectionModel.isSelectAllClosed) {
                // если выбран один, то автоматом выбирать все с данным состоянием
                if (currentSelectedState === CLOSED) {
                    if (wasSelected) {
                        selectedElementsInstance.clearSelection();
                    } else {
                        relatedData.cells.forEach(function (cell) {
                            selectedElementsInstance.add(cell);
                        }); // cells for selection
                    }
                    return;
                }
            }
            // toggle selection
            if (wasSelected) {
                selectedElementsInstance.remove(cellElement);
            } else {
                if (previousSelectedState !== currentSelectedState) {
                    selectedElementsInstance.clearSelection();
                }
                previousSelectedState = currentSelectedState;
                selectedElementsInstance.add(cellElement);
            }

            generateSelectedEvent(cellElement, !wasSelected);
        };
    }

    function SelectedElements() {
        var selectedElements = [];

        this.add = function (cellElement) {
            selectedElements.push(cellElement);
            addSelectionStyle(cellElement);
            sort();
        };

        this.remove = function (cellElement) {
            var index = selectedElements.indexOf(cellElement);
            selectedElements.splice(index, 1);
            removeSelectionStyle(cellElement);
            sort();
        };

        this.contains = function(cellElement) {
          return selectedElements.indexOf(cellElement) >= 0;
        };

        this.clear = function () {
            selectedElements.splice();
        };

        this.isEmpty = function () {
            return selectedElements.length === 0;
        };

        this.selectedElementsAsSerialNumbers = function () {
            var result = [];
            for (var i = 0; i < selectedElements.length; i++) {
                result.push(+selectedElements[i].dataset.serialnumber);
            }
            return result;
        };

        this.clearSelection = function () {
            selectedElements.forEach(function (element) {
                removeSelectionStyle(element);
            });
            selectedElements.length = 0;
        };

        function sort() {
            selectedElements.sort(function compareNum(a, b) {
                return a.dataset.serialnumber - b.dataset.serialnumber;
            });
        }
        function removeSelectionStyle(cellElement) {
            cellElement.classList.remove("tp_highlight");
        }
        function addSelectionStyle(cellElement) {
            cellElement.classList.add("tp_highlight");
        }
    }

    function findTableSizes(windowsSize, periodSize) {
        var periodsCount = windowsSize / periodSize;

        var result = {
            x: 1,
            y: periodsCount
        };

        for (var i = 2; i <= (periodsCount / 2); i++) {
            if (!(periodsCount % i)) {
                var j = periodsCount / i;
                if (((result.y - result.x) > (j - i)) && ((j - i) >= 0)) {
                    result.x = i;
                    result.y = j;
                }
            }
        }
        return result;
    }
    function createLabelGenerator(periodSize) {
        var firstPart = "00:00";
        var secondPart = "";

        return function () {
            var labelElement = document.createElement("label");
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
            return labelElement;
        };
    }

    function clearVisual() {
        [].forEach.call(tableElement.getElementsByTagName("td"), function (element) {
            var cellDiv = element.getElementsByTagName('div')[0];
            cellDiv.className = "";
            cellDiv.innerHTML = "";
        });
    }

    /**
     * @param periodBegin
     * @param periodEnd
     * @return array of cells within period
     */
    function getCellsForPeriod(periodBegin, periodEnd) {
        var periodSize = periodEnd - periodBegin,
            cellSize = startupParameters.cellSize,
            minSerialNumber,
            maxSerialNumber;
        if (periodSize === cellSize) {
            minSerialNumber = periodBegin / cellSize;
            maxSerialNumber = minSerialNumber;
        } else {
            var cellQty = periodSize/startupParameters.cellSize - 1;
            minSerialNumber = periodBegin / cellSize;
            maxSerialNumber = minSerialNumber + cellQty;
        }

        var result = [];
        for(var i = 0; i < generatedCells.length; i++){
            var serialnumber = generatedCells[i].dataset.serialnumber - 1;
            if(serialnumber >= minSerialNumber && serialnumber <= maxSerialNumber){
                result.push(generatedCells[i]);
            }
        }
        return result;
    }

    function generateSelectedEvent(cellElement, isSelected) {
        var newEvent = new CustomEvent("selected", {
            detail: {
                isSelected: isSelected,
                cellElement: cellElement,
                selectedElements: selectedElementsInstance,
                data: dataAndCellsRelation.get(cellElement)
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



    window.tablePlugin2 = main;

})();

