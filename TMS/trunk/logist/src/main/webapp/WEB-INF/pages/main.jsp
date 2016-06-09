<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование доков</title>
    <%--styles--%>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/media/custom/mainPage/favicon.ico"/>" >

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/datePicker/pickmeup.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Remodal-1.0.7/dist/remodal.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Remodal-1.0.7/dist/remodal-default-theme.css"/>" />

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/DataTables-1.10.12/css/jquery.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Select-1.2.0/css/select.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Buttons-1.2.1/css/buttons.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Editor-1.5.6/css/editor.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/chosen_v1.5.1/chosen.css"/>">

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/docAndDateSelector.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/tablePlugin2.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/donutCrudPlugin.css"/>">

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/main.css"/>">

    <%--common scripts--%>
    <script src="<c:url value="/media/es6-shim.min.js"/>"></script>
    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/datePicker/jquery.pickmeup.min.js"/>"></script>
    <script src="<c:url value="/media/Remodal-1.0.7/dist/remodal.min.js"/>"></script>

    <script src="<c:url value="/media/DataTables-1.10.12/js/jquery.dataTables.min.js"/>"></script>
    <script src="<c:url value="/media/Select-1.2.0/js/dataTables.select.min.js"/>"></script>
    <script src="<c:url value="/media/Buttons-1.2.1/js/dataTables.buttons.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/dataTables.custom_editor.js"/>"></script>
    <script src="<c:url value="/media/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>
    <script src="<c:url value="/media/editor.chosen.js"/>"></script>
    <script src="<c:url value="/media/jquery.mask-1.7.7/jquery.mask.min.js"/>"></script>
    <script src="<c:url value="/media/editor.mask.js"/>"></script>
    <script src="<c:url value="/media/dataTables.keyTable.min.js"/>"></script>

    <script src="<c:url value="/media/custom/mainPage/docAndDateSelector.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/tablePlugin2.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/donutCrudPlugin.js"/>"></script>


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
        "use strict";

            // ---------------------------------init table plugin----------------------------------------
            var tablePlugin = window.tablePlugin2({
                parentId: 'tableContainer',
                windowSize: +<c:out value="${windowSize}"/>,
                cellSize: +<c:out value="${periodSize}"/>,
                allowedStatesForSelection: {
                    isOpenedAllowed: <c:out value="${isSupplierManager || isWarehouseBoss}"/>,
                    isClosedAllowed: <c:out value="${isWarehouseBoss}"/>,
                    isOccupiedAllowed: true
                },
                selectionModel: {
                    isSelectAllOccupied: true,
                    isSelectAllClosed: false
                },

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
                },
                buttons: [
                    {
                        name: "отменить",
                        id: "sDeleteBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return (state === "OCCUPIED" && isFullPeriodSelected);
                        }
                    },
                    {
                        name: "изменить",
                        id: "sUpdateBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return (state === "OCCUPIED" && isFullPeriodSelected);
                        }
                    },
                    {
                        name: "зарезервировать",
                        id: "sInsertBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OPENED";
                        }
                    }
                ]
                </c:if>

                <c:if test="${isWarehouseDispatcher}">
                buttons: [
                    {
                        name: "изменить статусы",
                        id: "dChangeStatusBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return (state === "OCCUPIED" && isFullPeriodSelected);
                        }
                    }
                ]
                </c:if>

                <c:if test="${isWarehouseBoss}">
                buttons: [
                    {
                        name: "открыть",
                        id: "bOpenPeriodBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "CLOSED";
                        }
                    },
                    {
                        name: "закрыть",
                        id: "bClosePeriodBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OPENED";
                        }
                    },
                    {
                        name: "отменить",
                        id: "bCancelBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED" && isFullPeriodSelected;
                        }
                    },
                    {
                        name: "информация",
                        id: "bInfoBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED" && isFullPeriodSelected;
                        }
                    }
                ]
                </c:if>
            });
            tablePlugin.generate();

            var warehousesData = ${requestScope.docDateSelectorDataObject};

            // ---------------------------------init docDateSelector plugin----------------------------------------
            var $docAndDateSelector = $('#docAndDateSelector');
            var docDateSelector = $docAndDateSelector.docAndDateSelector({
                useWarehouseSelect:<c:out value="${useWarehouseSelect}"/>,
                data: warehousesData
            });

            docDateSelector.setOnSelected(function(event, docDateSelection) {
                sendTableAjax("getTableData");
            });


            // ---------------------------------init donutCrudPlugin----------------------------------------
            <c:if test="${isSupplierManager}">
            var warehousesKeyValuePairs = {};
            warehousesData.warehouses.forEach(function(warehouse) {
               warehousesKeyValuePairs[warehouse.warehouseId] = warehouse.warehouseName;
            });
            var supplierInputDataDialog = $('[data-remodal-id=modal]').remodal();

            var donutCrudPluginInstance = $("#routeListDataContainer").donutCrudPlugin({
                isEditable: true,
                ordersCrud: "all",
                editableFields: {
                    donutFields: ["driver", "licensePlate", "palletsQty", "driverPhoneNumber", "commentForDonut"],
                    ordersFields: ["orderNumber", "finalDestinationWarehouseId", "boxQty", "commentForStatus"]
                },
                orderStatuses: ${requestScope.orderStatuses},
                warehouses: warehousesKeyValuePairs
            });

            var sInsertBtn = tablePlugin.getButtonByPluginId("sInsertBtn");
            sInsertBtn.onclick = function(e) {
                donutCrudPluginInstance.setSupplierName('<c:out value="${sessionScope.user.supplier.inn}"/>');
                donutCrudPluginInstance.setPeriod(getSelectedPeriodAsString());
                donutCrudPluginInstance.setOnSubmit(function() {
                    sendTableAjax("insertDonut", {createdDonut: donutCrudPluginInstance.getData()}, function() {
                        donutCrudPluginInstance.setOnSubmit(null);
                        supplierInputDataDialog.close();
                    })
                });
                supplierInputDataDialog.open();
            };

            var sUpdateBtn = tablePlugin.getButtonByPluginId("sUpdateBtn");
            sUpdateBtn.onclick = function(e) {
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {donutDocPeriodId: donutDocPeriodId};
                // TODO get IDs for deleted requests and send
                $.ajax({
                    url: "selectDonut",
                    method: "POST",
                    data: sendObject,
                    dataType: "json"
                }).done(function (donutData) {
                    donutCrudPluginInstance.setData(donutData);
                    donutCrudPluginInstance.setSupplierName('<c:out value="${sessionScope.user.supplier.inn}"/>');
                    donutCrudPluginInstance.setPeriod(getSelectedPeriodAsString());
                    donutCrudPluginInstance.setOnSubmit(function() {
                        var sendObject = $.extend(
                                donutCrudPluginInstance.getData(),
                                {removedOrders: removedOrders, donutDocPeriodId: tablePlugin.getSelectionData()[0].data.docPeriodId}
                        );
                        sendTableAjax("updateDonut", {updatedDonut: sendObject}, function() {
                            donutCrudPluginInstance.setOnRowRemoved(null);
                            donutCrudPluginInstance.setOnSubmit(null);
                            supplierInputDataDialog.close();
                        })
                    });
                    var removedOrders = [];
                    donutCrudPluginInstance.setOnRowRemoved(function(rowData) {
                        if (rowData.orderId !== null) {
                            removedOrders.push(rowData.orderId);
                        }
                    });
                    supplierInputDataDialog.open();
                }).fail(function () {
                    window.alert("error");
                });
            };

            var sDeleteBtn = tablePlugin.getButtonByPluginId("sDeleteBtn");
            sDeleteBtn.onclick = function(e) {
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {donutDocPeriodId: donutDocPeriodId};
                sendTableAjax("deleteDonut", {dataForDelete: sendObject});
            };

            function getSelectedPeriodAsString() {
                var selectionData = tablePlugin.getSelectionData();
                var periodsString;
                var periods = selectionData[0].periods;
                if (periods.length === 1) {
                    periodsString = periods[0].periodBegin + ";" + periods[0].periodEnd;
                } else if (periods.length > 1) {
                    periodsString = periods[0].periodBegin + ";" + periods[periods.length - 1].periodEnd;
                } else
                    throw new Error("bad period");
                return periodsString;
            }

            </c:if>


            docDateSelector.setOnSelectionAvailable(function(event, isSelectionAvailable) {
                tablePlugin.setDisabled(!isSelectionAvailable);
            });
            $docAndDateSelector.triggerEvents();
            // дату надо пересыдать с временной зоной(Z)
            // date format: 'YYYY-mm-dd'
            function periodAsTimestamps(periodBegin, periodEnd, dateString, offset) {
                Date.parse(dateString);
            }

            function sendTableAjax(url, data, onDone) {
                //tablePlugin.setDisabled(true);
                var rawDocDateSelection = docDateSelector.getSelectionObject();
                var forSendDocDateSelection = $.extend(rawDocDateSelection, {date: rawDocDateSelection.date.getTime()});
                var sendObject = {docDateSelection: forSendDocDateSelection};
                if (data) {
                    sendObject = $.extend(true, sendObject, data);
                }
                for (var key in sendObject) {
                    if (sendObject.hasOwnProperty(key)) {
                        sendObject[key] = JSON.stringify(sendObject[key]);
                    }
                }

                $.ajax({
                    url: url,
                    method: "POST",
                    data: sendObject,
                    dataType: "json"
                }).done(function (tableData) {
                    tablePlugin.setData(tableData.docPeriods);
                    //ablePlugin.setDisabled(false);
                    if (onDone) onDone();
                }).fail(function () {
                    window.alert("error");
                   // tablePlugin.setDisabled(false);
                });
            }

        });
    </script>
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
            <div id="routeListDataContainer"></div>
        </div>
    </c:if>

</div>



</body>
</html>
