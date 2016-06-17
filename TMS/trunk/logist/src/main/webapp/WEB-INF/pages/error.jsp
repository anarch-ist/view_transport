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
    <div id="exception">${pageContext.exception}</div>
    <div id="uri">${pageContext.errorData.requestURI}</div>
    <div id="statusCode">${pageContext.errorData.statusCode}</div>
    <div id="stacktrace">
        <c:forEach var="trace"
                   items="${pageContext.exception.stackTrace}">
            <p>${trace}</p>
        </c:forEach>
    </div>
</div>

</body>
</html>
