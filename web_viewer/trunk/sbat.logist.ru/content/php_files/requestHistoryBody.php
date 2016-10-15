<html>
<head>
    <meta charset="UTF-8"/>
    <!--    Applying all the CSS-->
    <link rel="stylesheet" type="text/css" href="common_files/media/bootstrap-3.3.2/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>
    
<!--    Custom css-->
    <link rel="stylesheet" type="text/css" href="content/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/media.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/reset.css"/>

    <!--    Applying JS-->
    <!--     <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>+-->
    <script src="common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js" type="text/javascript"></script>
    <!--    <script src="common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>-->
    <script src="common_files/media/bootstrap-3.3.2/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="content/js/requestHistoryWindow.js" type="text/javascript"></script>
    <!--    <script src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js" type="text/javascript"></script>-->

</head>
<body>
<div id="requestHistoryDialog" title="История статусов">
    <div class="container">
        <div class="row">
            <div class="mid request-information">
                <div class="info">Информация о заявке</div>
                <div class="col-xs-12 col-sm-12 col-md-6 clg-lg-6>">
                    <table class="left-table">
                        <tr>
                            <td>Дата заявки:</td>
                            <td id="request-date"></td>
                        </tr>
                        <tr>
                            <td>Номер накладной:</td>
                            <td id="invoice-number"></td>
                        </tr>
                        <tr>
                            <td>Дата накладной:</td>
                            <td id="invoice-date"></td>
                        </tr>
                        <tr>
                            <td>Номер документа:</td>
                            <td id="document-number"></td>
                        </tr>
                        <tr>
                            <td>Дата документа:</td>
                            <td id="document-date"></td>
                        </tr>
                        <tr>
                            <td>Фирма:</td>
                            <td id="organization"></td>
                        </tr>
                        <tr>
                            <td>Комментарий:</td>
                            <td id="comments"></td>
                        </tr>
                    </table>
                </div>
                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                    <table class="right-table">
                        <tr>
                            <td>Количество коробок:</td>
                            <td id="box-quantity"></td>
                        </tr>
                        <tr>
                            <td>Клиент, ИНН:</td>
                            <td id="client-INN"></td>
                        </tr>
                        <tr>
                            <td>Торговый представитель:</td>
                            <td id="sales-representative"></td>
                        </tr>
                        <tr>
                            <td>Пункт доставки:</td>
                            <td id="arrival-point"></td>
                        </tr>
                        <tr>
                            <td>Склад отправки:</td>
                            <td id="departure-warehouse"></td>
                        </tr>
                        <tr>
                            <td>Количество паллет:</td>
                            <td id="pallet-quantity"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="mid status-history">
                <div class="info">История статусов</div>
                <div class="col-xs-12">
                    <table id="requestHistoryDialogTable">
                        <thead>
                        <tr>
                            <th>Время</th>
                            <th>Пункт</th>
                            <th>Статус</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>