<head xmlns:display="http://www.w3.org/1999/xhtml">
    <meta charset="UTF-8">
    <title></title>

    <link rel="stylesheet" type="text/css" href="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Selectize/css/selectize.min.css"/>
    <link rel="stylesheet" type="text/css"
          href="../common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Buttons-1.1.0/css/buttons.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Editor-1.5.4/css/editor.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/FieldType-Selectize/editor.selectize.min.css"/>
    <!--z-index:999-->
    <link rel="stylesheet" type="text/css" href="../common_files/media/font-awesome/css/font-awesome.min.css"/>
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
    <script src="../common_files/media/Buttons-1.1.0/js/dataTables.buttons.flash.min.js"></script>

    <!--custom-->
    <script src="content/js/route-point.js"></script>
    <script src="content/js/user.js"></script>
    <script src="content/js/client.js"></script>
    <script src="content/js/driver.js"></script>
    <script src="content/js/vehicle.js"></script>
    <script src="content/js/transport-company.js"></script>
    <script src="content/js/route.js"></script>
    <script src="content/js/requests-assignment.js"></script>
    <script src="content/js/transactions.js"></script>

</head>
<body>
<!--<ul id="menu">-->
<!--    <li id="routes">Маршруты</li>-->
<!--    <li id="users">Пользователи</li>-->
<!--    <li id="distances" class="ui-state-disabled">Расстояния между пунктами</li>-->
<!--</ul>-->

<div id="tabs">
    <div id="transactionWidget">
        Обмен с 1С:<br>
        Статус: <span id="transactionStatus"></span><br>
        Последний пакет: №<span id="lastTransactionPacket"></span>, <span id="lastTransactionServer"></span><br> <span
                id="lastTransactionTime"></span>(GMT)
    </div>
    <ul>
        <li><a href="#tabs-1"><i class="fa fa-compass" aria-hidden="true"></i> Маршруты</a></li>
        <li><a href="#tabs-2"><i class="fa fa-users" aria-hidden="true"></i> Пользователи</a></li>
        <li><a href="#tabs-3"><i class="fa fa-truck" aria-hidden="true"></i> ТК, ТС, <br>водители</a></li>
        <li><a href="#tabs-4"><i class="fa fa-magic" aria-hidden="true"></i> Распределение<br> заявок</a></li>
        <li><a href="#tabs-5"><i class="fa fa-exchange" aria-hidden="true"></i> Обмен с 1С</a></li>
<!--        <li><a >Fgsfas</a></li>-->
                <li onclick="location.href='../'"><a target="_blank" href="#"><i class="fa fa-table" aria-hidden="true"></i> На главную</a></li>
        <li onclick="location.href='../login/'"><a target="_blank" href="#"><i class="fa fa-sign-out" aria-hidden="true"></i> Выход</a></li>
        <!--        <li><a onclick="" href="../" id="logout">Выход</a></li>-->
    </ul>
    <div id="tabs-1">
        <h2>Маршруты</h2>
        <div id="usersCreation">
            <table id="routeTable">
                <thead>
                <tr>
                    <th>routeID</th>
                    <th>Имя маршрута</th>
                    <th>tarrifID</th>
                    <th>Стоимость за маршрут</th>
                    <th>Стоимость за точку</th>
                    <th>Стоимость за час</th>
                    <th>Стоимость за коробку</th>
                </tr>
                </thead>
            </table>
        </div>
        <div id="routesCreation">
            <h2>Направление</h2>
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
                    <th>userID</th>
                    <th>ФИО</th>
                    <th>Логин</th>
                    <th>Должность</th>
                    <th>Номер телефона</th>
                    <th>Электронная почта</th>
                    <th>Пароль</th>
                    <th>Роль</th>
                    <th>Пункт</th>
                    <th>ID клиента</th>
                </tr>
                </thead>
            </table>
        </div>
        <h2>Клиенты</h2>
        <div id="clientsCreation">
            <table id="clientsTable">
                <thead>
                <tr>
                    <th>clientID</th>
                    <th>Имя</th>
                    <th>ИНН</th>
                    <th>КПП</th>
                    <th>Кор. Счет</th>
                    <th>Кур. Счет</th>
                    <th>БИК</th>
                    <th>Номер контракта</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
    <div id="tabs-3">
        <h2>Транспортные компании</h2>
        <div id="transportCompaniesCreation">
            <table id="transportCompaniesTable">
                <thead>
                <tr>
                    <th>№ТК</th>
                    <th>Полное название</th>
                    <th>Название</th>
                    <th>ИНН</th>
                    <th>КПП</th>
                    <th>БИК</th>
                    <th>Кор. счет</th>
                    <th>Текущий счет</th>
                    <th>Название банка</th>
                    <th>Адрес</th>
                    <th>Почтовый адрес</th>
                    <th>Ключевые слова</th>
                    <th>Директор</th>
                    <th>Главный бухгалтер</th>
                </tr>
                </thead>
            </table>
        </div>
        <h2>Транспортные средства</h2>
        <div id="vehiclesCreation">
            <table id="vehiclesTable">
                <thead>
                <tr>
                    <th>№ТС</th>
                    <th>№ТК</th>
                    <th>Лицензия</th>
                    <th>Модель</th>
                    <th>Грузоподъемность</th>
                    <th>Объем</th>
                    <th>Тип погрузки</th>
                    <th>Количество паллет</th>
                    <th>Тип</th>
                    <th>Wialon ID</th>
                    <th>Принадлежность</th>
                </tr>
                </thead>
            </table>
        </div>
        <h2>Водители</h2>
        <div id="driversCreation">
            <table id="driversTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>№ТС</th>
                    <th>№ТК</th>
                    <th>Имя</th>
                    <th>Пасспорт</th>
                    <th>Телефон</th>
                    <th>Лицензия</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
    <div id="tabs-4">
        <div id="requestsAssignment"><h2>Распределение заявок</h2><br>
            <div class="tooltip">Как это работает?
                <div class="tooltiptext">Распределитель собирает все заявки, у которых указан выбранный склад как точка
                    отправки, затем - выбирает маршруты, которые проходят через склад отправки и склад доставки.<br> Для
                    каждого найденного маршрута создается маршрутный лист, в него записываются все заявки, которым
                    нашелся маршрут. <br>Если поступает маршрутный лист из 1С и в нем есть заявка, для которой создан
                    маршрутный лист с этой страницы - заявка перепишется на маршрутный лист из 1С автоматически
                </div>
            </div>
            <br><br>
            <label for="warehouseId">Распределить заявки со склада:</label><input id="warehouseId"><br>
            <input type="button" disabled="disabled" id="assignRequests" value="Распределить">
            <div id="routeListsLinks" style="display:none">
                <h3>Последние созданные маршрутные листы</h3>

                <ul id="autoInsertedRouteListLinks"></ul>
            </div>
        </div>
    </div>
    <div id="tabs-5">
        <h2>Последние пакеты из 1С</h2><br>
        <table id="transactionsTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Номер пакета</th>
                <th>Сервер</th>
                <th>Статус</th>
                <th>Дата транзакции</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>