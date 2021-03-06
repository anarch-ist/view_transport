(function ($) {
    "use strict";
    //https://harvesthq.github.io/chosen/options.html
    //https://harvesthq.github.io/chosen/
    //https://github.com/nazar-pc/PickMeUp
    $.fn.docAndDateSelector = function (options) {

        var settings = $.extend({
            useWarehouseSelect: true,
            offsetLabel: "Время склада:",
            docPlaceHolder: "Выберите док",
            warehousePlaceHolder: "Выберите склад",
            data: {}
        }, options);

        var data = settings.data;
        var useWarehouseSelect = settings.useWarehouseSelect;
        var firstWarehouse = data.warehouses[0]; // usable if useWarehouseSelect = false



        // sort all data
        sortByStringCompare(data.warehouses, "warehouseName");
        data.warehouses.forEach(function (warehouse) {
            sortByStringCompare(warehouse.docs, "docName");
        });

        // generate content
        this.empty();
        this.append(
            "<div id='docAndDateSelectorId' class='docAndDateSelector'>"+
            "<div id='datePickerId' class='datePicker'><input type='text' readonly='readonly'></div>" +
            "<div id='warehousePicker' class='placePicker'></div>" +
            "<div id='docPicker' class='placePicker'><select></select></div>" +
            "<div id='timeOffset'><label class='timeGMT'>" + settings.offsetLabel + "</label><div class='timeGMT'></div></div>" +
            "</div>"
        );

        // create offset component
        var $offsetDiv = $("#timeOffset").find("div");
        if (!useWarehouseSelect) {
            fillOffset(firstWarehouse);
        }

        // create doc component
        var $docSelect = $("#docPicker").find("select");
        $docSelect.append($("<option>"));
        if (useWarehouseSelect) {
            $docSelect.prop('disabled', true);
        }
        $docSelect.chosen({
            allow_single_deselect: true,
            placeholder_text_single: settings.docPlaceHolder
        });
        $docSelect.on('change', function () {
            generateEvents();
        });

        // create warehouse component
        if (useWarehouseSelect) {
            var $warehouseSelect = $("<select></select>");
            $("#warehousePicker").append($warehouseSelect);
            // add data to warehouse select
            var warehouses = data.warehouses;
            $warehouseSelect.append($("<option>"));
            for (var i = 0; i < warehouses.length; i++) {
                $warehouseSelect.append($("<option>").attr("value", warehouses[i].warehouseId).text(warehouses[i].warehouseName));
            }
            $warehouseSelect.chosen({
                allow_single_deselect: true,
                placeholder_text_single: settings.warehousePlaceHolder
            });
            $warehouseSelect.on('change', function (evt, params) {
                var warehouseId;
                if (params) {
                    warehouseId = +params.selected;
                } else {
                    warehouseId = null;
                }
                onWarehouseChange(warehouseId);
                generateEvents();
            });

        } else {
            var $warehouseLabel = $("<div></div>");
            $warehouseLabel.attr("value", firstWarehouse.warehouseId).text(firstWarehouse.warehouseName);
            $("#warehousePicker").append($warehouseLabel);
            fillDocsWithData(firstWarehouse.warehouseId);
        }

        // create data select component
        var minDate;
        var maxDate;
        if (useWarehouseSelect) {
            minDate = new Date();// current Date
            minDate.setDate(minDate.getDate() - 1);
            maxDate = new Date();
            maxDate.setYear(maxDate.getFullYear() + 1);
        } else {
            minDate = null;
            maxDate = null;
        }
        var dateSelect = $("#datePickerId").find("input");
        dateSelect.pickmeup({
            locale: {
                days: ["Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"],
                daysShort: ["Вск", "Пнд", "Втр", "Срд", "Чтв", "Птн", "Сбт", "Вск"],
                daysMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"],
                months: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
                monthsShort: ["Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"]
            },
            select_year: false,
            date: new Date(),
            hide_on_select: true,
            change: function () {
                generateEvents();
            },
            min: minDate,
            max: maxDate

        });
        dateSelect.pickmeup('set_date', new Date());


        //Setting up warehouse report datepicker
        var whReportRangeSelect = $('#warehouseReportPickMeUpRange');
        whReportRangeSelect.pickmeup({
            locale: {
                days: ["Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"],
                daysShort: ["Вск", "Пнд", "Втр", "Срд", "Чтв", "Птн", "Сбт", "Вск"],
                daysMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"],
                months: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
                monthsShort: ["Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"]
            },
            // flat: true,
            mode: 'range',
            format: "Y-m-d",
            select_year: false,
            date: new Date(),
            separator: "/"
        });
        whReportRangeSelect.pickmeup('set_date', new Date());
        //I just can't figure where else to put it.
        $('#openWarehouseReport').on('click', function () {
            window.open("getWarehouseReport?periodBegin="+$('#warehouseReportPickMeUpRange').val().split('/')[0]+"&periodEnd="+$('#warehouseReportPickMeUpRange').val().split('/')[1]+"&warehouseId="+getSelectedWarehouseId());
        });


        //-------------------------- METHODS -------------------------------
        this.setSelectedDate = function (date) {
            dateSelect.pickmeup('set_date', date);
        };

        /**
         * use this method with useWarehouseSelect=false
         * @param docId
         */
        this.setSelectedDoc = function(docId) {
            setDoc(docId);
        };

        /**
         * use this method with useWarehouseSelect=true
         * @param warehouseId
         * @param docId
         */
        this.setSelectedWarehouseAndDoc = function (warehouseId, docId) {
            $warehouseSelect.val(warehouseId);
            $warehouseSelect.trigger("chosen:updated");
            onWarehouseChange(warehouseId);
            setDoc(docId);
        };

        this.isSelectionAvailable = function() {
            return lastSelectionAvailable;
        };

        this.setOnSelected = function (handler) {
            $(document).on("docDateSelected", handler);
        };

        this.setOnSelectionAvailable = function(handler) {
            $(document).on("selectionAvailableChanged", handler);
        };

        this.triggerEvents = function() {
            generateEvents();
        };

        this.withWarehouseSelect = function() {
            return useWarehouseSelect;
        };

        this.getSelectedWarehouseDocs = function(){

        };

        //-------------------------- FUNCTIONS -------------------------------
        function setDoc(docId) {
            $docSelect.val(docId);
            $docSelect.trigger("chosen:updated");
        }

        function fillOffset(warehouseById) {
            $offsetDiv.data("offset", warehouseById.timeOffset);
            $offsetDiv.text(warehouseById.rusTimeZoneAbbr + " GMT+" + warehouseById.timeOffset);
        }

        function onWarehouseChange(warehouseId) {
            if (warehouseId) {
                fillDocsWithData(warehouseId);
                $docSelect.prop('disabled', false).trigger("chosen:updated");
                // create offset
                var warehouseById = findWarehouseById(warehouseId);
                fillOffset(warehouseById);
            }
            else {
                var $emptyOption = $docSelect.find(":first-child");
                $emptyOption.prop('selected', true);
                $docSelect.prop('disabled', true).trigger("chosen:updated");
                $offsetDiv.text("");
            }
        }

        var lastSelectionAvailable = null; // null is initial state
        function generateEvents() {
            var docDateSelection = getSelectionObject();
            var selectionAvailable = (docDateSelection !== null);
            if (selectionAvailable) {
                $(document).trigger("docDateSelected", docDateSelection);
            }
            if (lastSelectionAvailable === null || selectionAvailable !== lastSelectionAvailable) {
                $(document).trigger("selectionAvailableChanged", selectionAvailable);
            }
            lastSelectionAvailable = selectionAvailable;
        }

        /**
         *
         * @returns {null or selection Object}
         */
        function getSelectionObject() {
            var selectedDate = dateSelect.pickmeup('get_date', 'Y-m-d'); // actually 'YYYY-MM-DD'
            var selectedDocId =  +$docSelect.chosen().val();
            getSelectedDocs();
            var selectedWarehouseId = +getSelectedWarehouseId();

            if (selectedDocId && selectedWarehouseId) {
                var utcDate = new Date(new Date(Date.parse(selectedDate)).getTime() - getOffset() * 3600 * 1000);
                return {date: utcDate, warehouseId: selectedWarehouseId, docId: selectedDocId};
            } else {
                return null;
            }

        }

        /**
         *
         * @returns {null or selection Object}
         */
        function getSelectedDocs(){
            var selectedDate = dateSelect.pickmeup('get_date', 'Y-m-d');
            var selectedWarehouseId = Number(getSelectedWarehouseId());

            if (selectedWarehouseId){
                var warehouseDocs =  findWarehouseById(Number(getSelectedWarehouseId())).docs;
                var utcDate = new Date(new Date(Date.parse(selectedDate)).getTime() - getOffset() * 3600 * 1000);
                var selectedDocs = [];
                warehouseDocs.forEach(function(item,i,arr){
                    selectedDocs.push({date:utcDate, warehouseId:selectedWarehouseId, docId: item.docId, docName: item.docName})
                });
                //return {date: utcDate, warehouseId: selectedWarehouseId, docId: selectedDocId};
                return selectedDocs;
            } else {
                return null;
            }


        }

        function getSelectedWarehouseId() {
            if (useWarehouseSelect) {
                return $warehouseSelect.chosen().val();
            } else {
                return $warehouseLabel.attr("value");
            }
        }

        function fillDocsWithData(warehouseId) {
            var docs = findWarehouseById(warehouseId).docs;
            $docSelect.empty();
            $docSelect.append($("<option>"));
            for (var i = 0; i < docs.length; i++) {
                $docSelect.append($("<option>").attr("value", docs[i].docId).text(docs[i].docName));
            }
            $docSelect.trigger("chosen:updated");
        }

        function findWarehouseById(warehouseId) {
            return $.grep(data.warehouses, function (e) {

                return e.warehouseId === warehouseId;

            })[0];
        }

        function sortByStringCompare(objects, propertyName) {
            function compare(a, b) {
                return a[propertyName] < b[propertyName] ? -1 : a[propertyName] > b[propertyName];
            }
            objects.sort(compare);
        }

        function getOffset() {
            return +$offsetDiv.data("offset");
        }
        this.getOffset = getOffset;
        this.getSelectionObject = getSelectionObject;
        this.getSelectedDocs = getSelectedDocs;

        return this;
    };

}(jQuery));


