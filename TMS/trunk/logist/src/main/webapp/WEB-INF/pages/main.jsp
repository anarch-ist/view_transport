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
    <%--common scripts--%>
    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/datePicker/jquery.pickmeup.min.js"/>"></script>
    <script src="<c:url value="/media/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>
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
            "use strict";

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
            tablePlugin.setOnClicked(function(e) {
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
            docDateSelector.setOnSelected(function(event, date, warehouseId, docId) {
                // на время отправки данных и их получения - таблица должна уходить в состояние disabled = true;
                //tablePlugin.setDisabled(true);
                $.ajax({
                    url: "getTableData",
                    method: "POST",
                    data: {date: date, warehouseId: warehouseId, docId: docId},
                    dataType: "json"
                }).done(function (tableData) {
                    var periodsArray = tableData.docPeriods;
                    periodsArray.forEach(function(period) {
                        if (period.state === "DISABLED") {
                            tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, period.state);
                        } else if (period.state === "OCCUPIED") {
                            tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, period.state, period.supplierName);
                            //period.supplierId;
                        }
                    });
                    //tablePlugin.setDisabled(false);
//                    tableData.periodBegin;
//                    tableData.periodEnd;

                    console.log(tableData);

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
                $.ajax({
                    url: "setTableData",
                    method: "POST",
                    data: {docAndDate: selectionObject, period: tablePlugin.getSelectedPeriod()},
                    dataType: "json"
                }).done(function (tableData) {
                    var periodsArray = tableData.docPeriods;
                    periodsArray.forEach(function(period) {
                        if (period.state === "DISABLED") {
                            tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, period.state);
                        } else if (period.state === "OCCUPIED") {
                            tablePlugin.setNotFreeState(period.periodBegin, period.periodEnd, period.state, period.supplierName);
                            //period.supplierId;
                        }
                    });

                }).fail(function () {
                    window.alert("error");
                    //tablePlugin.setDisabled(false);

                });
            };
            </c:if>





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
</div>



</body>
</html>
