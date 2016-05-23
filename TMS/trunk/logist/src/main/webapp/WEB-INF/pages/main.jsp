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
    <%--common scripts--%>
    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/datePicker/jquery.pickmeup.min.js"/>"></script>
    <script src="<c:url value="/media/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/docAndDateSelector.js"/>"></script>
    <%--specific scripts for different user roles--%>
    <%--<c:set var="requestData" scope="request" value="${requestScope.docDateSelectorDataObject}"/>--%>
    <c:set var="userRole" scope="session" value="${sessionScope.user.userRole.userRoleId}"/>
    <c:choose>
        <c:when test="${userRole == 'SUPPLIER_MANAGER'}">
            <script src="<c:url value="/media/custom/mainPage/roleSupplierManager.js"/>"></script>
        </c:when>
        <c:when test="${userRole == 'WH_BOSS'}">
            <script src="<c:url value="/media/custom/mainPage/roleWarehouseBoss.js"/>"></script>
        </c:when>
        <c:when test="${userRole == 'WH_DISPATCHER'}">
            <script src="<c:url value="/media/custom/mainPage/roleWarehouseDispatcher.js"/>"></script>
        </c:when>
    </c:choose>
</head>

<body>

<div id="userPane">
    <form action="logout" method="post">
        <input id="exit" type="submit" value="выйти"/>
    </form>

    <c:out value="${userRole}"/>
    <table>
        <tr>
            <td>имя</td><td><c:out value="${sessionScope.user.userName}"/></td>
        </tr>
        <tr>
            <td>роль</td><td><c:out value="${sessionScope.user.userRole.userRoleId}"/></td>
        </tr>
        <tr>
            <td>должность</td><td><c:out value="${sessionScope.user.position}"/></td>
        </tr>
    </table>
</div>

<div id="docsPane">
    <div id="docAndDateSelector" data-component_data=${requestScope.docDateSelectorDataObject}></div>
</div>


</body>
</html>
