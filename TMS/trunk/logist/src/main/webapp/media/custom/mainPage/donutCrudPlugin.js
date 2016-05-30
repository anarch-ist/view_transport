(function ($) {
    "use strict";

    $.fn.donutCrudPlugin = function (options) {

        var settings = $.extend({
            // big save button exist or not if false
            isEditable: true,
            ordersCrud: ["C", "U", "D"],
            editableFields: {
                ordersFields: ["orderNumber", "finalDestinationWarehouse", "boxQty", "commentForStatus", "orderStatus"],
                donutFields: ["driver", "licensePlate", "palletsQty", "driverPhoneNumber", "comment", "period", "supplier"]
            }
        }, options);

        // generate content
        {
            this.empty();

            var $routeListTable = $("<table>").attr("id", "routeListTable");

            var $periodTr = $("<tr>");
            var $periodDiv = $("<td>");
            var $periodLabel = $("<label>").text("Интервал");
            var $period = $("<td>");
            var $periodInput = $("<input>");

            var $companyTr = $("<tr>");
            var $companyDiv = $("<td>");
            var $companyLabel = $("<label>").text("Поставщик");
            var $company = $("<td>");
            var $companyInput = $("<input>");

            var $driverTr = $("<tr>");
            var $driverNameDiv = $("<td>");
            var $driverNameLabel = $("<label>").text("ФИО водителя:");
            var $driverName = $("<td>");
            var $driverNameInput = $("<input>");

            var $licenseTr = $("<tr>");
            var $licensePlateDiv = $("<td>");
            var $licensePlateLabel = $("<label>").text("№ транспортного средства:");
            var $licensePlate = $("<td>");
            var $licensePlateInput = $("<input>");

            var $palletTr = $("<tr>");
            var $palletQtyDiv = $("<td>");
            var $palletQtyLabel = $("<label>").text("Количество паллет:");
            var $palletQty = $("<td>");
            var $palletQtyInput = $("<input>");

            var $driverPhoneTr = $("<tr>");
            var $driverPhoneDiv = $("<td>");
            var $driverPhoneLabel = $("<label>").text("Телефон водителя:");
            var $driverPhone = $("<td>");
            var $driverPhoneInput = $("<input>");

            var $commentTr = $("<tr>");
            var $commentDiv = $("<td>");
            var $commentLabel = $("<label>").text("Комментарий:");
            var $comment = $("<td>");
            var $commentArea = $("<textarea>");

            this.append(
                $routeListTable.append(
                    $periodTr.append($periodDiv.append($periodLabel), $period.append($periodInput)),
                    $companyTr.append($companyDiv.append($companyLabel), $company.append($companyInput)),
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

            this.append(
                $ordersTable.append(
                    $thead.append(
                        $orderTr.append(
                            $selectTh, $numberTh, $finalDestinationTh, $palletQtyTh, $commentTh, $statusTh
                        )
                    )
                )
            );

        }



        var idGenerator = makeCounter();
        var ordersDataTableEditor = new $.fn.dataTable.Editor( {
            // When editing data the changes only reflected in the DOM, not in any AJAX backend datasource and not localstorage.
            ajax: function (method, url, data, successCallback, errorCallback) {
                var output = {data: []};

                if (data.action === 'create') {
                    var addedRow = data.data[Object.keys(data.data)[0]];
                    addedRow.orderId = "id" + idGenerator(); // get new GUID from custom method
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
                name: "number"
            }, {
                label: "Конечный склад доставки:",
                name: "finalWarehouseDestination"
            }, {
                label: "Количество коробок:",
                name: "boxQty"
            }, {
                label: "Комментарий:",
                name: "comment"
            }, {
                label: "Статус:",
                name: "status"
            }
            ]
        } );


        // generate dataTable
        var dataTable = $('#ordersDataTable').DataTable({
            paging:false,
            searching: false,
            dom: 'Bt',
            idSrc: "orderId",
            select: {
                style:    'os',
                selector: 'td:first-child'
            },
            keys: {
                columns: ':not(:first-child)',
                keys: [ 9 ]
            },
            buttons: [
                {
                    text: 'Создать',
                    action: function (e, dt, node, config) {
                        ordersDataTableEditor.submit();
                        ordersDataTableEditor
                            .create(false)
                            .set("number", "")
                            .set("finalWarehouseDestination", "")
                            .set("boxQty", "")
                            .set("comment", "")
                            .set("status", "S")
                            .submit();
                    }
                },
                {
                    extend: 'selected',
                    className: 'deleteBtn',
                    text: 'Удалить',
                    action: function (e, dt, node, config) {
                        ordersDataTableEditor
                            .remove('.selected', false)
                            .submit();
                    }
                }
            ],
            columns: [
                {
                    "data": null,
                    defaultContent: '',
                    className: 'select-checkbox',
                    orderable: false
                },
                {"data": "number"},
                {"data": "finalWarehouseDestination"},
                {"data": "boxQty"},
                {"data": "comment"},
                {"data": "status"}
            ],
            columnDefs: [
                {"name": "number", "orderable": false, "targets": 0},
                {"name": "finalWarehouseDestination", "orderable": false, "targets": 1},
                {"name": "boxQty", "orderable": false, "targets": 2},
                {"name": "comment", "orderable": false, "targets": 3},
                {"name": "status", "orderable": false, "targets": 4}
            ]
        });

        dataTable.on( 'key-focus', function ( e, datatable, cell ) {
            ordersDataTableEditor.submit();
            ordersDataTableEditor.inline(cell.index(), {
                onBlur: 'submit',
                onReturn: 'submit',
                submit: 'all'
            });
        } );

        $('#ordersDataTable').on('click', 'tbody td', function () {
            ordersDataTableEditor.inline(this, {
                onBlur: 'submit',
                onReturn: 'submit',
                submit: 'all'
            });
        });
        ordersDataTableEditor.on("submitComplete", function(e, json, data) {
            window.console.log(e);
        });
        // --------------------------- FUNCTIONS --------------------------------
        function makeCounter() {
            var counter = 0;
            return function () {
                return counter++;
            };
        }

        return this;
    };

}(jQuery));

