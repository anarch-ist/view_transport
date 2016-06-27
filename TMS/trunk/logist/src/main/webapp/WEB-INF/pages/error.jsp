<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Error Page</title>
</head>
<body>

<div id="errorRoot">
    <table>
        <caption>Ошибка!</caption>
        <tr>
            <td>URL</td>
            <td id="uri">${pageContext.errorData.requestURI}</td>
        </tr>
        <tr>
            <td>Код</td>
            <td id="statusCode">${pageContext.errorData.statusCode}</td>
        </tr>
        <tr>
            <td>Сообщение</td>
            <td id="exception">${pageContext.exception}</td>
        </tr>
    </table>
    <%--<div id="stacktrace">--%>
        <%--<c:forEach var="trace"--%>
                   <%--items="${pageContext.exception.stackTrace}">--%>
            <%--<p>${trace}</p>--%>
        <%--</c:forEach>--%>
    <%--</div>--%>
</div>

</body>
</html>
