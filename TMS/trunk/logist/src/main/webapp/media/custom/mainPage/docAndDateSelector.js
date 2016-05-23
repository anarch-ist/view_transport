(function ($) {
    "use strict";
    //https://harvesthq.github.io/chosen/options.html
    //https://harvesthq.github.io/chosen/
    //https://github.com/nazar-pc/PickMeUp
    $.fn.docAndDateSelector = function (options) {

        var settings = $.extend({
            useWarehouseRole: true,
            offsetLabel: "Время склада:",
            docPlaceHolder: "Выберите док",
            warehousePlaceHolder: "Выберите склад",
            data: {}
        }, options);

        var data = settings.data;
        var useWarehouseRole = settings.useWarehouseRole;
        var firstWarehouse = data.warehouses[0]; // usable if useWarehouseRole = false

        // sort all data
        sortByStringCompare(data.warehouses, "warehouseName");
        data.warehouses.forEach(function (warehouse) {
            sortByStringCompare(warehouse.docs, "docName");
        });

        // generate content
        this.empty();
        this.append(
            "<div id='docAndDateSelector'>"+
            "<div id='datePicker'><input type='text' readonly='readonly'></div>" +
            "<div id='warehousePicker'></div>" +
            "<div id='docPicker'><select></select></div>" +
            "<div id='timeOffset'><label>" + settings.offsetLabel + "</label><div></div></div>" +
            "</div>"
        );

        // create offset component
        var $offsetDiv = $("#timeOffset").find("div");
        if (!useWarehouseRole) {
            fillOffset(firstWarehouse);
        }

        // create doc component
        var $docSelect = $("#docPicker").find("select");
        $docSelect.append($("<option>"));
        if (useWarehouseRole) {
            $docSelect.prop('disabled', true);
        }
        $docSelect.chosen({
            allow_single_deselect: true,
            placeholder_text_single: settings.docPlaceHolder
        });
        $docSelect.on('change', function () {
            generateEventIfValidState();
        });

        // create warehouse component
        if (useWarehouseRole) {
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
        if (useWarehouseRole) {
            minDate = new Date();// current Date
            minDate.setDate(minDate.getDate() - 1);
            maxDate = new Date();
            maxDate.setYear(maxDate.getFullYear() + 1);
        } else {
            minDate = null;
            maxDate = null;
        }
        var dateSelect = $("#datePicker").find("input");
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
                generateEventIfValidState();
            },
            min: minDate,
            max: maxDate

        });
        dateSelect.pickmeup('set_date', new Date());


        //-------------------------- METHODS -------------------------------
        this.setSelectedDate = function (date) {
            dateSelect.pickmeup('set_date', date);
        };

        /**
         * use this method with useWarehouseRole=false
         * @param docId
         */
        this.setSelectedDoc = function(docId) {
            setDoc(docId);
        };

        /**
         * use this method with useWarehouseRole=true
         * @param warehouseId
         * @param docId
         */
        this.setSelectedWarehouseAndDoc = function (warehouseId, docId) {
            $warehouseSelect.val(warehouseId);
            $warehouseSelect.trigger("chosen:updated");
            onWarehouseChange(warehouseId);
            setDoc(docId);
        };

        this.setOnSelected = function (handler) {
            $(document).on("docDateSelected", handler);
        };


        //-------------------------- FUNCTIONS -------------------------------
        function setDoc(docId) {
            $docSelect.val(docId);
            $docSelect.trigger("chosen:updated");
        }

        function fillOffset(warehouseById) {
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

        function generateEventIfValidState() {
            var selectedDate = dateSelect.pickmeup('get_date', true);
            var selectedDoc =  $docSelect.chosen().val();
            var selectedWarehouse = getSelectedWarehouseId();
            if (selectedDoc && selectedWarehouse) {
                $(document).trigger("docDateSelected", [selectedDate, selectedWarehouse, selectedDoc]);
            }
        }

        function getSelectedWarehouseId() {
            if (useWarehouseRole) {
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

        return this;
    };

}(jQuery));

