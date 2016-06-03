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
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/tablePlugin.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/donutCrudPlugin.css"/>">

    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/mainPage/main.css"/>">

    <%--common scripts--%>
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
    <script src="<c:url value="/media/custom/mainPage/tablePlugin.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/tableControlsPlugin.js"/>"></script>
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
//            "use strict";

            // ---------------------------------init table plugin----------------------------------------
            var tablePlugin = window.tablePlugin({
                parentId: 'tableContainer',
                windowSize: +<c:out value="${windowSize}"/>,
                cellSize: +<c:out value="${periodSize}"/>,
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

            var warehousesData = ${requestScope.docDateSelectorDataObject};
            // ---------------------------------init docDateSelector plugin----------------------------------------
            var $docAndDateSelector = $('#docAndDateSelector');
            var docDateSelector = $docAndDateSelector.docAndDateSelector({
                useWarehouseSelect:<c:out value="${useWarehouseSelect}"/>,
                data: warehousesData
            });
            //docDateSelector.setSelectedDate(new Date(2015, 10, 30));
            //docDateSelector.setSelectedWarehouseAndDoc(1, 3);
            //docDateSelector.setSelectedDoc(3);


            docDateSelector.setOnSelected(function(event, docDateSelection) {
                // на время отправки данных и их получения - таблица должна уходить в состояние disabled = true;
                //tablePlugin.setDisabled(true);
                $.ajax({
                    url: "getTableData",
                    method: "POST",
                    data: {docDateSelection: JSON.stringify(docDateSelection)},
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
                    {name:"Отменить", id:"cancelSupOrderBtn", enabledIfAnySelected: true},
                    {name:"Изменить", id:"updateSupBtn", enabledIfAnySelected: true},
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



            // ---------------------------------init donutCrudPlugin----------------------------------------
            <c:if test="${isSupplierManager}">
            var warehousesKeyValuePairs = {};
            warehousesData.warehouses.forEach(function(warehouse) {
               warehousesKeyValuePairs[warehouse.warehouseId] = warehouse.warehouseName;
            });

            var donutCrudPluginInstance = $("#routeListDataContainer").donutCrudPlugin({
                isEditable: true,
                ordersCrud: "all",
                editableFields: {
                    donutFields: ["driver", "licensePlate", "palletsQty", "driverPhoneNumber", "commentForDonut"],
                    ordersFields: ["orderNumber", "finalDestinationWarehouseId", "boxQty", "commentForStatus"]
                },
                orderStatuses: ${requestScope.orderStatuses},
                warehouses: warehousesKeyValuePairs,
                onSubmit: function() {
                    $.ajax({
                        url: "setTableData",
                        method: "POST",
                        data: {
                            docDateSelection: JSON.stringify(docDateSelector.getSelectionObject()),
                            donut: JSON.stringify(donutCrudPluginInstance.getData())
                        },
                        dataType: "json"
                    }).done(function (tableData) {
                        // refresh table
                        var periodsArray = tableData.docPeriods;
                        fillTableWithData(periodsArray);
                        supplierInputDataDialog.close();
                    }).fail(function () {
                        window.alert("error");
                    });
                }
            });

            var occupyPeriodBtnElem = tableControlsPlugin.getButtonByPluginId("occupySupPeriodBtn");

            occupyPeriodBtnElem.onclick = function(e) {
                var docDateSelection = docDateSelector.getSelectionObject();
                if (docDateSelection === null) {
                    return;
                }
                var selectedPeriod = tablePlugin.getSelectedPeriod();
                var periodString = selectedPeriod.periodBegin + ";" + selectedPeriod.periodEnd;
                donutCrudPluginInstance.setPeriod(periodString);
                supplierInputDataDialog.open();
            };

            var supplierInputDataDialog = $('[data-remodal-id=modal]').remodal();

            </c:if>


            createBindingBetweenSelectorAndTable();
            $docAndDateSelector.triggerEvents();

            <%--------------------------------------------FUNCTIONS---------------------------------------------------%>
            function fillTableWithData(periodsArray) {
                tablePlugin.clear();
                periodsArray.forEach(function (period) {
                    if (period.state === "DISABLED") {
                        tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, "disabled");
                    } else if (period.state === "OCCUPIED") {
                        tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, "occupied", period.supplierName);
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
