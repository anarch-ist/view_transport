(function ( $ ) {
    "use strict";

    $.fn.docAndDateSelector = function( options ) {

        // This is the easiest way to have default options.
        var settings = $.extend({
            // These are the defaults.
            color: "#556b2f",
            backgroundColor: "white"
        }, options );

        // clean all inside container
        this.empty();

        this.append("<div id='datePicker'>fffffff</div> <div id='warehousePicker'></div> <div id='docPicker'></div>");
        var pickmeup = $("#datePicker").pickmeup({
            locale: {
                days: ["Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"],
                daysShort: ["Вск", "Пнд", "Втр", "Срд", "Чтв", "Птн", "Сбт", "Вск"],
                daysMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"],
                months: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
                monthsShort: ["Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"]
            },
            change: function(formattedDate) {
                window.console.log(formattedDate);
            }
        });

        this.testMethod = function() {

        };

        this.setWarehousePicker = function() {

        };

        this.setOnSelected = function(handler) {
            handler();
        };




        //
        return this;
    };

}( jQuery ));
