<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование доков</title>
    <%--styles--%>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/media/custom/mainPage/favicon.ico"/>">

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/datePicker/pickmeup.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Remodal-1.0.7/dist/remodal.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Remodal-1.0.7/dist/remodal-default-theme.css"/>"/>

    <link rel="stylesheet" type="text/css"
          href="<c:url value="/media/DataTables-1.10.12/css/jquery.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Select-1.2.0/css/select.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Buttons-1.2.1/css/buttons.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/Editor-1.5.6/css/editor.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/chosen_v1.5.1/chosen.css"/>">

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/docAndDateSelector.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/tablePlugin2.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/donutCrudPlugin.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/tableOverviewPlugin.css"/>">

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/main.css"/>">

    <%--common scripts--%>
    <script src="<c:url value="/media/es5-shim.min.js"/>"></script>
    <script src="<c:url value="/media/es6-shim.min.js"/>"></script>
    <script src="<c:url value="/media/customEventPolyForIE.js"/>"></script>
    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/datePicker/jquery.pickmeup.min.js"/>"></script>


    <script>
        window.REMODAL_GLOBALS = {
            DEFAULTS: {
                hashTracking: false,
                closeOnOutsideClick: false
            }
        };
    </script>
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

    <script src="<c:url value="/media/pdfmake.min.js"/>"></script>
    <script src="<c:url value="/media/vfs_fonts.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/tableOverviewPlugin.js"/>"></script>

    <%--specific scripts for different user roles--%>
    <c:set var="periodSize" scope="application" value="${initParam.periodSize}"/>
    <c:set var="windowSize" scope="application" value="${initParam.windowSize}"/>
    <c:set var="userRole" scope="session" value="${sessionScope.user.userRole.userRoleId}"/>
    <c:set var="isSupplierManager" scope="page" value="${userRole == 'SUPPLIER_MANAGER'}"/>
    <c:set var="isWarehouseBoss" scope="page" value="${userRole == 'WH_BOSS'}"/>
    <c:set var="isWarehouseSupervisor" scope="page" value="${userRole == 'WH_SUPERVISOR'}"/>
    <c:set var="isWarehouseDispatcher" scope="page" value="${userRole == 'WH_DISPATCHER'}"/>
    <c:set var="isSecurityOfficer" scope="page" value="${userRole == 'WH_SECURITY_OFFICER'}"/>
    <c:set var="useWarehouseSelect" scope="page"
           value="${isWarehouseSupervisor || !(isWarehouseBoss || isWarehouseDispatcher || isSecurityOfficer)}"/>
    <c:set var="maxCells" scope="request" value="${requestScope.maxCells}"/>


    <script>
        $(document).ready(function () {
            "use strict";

            // ---------------------------------init table plugin----------------------------------------
            var tablePlugin = window.tablePlugin2({
                parentId: 'tableContainer',
                windowSize: +<c:out value="${windowSize}"/>,
                cellSize: +<c:out value="${periodSize}"/>,
                allowedStatesForSelection: {
                    isOpenedAllowed: <c:out value="${isSupplierManager || isWarehouseBoss || isWarehouseSupervisor}"/>,
                    isClosedAllowed: <c:out value="${isWarehouseBoss || isWarehouseSupervisor}"/>,
                    isOccupiedAllowed: true
                },
                selectionModel: {
                    isSelectAllOccupied: true,
                    isSelectAllClosed: false
                },

                <c:if test="${isSupplierManager}">
                selectionConstraint: function (serialNumber, selectedSerialNumbers, isSelected) {
                    if (selectedSerialNumbers.length === 0) {
                        return false;
                    }
                    else if (selectedSerialNumbers.length === 1) {
                        if (isSelected) {
                            return false;
                        } else {
                            var selectedSerialNumber = selectedSerialNumbers[0];
                            if ((serialNumber === (selectedSerialNumber - 1)) || (serialNumber === (selectedSerialNumber + 1))) {
                                return false;
                            }
                        }
                    } else {
                        var min = selectedSerialNumbers[0];
                        var max = selectedSerialNumbers[selectedSerialNumbers.length - 1];
                        if (isSelected && (serialNumber === min || serialNumber === max)) {
                            return false;
                        }
                        if ((serialNumber === (min - 1)) || (serialNumber === (max + 1))) {
                            return false;
                        }
                    }
                    return true;
                },
                buttons: [
                    {
                        name: "Отменить",
                        id: "sDeleteBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return (state === "OCCUPIED" && isFullPeriodSelected);
                        }
                    },
                    {
                        name: "Изменить",
                        id: "sUpdateBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return (state === "OCCUPIED" && isFullPeriodSelected);
                        }
                    },
                    {
                        name: "Зарезервировать",
                        id: "sInsertBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OPENED";
                        }
                    },
                    {
                        name: "История поставщика",
                        id: "supplierHistoryBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED";
                        }
                    }
                ]
                </c:if>

                <c:if test="${isWarehouseDispatcher}">
                buttons: [
                    {
                        name: "Изменить статусы",
                        id: "dUpdateBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return (state === "OCCUPIED" && isFullPeriodSelected);
                        }
                    },
                    {
                        name: "История поставщика",
                        id: "supplierHistoryBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED";
                        }
                    },
                    {
                        name: "Отчет",
                        id: "warehouseReportBtn",
                        enabledIfAnySelected: false,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return true;
                        }
                    }
                ]
                </c:if>

                <c:if test="${isSecurityOfficer}">
                buttons: [
                    {
                        name: "Прибытие",
                        id: "oUpdateBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return (state === "OCCUPIED" && isFullPeriodSelected);
                        }
                    },
                    {
                        name: "История поставщика",
                        id: "supplierHistoryBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED";
                        }
                    }
                ]
                </c:if>

                <c:if test="${isWarehouseBoss || isWarehouseSupervisor}">
                buttons: [
                    {
                        name: "Открыть",
                        id: "bOpenPeriodBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "CLOSED";
                        }
                    },
                    {
                        name: "Закрыть",
                        id: "bClosePeriodsBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OPENED";
                        }
                    },
                    {
                        name: "Отменить",
                        id: "bCancelDonutBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED" && isFullPeriodSelected;
                        }
                    },
                    {
                        name: "Информация",
                        id: "bInfoBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED" && isFullPeriodSelected;
                        }
                    },
                    {
                        name: "История поставщика",
                        id: "supplierHistoryBtn",
                        enabledIfAnySelected: true,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return state === "OCCUPIED";
                        }
                    },
                    {
                        name: "Отчет",
                        id: "warehouseReportBtn",
                        enabledIfAnySelected: false,
                        enabledIf: function (state, isFullPeriodSelected) {
                            return true;
                        }
                    }
                ]
                </c:if>,
                maxCellsSelected: <c:out value="${maxCells}"/>

            });


//             ---------------------------------init tableOverview plugin----------------------------------------
            var overviewTableContainer = $('#overviewTableContainer');
            var tableOverviewPlugin = window.tableOverviewPlugin({
                parentId: 'overviewTableContainer',
                cellSize: 30,
                windowSize: 60 * 24
            });

            // ---------------------------------init docDateSelector plugin----------------------------------------
            var $docAndDateSelector = $('#docAndDateSelector');

            var docDateSelector = $docAndDateSelector.docAndDateSelector({
                useWarehouseSelect:<c:out value="${useWarehouseSelect}"/>,
                data: ${requestScope.docDateSelectorDataForRole}
            });
            docDateSelector.setOnSelected(function (event, docDateSelection) {
                sendTableAjax("getTableData");
                overviewTableAjax();
            });

            // ---------------------------------init donutCrudPlugin----------------------------------------
            <c:if test="${isSupplierManager}">
            var donutCrudPluginDialog = $('[data-remodal-id=donutsDialog]').remodal();

            var donutCrudPluginInstance = $("#routeListDataContainer").donutCrudPlugin({
                isEditable: true,
                ordersCrud: "all",
                editableFields: {
                    donutFields: ["driver", "licensePlate", "palletsQty", "driverPhoneNumber", "commentForDonut"],
                    ordersFields: ["orderNumber", "finalDestinationWarehouseId", "boxQty", "commentForStatus", "invoiceNumber", "goodsCost", "orderPalletsQty"]
                },
                orderStatuses: ${requestScope.orderStatusesForRole},
                warehouses: ${requestScope.warehousesForDonutCrudPlugin}
            });
            donutCrudPluginInstance.setPeriodToString(function (period) {
                return tablePlugin.getLabelGenerator().getLabelTextFromMinutes(period.periodBegin, period.periodEnd);
            });

            var sInsertBtn = tablePlugin.getButtonByPluginId("sInsertBtn");
            sInsertBtn.onclick = function (e) {
                donutCrudPluginInstance.clear();
                donutCrudPluginInstance.setSupplierName('<c:out value="${sessionScope.user.supplier.inn}"/>');
                donutCrudPluginInstance.setPeriod(getSelectedPeriod());
                donutCrudPluginInstance.setOnSubmit(function () {
                    var data = donutCrudPluginInstance.getData();
                    var utcDate = docDateSelector.getSelectionObject().date.getTime();
                    data.period.periodBegin = toUtcDateTime(utcDate, data.period.periodBegin);
                    data.period.periodEnd = toUtcDateTime(utcDate, data.period.periodEnd);
                    sendTableAjax("insertDonut", {createdDonut: data}, function () {
                        donutCrudPluginInstance.setOnSubmit(null);
                        donutCrudPluginDialog.close();
                    })
                });
                donutCrudPluginDialog.open();
            };

            var sUpdateBtn = tablePlugin.getButtonByPluginId("sUpdateBtn");
            sUpdateBtn.onclick = function (e) {
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {donutDocPeriodId: donutDocPeriodId};

                $.ajax({
                    url: "selectDonut",
                    method: "POST",
                    data: sendObject,
                    dataType: "json"
                }).done(function (donutData) {
                    donutCrudPluginInstance.setData(donutData);
                    donutCrudPluginInstance.setSupplierName('<c:out value="${sessionScope.user.supplier.inn}"/>');
                    donutCrudPluginInstance.setPeriod(getSelectedPeriod());
                    donutCrudPluginInstance.setOnSubmit(function () {
                        var utcDate = docDateSelector.getSelectionObject().date.getTime();
                        var data = donutCrudPluginInstance.getData();
                        data.period.periodBegin = toUtcDateTime(utcDate, data.period.periodBegin);
                        data.period.periodEnd = toUtcDateTime(utcDate, data.period.periodEnd);
                        var sendObject = $.extend(
                                data,
                                {
                                    removedOrders: removedOrders,
                                    donutDocPeriodId: tablePlugin.getSelectionData()[0].data.docPeriodId
                                }
                        );
                        sendTableAjax("updateDonut", {updatedDonut: sendObject}, function () {
                            donutCrudPluginInstance.setOnRowRemoved(null);
                            donutCrudPluginInstance.setOnSubmit(null);
                            donutCrudPluginDialog.close();
                        })
                    });
                    var removedOrders = [];
                    donutCrudPluginInstance.setOnRowRemoved(function (rowData) {
                        if (rowData.orderId !== null) {
                            removedOrders.push(rowData.orderId);
                        }
                    });
                    donutCrudPluginDialog.open();
                });
            };

            var sDeleteBtn = tablePlugin.getButtonByPluginId("sDeleteBtn");
            sDeleteBtn.onclick = function (e) {
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {donutDocPeriodId: donutDocPeriodId};
                sendTableAjax("deleteDonut", sendObject);
            };


            </c:if>

            <c:if test="${isWarehouseDispatcher}">
            var donutCrudPluginDialog = $('[data-remodal-id=donutsDialog]').remodal();

            var donutCrudPluginInstance = $("#routeListDataContainer").donutCrudPlugin({
                isEditable: true,
                ordersCrud: "update",
                editableFields: {
                    donutFields: ["palletsQty"],
                    ordersFields: ["orderStatusId", "commentForStatus", "orderPalletsQty"]
                },
                orderStatuses: ${requestScope.orderStatusesForRole},
                warehouses: ${requestScope.warehousesForDonutCrudPlugin}
            });
            donutCrudPluginInstance.setPeriodToString(function (period) {
                return tablePlugin.getLabelGenerator().getLabelTextFromMinutes(period.periodBegin, period.periodEnd);
            });


            var dUpdateBtn = tablePlugin.getButtonByPluginId("dUpdateBtn");
            dUpdateBtn.onclick = function (e) {
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {donutDocPeriodId: donutDocPeriodId};

                $.ajax({
                    url: "selectDonut",
                    method: "POST",
                    data: sendObject,
                    dataType: "json"
                }).done(function (donutData) {
                    donutCrudPluginInstance.setData(donutData);
                    donutCrudPluginInstance.setPeriod(getSelectedPeriod());
                    donutCrudPluginInstance.setOnSubmit(function () {
                        var sendObject = $.extend(
                                donutCrudPluginInstance.getData(),
                                {
                                    removedOrders: [],
                                    donutDocPeriodId: tablePlugin.getSelectionData()[0].data.docPeriodId
                                }
                        );
                        sendTableAjax("updateDonut", {updatedDonut: sendObject}, function () {
                            donutCrudPluginInstance.setOnRowRemoved(null);
                            donutCrudPluginInstance.setOnSubmit(null);
                            donutCrudPluginDialog.close();
                        })
                    });
                    donutCrudPluginDialog.open();
                });
            };
            var warehouseReportBtn = tablePlugin.getButtonByPluginId("warehouseReportBtn");
            warehouseReportBtn.onclick = function (e) {
                $('[data-remodal-id=warehouseReportPickRange]').remodal().open();
            };
            </c:if>

            <c:if test="${isWarehouseBoss || isWarehouseSupervisor}">
            var donutCrudPluginDialog = $('[data-remodal-id=donutsDialog]').remodal();

            var donutCrudPluginInstance = $("#routeListDataContainer").donutCrudPlugin({
                isEditable: false,
                orderStatuses: ${requestScope.orderStatusesForRole},
                warehouses: ${requestScope.warehousesForDonutCrudPlugin}
            });
            donutCrudPluginInstance.setPeriodToString(function (period) {
                return tablePlugin.getLabelGenerator().getLabelTextFromMinutes(period.periodBegin, period.periodEnd);
            });


            var bInfoBtn = tablePlugin.getButtonByPluginId("bInfoBtn");
            bInfoBtn.onclick = function (e) {
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {donutDocPeriodId: donutDocPeriodId};

                $.ajax({
                    url: "selectDonut",
                    method: "POST",
                    data: sendObject,
                    dataType: "json"
                }).done(function (donutData) {
                    donutCrudPluginInstance.setData(donutData);
                    donutCrudPluginInstance.setPeriod(getSelectedPeriod());
                    donutCrudPluginDialog.open();
                });
            };
            var bClosePeriodsBtn = tablePlugin.getButtonByPluginId("bClosePeriodsBtn");
            bClosePeriodsBtn.onclick = function () {
                var utcDate = docDateSelector.getSelectionObject().date.getTime();
                var selectionData = tablePlugin.getSelectionData();
                var periods = selectionData[0].periods;
                var absolutePeriods = [];
                periods.forEach(function (period) {
                    absolutePeriods.push({
                        periodBegin: toUtcDateTime(utcDate, period.periodBegin),
                        periodEnd: toUtcDateTime(utcDate, period.periodEnd)
                    });
                });
                sendTableAjax("insertDocPeriods", {periodsForInsert: absolutePeriods});
            };
            var bOpenPeriodBtn = tablePlugin.getButtonByPluginId("bOpenPeriodBtn");
            bOpenPeriodBtn.onclick = function () {
                var selectionObject = docDateSelector.getSelectionObject();
                var utcDate = selectionObject.date.getTime();
                var docId = selectionObject.docId;
                var selectionData = tablePlugin.getSelectionData();
                var sendData = [];
                selectionData.forEach(function (elem) {
                    elem.data.periodBegin = toUtcDateTime(utcDate, elem.data.periodBegin);
                    elem.data.periodEnd = toUtcDateTime(utcDate, elem.data.periodEnd);
                    var periods = elem.periods;
                    periods.forEach(function (period) {
                        period.periodBegin = toUtcDateTime(utcDate, period.periodBegin);
                        period.periodEnd = toUtcDateTime(utcDate, period.periodEnd);
                    });
                    sendData.push(getDataForSendOpen(elem, docId));
                });
                sendTableAjax("openDocPeriods", {openPeriodsData: sendData});
            };
            <%--BINDING dto.OpenDocPeriodsData.java--%>
            function getDataForSendOpen(obj, docId) {
                if (obj.periods.length === 1 && obj.data.periodBegin === obj.periods[0].periodBegin && obj.data.periodEnd === obj.periods[0].periodEnd) {
                    return [{
                        action: "DELETE",
                        docPeriodId: obj.data.docPeriodId
                    }];
                } else if (obj.periods.length === 1) {
                    if (obj.data.periodBegin === obj.periods[0].periodBegin) {
                        return [{
                            periodBegin: obj.periods[0].periodEnd,
                            periodEnd: obj.data.periodEnd,
                            action: "UPDATE",
                            docPeriodId: obj.data.docPeriodId
                        }];
                    } else if (obj.data.periodEnd === obj.periods[0].periodEnd) {
                        return [{
                            periodBegin: obj.data.periodBegin,
                            periodEnd: obj.periods[0].periodBegin,
                            action: "UPDATE",
                            docPeriodId: obj.data.docPeriodId
                        }];
                    } else {
                        return [
                            {
                                periodBegin: obj.data.periodBegin,
                                periodEnd: obj.periods[0].periodBegin,
                                action: "UPDATE",
                                docPeriodId: obj.data.docPeriodId
                            },
                            {
                                periodBegin: obj.periods[0].periodEnd,
                                periodEnd: obj.data.periodEnd,
                                action: "INSERT",
                                docId: docId
                            }
                        ];
                    }
                } else {
                    var result = [];
                    if (obj.data.periodBegin !== obj.periods[0].periodBegin) {
                        result.push({
                            periodBegin: obj.data.periodBegin,
                            periodEnd: obj.periods[0].periodBegin,
                            action: "UPDATE",
                            docPeriodId: obj.data.docPeriodId
                        });
                    }
                    for (var i = 0; i < obj.periods.length - 1; i++) {
                        if (i === 0 && result.length === 0) {
                            result.push({
                                periodBegin: obj.periods[i].periodEnd,
                                periodEnd: obj.periods[i + 1].periodBegin,
                                action: "UPDATE",
                                docPeriodId: obj.data.docPeriodId
                            });
                        } else {
                            result.push({
                                periodBegin: obj.periods[i].periodEnd,
                                periodEnd: obj.periods[i + 1].periodBegin,
                                action: "INSERT",
                                docId: docId
                            });
                        }
                    }
                    if (obj.data.periodEnd !== obj.periods[obj.periods.length - 1].periodEnd) {
                        result.push({
                            periodBegin: obj.periods[obj.periods.length - 1].periodEnd,
                            periodEnd: obj.data.periodEnd,
                            action: "INSERT",
                            docId: docId
                        });
                    }
                    return result;
                }
            }

            var emailDialog = $('[data-remodal-id=sendEmailDialog]').remodal();
            var bCancelDonutBtn = tablePlugin.getButtonByPluginId("bCancelDonutBtn");
            var intervalAsText;
            bCancelDonutBtn.onclick = function () {
                var period = getSelectedPeriod();
                intervalAsText = tablePlugin.getLabelGenerator().getLabelTextFromMinutes(period.periodBegin, period.periodEnd);
                $("#emailInterval").text(intervalAsText);
                $("#emailSupplier").text(tablePlugin.getSelectionData()[0].data.supplierName);
                $("#submitEmail").prop("disabled", false);
                emailDialog.open();
            };
            $("#submitEmail").on("click", function () {
                $("#submitEmail").prop("disabled", true);
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {
                    donutDocPeriodId: donutDocPeriodId,
                    emailContent: $("#emailMessageArea").val(),
                    intervalAsText: intervalAsText
                };
                sendTableAjax("deleteDonutWithNotification", sendObject, function () {
                    emailDialog.close();
                });
            });

            var emailDialogSplitted = $('[data-remodal-id=sendEmailDialogSplitted]').remodal();
            $("#submitEmailSplitted").on("click", function () {
                var btn = $("#submitEmailSplitted");
                btn.prop("disabled", true);
                var data = btn.data("data");
                var isBegin = btn.data("isBegin");
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var sendObject = {
                    periodToRemove: data,
                    isBegin: isBegin,
                    donutDocPeriodId: donutDocPeriodId,
                    emailContent: $("#emailMessageAreaSplitted").val(),
                    intervalAsText: intervalAsText
                };
                sendTableAjax("deleteDonutWithNotification", sendObject, function () {
                    emailDialogSplitted.close();
                });
            });

            var cantDeletePeriodDialog = $('[data-remodal-id=cantDeletePeriodDialog]').remodal();
            $("#submitCantDeletePeriod").on("click", function () {
                cantDeletePeriodDialog.close();
            });

            var warehouseReportBtn = tablePlugin.getButtonByPluginId("warehouseReportBtn");
            warehouseReportBtn.onclick = function (e) {
                $('[data-remodal-id=warehouseReportPickRange]').remodal().open();
            };


            </c:if>

            <c:if test="${isSecurityOfficer}">
            var $confirmArrivalDialog = $('[data-remodal-id=confirmArrivalDialog]').remodal();
            var oUpdateBtn = tablePlugin.getButtonByPluginId("oUpdateBtn");
            var currentDonutDocPeriodId;

            $("#officerConfirm").on("click", function (e) {
                sendTableAjax("setOrderStatusesToArrived", {donutDocPeriodId: currentDonutDocPeriodId}, function () {
                    $confirmArrivalDialog.close();
                })
            });

            oUpdateBtn.onclick = function (e) {

                // открыть диалоговое окно с двумя labels - номер машины и имя водителя
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                currentDonutDocPeriodId = donutDocPeriodId;
                var sendObject = {donutDocPeriodId: donutDocPeriodId};

                $.ajax({
                    url: "selectDonut",
                    method: "POST",
                    data: sendObject,
                    dataType: "json"
                }).done(function (donutData) {
                    $("#licensePlate").text(donutData.driver);
                    $("#driverName").text(donutData.licensePlate);
                    $confirmArrivalDialog.open();
                });


            };
            </c:if>

            var supplierHistoryBtn = tablePlugin.getButtonByPluginId("supplierHistoryBtn");
            supplierHistoryBtn.onclick = function (e) {
                var selectionData = tablePlugin.getSelectionData()[0];
                var donutDocPeriodId = selectionData.data.docPeriodId;
                var redirectWindow = window.open('getSupplierHistory?docPeriodId=' + donutDocPeriodId, '_blank');
                redirectWindow.location;


            };


            docDateSelector.setOnSelectionAvailable(function (event, isSelectionAvailable) {
                tablePlugin.setDisabled(!isSelectionAvailable);
            });

            <%------------------ SESSION DATA LOADING ----------------------%>
            <c:if test="${requestScope.lastDocDateSelection != null}">
            $docAndDateSelector.setSelectedDate(new Date(<c:out value="${requestScope.lastDocDateSelection.utcDate}"/>));
            if ($docAndDateSelector.withWarehouseSelect()) {
                var warehouseId = <c:out value="${requestScope.lastDocDateSelection.warehouseId}"/>;
                var docId = <c:out value="${requestScope.lastDocDateSelection.docId}"/>;
                $docAndDateSelector.setSelectedWarehouseAndDoc(warehouseId, docId);
            } else {
                $docAndDateSelector.setSelectedDoc(<c:out value="${requestScope.lastDocDateSelection.docId}"/>);
            }
            </c:if>
            $docAndDateSelector.triggerEvents();

            <%------------------ CONTEXT MENU ------------------------------%>

            $(document).bind("mousedown", function (event) {
                if (!$(event.target).parents(".custom-menu").length > 0) {
                    $(".custom-menu").hide(100);
                }
            });

            $(document).on("contextmenu", ".mainTable td.tp_highlight:has(div.tp_occupied.tp_in_process.tp_owned)", function (event) {
                event.preventDefault();
                var item = $("#customMenuItem1");
                item.text("Отменить период " + $(this).find("label").text());
                item.attr("data", $(this).attr("data-serialnumber"));
                item.attr("supplier", $(this).find("div.tp_occupied.tp_in_process.tp_owned").text());
                item.click(function () {
                    var data = $(this).attr("data");
                    var supplier = $(this).attr("supplier");
                    var periodBegin = (data - 1) * 30;
                    var periodEnd = data * 30;
                    var donutPeriod = getSelectedPeriod();
                    if (periodBegin != donutPeriod.periodBegin && periodEnd != donutPeriod.periodEnd) {
                        var alertDialog = $('[data-remodal-id=cantDeletePeriodDialog]').remodal();
                        alertDialog.open();
                    } else {
                        var intervalAsText = tablePlugin.getLabelGenerator().getLabelTextFromMinutes(periodBegin, periodEnd);
                        var emailDialog = $('[data-remodal-id=sendEmailDialogSplitted]').remodal();
                        $("#emailIntervalSplitted").text(intervalAsText);
                        $("#emailSupplierSplitted").text(supplier);
                        var btn = $("#submitEmailSplitted");
                        btn.data("data", data);
                        btn.data("isBegin", periodBegin == donutPeriod.periodBegin);
                        btn.prop("disabled", false);
                        emailDialog.open();
                    }
                    $(".custom-menu").hide(100);
                    item.unbind("click");
                });
                $(".custom-menu").finish().toggle(100).css({
                    top: event.pageY + "px",
                    left: event.pageX + "px"
                });
            });

            <%------------------ FUNCTIONS ----------------------%>
            function toUtcDateTime(utcDate, periodPart) {
                return utcDate + periodPart * 60 * 1000;
            }

            function getSelectedPeriod() {
                var periods = tablePlugin.getSelectionData()[0].periods;
                return {periodBegin: periods[0].periodBegin, periodEnd: periods[0].periodEnd};
            }

            //Already implemented ajax function just won't cut it
            function overviewTableAjax(url, data, onDone) {
                var rawDocDateSelection = docDateSelector.getSelectedDocs();
                var sendObject ={};
                tableOverviewPlugin.clearAll();
                rawDocDateSelection.forEach(function(item,i,arr){
                    item = $.extend(item,{date: item.date.getTime()});
                    sendObject = {docDateSelection: item};
                    for (var key in sendObject) {
                        if (sendObject.hasOwnProperty(key)) {
                            sendObject[key] = JSON.stringify(sendObject[key]);
                        }
                    }
                    $.ajax({
                        url: "getTableData",
                        //by the way, this function could really use it's own serlet
                        method: "POST",
                        data: sendObject,
                        dataType: "json",
                        async: false
                    }).done(function(overviewTableData){
                        overviewTableData.docName = item.docName;
                        tableOverviewPlugin.pushTable(overviewTableData);
                    })
                });
            }

            function sendTableAjax(url, data, onDone) {
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
                    if (onDone) onDone();
                });
            }

            <%------------------ERROR HANDLING ----------------------%>
            var errorDialogContainer = $('[data-remodal-id=errorDialog]');
            var errorDialog = errorDialogContainer.remodal();
            $(document).ajaxError(function (ev, jqXHR, ajaxSettings, thrownError) {
                var tempDom = $('<output>').append($.parseHTML(jqXHR.responseText));
                var $errorRoot = $('#errorRoot', tempDom);
                errorDialogContainer.children("div").empty().append($errorRoot);
                errorDialog.open();
            });

        });
    </script>
</head>

<body>

<div id="userPane">

    <div id="exit"  class="dropdown">
        <span><c:out value="${sessionScope.user.userName}"/> </span>
        <div class="dropdown-content">
            <form action="logout" method="post">
                <input type="submit" value="Выйти"/>
            </form>
        </div>
    </div>

    <%--<table class="profileTable">--%>
        <%--<tr>--%>
            <%--<td>Имя</td>--%>
            <%--<td><c:out value="${sessionScope.user.userName}"/></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td>роль</td>--%>
            <%--<td><c:out value="${requestScope.userRoleRusName}"/></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td>Должность</td>--%>
            <%--<td><c:out value="${sessionScope.user.position}"/></td>--%>
        <%--</tr>--%>
    <%--</table>--%>
</div>

<div id="docsPane">

    <div id="docAndDateSelector"></div>
    <div id="tableContainer"></div>
    <div id="tableControlsContainer"></div>

    <%--dialogs--%>
    <div data-remodal-id="errorDialog">
        <button data-remodal-action="close" class="remodal-close"></button>
        <div></div>
    </div>

    <c:if test="${isWarehouseBoss || isWarehouseSupervisor || isWarehouseDispatcher || isSupplierManager}">
        <div data-remodal-id="donutsDialog">
            <button data-remodal-action="close" class="remodal-close"></button>
            <h1>Ввод данных</h1>
            <div id="routeListDataContainer"></div>
        </div>

        <%--Report dialog goes here--%>
        <div data-remodal-id="warehouseReportPickRange">
            <button data-remodal-action="close" class="remodal-close"></button>
            <h1>Отчет по диапазону дат</h1>
            <div>
                <input readonly class="datePicker" id="warehouseReportPickMeUpRange">
            </div>
            <button id="openWarehouseReport">Открыть отчет</button>
        </div>
    </c:if>

    <c:if test="${isWarehouseBoss || isWarehouseSupervisor}">
        <div data-remodal-id="sendEmailDialog">
            <button data-remodal-action="close" class="remodal-close"></button>
            <h1>Отмена доставки</h1>
            <table>
                <tr>
                    <td>Интервал</td>
                    <td id="emailInterval"></td>
                </tr>
                <tr>
                    <td>Поставщик</td>
                    <td id="emailSupplier"></td>
                </tr>
                <tr>
                    <td colspan="2"><label for="emailMessageArea">Сообщение поставщику</label></td>
                </tr>
            </table>

            <textarea id="emailMessageArea" style="resize:none" cols="75" rows="10" autofocus></textarea>
            <button id="submitEmail">Отправить</button>
        </div>
        <div data-remodal-id="sendEmailDialogSplitted">
            <button data-remodal-action="close" class="remodal-close"></button>
            <h1>Отмена интервала доставки</h1>
            <table>
                <tr>
                    <td>Интервал</td>
                    <td id="emailIntervalSplitted"></td>
                </tr>
                <tr>
                    <td>Поставщик</td>
                    <td id="emailSupplierSplitted"></td>
                </tr>
                <tr>
                    <td colspan="2"><label for="emailMessageAreaSplitted">Сообщение поставщику</label></td>
                </tr>
            </table>

            <textarea id="emailMessageAreaSplitted" style="resize:none" cols="75" rows="10" autofocus></textarea>
            <button id="submitEmailSplitted">Отправить</button>
        </div>
        <div data-remodal-id="cantDeletePeriodDialog">
            <button data-remodal-action="close" class="remodal-close"></button>
            <h1>Ошибка</h1>
            <table>
                <tr>
                    <td>Невозможно отменить интервал: допускается отмена только начального или конечного интервала</td>
                </tr>
            </table>

            <button id="submitCantDeletePeriod">OK</button>
        </div>

    </c:if>

    <c:if test="${isSecurityOfficer}">
        <div data-remodal-id="confirmArrivalDialog">
            <button data-remodal-action="close" class="remodal-close"></button>
            <h1>Данные прибытия</h1>
            <table>
                <tr>
                    <td>Номер автомобиля</td>
                    <td id="licensePlate"></td>
                </tr>
                <tr>
                    <td>Имя водителя</td>
                    <td id="driverName"></td>
                </tr>
            </table>
            <button id="officerConfirm">Подтвердить</button>
        </div>
    </c:if>

</div>

<div class="overview-table-wrapper">
    <div class="overview-table-container" id="overviewTableContainer">
        <%--<table class="overview-table">--%>
            <%--<tr>--%>
                <%--<td>--%>
                <%--</td>--%>
                <%--<td>--%>
                <%--</td>--%>
                <%--<td>--%>
                    <%----%>
                <%--</td>--%>
            <%--</tr>--%>
        <%--</table>--%>
        <%--<div class="overview-table">--%>
        <%--<h2>2/3</h2>--%>
        <%--Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid cum quasi nulla molestias accusamus aspernatur reiciendis qui optio tenetur modi repellendus distinctio dolore nesciunt. Repellat provident explicabo accusamus autem perspiciatis.--%>
        <%--</div>--%>
        <%--<div class="overview-table">--%>
        <%--<h2>1/3</h2>--%>
        <%--Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid cum quasi nulla molestias accusamus aspernatur reiciendis qui optio tenetur modi repellendus distinctio dolore nesciunt. Repellat provident explicabo accusamus autem perspiciatis.--%>
        <%--</div>--%>
        <%--<div class="overview-table">--%>
        <%--<h2>1/3</h2>--%>
        <%--Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid cum quasi nulla molestias accusamus aspernatur reiciendis qui optio tenetur modi repellendus distinctio dolore nesciunt. Repellat provident explicabo accusamus autem perspiciatis.--%>
        <%--</div>--%>
        <%--<div class="overview-table">--%>
        <%--<h2>1/3</h2>--%>
        <%--Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid cum quasi nulla molestias accusamus aspernatur reiciendis qui optio tenetur modi repellendus distinctio dolore nesciunt. Repellat provident explicabo accusamus autem perspiciatis.--%>
        <%--</div>--%>
        <%--<div class="overview-table">--%>
        <%--<h2>1/3</h2>--%>
        <%--Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid cum quasi nulla molestias accusamus aspernatur reiciendis qui optio tenetur modi repellendus distinctio dolore nesciunt. Repellat provident explicabo accusamus autem perspiciatis.--%>
        <%--</div>--%>
    </div>

</div>

</body>
</html>
