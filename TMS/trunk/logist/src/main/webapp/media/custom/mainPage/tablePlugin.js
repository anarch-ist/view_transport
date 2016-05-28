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
                    // generate selected event
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
                divElement.setAttribute("id", "companyDiv");
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

    main.setOnClicked = function(handler) {
        tableElement.addEventListener("selected", handler);
    };

    main.setOnAnySelected = function(handler) {
        var notAnySelected = true;
        main.setOnClicked(function() {
            if (selectedElements.length === 0) {
                handler(false);
                notAnySelected = true;
            } else if (selectedElements.length !== 0 && notAnySelected) {
                handler(true);
                notAnySelected = false;
            }
        });
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
        selectedElements.length = 0;
        [].forEach.call(tableElement.getElementsByTagName("td"), function(element) {
            element.children[1].classList.remove('occupied', 'highlight', 'disabled');
            element.children[1].innerHTML = "";
        });
    };

    // TODO implement this method
    /**
     * @return array of periods with identical state
     */
    main.getSelectedPeriods = function() {
    };

    // TODO implement this method
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
    };


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

    // index = 0 if start of periods and nothing if end
    function splitPeriodsAndCountTime(str, index){
        var hyphenArr = str.split('-');
        var colonArr = [];
        var result;
        if(index === 0){
            colonArr = hyphenArr[0].split(':');
        }else{
            colonArr = hyphenArr[hyphenArr.length - 1].split(':');
        }
        var hours = +colonArr[0];
        hours = hours * 60;
        var minutes = +colonArr[1];
        result = hours + minutes;

        return result;
    }

    function getCellById(x, y) {
            var result = document.getElementById("cell_" + x + "_" + y);
            return result;
    }

//    main.sendDocPeriods = function() {
//        var result = [];
//
//        var previousState;
//        var previousSerialNumber;
//        var sequentialStates = [];
//
//
//        selectedElements.push(getCellById(1, 6));
//        selectedElements.push(getCellById(2, 1));
//        selectedElements.push(getCellById(2, 3));
//        selectedElements.push(getCellById(2, 4));
//        selectedElements.push(getCellById(3, 3));
//        selectedElements.push(getCellById(3, 4));
//        selectedElements.push(getCellById(3, 5));
//        selectedElements.push(getCellById(3, 6));
//        selectedElements.push(getCellById(4, 1));
//        selectedElements.push(getCellById(4, 2));
//
//        for(var i = 0; i < selectedElements.length; ){
//            var cellState = selectedElements[i].lastElementChild.getAttribute('class');
//            var cellSerialNumber = selectedElements[i].dataset.serialnumber;
////            window.console.log(cellState + " " + cellSerialNumber);
//            if(!previousState){
//                sequentialStates.push(i);
//                previousState = cellState;
//                previousSerialNumber = cellSerialNumber;
//                i++;
//            }else{
////            window.console.log(previousState + " " + previousSerialNumber + " " + cellState + " " + cellSerialNumber);
//
//                if(previousState === cellState && ((cellSerialNumber - 1) == previousSerialNumber)){
//
//                    sequentialStates.push(i);
//                    previousState = cellState;
//                    previousSerialNumber = cellSerialNumber;
//                    i++;
//                       window.console.log(cellState + " " + cellSerialNumber + " " + previousState + " " + previousSerialNumber);
//                }else if(previousState !== cellState || (cellSerialNumber - 1) != previousSerialNumber){
//                    window.console.log(sequentialStates + " k");
////                    window.console.log(selectedElements[0] + " h");
//                    var firstIndex = sequentialStates[0];
//                    var lastIndex = sequentialStates[sequentialStates.length - 1];
//                    var firstElem = selectedElements[firstIndex];
//                    var lastElem = selectedElements[lastIndex];
//                    // periods
//                    var firstElemLabel = firstElem.firstElementChild.innerHTML;
//                    var lastElemLabel = lastElem.firstElementChild.innerHTML;
//                    var firstTime = splitPeriodsAndCountTime(firstElemLabel, 0);
//                    var lastTime = splitPeriodsAndCountTime(lastElemLabel);
//                    window.console.log(firstElemLabel + " " + firstTime + " " + lastElemLabel + " " + lastTime);
//                    // company
////                    if(previousState === 'occupied'){
////                        var companies = [];
////                        var previousCompany;
////                        var currentCompany;
////
////                        var objComp = {
////                            startTime: beginTime,
////                            endTime: stopTime,
////                            state: previousState,
////                            company: previousCompany
////                        };
////                        result.push(objComp);
//
////                        for(var x = 0; x < sequentialStates.length; ){
////
////
////                            currentCompany = selectedElements[sequentialStates[x]].lastElementChild.innerHTML;
////                            window.console.log(currentCompany + " " + selectedElements[sequentialStates[x]].firstElementChild.innerHTML);
////                            if(!previousCompany){
////                                previousCompany = currentCompany;
////                                companies.push(selectedElements[sequentialStates[x]]);
////                                x++;
//////                                window.console.log(selectedElements[sequentialStates[x]]);
////                            }else{
////                                if(previousCompany === currentCompany){
////                                    companies.push(selectedElements[sequentialStates[x]]);
////                                    previousCompany = currentCompany;
////                                    x++;
////
////                                }else if(previousCompany !== currentCompany || x === (sequentialStates.length - 1)){
////                                    window.console.log(previousCompany + " " + currentCompany);
////                                    var beginTime = splitPeriodsAndCountTime(companies[0].firstElementChild.innerHTML , 0);
////                                    var stopTime = splitPeriodsAndCountTime(companies[companies.length - 1].firstElementChild.innerHTML);
////                                    var objComp = {
////                                        startTime: beginTime,
////                                        endTime: stopTime,
////                                        state: previousState,
////                                        company: previousCompany
////                                    };
//////                                    window.console.log(objComp);
////                                    previousCompany = undefined;
////                                    result.push(objComp);
////                                    companies.length = 0;
////                                }
////                            }
////                        }
////                    }else
////                    {
//                        var obj = {
//                            startTime: firstTime,
//                            endTime: lastTime,
//                            state: previousState
//                        };
//                        result.push(obj);
//                    }
//                    previousState = undefined;
//                    previousSerialNumber = undefined;
//                    sequentialStates.length = 0;
//
//                }
//            }
//            window.console.log(result.length);
//
////            previousSerialNumber = cellSerialNumber;
////            previousState = cellState;
//
//
//        var formElem = document.createElement("form");
//        formElem.setAttribute('Action', 'submit');
//
//        // send result as content????
//        //formElem.setAttribute('Content', result);
//        var inputElem = document.createElement("input");
//        inputElem.setAttribute('Type', 'button');
//        inputElem.setAttribute('Value', 'Отправить');
//        formElem.appendChild(inputElem);
//        document.body.appendChild(formElem);
//    };


    window.tablePlugin = main;

})();
