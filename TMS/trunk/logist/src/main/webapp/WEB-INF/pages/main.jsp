<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование доков</title>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/media/custom/mainPage/favicon.ico"/>" >
    <link rel="stylesheet" type="text/css"        href="<c:url value="/media/dateTimePicker/jquery.filthypillow.css"/>">
    <link rel="stylesheet" type="text/css"        href="<c:url value="/media/custom/mainPage/main.css"/>">

    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/dateTimePicker/moment.js"/>"></script>
    <script src="<c:url value="/media/dateTimePicker/jquery.filthypillow.min.js"/>"></script>
    <script src="<c:url value="/media/custom/mainPage/main.js"/>"></script>

</head>
<body>

<div id="userPane">
    <form action="logout" method="post">
        <input id="exit" type="submit" value="выйти"/>
    </form>

    <c:set var="userRole" scope="session" value="${sessionScope.user.userRole.userRoleId}"/>
    <c:set var="requestData" scope="request" value="${requestScope.docDateSelectorDataObject}"/>
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

    <div id="docAndDateSelector">

        <div id="datePicker">
            <label for="date">Выберите дату </label>
            <input id="date" class="filthypillow-1" />
        </div>

        <div id="warehousePicker">

        </div>
        <c:choose>
            <c:when test="${userRole == 'SUPPLIER_MANAGER'}">
                <c:out value="${requestData}"/>
            </c:when>

            <c:when test="${userRole == 'WH_BOSS' || userRole == 'WH_DISPATCHER'}">
                <%--label tag нужно вызвать метод из DAO, получить склад пользователя--%>
                <c:out value="${requestData}"/>
            </c:when>
        </c:choose>

        <%--<div id="warehousePicker">--%>
            <%--<select id="warehousePicker" name="warehouse"></select>--%>
        <%--</div>  <!--fill with jsp-->--%>
        <div id="docPicker"></div>

    </div>

    <div id="tableControls">
        <button id="cargoInformation">Информация о грузе</button>
        <button id="changeStatus">Изменить статус</button>
    </div>

</div>


</body>
</html>
