<html>
<head>
    <meta charset="UTF-8"/>

    <!--    Custom css-->
    <link rel="stylesheet" type="text/css" href="content/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/media.css"/>


    <!--    Applying all the CSS-->
    <link rel="stylesheet" type="text/css" href="common_files/media/Bootstrap-3.3.2/css/bootstrap.min.css"/>
<!--    <link rel="stylesheet" type="text/css"-->
    <!--          href="common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>-->
    <link rel="stylesheet" type="text/css"
          href="common_files/media/DataTables-1.10.12/css/datatables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css"
          href="common_files/media/DataTables-1.10.12/css/jquery.dataTables.css"/>
<!--    <link rel="stylesheet" type="text/css" href="common_files/media/Editor-1.6.3/css/editor.jqueryui.min.css"/>-->
    <link rel="stylesheet" type="text/css" href="common_files/media/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Select-1.2.0/css/select.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Buttons-1.2.2/css/buttons.bootstrap.css"/>
<!--    <link rel="stylesheet" type="text/css" href="common_files/media/Buttons-1.2.2/css/buttons.dataTables.css"/>-->


    <!--    Applying JS-->
    <script src="common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js" type="text/javascript"></script>
    <script src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="common_files/media/Select-1.2.0/js/dataTables.select.min.js"></script>
    <!--        <script src="common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>-->
    <script src="common_files/media/Bootstrap-3.3.2/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="common_files/media/jquery-mask/dist/jquery.mask.min.js" type="text/javascript"></script>
    <script src="common_files/media/Buttons-1.2.2/js/dataTables.buttons.js"></script>
    <script src="common_files/media/Buttons-1.2.2/js/buttons.bootstrap.js"></script>
<!--    <script src="common_files/media/Buttons-1.2.2/js/buttons.html5.js"></script>-->

    <script src="content/js/routeListPage.js" type="text/javascript"></script>
    <!--    <script src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js" type="text/javascript"></script>-->
    <title>Маршрутный лист</title>

</head>
<body>
<div id="requestHistoryDialog">
    <div class="container">
        <div class="row">
            <div class="mid request-information">
                <div class="info">Заявки для маршрутного листа <span id="routeListNumber"></span> </div>



            </div>

        </div>
        <div class="row">


            <div class="mid status-history">
                <table  id="requestsForRouteListTable" class="table table-bordered table-striped" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>Внешний ID</th>
                        <th>Номер накладной</th>
                        <th>Статус</th>
                        <th>Дата доставки</th>
                        <th>Номер документа</th>
                        <th>Кол-во коробок</th>
                    </tr>
                    </thead>
                </table>


            </div>
        </div>
    </div>
</div>


</body>

</html>
