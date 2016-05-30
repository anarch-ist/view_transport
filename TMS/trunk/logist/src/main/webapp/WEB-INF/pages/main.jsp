<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование доков</title>
    <%--styles--%>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/media/custom/mainPage/favicon.ico"/>" >
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/main.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/datePicker/pickmeup.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/chosen_v1.5.1/chosen.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/docAndDateSelector.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/tablePlugin.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Remodal-1.0.7/dist/remodal.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Remodal-1.0.7/dist/remodal-default-theme.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/DataTables-1.10.12/css/jquery.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Select-1.2.0/css/select.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Buttons-1.2.1/css/buttons.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Editor-1.5.6/css/editor.dataTables.min.css"/>"/>
    <%--common scripts--%>
    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/datePicker/jquery.pickmeup.min.js"/>"></script>
    <script src="<c:url value="/media/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>
    <script src="<c:url value="/media/Remodal-1.0.7/dist/remodal.min.js"/>"></script>
    <script src="<c:url value="/media/DataTables-1.10.12/js/jquery.dataTables.min.js"/>"></script>
    <script src="<c:url value="/media/Select-1.2.0/js/dataTables.select.min.js"/>"></script>
    <script src="<c:url value="/media/Buttons-1.2.1/js/dataTables.buttons.js"/>"></script>
    <script src="<c:url value="/media/Editor-1.5.6/js/dataTables.editor.min.js"/>"></script>

    <script src="<c:url value="/media/custom/mainPage/docAndDateSelector.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/tablePlugin.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/tableControlsPlugin.js"/>"></script>


    <%--specific scripts for different user roles--%>
    <c:set var="periodSize" scope="application" value="${initParam.periodSize}"/>
    <c:set var="windowSize" scope="application" value="${initParam.windowSize}"/>
    <c:set var="userRole" scope="session" value="${sessionScope.user.userRole.userRoleId}"/>
    <c:set var="isSupplierManager" scope="page" value="${userRole == 'SUPPLIER_MANAGER'}"/>
    <c:set var="isWarehouseBoss" scope="page" value="${userRole == 'WH_BOSS'}"/>
    <c:set var="isWarehouseDispatcher" scope="page" value="${userRole == 'WH_DISPATCHER'}"/>
    <c:set var="useWarehouseSelect" scope="page" value="${!(isWarehouseBoss || isWarehouseDispatcher)}"/>

    <script>
        $(document).ready(function(){
//            "use strict";

            // ---------------------------------init table plugin----------------------------------------
            var tablePlugin = window.tablePlugin({
                parentId: 'tableContainer',
                windowSize: +<c:out value="${windowSize}"/>,
                periodSize: +<c:out value="${periodSize}"/>,
                <c:if test="${isSupplierManager}">
                selectionConstraint: function(serialNumber, selectedSerialNumbers, isSelected) {
                    if (selectedSerialNumbers.length === 0) {
                        return false;
                    }
                    else if(selectedSerialNumbers.length === 1) {
                        if (isSelected) {
                            return false;
                        } else {
                            var selectedSerialNumber = selectedSerialNumbers[0];
                            if((serialNumber === (selectedSerialNumber - 1)) || (serialNumber === (selectedSerialNumber + 1))){
                                return false;
                            }
                        }
                    }else {
                        var min = selectedSerialNumbers[0];
                        var max = selectedSerialNumbers[selectedSerialNumbers.length - 1];
                        if (isSelected && (serialNumber === min || serialNumber === max)) {
                            return false;
                        }
                        if((serialNumber === (min - 1)) || (serialNumber === (max + 1))){
                            return false;
                        }
                    }
                    return true;
                }
                </c:if>

            });
            tablePlugin.generate();

            //tablePlugin.setString("Panasonic", 2, 3);
            tablePlugin.setOnSelectionChanged(function(e) {
                //window.console.log(e);
                //window.console.log(e.detail);
            });
            //tablePlugin.setDisabled(true);


            // ---------------------------------init docDateSelector plugin----------------------------------------
            var $docAndDateSelector = $('#docAndDateSelector');
            var docDateSelector = $docAndDateSelector.docAndDateSelector({
                useWarehouseSelect:<c:out value="${useWarehouseSelect}"/>,
                data: ${requestScope.docDateSelectorDataObject}
            });
            //docDateSelector.setSelectedDate(new Date(2015, 10, 30));
            //docDateSelector.setSelectedWarehouseAndDoc(1, 3);
            //docDateSelector.setSelectedDoc(3);


            docDateSelector.setOnSelected(function(event, selectionObject) {
                // на время отправки данных и их получения - таблица должна уходить в состояние disabled = true;
                //tablePlugin.setDisabled(true);
                $.ajax({
                    url: "getTableData",
                    method: "POST",
                    data: {selectionObject: JSON.stringify(selectionObject)},
                    dataType: "json"
                }).done(function (tableData) {
                    var periodsArray = tableData.docPeriods;
                    fillTableWithData(periodsArray);
                    //tablePlugin.setDisabled(false);
//                    tableData.periodBegin;
//                    tableData.periodEnd;
                    //console.log(tableData);

                }).fail(function () {
                    window.alert("error");
                    //tablePlugin.setDisabled(false);

                });
            });

            // ---------------------------------init tableControlsPlugin----------------------------------------
            var tableControlsPlugin = window.tableControlsPlugin({
                tablePlugin:tablePlugin,
                parentId:"tableControlsContainer",
                buttons: [
                    <c:if test="${isSupplierManager}">
                    {name:"Добавить комментарий", id :"addSupCommentBtn", enabledIfAnySelected: true},
                    {name:"Отменить", id:"cancelSupOrderBtn", enabledIfAnySelected: true},
                    {name:"Зарезервировать", id:"occupySupPeriodBtn", enabledIfAnySelected: true}
                    </c:if>
                    <c:if test="${isWarehouseBoss}">
                    {name:"Информация о грузе", id :"cargoInfoBtn", enabledIfAnySelected: true},
                    {name:"Открыть интервал", id:"setFreePeriodBtn", enabledIfAnySelected: true},
                    {name:"Закрыть интервал", id:"setDisabledPeriodBtn", enabledIfAnySelected: true}
                    </c:if>
                    <c:if test="${isWarehouseDispatcher}">
                    {name:"Информация о грузе", id :"cargoInfoBtn", enabledIfAnySelected: true},
                    {name:"Изменить статус", id:"changeStatusBtn", enabledIfAnySelected: true}
                    </c:if>
                ]
            });
            tableControlsPlugin.generateContent();
            <c:if test="${isSupplierManager}">
            var occupyPeriodBtnElem = tableControlsPlugin.getButtonByPluginId("occupySupPeriodBtn");

            occupyPeriodBtnElem.onclick = function(e) {
                var selectionObject = docDateSelector.getSelectionObject();
                if (selectionObject === null) {
                    return;
                }
                supplierInputDataDialog.open();

//                $.ajax({
//                    url: "setTableData",
//                    method: "POST",
//                    data: {
//                        selectionObject: JSON.stringify(selectionObject),
//                        period: JSON.stringify(tablePlugin.getSelectedPeriod())
//                    },
//                    dataType: "json"
//                }).done(function (tableData) {
//                    // refresh table
//                    var periodsArray = tableData.docPeriods;
//                    fillTableWithData(periodsArray);
//
//                }).fail(function () {
//                    window.alert("error");
//                    //tablePlugin.setDisabled(false);
//
//                });
            };

            var supplierInputDataDialog = $('[data-remodal-id=modal]').remodal();
            var idGenerator = makeCounter();
            var ordersDataTableEditor = new $.fn.dataTable.Editor( {
                // When editing data the changes only reflected in the DOM, not in any AJAX backend datasource and not localstorage.
                ajax: function (method, url, d, successCallback, errorCallback) {
                    var output = {data: []};

                    if (d.action === 'create') {
                        var addedRow = d.data[Object.keys(d.data)[0]];
                        addedRow.id = "id" + idGenerator(); // get new GUID from custom method
                        console.log(addedRow.id);
                        output.data.push(addedRow);
                    }

                    else if (d.action === 'edit') {
                        var key = Object.keys(d.data)[0];
                        var editedRow = d.data[Object.keys(d.data)[0]];
                        editedRow.id = key;
                        output.data.push(editedRow);
                    }

                    successCallback(output);
                },

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

            $('#ordersDataTable').on('click', 'tbody td:not(:first-child)', function (e) {
                ordersDataTableEditor.inline(this);
            });

            var dataTable = $('#ordersDataTable').DataTable({
                paging:false,
                searching: false,
                dom: 'Bt',
                select: {
                    style:    'os',
                    selector: 'td:first-child'
                },
                buttons: [
                    {
                        text: 'Создать',
                        action: function (e, dt, node, config) {
                            ordersDataTableEditor
                                    .create(false)
                                    //.set("DT_RowId", "id" + idGenerator())
                                    .set("number", "testN")
                                    .set("finalWarehouseDestination", "testW")
                                    .set("boxQty", "testBoxq")
                                    .set("comment", "testComm")
                                    .set("status", "testSt")
                                    .submit();
//                            dataTable.row.add( {
//                                "number":       ,
//                                "finalWarehouseDestination":   "testW",
//                                "":     "",
//                                "": "",
//                                "":     ""
//                            } ).draw( false );
                        }
                    },
                    {
                        extend: 'selectedSingle',
                        className: 'deleteBtn',
                        text: 'удалить',
                        action: function (e, dt, node, config) {
                            //$.showRequestStatusDialog("changeStatusForRequest", dataTable);
                        }
                    }
                ],
                columns: [
//                    {
//                        data: null,
//                        defaultContent: '',
//                        className: 'select-checkbox',
//                        orderable: false
//                    },
                    {"data": "number"},
                    {"data": "finalWarehouseDestination"},
                    {"data": "boxQty"},
                    {"data": "comment"},
                    {"data": "status"}
                ]

//                columnDefs: [
//                    {"name": "number", "orderable": false, "targets": 0},
//                    {"name": "finalWarehouseDestination", "orderable": false, "targets": 1},
//                    {"name": "boxQty", "orderable": false, "targets": 2},
//                    {"name": "comment", "orderable": false, "targets": 3},
//                    {"name": "status", "orderable": false, "targets": 4}
//                ]
            });

            function makeCounter() {
                var counter = 0;
                return function () {
                    return counter++;
                }
            }

            </c:if>
            createBindingBetweenSelectorAndTable();
            $docAndDateSelector.triggerEvents();

            // ---------------------------------init dialogs----------------------------------------






            <%--------------------------------------------FUNCTIONS---------------------------------------------------%>
            function fillTableWithData(periodsArray) {
                tablePlugin.clear();
                periodsArray.forEach(function (period) {
                    if (period.state === "DISABLED") {
                        tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, period.state);
                    } else if (period.state === "OCCUPIED") {
                        tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, period.state, period.supplierName);
                        //period.supplierId;
                    }
                });
            }

            function createBindingBetweenSelectorAndTable() {
                docDateSelector.setOnSelectionAvailable(function(event, isSelectionAvailable) {
                    tablePlugin.setDisabled(!isSelectionAvailable);
                });
            }


        });
    </script>
    <%--<c:choose>--%>
        <%--<c:when test="${userRole == 'SUPPLIER_MANAGER'}">--%>

        <%--</c:when>--%>
        <%--<c:when test="${userRole == 'WH_BOSS'}">--%>
            <%--<script src="<c:url value="/media/custom/mainPage/roleWarehouseBoss.js"/>"></script>--%>
        <%--</c:when>--%>
        <%--<c:when test="${userRole == 'WH_DISPATCHER'}">--%>
            <%--<script src="<c:url value="/media/custom/mainPage/roleWarehouseDispatcher.js"/>"></script>--%>
        <%--</c:when>--%>
    <%--</c:choose>--%>
</head>

<body>

<div id="userPane">
    <form action="logout" method="post">
        <input id="exit" type="submit" value="выйти"/>
    </form>
    <table>
        <tr>
            <td>имя</td><td><c:out value="${sessionScope.user.userName}"/></td>
        </tr>
        <tr>
            <td>роль</td><td><c:out value="${userRole}"/></td>
        </tr>
        <tr>
            <td>должность</td><td><c:out value="${sessionScope.user.position}"/></td>
        </tr>
    </table>
</div>

<div id="docsPane">
    <div id="docAndDateSelector"></div>
    <div id="tableContainer"></div>
    <div id="tableControlsContainer"></div>

    <%--dialogs--%>
    <c:if test="${isSupplierManager}">
        <div data-remodal-id="modal">
            <button data-remodal-action="close" class="remodal-close"></button>
            <h1>Ввод данных</h1>
            <div id="routeListDataContainer">
                <table>
                    <tr>
                        <td><label for="driverNameDiv">ФИО водителя:</label></td>
                        <td><input type="text" id="driverNameDiv"/></td>
                    </tr>
                    <tr>
                        <td><label for="licensePlateDiv">№ транспортного средства:</label></td>
                        <td><input type="text" id="licensePlateDiv"/></td>
                    </tr>
                    <tr>
                        <td><label for="palletsQty">Количество паллет:</label></td>
                        <td><input type="text" id="palletsQty"/></td>
                    </tr>
                    <tr>
                        <td><label for="driverPhoneNumberDiv">Телефон водителя</label></td>
                        <td><input type="text" id="driverPhoneNumberDiv"/></td>
                    </tr>
                </table>
            </div>
            <div id="ordersDataContainer">
                <table id="ordersDataTable" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>Конечный склад доставки</th>
                        <th>Количество коробок</th>
                        <th>Комментарий</th>
                        <th>Статус</th>
                    </tr>
                    </thead>
                </table>
            </div>

        </div>
    </c:if>

</div>



</body>
</html>
