<html>
<head>
    <meta charset="UTF-8"/>
    <!--    Applying all the CSS-->
    <link rel="stylesheet" type="text/css" href="common_files/media/Bootstrap-3.3.2/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css"
          href="common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/font-awesome/css/font-awesome.min.css"/>

    <!--    Custom css-->
    <link rel="stylesheet" type="text/css" href="content/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="content/css/media.css"/>


    <!--    Applying JS-->
    <script src="common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js" type="text/javascript"></script>
    <!--    <script src="common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>-->
    <script src="common_files/media/Bootstrap-3.3.2/js/bootstrap.min.js" type="text/javascript"></script>
    <!--    <script src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js" type="text/javascript"></script>-->
    <script src="common_files/media/jquery-mask/dist/jquery.mask.min.js" type="text/javascript"></script>

    <!--    Custom JS-->
    <script src="content/js/documentsWindow.js" type="text/javascript"></script>

    <!--    <script src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js" type="text/javascript"></script>-->
    <title>Документы</title>

</head>
<body>
<div id="requestHistoryDialog">
    <div class="container">
        <div class="row">
            <div class="mid request-information">
                <div class="info">Информация о заявке</div>
                <div class="loading col-lg-12 ">
                    <img src="content/css/loading.gif">
                </div>
                <div class="col-xs-12 col-sm-12 col-md-6 clg-lg-6" >
                    <table class="left-table" style="display: none">
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
                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" >
                    <table class="right-table" style="display: none">
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
                <div class="info">Документы к заявке</div>
                <div class="col-xs-12" id="documents-container">


                </div>
                

                <a class="btn btn-default btn-lg" style="float: right" href="../../index.php">
                    На главную
                </a>
<!--                    <button type="button" id="addDocumentButton" class="btn btn-default btn-lg" data-toggle="modal" data-target="#addDocumentModal">-->
<!--                        Добавить документ-->
<!--                    </button>-->
            </div>
        </div>
    </div>
</div>
<div id="addDocumentModal" class="modal fade" role="dialog">
    <form enctype="multipart/form-data" action="content/php_files/getData.php" method="POST">
        <div class="modal-dialog modal-lg">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Добавить документ</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-lg-12">
                                <label for="documentGroup">Группа документа<span style="color:#c22929;">*</span></label>
                                <select class="form-control" id="documentGroup">
                                    <option disabled selected value> -- Выберите группу --</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <input type="hidden" name="MAX_FILE_SIZE" value="30000" />
                                <input type="hidden" name="status" value="uploadDocuments" />
                                <input type="hidden" name="requestIDExternal" value="<?echo $_GET['reqIdExt']?>" />
                                <!-- Название элемента input определяет имя в массиве $_FILES -->
                                <label class="btn btn-default btn-file">
                                    Browse <input name="docFiles[]" type="file" multiple/>
                                </label>
                            </div>
                        </div>
                    </div>
                    </div>
                <div class="modal-footer">
                    <button id="upload" value="Отправить" class="btn btn-warning"></button>
                </div>
            </div>

        </div>
    </form>
</div>

</body>

</html>
