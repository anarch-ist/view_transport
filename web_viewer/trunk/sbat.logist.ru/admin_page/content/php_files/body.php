<head xmlns:display="http://www.w3.org/1999/xhtml">
    <meta charset="UTF-8">
    <title></title>

    <link rel="stylesheet" type="text/css" href="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Selectize/css/selectize.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Buttons-1.1.0/css/buttons.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Editor-1.5.4/css/editor.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/FieldType-Selectize/editor.selectize.min.css"/> <!--z-index:999-->
    <link rel="stylesheet" type="text/css" href="../common_files/media/Select-1.1.0/css/select.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../admin_page/content/css/admin.css"/>

    <script src="../common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js"></script>
    <script src="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>
    <script src="../common_files/media/jquery-mask/dist/jquery.mask.min.js"></script>
    <script src="../common_files/media/Selectize/js/standalone/selectize.min.js"></script>
    <script src="../common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js"></script>
    <script src="../common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js"></script>
    <script src="../common_files/media/Buttons-1.1.0/js/dataTables.buttons.min.js"></script>
    <script src="../common_files/media/Buttons-1.1.0/js/buttons.jqueryui.min.js"></script>
    <script src="../common_files/media/Editor-1.5.4/js/dataTables.editor.min.js"></script>
    <script src="../common_files/media/Editor-1.5.4/js/editor.jqueryui.min.js"></script>
    <script src="../common_files/media/FieldType-Mask/editor.mask.min.js"></script>
    <script src="../common_files/media/FieldType-Selectize/editor.selectize.min.js"></script>
    <script src="../common_files/media/Select-1.1.0/js/dataTables.select.min.js"></script>
    <script src="../common_files/media/md5.js"></script>

    <!--custom-->
    <script src="content/js/routePoints.js"></script>

</head>
<body>
<!--<ul id="menu">-->
<!--    <li id="routes">Маршруты</li>-->
<!--    <li id="users">Пользователи</li>-->
<!--    <li id="distances" class="ui-state-disabled">Расстояния между пунктами</li>-->
<!--</ul>-->

<div id="tabs">
    <ul>
        <li><a href="#tabs-1">Маршруты</a></li>
        <li><a href="#tabs-2">Пользователи</a></li>
    </ul>
    <div id="tabs-1">
        <h2>Маршруты</h2>
        <div id="routesCreation">
            <label for="routeSelect">Направление: </label>
            <select id="routeSelect"></select>

            <div>
                <div>
                    <table>
                        <tr>
                            <td><label for="daysOfWeekSelect">Дни недели: </label></td>
                            <td>
                                <div id="daysOfWeekSelect" style="display: inline-block">
                                    <input type="checkbox" id="monday"><label for="monday">ПН</label>
                                    <input type="checkbox" id="tuesday"><label for="tuesday">ВТ</label>
                                    <input type="checkbox" id="wednesday"><label for="wednesday">СР</label>
                                    <input type="checkbox" id="thursday"><label for="thursday">ЧТ</label>
                                    <input type="checkbox" id="friday"><label for="friday">ПТ</label>
                                    <input type="checkbox" id="saturday"><label for="saturday">СБ</label>
                                    <input type="checkbox" id="sunday"><label for="sunday">ВС</label>
                                </div>
                            </td>
                            <td>
                                <button id="updateDaysOfWeek" style="display: inline-block">Обновить</button>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="startRouteTimeInput">Старт маршрута: </label></td>
                            <td><input id="startRouteTimeInput"></td>
                            <td>
                                <button id="updateStartRouteTime">Обновить</button>
                            </td>
                        </tr>
                    </table>

                    <img id="ajaxLoaderGif" src="../admin_page/content/res/ajax-loader-small.gif" style="display: none">

                    <div>
                        <div style="width: 49%; display: inline-block">
                            <table id="routePointsTable">
                                <thead>
                                <tr>
                                    <th>Порядковый номер пункта</th>
                                    <th>Пункт</th>
                                    <th>Продолжительность разгрузочно-погрузочных операций</th>
                                </tr>
                                </thead>
                            </table>
                        </div>

                        <div style="width: 50%; display: inline-block">
                            <table id="relationsBetweenRoutePointsTable">
                                <thead>
                                <tr>
                                    <th>Начальный пункт</th>
                                    <th>Конечный пункт</th>
                                    <th>Расстояние</th>
                                    <th>Время в пути</th>
                                </tr>
                                </thead>
                            </table>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="tabs-2">
        <h2>Пользователи</h2>
        <div id="usersCreation">
            <table id="usersTable">
                <thead>
                <tr>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th>Отчество</th>
                    <th>Должность</th>
                    <th>Номер телефона</th>
                    <th>Электронная почта</th>
                    <th>Пароль</th>
                    <th>Роль</th>
                    <th>Пункт</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</body>