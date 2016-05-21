(function ( $ ) {
    "use strict";
    //https://harvesthq.github.io/chosen/options.html
    //https://harvesthq.github.io/chosen/
    //https://github.com/nazar-pc/PickMeUp
    $.fn.docAndDateSelector = function( options ) {

        // This is the easiest way to have default options.
        var settings = $.extend({
            // These are the defaults.
            color: "#556b2f",
            backgroundColor: "white"
        }, options );

        var exampleData = {
            "warehouses": [
                {
                    "warehouseId": 1,
                    "warehouseName": "msk_warehouse",
                    "rusTimeZoneAbbr": "MSK",
                    "docs": [
                        {
                            "docId": 1,
                            "docName": "msk_warehouse_doc1"
                        },
                        {
                            "docId": 2,
                            "docName": "msk_warehouse_doc2"
                        },
                        {
                            "docId": 3,
                            "docName": "msk_warehouse_doc3"
                        }
                    ]
                },
                {
                    "warehouseId": 2,
                    "warehouseName": "ekt_warehouse",
                    "rusTimeZoneAbbr": "YEKT",
                    "docs": [
                        {
                            "docId": 4,
                            "docName": "ekt_warehouse_doc1"
                        },
                        {
                            "docId": 5,
                            "docName": "ekt_warehouse_doc2"
                        },
                        {
                            "docId": 6,
                            "docName": "ekt_warehouse_doc3"
                        }
                    ]
                }
            ],
            "timeZones": {
                "VLAT": 10.0,
                "EET": 2.0,
                "YEKT": 5.0,
                "PETT": 12.0,
                "SAMT": 4.0,
                "IRKT": 8.0,
                "MAGT": 10.0,
                "OMST": 6.0,
                "MSK": 3.0,
                "KRAT": 7.0,
                "YAKT": 9.0
            }
        };

        // sort all data
        sortByStringCompare(exampleData.warehouses, "warehouseName");
        exampleData.warehouses.forEach(function(warehouse) {
            sortByStringCompare(warehouse.docs, "docName");
        });

        // clean all inside container
        this.empty();

        this.append(
            "<div id='datePicker'><input type='text'></div>" +
            "<div id='warehousePicker'><select style='width:350px;'></select></div>" +
            "<div id='docPicker'><select style='width:350px;'></select></div>"
        );
        var docSelect = $("#docPicker").find("select");
        docSelect.chosen({});
        docSelect.on('change', function(evt, params) {
            $(document).trigger( "docDateSelected", [ "bim", "baz" ] );
        });

        var warehouseSelect = $("#warehousePicker").find("select");
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
            change: function(formattedDate) {
                $(document).trigger( "docDateSelected", [ "bim", "baz" ] );
            }
        });
        dateSelect.pickmeup('set_date', new Date());


        // add data to warehouse select
        var warehouses = exampleData.warehouses;
        for (var i = 0; i < warehouses.length; i++) {
            warehouseSelect.append($("<option>").attr("value", warehouses[i].warehouseId).text(warehouses[i].warehouseName));
        }
        warehouseSelect.chosen({});
        warehouseSelect.on('change', function(evt, params) {
            // fill docs with data
            var warehouseId = +params.selected;
            var docs = findWarehouseById(warehouses, warehouseId).docs;
            docSelect.empty();
            for (var i = 0; i < docs.length; i++) {
                docSelect.append($("<option>").attr("value", docs[i].docId).text(docs[i].docName));
            }
            docSelect.trigger("chosen:updated");
        });


        this.setSelectedDate = function(date) {
            dateSelect.pickmeup('set_date', date);
        };

        // TODO
        this.setSelectedWarehouse = function(warehouseId) {

        };

        // TODO
        this.setSelectedDoc = function(docId) {

        };

        this.setWarehousePicker = function(warehousePicker) {

        };

        this.setOnSelected = function(handler) {
            handler();
        };


        //-------------------------- helper functions -------------------------------
        function sortByStringCompare(objects, propertyName) {
            function compare(a, b) {
                return a[propertyName] < b[propertyName] ? -1 : a[propertyName] > b[propertyName];
            }
            objects.sort(compare);
        }

        function findWarehouseById(warehouses, id) {
            return $.grep(warehouses, function(e){ return e.warehouseId === id; })[0];
        }

        return this;
    };

}( jQuery ));

