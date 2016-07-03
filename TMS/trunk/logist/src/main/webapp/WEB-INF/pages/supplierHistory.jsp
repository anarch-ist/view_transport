<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/DataTables-1.10.12/css/jquery.dataTables.min.css"/>"/>
    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/DataTables-1.10.12/js/jquery.dataTables.min.js"/>"></script>
    <script>
        $(document).ready(function () {
            $('#allDonutsForSupplierTable').DataTable();
        });
    </script>
</head>
<body>
<c:set var="allDonutsForSupplier" scope="request" value="${requestScope.allDonutsForSupplier}"/>

<table id="allDonutsForSupplierTable" cellspacing="0" width="100%">
    <caption><c:out value="${requestScope.supplierName}"/></caption>
    <thead>
    <tr>
        <th>Начало периода</th>
        <th>Конец периода</th>
        <th>Склад</th>
        <th>Док</th>
        <th>Заявки</th>
        <th>Статусы заявок</th>
        <th>Комментарий</th>
        <th>Время последнего обновления</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="donut" items="${requestScope.allDonutsForSupplier}">
        <tr>
            <td>${donut.periodBegin}</td>
            <td>${donut.periodEnd}</td>
            <td>${donut.warehouseName}</td>
            <td>${donut.docName}</td>
            <td>${donut.orderNumbersAsString}</td>
            <td>${donut.orderStatusesAsString}</td>
            <td>${donut.comment}</td>
            <td>${donut.lastModified}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>





</body>
</html>
