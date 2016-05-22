(function ($) {
    "use strict";
    //https://harvesthq.github.io/chosen/options.html
    //https://harvesthq.github.io/chosen/
    //https://github.com/nazar-pc/PickMeUp
    $.fn.docAndDateSelector = function (options) {

        var settings = $.extend({
            useWarehouseRole: true,
            data: {}
        }, options);

        var data = settings.data;
        var useWarehouseRole = settings.useWarehouseRole;

        // sort all data
        sortByStringCompare(data.warehouses, "warehouseName");
        data.warehouses.forEach(function (warehouse) {
            sortByStringCompare(warehouse.docs, "docName");
        });

        this.empty();

        this.append(
            "<div id='docAndDateSelector'>"+
            "<div id='datePicker'><input type='text' readonly='readonly'></div>" +
            "<div id='warehousePicker'></div>" +
            "<div id='docPicker'><select></select></div>"+
            "</div>"
        );

        // create docSelect
        var docSelect = $("#docPicker").find("select");
        docSelect.append($("<option>"));
        if (useWarehouseRole) {
            docSelect.prop('disabled', true);
        }
        docSelect.chosen({
            allow_single_deselect: true,
            placeholder_text_single: "Выберите док"
        });
        docSelect.on('change', function (evt, params) {
            generateEventIfValidState();
        });

        // create Warehouse component
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
                placeholder_text_single: "Выберите склад"
            });
            $warehouseSelect.on('change', function (evt, params) {
                // if not empty select
                if (params) {
                    var warehouseId = +params.selected;
                    fillDocsWithData(warehouseId);
                    docSelect.prop('disabled', false).trigger("chosen:updated");
                }
                else {
                    var $emptyOption = docSelect.find(":first-child");
                    $emptyOption.prop('selected', true);
                    docSelect.prop('disabled', true).trigger("chosen:updated");
                }
            });

        } else {
            var $warehouseLabel = $("<div></div>");
            $warehouseLabel.attr("value", data.warehouses[0].warehouseId).text(data.warehouses[0].warehouseName);
            $("#warehousePicker").append($warehouseLabel);
            fillDocsWithData(data.warehouses[0].warehouseId);
        }

        // create data select
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
        var pickmeup = dateSelect.pickmeup({
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
            change: function (formattedDate) {
                generateEventIfValidState();
            },
            min: minDate,
            max: maxDate

        });
        dateSelect.pickmeup('set_date', new Date());


        this.setSelectedDate = function (date) {
            dateSelect.pickmeup('set_date', date);
        };

        // TODO
        this.setSelectedWarehouse = function (warehouseId) {
            $warehouseSelect.val(warehouseId);
            $warehouseSelect.trigger("chosen:updated");
        };

        // TODO
        this.setSelectedDoc = function (docId) {
            docSelect.val(docId);
            docSelect.trigger("chosen:updated");
        };

        this.setOnSelected = function (handler) {
            $(document).on("docDateSelected", handler);
        };


        //-------------------------- helper functions -------------------------------


        function generateEventIfValidState() {
            var selectedDate = dateSelect.pickmeup('get_date', true);
            var selectedDoc =  docSelect.chosen().val();
            var selectedWarehouse = getSelectedWarehouseId();
            if (selectedDoc && selectedWarehouse) {
                $(document).trigger("docDateSelected", [selectedDate, selectedWarehouse, selectedDoc]);
            }
        }

        function getSelectedWarehouseId() {
            if (settings.useWarehouseRole) {
                return $warehouseSelect.chosen().val();
            } else {
                return $warehouseLabel.attr("value");
            }
        }

        function fillDocsWithData(warehouseId) {
            var docs = findWarehouseById(data.warehouses, warehouseId).docs;
            docSelect.empty();
            docSelect.append($("<option>"));
            for (var i = 0; i < docs.length; i++) {
                docSelect.append($("<option>").attr("value", docs[i].docId).text(docs[i].docName));
            }
            docSelect.trigger("chosen:updated");
        }

        function sortByStringCompare(objects, propertyName) {
            function compare(a, b) {
                return a[propertyName] < b[propertyName] ? -1 : a[propertyName] > b[propertyName];
            }
            objects.sort(compare);
        }

        function findWarehouseById(warehouses, id) {
            return $.grep(warehouses, function (e) {
                return e.warehouseId === id;
            })[0];
        }

        return this;
    };

}(jQuery));

