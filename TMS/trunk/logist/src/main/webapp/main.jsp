<%@ page import="ru.logistica.tms.dao.usersDao.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование доков</title>

    <link rel="stylesheet" type="text/css" href="media/dateTimePicker/jquery.filthypillow.css">
    <link rel="shortcut icon" href="media/custom/mainPage/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="media/custom/mainPage/main.css">

    <script src="media/jQuery-2.1.4/jquery-2.1.4.min.js"></script>
    <script src="media/dateTimePicker/moment.js"></script>
    <script src="media/dateTimePicker/jquery.filthypillow.min.js"></script>
    <script src="media/custom/mainPage/main.js"></script>

</head>
<body>

<div id="userPane">
    <form action="logout" method="post">
        <input id="exit" type="submit" value="выйти"/>
    </form>

    <% User user = (User) request.getSession().getAttribute("user");%>
    <table>
        <tr>
            <td>имя</td><td><% out.println(user.getUserName()); %></td>
        </tr>
        <tr>
            <td>роль</td><td><% out.println(user.getUserRole().getUserRoleRusName()); %></td>
        </tr>
        <tr>
            <td>должность</td><td><% out.println(user.getPosition()); %></td>
        </tr>
    </table>
</div>

<div id="docsPane">

    <div id="docAndDateSelector">

        <div id="datePicker">
            <label for="date">Выберите дату </label>
            <input id="date" class="filthypillow-1" />
        </div>

        <%--<div id="warehousePicker">--%>
            <%--<select id="warehousePicker" name="warehouse"></select>--%>
        <%--</div>  <!--fill with jsp-->--%>
        <div id="docPicker"></div>

    </div>

    <div id="docsTable">
        <table id="mainTable">
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod1st" class="timePeriod">00:00-00:30</td>
                <td id="timePeriod2nd" class="timePeriod">00:30-01:00</td>
                <td id="timePeriod3rd" class="timePeriod">01:00-01:30</td>
                <td id="timePeriod4th" class="timePeriod">01:30-02:00</td>
                <td id="timePeriod5th" class="timePeriod">02:00-02:30</td>
                <td id="timePeriod6th" class="timePeriod">02:30-03:00</td>
            </tr>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod7th" class="timePeriod">03:00-03:30</td>
                <td id="timePeriod8th" class="timePeriod">03:30-04:00</td>
                <td id="timePeriod9th" class="timePeriod">04:00-04:30</td>
                <td id="timePeriod10th" class="timePeriod">04:30-05:00</td>
                <td id="timePeriod11th" class="timePeriod">05:00-05:30</td>
                <td id="timePeriod12th" class="timePeriod">05:30-06:00</td>
            </tr>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod13th" class="timePeriod">06:00-06:30</td>
                <td id="timePeriod14th" class="timePeriod">06:30-07:00</td>
                <td id="timePeriod15th" class="timePeriod">07:00-07:30</td>
                <td id="timePeriod16th" class="timePeriod">07:30-08:00</td>
                <td id="timePeriod17th" class="timePeriod">08:00-08:30</td>
                <td id="timePeriod18th" class="timePeriod">08:30-09:00</td>
            </tr>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod19th" class="timePeriod">09:00-09:30</td>
                <td id="timePeriod20th" class="timePeriod">09:30-10:00</td>
                <td id="timePeriod21st" class="timePeriod">10:00-10:30</td>
                <td id="timePeriod22nd" class="timePeriod">10:30-11:00</td>
                <td id="timePeriod23rd" class="timePeriod">11:00-11:30</td>
                <td id="timePeriod24th" class="timePeriod">11:30-12:00</td>
            </tr>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod25th" class="timePeriod">12:00-12:30</td>
                <td id="timePeriod26th" class="timePeriod">12:30-13:00</td>
                <td id="timePeriod27th" class="timePeriod">13:00-13:30</td>
                <td id="timePeriod28th" class="timePeriod">13:30-14:00</td>
                <td id="timePeriod29th" class="timePeriod">14:00-14:30</td>
                <td id="timePeriod30th" class="timePeriod">14:30-15:00</td>
            </tr>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod31rd" class="timePeriod">15:00-15:30</td>
                <td id="timePeriod32nd" class="timePeriod">15:30-16:00</td>
                <td id="timePeriod33rd" class="timePeriod">16:00-16:30</td>
                <td id="timePeriod34th" class="timePeriod">16:30-17:00</td>
                <td id="timePeriod35th" class="timePeriod">17:00-17:30</td>
                <td id="timePeriod36th" class="timePeriod">17:30-18:00</td>
            </tr>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod37th" class="timePeriod">18:00-18:30</td>
                <td id="timePeriod38th" class="timePeriod">18:30-19:00</td>
                <td id="timePeriod39th" class="timePeriod">19:00-19:30</td>
                <td id="timePeriod40th" class="timePeriod">19:30-20:00</td>
                <td id="timePeriod41st" class="timePeriod">20:00-20:30</td>
                <td id="timePeriod42nd" class="timePeriod">20:30-21:00</td>
            </tr>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
            <tr align="center" class="lineOfTimePeriods">
                <td id="timePeriod43rd" class="timePeriod">21:00-21:30</td>
                <td id="timePeriod44th" class="timePeriod">21:30-22:00</td>
                <td id="timePeriod45th" class="timePeriod">22:00-22:30</td>
                <td id="timePeriod46th" class="timePeriod">22:30-23:00</td>
                <td id="timePeriod47th" class="timePeriod">23:00-23:30</td>
                <td id="timePeriod48th" class="timePeriod">23:30-00:00</td>
            <tr align="center" class="lineOfCompanies">
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
                <td class="company"></td>
            </tr>
        </table>
    </div>

    <div id="tableControls">
        <button id="cargoInformation">Информация о грузе</button>
        <button id="changeStatus">Изменить статус</button>
    </div>

</div>


</body>
</html>
