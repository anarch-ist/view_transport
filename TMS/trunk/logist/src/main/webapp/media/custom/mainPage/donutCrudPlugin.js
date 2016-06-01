(function ($) {
    "use strict";

    $.fn.donutCrudPlugin = function (options) {

        var settings = $.extend({
            // big save button exist or not if false
            isEditable: true,
            ordersCrud: "all", // all, update, read
            editableFields: {
                donutFields: ["period", "driver", "licensePlate", "palletsQty", "driverPhoneNumber", "commentForDonut"],
                ordersFields: ["orderNumber", "finalDestinationWarehouseId", "boxQty", "commentForStatus", "orderStatusId"]
            },
            orderStatuses: {"EXAMPLE_ORDER_ID": "EXAMPLE_ORDER_V"},
            warehouses: {1: "EXAMPLE_WH"},
            onSubmit: function() {}
        }, options);


        var pluginsSettings = processSettings();
        var createInline = pluginsSettings.createInline;
        var resultCssPartClick = pluginsSettings.resultCssPartClick;
        var resultCssPartKeys = pluginsSettings.resultCssPartKeys;
        var _this = this;


        var donutFields = generateContent();


        //create and configure table

        var ordersDataTableEditor = new $.fn.dataTable.Editor( {
            // When editing data the changes only reflected in the DOM, not in any AJAX backend datasource and not localstorage.
            ajax: function (method, url, data, successCallback, errorCallback) {
                var output = {data: []};

                if (data.action === 'create') {
                    var addedRow = data.data[Object.keys(data.data)[0]];
                    addedRow.orderId = getId();
                    output.data.push(addedRow);
                }

                else if (data.action === 'edit') {
                    var key = Object.keys(data.data)[0];
                    var editedRow = data.data[Object.keys(data.data)[0]];
                    editedRow.orderId = key;
                    output.data.push(editedRow);
                }

                successCallback(output);
            },
            idSrc: "orderId",
            table: "#ordersDataTable",
            fields: [ {
                label: "Номер заявки:",
                name: "orderNumber"
            }, {
                label: "Конечный склад доставки:",
                name: "finalDestinationWarehouseId",
                type: "chosen",
                options: pluginsSettings.warehouseSelectPairs,
                opts: {
                    inherit_select_classes: true
                }
            }, {
                label: "Количество коробок:",
                name: "boxQty",
                type: "mask",
                mask: "##"
            }, {
                label: "Комментарий:",
                name: "commentForStatus"
            }, {
                label: "Статус:",
                name: "orderStatusId",
                type: "chosen",
                options: pluginsSettings.orderStatusSelectPairs,
                opts: {
                    disable_search: true,
                    inherit_select_classes: true
                }
            }
            ]
        } );

        // generate dataTable
        var buttons = [];
        if (settings.ordersCrud === "all") {
            buttons.push({
                text: 'Создать',
                action: function (e, dt, node, config) {
                    ordersDataTableEditor.submit();
                    createRow("", "", Object.keys(settings.warehouses)[0], "", "", Object.keys(settings.orderStatuses)[0]);
                }
            });
        }
        if (settings.ordersCrud === "all") {
            buttons.push({
                extend: 'selected',
                className: 'deleteBtn',
                text: 'Удалить',
                action: function (e, dt, node, config) {
                    ordersDataTableEditor
                        .remove('.selected', false)
                        .submit();
                }
            });
        }
        var $ordersDataTable = $('#ordersDataTable');
        var dataTable = $ordersDataTable.DataTable({
            paging:false,
            searching: false,
            dom: 'Bt',
            idSrc: "orderId",
            select: {
                style:    'os',
                selector: 'td:first-child'
            },
            keys: {
                columns: resultCssPartKeys,
                keys: [9]
            },
            buttons: buttons,
            columns: [
                {
                    data: null,
                    defaultContent: '',
                    className: 'select-checkbox',
                    orderable: false
                },
                {"data": "orderNumber"},
                {"data": "finalDestinationWarehouseId"},
                {"data": "boxQty"},
                {"data": "commentForStatus"},
                {"data": "orderStatusId"}
            ],
            columnDefs: [
                {"name": "orderNumber", "orderable": false, "targets": 1},
                {"name": "finalDestinationWarehouseId", "orderable": false, "targets": 2,
                    render: function (data, type, full, meta) {
                        return type === 'display' ? settings.warehouses[data]:data;
                    }
                },
                {"name": "boxQty", "orderable": false, "targets": 3},
                {"name": "commentForStatus", "orderable": false, "targets": 4},
                {"name": "orderStatusId", "orderable": false, "targets": 5,
                    render: function (data, type, full, meta) {
                        return type === 'display' ? settings.orderStatuses[data]:data;
                    }
                }
            ]
        });

        // create inline editing
        if (createInline) {
            dataTable.on('key-focus', function (e, datatable, cell) {
                ordersDataTableEditor.submit();
                ordersDataTableEditor.inline(cell.index(), {
                    onBlur: 'submit',
                    onReturn: 'submit',
                    submit: 'all'
                });
            });

            $ordersDataTable.on('click', 'tbody td' + resultCssPartClick, function () {
                if (dataTable.rows().count() !== 0) {
                    ordersDataTableEditor.inline(this, {
                        onBlur: 'submit',
                        onReturn: 'submit',
                        submit: 'all'
                    });
                }
            });
        }

        if (settings.isEditable) {
            var submitBtn = $("<button>").text("Отправить");
            submitBtn.on("click", function(e) {
                settings.onSubmit();
            });
            _this.append(submitBtn);
        }

        // --------------------------- METHODS --------------------------------

        this.setPeriod = function(periodString){
            donutFields.periodInput.val(periodString);
        };

        this.setData = function(data) {
            donutFields.companyNameDiv.text(data.supplierName);
            this.setPeriod(data.period);
            donutFields.driverNameInput.val(data.driver);
            donutFields.licensePlateInput.val(data.licensePlate);
            donutFields.palletQtyInput.val(data.palletsQty);
            donutFields.driverPhoneNumberInput.val(data.driverPhoneNumber);
            donutFields.commentArea.val(data.commentForDonut);
            ordersDataTableEditor.remove('tr');
            data.orders.forEach(function(order) {
                createRow(order.orderId, order.orderNumber, order.finalDestinationWarehouseId, order.boxQty, order.commentForStatus, order.orderStatusId);
            });

        };

        this.getData = function() {
            var result = {};
            result.period = donutFields.periodInput.val();
            result.commentForDonut = donutFields.commentArea.val();
            result.supplierName = donutFields.companyNameDiv.text();
            result.driver = donutFields.driverNameInput.val();
            result.licensePlate = donutFields.licensePlateInput.val();
            result.palletsQty = +donutFields.palletQtyInput.val();
            result.driverPhoneNumber = donutFields.driverPhoneNumberInput.val();
            var orders = [];
            dataTable.data().each(function(row) {
                var copy = $.extend({}, row);
                copy.boxQty = +copy.boxQty;
                copy.finalDestinationWarehouseId = +copy.finalDestinationWarehouseId;
                if (copy.orderId.lastIndexOf("virtualId", 0) === 0) {
                    copy.orderId = null;
                }
                orders.push(copy);
            });
            result.orders = orders;
            return result;
        };

        // --------------------------- FUNCTIONS --------------------------------
        var idGenerator = makeCounter();
        var currentId;
        function getId() {
            if (currentId) {
                return currentId;
            } else {
                return "virtualId" + idGenerator(); // get new GUID from custom method
            }
        }
        function createRow(orderId, orderNumber, finalWarehouseDestinationId, boxQty, commentForStatus, statusId) {
            currentId = orderId;
            ordersDataTableEditor
                .create(false)
                .set("orderNumber", orderNumber)
                .set("finalDestinationWarehouseId", finalWarehouseDestinationId)
                .set("boxQty", boxQty)
                .set("commentForStatus", commentForStatus)
                .set("orderStatusId", statusId)
                .submit();
        }

        function makeCounter() {
            var counter = 0;
            return function () {
                return counter++;
            };
        }

        function setReadOnlyIfOption(donutOptionName, $donutInput) {
            if (settings.editableFields.donutFields.indexOf(donutOptionName) === -1) {
                $donutInput.attr("readonly", "readonly");
            }
        }

        function processSettings() {

            if (!settings.isEditable) {
                settings.ordersCrud = "read";
                settings.editableFields.ordersFields = [];
                settings.editableFields.donutFields = [];
            }

            var editableOrdersFieldsNotEmpty = settings.editableFields.ordersFields.length !== 0;
            var createInline = (settings.ordersCrud === "update" || settings.ordersCrud === "all" || editableOrdersFieldsNotEmpty);

            var resultCssPartClick = "";
            var resultCssPartKeys = "";
            if (editableOrdersFieldsNotEmpty) {
                var ordersFieldsNthNumbers = {
                    "orderNumber": 2,
                    "finalDestinationWarehouseId": 3,
                    "boxQty": 4,
                    "commentForStatus": 5,
                    "orderStatusId": 6
                };

                if (Object.keys(ordersFieldsNthNumbers).length === settings.editableFields.ordersFields.length) {
                    resultCssPartClick = ":not(:first-child)";
                } else {
                    for (var prName in ordersFieldsNthNumbers) {
                        if (!ordersFieldsNthNumbers.hasOwnProperty(prName)) {
                            continue;
                        }
                        var nthNumber = ordersFieldsNthNumbers[prName];
                        if (settings.editableFields.ordersFields.indexOf(prName) < 0) {
                            resultCssPartClick += ":nth-child(" + nthNumber + "), ";
                        }
                    }
                    resultCssPartClick = resultCssPartClick.slice(0, resultCssPartClick.length - 2);
                    resultCssPartClick = ":not(:first-child, " + resultCssPartClick + ")";
                }


                settings.editableFields.ordersFields.forEach(function (orderField) {
                    var ordersFieldsNthNumber = ordersFieldsNthNumbers[orderField];
                    if (ordersFieldsNthNumber) {
                        resultCssPartKeys += ":nth-child(" + ordersFieldsNthNumber + "), ";
                    }
                });
                resultCssPartKeys = resultCssPartKeys.slice(0, resultCssPartKeys.length - 2);
            }

            var orderStatusSelectPairs = [];
            for(var orderStatusId in settings.orderStatuses) {
                if (!settings.orderStatuses.hasOwnProperty(orderStatusId)) {
                    continue;
                }
                var orderStatusName = settings.orderStatuses[orderStatusId];
                orderStatusSelectPairs.push({label:orderStatusName, value:orderStatusId});
            }


            var warehouseSelectPairs = [];
            for(var warehouseId in settings.warehouses) {
                if (!settings.warehouses.hasOwnProperty(warehouseId)) {
                    continue;
                }
                var warehouseName = settings.warehouses[warehouseId];
                warehouseSelectPairs.push({label:warehouseName, value:+warehouseId});
            }

            return {
                createInline: createInline,
                resultCssPartClick: resultCssPartClick,
                resultCssPartKeys: resultCssPartKeys,
                orderStatusSelectPairs: orderStatusSelectPairs,
                warehouseSelectPairs: warehouseSelectPairs
            };
        }

        function generateContent() {
            _this.empty();

            var $routeListTable = $("<table>").attr("id", "routeListTable");

            var $companyTr = $("<tr>");
            var $companyDiv = $("<td>");
            var $companyLabel = $("<label>").text("Поставщик");
            var $company = $("<td>");
            var $companyName = $("<div>");

            var $periodTr = $("<tr>");
            var $periodDiv = $("<td>");
            var $periodLabel = $("<label>").text("Интервал");
            var $period = $("<td>");
            var $periodInput = $("<input>");
            setReadOnlyIfOption("period", $periodInput);

            var $driverTr = $("<tr>");
            var $driverNameDiv = $("<td>");
            var $driverNameLabel = $("<label>").text("ФИО водителя");
            var $driverName = $("<td>");
            var $driverNameInput = $("<input>");
            setReadOnlyIfOption("driver", $driverNameInput);

            var $licenseTr = $("<tr>");
            var $licensePlateDiv = $("<td>");
            var $licensePlateLabel = $("<label>").text("№ транспортного средства");
            var $licensePlate = $("<td>");
            var $licensePlateInput = $("<input>");
            setReadOnlyIfOption("licensePlate", $licensePlateInput);

            var $palletTr = $("<tr>");
            var $palletQtyDiv = $("<td>");
            var $palletQtyLabel = $("<label>").text("Количество паллет");
            var $palletQty = $("<td>");
            var $palletQtyInput = $("<input>").attr("type", "number").attr("min", "0").attr("max", "99").attr("step", "1");
            setReadOnlyIfOption("palletsQty", $palletQtyInput);

            var $driverPhoneTr = $("<tr>");
            var $driverPhoneDiv = $("<td>");
            var $driverPhoneLabel = $("<label>").text("Телефон водителя");
            var $driverPhone = $("<td>");
            var $driverPhoneInput = $("<input>");
            setReadOnlyIfOption("driverPhoneNumber", $driverPhoneInput);

            var $commentTr = $("<tr>");
            var $commentDiv = $("<td>");
            var $commentLabel = $("<label>").text("Комментарий");
            var $comment = $("<td>");
            var $commentArea = $("<textarea>");
            $commentArea.css("resize","none");
            setReadOnlyIfOption("commentForDonut", $commentArea);

            _this.append(
                $routeListTable.append(
                    $companyTr.append($companyDiv.append($companyLabel), $company.append($companyName)),
                    $periodTr.append($periodDiv.append($periodLabel), $period.append($periodInput)),
                    $driverTr.append($driverNameDiv.append($driverNameLabel), $driverName.append($driverNameInput)),
                    $licenseTr.append($licensePlateDiv.append($licensePlateLabel), $licensePlate.append($licensePlateInput)),
                    $palletTr.append($palletQtyDiv.append($palletQtyLabel), $palletQty.append($palletQtyInput)),
                    $driverPhoneTr.append($driverPhoneDiv.append($driverPhoneLabel), $driverPhone.append($driverPhoneInput)),
                    $commentTr.append($commentDiv.append($commentLabel), $comment.append($commentArea))
                )
            );

            var $ordersTable = $('<table cellpadding="0" cellspacing="0" border="0" class="display" width="100%" id="ordersDataTable">');

            var $thead = $("<thead>");
            var $orderTr = $("<tr>");
            var $selectTh = $("<th>");
            var $numberTh = $("<th>").text("№");
            var $finalDestinationTh = $("<th>").text("Конечный склад доставки");
            var $palletQtyTh = $("<th>").text("Количество коробок");
            var $commentTh = $("<th>").text("Комментарий");
            var $statusTh = $("<th>").text("Статус");

            _this.append(
                $ordersTable.append(
                    $thead.append(
                        $orderTr.append(
                            $selectTh, $numberTh, $finalDestinationTh, $palletQtyTh, $commentTh, $statusTh
                        )
                    )
                )
            );

            return {
                companyNameDiv: $companyName,
                periodInput: $periodInput,
                driverNameInput: $driverNameInput,
                licensePlateInput: $licensePlateInput,
                palletQtyInput: $palletQtyInput,
                driverPhoneNumberInput: $driverPhoneInput,
                commentArea: $commentArea
            };
        }

        return this;
    };

}(jQuery));

