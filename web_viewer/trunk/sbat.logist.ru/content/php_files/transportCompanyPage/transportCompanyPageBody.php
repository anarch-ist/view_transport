<html>
<head>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="content/css/transportCompanyPage.css">
    <!--    Applying all the CSS-->
    <link rel="stylesheet" type="text/css" href="common_files/media/Bootstrap-3.3.2/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css"
          href="common_files/media/DataTables-1.10.12/css/datatables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css"
          href="common_files/media/DataTables-1.10.12/css/jquery.dataTables.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Editor-1.6.3/css/editor.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Buttons-1.2.2/css/buttons.bootstrap.css"/>
    <!--    <link rel="stylesheet" type="text/css" href="common_files/media/Buttons-1.2.2/css/buttons.dataTables.css"/>-->
    <link rel="stylesheet" type="text/css" href="common_files/media/Select-1.2.0/css/select.jqueryui.min.css"/>
    <!--    Custom css-->


    <!--    Applying JS-->
    <script src="common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js" type="text/javascript"></script>
    <script src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="common_files/media/Buttons-1.2.2/js/dataTables.buttons.js"></script>
    <script src="common_files/media/Buttons-1.2.2/js/buttons.bootstrap.js"></script>
    <script src="common_files/media/Buttons-1.2.2/js/buttons.html5.js"></script>
    <script src="common_files/media/Select-1.2.0/js/dataTables.select.min.js"></script>
    <!--        <script src="common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>-->
    <script src="common_files/media/Bootstrap-3.3.2/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="common_files/media/jquery-mask/dist/jquery.mask.min.js" type="text/javascript"></script>
    <script src="content/js/transportCompanyTable.js" type="text/javascript"></script>
    <script src="content/js/transportCompanyEditor.js" type="text/j avascript"></script>
    <!--    Custom JS-->
    <!--    <script src="content/js/documentsWindow.js" type="text/javascript"></script>-->

    <!--    <script src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js" type="text/javascript"></script>-->
    <title>Страница транспортной компании</title>

</head>
<body>
<div class="container">
    <table  id="TCRouteListsTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>Номер маршрутного листа</th>
            <th>Статус</th>
            <th>Время отправки</th>
            <th>Дата создания</th>
        </tr>
        </thead>
    </table>
</div>
    <div class="container">
    <h2>Транспортные средства</h2>
    <div id="vehicalCreationForTCPage">
        <table id="TCPageVehiclesTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Лицензия</th>
                <th>Модель</th>
                <th>Грузоподъемность</th>
                <th>Объем</th>
                <th>Тип погрузки</th>
                <th>Количество паллет</th>
                <th>Тип</th>
                <th>Wialon ID</th>
            </tr>
            </thead>
        </table>
    </div>
    <h2>Водители</h2>
    <div>
        <table id="TCPageDriversTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Имя</th>
                <th>Пасспорт</th>
                <th>Телефон</th>
                <th>Лицензия</th>
            </tr>
            </thead>
        </table>
    </div>
    </div>
</body>

</html>
