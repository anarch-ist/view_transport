$(document).ready(function() {
    "use strict";

    var $fp = $(".filthypillow-1"),
        now = moment().subtract("seconds", 1);
    $fp.filthypillow({
        minDateTime: function () {
            return now;
        }
    });
    $fp.on("focus", function () {
        $fp.filthypillow("show");
    });
    $fp.on("fp:save", function (e, dateObj) {
        $fp.val(dateObj.format("MMM DD YYYY hh:mm A"));
        $fp.filthypillow("hide");
    });


    $(function () {
        $('input.datetime').datetime({
            userLang: 'ru'
        });
    });


});