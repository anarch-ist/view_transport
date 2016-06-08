(function() {
    "use strict";

    var DEFAULT_PARAMETERS = {
        parentId: '',
        cellSize: 30, // in minutes!!!
        windowSize: 60 * 24,
        selectionConstraint: null,
        allowedStatesForSelection: {
            isOpenedAllowed: true,
            isClosedAllowed: true,
            isOccupiedAllowed: true
        },
        selectionModel: {
            isSelectAllOccupied: false,
            isSelectAllClosed: false
        },
        buttons: null
    };

    var CLOSED = "CLOSED",
        OCCUPIED = "OCCUPIED",
        OPENED = "OPENED";

    var startupParameters = {};
    var main = function (initParams) {
        startupParameters = merge(DEFAULT_PARAMETERS, initParams);
        return main;
    };

    var data = [];
    var dataAndCellsRelation = new Map();

    var tableElement = document.createElement("table");
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
        createEventDependencies();

        if (startupParameters.buttons !== null) {
            var tableControls = new TableControls(parentElem, main, startupParameters.buttons);
            tableControls.generateContent();
        }

        // generate createdEvent
        var newEvent = new CustomEvent("created", {
            bubbles: true,
            cancelable: false
        });
        tableElement.dispatchEvent(newEvent);
    };


    // ----------------------------------METHODS------------------------------------------



    /**
     * set data array and creates view for this data
     * @param newData
     */
    main.setData = function(newData) {

        data = newData;
        selectedElementsInstance.clearSelection();
        clearVisual();

        var newEvent = new CustomEvent("cleared", {
            bubbles: true,
            cancelable: false
        });
        tableElement.dispatchEvent(newEvent);

        data.forEach(function(dataElem) {
            dataElem.cells = [];
            var cellsForPeriod = getCellsForPeriod(dataElem.periodBegin, dataElem.periodEnd);
            for (var i = 0; i < cellsForPeriod.length; i++) {

                var cellDiv = cellsForPeriod[i].getElementsByTagName('div')[0];

                dataElem.cells.push(cellsForPeriod[i]);
                dataAndCellsRelation.set(cellsForPeriod[i], dataElem);

                if (dataElem.state === OCCUPIED) {
                    cellDiv.innerHTML = dataElem.supplierName;
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
    main.getSelectionData = function() {
        return selectedElementsInstance.getSelectionData();
    };
    main.isAnySelected = function() {
        return (!selectedElementsInstance.isEmpty());
    };

    main.isDisabled = function() {
        return previousDisabled;
    };

    var previousDisabled;
    main.setDisabled = function(disabled) {
        if (disabled === previousDisabled) {
            return;
        }
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

        generateDisabledChangeEvent(disabled);

        previousDisabled = disabled;
    };

    // Handlers
    main.setOnCreated = function(handler) {
        tableElement.addEventListener("created", handler);
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
    main.setOnCleared = function(handler) {
        tableElement.addEventListener("cleared", handler);
    };

    function createEventDependencies() {
        main.setOnCreated(function() {
            generateDisabledChangeEvent(false);
            generateAnySelectedEvent(false);
        });

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

        main.setOnCleared(function() {
            generateAnySelectedEvent(false);
        });
    }


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

            // if not owner always restrict selection
            if (currentSelectedState !== OPENED && !relatedData.owned) {
                return;
            }

            // toggle selection
            if (wasSelected) {
                clearSelection(cellElement, currentSelectedState);
            } else {
                if (previousSelectedState !== currentSelectedState) {
                    selectedElementsInstance.clearSelection();
                }
                previousSelectedState = currentSelectedState;
                addSelection(cellElement, currentSelectedState, relatedData);
            }
            generateSelectedEvent(cellElement, !wasSelected, currentSelectedState);
        };

        function clearSelection(cellElement, currentSelectedState) {
            if (
                (startupParameters.selectionModel.isSelectAllClosed && currentSelectedState === CLOSED) ||
                (startupParameters.selectionModel.isSelectAllOccupied && currentSelectedState === OCCUPIED)
            ) {
                selectedElementsInstance.clearSelection();
            } else {
                selectedElementsInstance.remove(cellElement);
            }
        }

        function addSelection(cellElement, currentSelectedState, relatedData) {
            if (
                (startupParameters.selectionModel.isSelectAllClosed && currentSelectedState === CLOSED) ||
                (startupParameters.selectionModel.isSelectAllOccupied && currentSelectedState === OCCUPIED)
            ) {
                relatedData.cells.forEach(function (cell) {
                    selectedElementsInstance.add(cell);
                });
            } else {
                selectedElementsInstance.add(cellElement);
            }
        }
    }


    function SelectedElements() {
        var selectedElements = [];

        this.getSelectedCells = function() {
            return selectedElements;
        };

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

        // example result:
        //var exampleResult = [
        //    {data: null, periods: [{periodBegin: 630, periodEnd: 660}, {periodBegin: 660, periodEnd:690}]},
        //    {data: null, periods: [{periodBegin: 630, periodEnd: 660}, {periodBegin: 660, periodEnd:690}]}
        //];
        this.getSelectionData = function() {
            var result = [];
            var allObjects = [];
            var selectedElementsWithoutState = [];
            for (var i = 0; i < selectedElements.length; i++) {
                var dataObject = dataAndCellsRelation.get(selectedElements[i]);
                if (dataObject === null) {
                    selectedElementsWithoutState.push(selectedElements[i]);
                }
                if (allObjects.indexOf(dataObject) === -1) {
                    allObjects.push(dataObject);
                }
            }

            for (var j = 0; j < allObjects.length; j++) {
                if (allObjects[j] !== null) {
                    var cells = intersect(allObjects[j].cells, selectedElements);
                    var data = cloneWithoutCells(allObjects[j]);
                    result.push({data: data, periods: getPeriodsFromCells(cells)});
                } else {
                    result.push({data:null, periods:getPeriodsFromCells(selectedElementsWithoutState)});
                }
            }

            return result;
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
        function intersect(a, b) {
            var t;
            if (b.length > a.length) {
                t = b, b = a, a = t;
            } // indexOf to loop over shorter
            return a.filter(function (e) {
                if (b.indexOf(e) !== -1) return true;
            });
        }
        function getPeriodsFromCells(cells) {
            var periods = [];
            cells.forEach(function(cell) {
                periods.push(getPeriodForCell(cell));
            });
            return periods;
        }
        function cloneWithoutCells(object) {
            return JSON.parse(JSON.stringify(object, function(key, value) {
                if (key === "cells") {
                    return undefined;
                }
                return value;
            }));
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

    function getPeriodForCell(cellElement) {
        var periodEnd = cellElement.dataset.serialnumber * startupParameters.cellSize;
        var periodBegin = periodEnd - startupParameters.cellSize;
        return {periodBegin: periodBegin, periodEnd: periodEnd};
    }

    function generateSelectedEvent(cellElement, isSelected, currentSelectedState) {
        var newEvent = new CustomEvent("selected", {
            detail: {
                cellElement: cellElement,
                isSelected: isSelected,
                currentSelectedState: currentSelectedState,
                selectedElements: selectedElementsInstance.getSelectedCells(),
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

    function generateDisabledChangeEvent(disabled) {
        var newEvent = new CustomEvent("disableChanged", {
            detail: disabled,
            bubbles: true,
            cancelable: false
        });
        tableElement.dispatchEvent(newEvent);
    }


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


    function TableControls(containerElem, tablePlugin, buttonsDescription) {
        var state = {isDisabled:null, isAnySelected:null, currentSelectedState:null, isFullPeriodSelected:null};
        this.generateContent = function() {
            var buttons = [];

            tablePlugin.setOnCreated(function() {
                state.isDisabled = tablePlugin.isDisabled();
                state.isAnySelected = tablePlugin.isDisabled();
            });

            tablePlugin.setOnDisableChanged(function(e) {
                state.isDisabled = e.detail;
                buttons.forEach(function(buttonElem){
                    handleState(buttonElem, state);
                });
            });

            tablePlugin.setOnAnySelected(function(e) {
                state.isAnySelected = e.detail;
                buttons.forEach(function(buttonElem){
                    handleState(buttonElem, state);
                });
            });

            tablePlugin.setOnSelectionChanged(function(e) {
                var selectedCells = e.detail.selectedElements;
                var stateCells = e.detail.data;
                if (stateCells === null) {
                    state.isFullPeriodSelected = true;
                } else {
                    state.isFullPeriodSelected = selectedCells.length > 0 ? selectedCells.length === stateCells.cells.length : false;
                }

                state.currentSelectedState = e.detail.currentSelectedState;

                buttons.forEach(function(buttonElem){
                    handleState(buttonElem, state);
                });
            });


            for (var i = 0; i < buttonsDescription.length; i++) {
                var buttonElem = document.createElement('input');
                buttonElem.setAttribute('type', 'button');
                buttonElem.setAttribute('value', buttonsDescription[i].name);
                buttonElem.setAttribute('id', buttonsDescription[i].id);
                buttonElem.id = buttonsDescription[i].id;
                containerElem.appendChild(buttonElem);
                buttons.push({
                    buttonElem:buttonElem,
                    enabledIfAnySelected: buttonsDescription[i].enabledIfAnySelected,
                    enabledIf: buttonsDescription[i].enabledIf
                });
            }
        };

        tablePlugin.getButtonByPluginId = function(pluginId) {
            return document.getElementById(pluginId);
        };

        function handleState(button, state) {
            var buttonElem = button.buttonElem;

            if (state.isDisabled) {
                buttonElem.disabled = true;
                return;
            }

            if (button.enabledIfAnySelected) {
                if (!state.isAnySelected) {
                    buttonElem.disabled = true;
                } else {
                    handleEnabledIf();
                }
            } else {
                handleEnabledIf();
            }

            function handleEnabledIf() {
                if (button.enabledIf) {
                    buttonElem.disabled = !button.enabledIf(state.currentSelectedState, state.isFullPeriodSelected);
                } else {
                    buttonElem.disabled = false;
                }
            }

        }
    }

    window.tablePlugin2 = main;

})();

