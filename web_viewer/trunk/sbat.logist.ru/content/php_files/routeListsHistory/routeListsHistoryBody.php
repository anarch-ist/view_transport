<html>
<head>
    <meta charset="UTF-8"/>
    <!--    Applying all the CSS-->
    <link rel="stylesheet" type="text/css" href="common_files/media/Bootstrap-3.3.2/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/font-awesome/css/font-awesome.min.css"/>

    <!--    Custom css-->
    <link rel="stylesheet" type="text/css" href="content/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/media.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/routeListHistory.css"/>


    <!--    Applying JS-->
    <script src="common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js" type="text/javascript"></script>
    <!--    <script src="common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>-->
    <script src="common_files/media/Bootstrap-3.3.2/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="common_files/media/jquery-mask/dist/jquery.mask.min.js" type="text/javascript"></script>
    <script src="http://api-maps.yandex.ru/2.1/?lang=ru-RU" type="text/javascript"></script>

    <!--    Custom JS-->
    <script src="content/js/routeListsHistory.js" type="text/javascript"></script>

    <!--    <script src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js" type="text/javascript"></script>-->

</head>
<body>
<div id="routeListsHistoryDialog" title="История статусов">
    <div class="container">
        <div class="row">
            <div class="mid request-information">
                <div class="info">Информация о МЛ <span id="routeList_Number"></span></div>
                <div class="loading col-lg-12 ">
<!--                    <img src="content/css/loading.gif">-->
                </div>
                <div class="col-xs-12 col-sm-12 col-md-6 clg-lg-6" >
                    <table id="routeListDataTable" class="left-table" >
                        <tr>
                            <td>Номер маршрутного листа:</td>
                            <td id="routeListsNumber"></td>
                        </tr>
                        <tr>
                            <td>Дата создания:</td>
                            <td id="creationDate"></td>
                        </tr>
                        <tr>
                            <td>Дата отправки:</td>
                            <td id="departureDate"></td>
                        </tr>

                    </table>
                </div>
                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" >
                    <table class="right-table" >
                        <tr>
                            <td>Количество паллет:</td>
                            <td id="palletsQty"></td>
                        </tr>
                        <tr>
                            <td>Телефон водителя:</td>
                            <td id="driversNumber"></td>
                        </tr>
                        <tr>
                            <td>Лицензия:</td>
                            <td id="licensePlate"></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="mid" id="map" hidden></div>
            <div class="row">
                <div class="mid status-history">
                    <div class="info">История МЛ</div>
                    <div class="col-xs-12">
                        <table id="routeListHistoryDialogTable" class="table table-striped">
                            <thead>
                            <tr>
                                <th>Время</th>
                                <th>Номер МЛ</th>
                                <th>Дата создания МЛ</th>
                                <th>Форвардер</th>
                                <th>Телефон водителя</th>
                                <th>Статус</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <a class="btn btn-default btn-lg" style="float: right" href="../../index.php">
                        На главную
                    </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>
