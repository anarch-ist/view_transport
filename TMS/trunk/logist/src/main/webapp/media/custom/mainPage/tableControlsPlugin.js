(function () {
    "use strict";
    var DEFAULT_PARAMETERS = {
        tablePlugin: null,
        parentId: null,
        buttons: null,
        bindings: null
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
    var containerElem;
    main.generateContent = function() {
        checkSettings();

        var buttons = startupParameters.buttons;
        containerElem = document.getElementById(startupParameters.parentId);

        for (var i = 0; i < buttons.length; i++) {
            var buttonElem = document.createElement('input');
            buttonElem.setAttribute('type', 'button');
            buttonElem.setAttribute('value', buttons[i].name);
            buttonElem.setAttribute('id', buttons[i].id);
            buttonElem.id = buttons[i].id;

            startupParameters.tablePlugin.setOnDisableChanged(
                (function(innerButtonElem) {
                    return function (e) {
                        innerButtonElem.disabled = e.detail;
                    };
                }(buttonElem))
            );

            if (buttons[i].enabledIfAnySelected) {
                buttonElem.enabledIfAnySelected = buttons[i].enabledIfAnySelected;
                buttonElem.disabled = !startupParameters.tablePlugin.isAnySelected();
                startupParameters.tablePlugin.setOnAnySelected(
                    (function(innerButtonElem) {
                        return function (e) {
                            innerButtonElem.disabled = !e.detail;
                        };
                    }(buttonElem))
                );
            }

            containerElem.appendChild(buttonElem);
        }
    };

    main.getButtonByPluginId = function(pluginId) {
        return document.getElementById(pluginId);
    };

    main.setDisabled = function(disabled) {
        var elementsByTagName = document.getElementById(startupParameters.parentId).getElementsByTagName("input");
        [].forEach.call(elementsByTagName, function(element) {
            element.disabled = disabled;
        });
    };

    // ----------------------------------HELPER FUNCTIONS------------------------------------------
    function checkSettings() {
        if (startupParameters.tablePlugin === null) {
            throw new Error("table plugin must be set as parameter");
        }
        if (startupParameters.parentId === null) {
            throw new Error("parentId must be set as parameter");
        }
    }

    window.tableControlsPlugin = main;

})();
