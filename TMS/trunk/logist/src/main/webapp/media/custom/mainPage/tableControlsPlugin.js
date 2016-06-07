(function () {
    "use strict";

    var DEFAULT_PARAMETERS = {
        tablePlugin: null,
        parentId: null,
        buttons: null
    };
    var startupParameters = {};
    var state = {isDisabled:null, isAnySelected:null, currentSelectedState:null, isFullPeriodSelected:null};
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

        var buttonsDescription = startupParameters.buttons;
        var buttons = [];
        containerElem = document.getElementById(startupParameters.parentId);

        startupParameters.tablePlugin.setOnCreated(function() {
            state.isDisabled = startupParameters.tablePlugin.isDisabled();
            state.isAnySelected = startupParameters.tablePlugin.isDisabled()
        });

        startupParameters.tablePlugin.setOnDisableChanged(function(e) {
            state.isDisabled = e.detail;
            buttons.forEach(function(buttonElem){
                handleState(buttonElem, state);
            });
        });

        startupParameters.tablePlugin.setOnAnySelected(function(e) {
            state.isAnySelected = e.detail;
            buttons.forEach(function(buttonElem){
                handleState(buttonElem, state);
            });
        });

        startupParameters.tablePlugin.setOnSelectionChanged(function(e) {
            state.isFullPeriodSelected = null; //TODO
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
