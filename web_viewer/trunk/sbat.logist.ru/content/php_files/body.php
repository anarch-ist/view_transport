<title>логистика</title>
<head>
    <!--jquery-->
    <script type="text/javascript" language="javascript" src="common_files/media/js/jquery.js"></script>
    <!--jquery-ui-->
    <link rel="stylesheet" type="text/css" href="common_files/media/css/jquery-ui.min.css">
    <script type="text/javascript" language="javascript" src="common_files/media/js/jquery-ui.min.js"></script>

    <!--dataTables with plugins libs-->
    <link rel="stylesheet" type="text/css" href="common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Buttons-1.1.0/css/buttons.jqueryui.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Select-1.1.0/css/select.jqueryui.css"/>

    <script type="text/javascript" src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.js"></script>
    <script type="text/javascript" src="common_files/media/Buttons-1.1.0/js/dataTables.buttons.js"></script>
    <script type="text/javascript" src="common_files/media/Buttons-1.1.0/js/buttons.jqueryui.js"></script>
    <script type="text/javascript" src="common_files/media/Select-1.1.0/js/dataTables.select.js"></script>

    <!--dateTimePicker-->
    <link rel="stylesheet" type="text/css" href="common_files/media/dateTimePicker/jquery-ui-timepicker-addon.css"/>
    <script type="text/javascript" src="common_files/media/dateTimePicker/jquery-ui-timepicker-addon.js"></script>

    <script src="common_files/media/jqueryCookie-1.4.1/jquery.cookie.js"></script>

    <!--custom styles-->

    <!--custom js-->
    <script type="text/javascript" language="javascript" src="content/js/main.js"></script>


</head>
<body>

<div class="container">
    <div id="menu">

        <label for="tableTypeSelect">Выберите таблицу:</label>
        <select name="tableType" id="tableTypeSelect">
            <option selected="selected">Текущие статусы</option>
            <option>История статусов</option>
            <option>Маршрутные листы</option>
        </select>

        <div id="buttonsContainer" style="display: inline-block">
            <button>
                Сброс фильтров
            </button>
            <button id="logout">выйти</button>
        </div>

    </div>

    <table id="user-grid" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">
        <thead>
            <tr>
                <th>ID заявки</th>
                <th>ID внутренней заявки</th>
                <th>ID накладной</th>
                <th>Клиент, ИНН</th>
                <th>Пункт доставки</th>
                <th>Склад отправки</th>
                <th>Торговый представитель</th>
                <th>Статус</th>
                <th>Количество коробок</th>
                <th>Водитель</th>
                <th>Номер ТС</th>
                <th>Количество паллет</th>
                <th>Маршрутный лист</th>
                <th>Направление</th>
                <th>Текущее подразделение</th>
                <th>Следующий пункт маршрута</th>
                <th>Плановое время прибытия в следующий пункт</th>
            </tr>
        </thead>

        <tfoot>
            <tr>
                <th>ID заявки</th>
                <th>ID внутренней заявки</th>
                <th>ID накладной</th>
                <th>Клиент, ИНН</th>
                <th>Пункт доставки</th>
                <th>Склад отправки</th>
                <th>Торговый представитель</th>
                <th>Статус</th>
                <th>Количество коробок</th>
                <th>Водитель</th>
                <th>Номер ТС</th>
                <th>Количество паллет</th>
                <th>Маршрутный лист</th>
                <th>Направление</th>
                <th>Текущее подразделение</th>
                <th>Следующий пункт маршрута</th>
                <th>Плановое время прибытия в следующий пункт</th>
            </tr>
        </tfoot>
    </table>
</div>
</body>