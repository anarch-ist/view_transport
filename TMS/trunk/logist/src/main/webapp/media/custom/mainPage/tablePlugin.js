(function() {
    "use strict";
    var DEFAULT_PARAMETERS = {
        parentId: '',
        periodLength: 30,
        windowSize: 60 * 24
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

    // TODO
    function findTableSizes(periodsCount) {

    }




    main.defaultParameters = DEFAULT_PARAMETERS;

    main.generate = function(){
        var elem = document.getElementById(startupParameters.parentId);
        var tableElement = document.createElement("table");
        elem.appendChild(tableElement);
    };



    window.tablePlugin = main;

})();
