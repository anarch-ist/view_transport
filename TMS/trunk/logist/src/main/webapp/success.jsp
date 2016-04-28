<%@ page import="ru.logistica.tms.dao.usersDao.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript" src="media/jQuery-2.1.4/jquery-2.1.4.min.js" ></script>

</head>
<body>
<h3>Login Successful!</h3>
<button id="exitButton">выйти</button>
<script>
    $("#exitButton").on("click", function() {

        // delete all cookies
        var cookies = document.cookie.split(";");

        for (var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i];
            var eqPos = cookie.indexOf("=");
            var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
            document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
        }

        window.location.href = "login.html";
    });
</script>
<% User user = (User)request.getSession().getAttribute("user");%>
имя = <% out.println(user.getUserName()); %>
роль = <% out.println(user.getUserRole().getUserRoleRusName()); %>
должность = <% out.println(user.getPosition()); %>

</body>
</html>
