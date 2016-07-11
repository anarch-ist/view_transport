<title>логистика</title>
<head>
    <meta charset="UTF-8"/>

    <link rel="stylesheet" type="text/css" href="common_files/media/Custom_CSS/jquery-ui-1.11.4.custom/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Selectize/css/selectize.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Buttons-1.1.0/css/buttons.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Editor-1.5.4/css/editor.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/FieldType-Selectize/editor.selectize.min.css"/>
    <link rel="stylesheet" type="text/css" href="common_files/media/Select-1.1.0/css/select.jqueryui.min.css"/>

    <script src="common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js"></script>
    <script src="common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>
    <script src="common_files/media/jquery-mask/dist/jquery.mask.min.js"></script>
    <script src="common_files/media/Selectize/js/standalone/selectize.min.js"></script>
    <script src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js"></script>
    <script src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js"></script>
    <script src="common_files/media/Buttons-1.1.0/js/dataTables.buttons.min.js"></script>
    <script src="common_files/media/Buttons-1.1.0/js/buttons.jqueryui.min.js"></script>
    <script src="common_files/media/Editor-1.5.4/js/dataTables.editor.min.js"></script>
    <script src="common_files/media/Editor-1.5.4/js/editor.jqueryui.min.js"></script>
    <script src="common_files/media/FieldType-Mask/editor.mask.min.js"></script>
    <script src="common_files/media/FieldType-Selectize/editor.selectize.min.js"></script>
    <script src="common_files/media/Select-1.1.0/js/dataTables.select.min.js"></script>
    <script src="common_files/media/ColReorder-1.3.1/js/dataTables.colReorder.min.js"></script>
    <script src="common_files/media/FixedHeader-3.1.1/js/dataTables.fixedHeader.min.js"></script>

    <!--dateTimePicker-->
    <link rel="stylesheet" type="text/css" href="common_files/media/dateTimePicker/jquery-ui-timepicker-addon.css"/>
    <script type="text/javascript" src="common_files/media/dateTimePicker/jquery-ui-timepicker-addon.js"></script>

<!--    <link rel="stylesheet" type="text/css" href="common_files/media/dateTimePicker/jquery.datetimepicker_not_legal.css"/>-->
<!--    <script type="text/javascript" src="common_files/media/dateTimePicker/jquery.datetimepicker_not_legal.js"></script>-->


    <!--jqueryCookiePlugin-->
    <script src="common_files/media/jqueryCookie-1.4.1/jquery.cookie.js"></script>
    <!--custom js-->
    <script type="text/javascript" language="javascript" src="content/js/main.js"></script>
    <script type="text/javascript" language="javascript" src="content/js/requestHistoryDialog.js"></script>
    <script type="text/javascript" language="javascript" src="content/js/statusChangeDialog.js"></script>
    <script type="text/javascript" language="javascript" src="content/js/columnSelectDialog.js"></script>

</head>
<body>

<div class="container">

    <div id="menu"
         style="z-index:1;width: 500px; display: inline-block; float: right; height: 41px;  margin-left: -10px;margin-top: -5px;position:fixed;top:5px;z-index:99999;right:10px;">
        <div id="buttonsContainer" style="text-align: right;padding: 2px;">
            <div style="color: #0070a3;font-size: 1.1em;font-weight: bold;display: inline-block;margin-right: 15px;">
                <?php
                $data = $privUser->getUserInfo()->toArray();
                //exit(print_r($data));

                $userRole = $privUser->getUserEntity()->getUserRole($data['userID']);
                echo $data['userName'] . ' | ' . $userRole . ' | ' . $privUser->getPointEntity()->selectPointByUserID($data['userID']);

                ?>
            </div>
            <div id="userRoleContainer" style="display: none">
                <?php
                echo '<span id="data-role" data-role="'.$data['userRoleID'].'">'.$userRole.'</span>';
                ?>
            </div>
            <button id="logout">выйти</button>
        </div>
    </div>

    <table id="user-grid" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">
        <thead>
        <tr>
            <th class="col1">ID заявки</th>
            <th class="col2">Номер внутренней заявки</th>
            <th class="col3">Дата заявки</th>
            <th class="col4">Номер накладной</th>
            <th class="col5">Дата накладной</th>
            <th class="col6">Номер документа</th>
            <th class="col7">Дата документа</th>
            <th class="col8">Фирма</th>
            <th class="col9">Зона склада</th>
            <th class="col10">Комментарий</th>
            <th class="col11">Количество коробок</th>
            <th class="col12">Статус заявки</th>
            <th class="col13">Клиент, Номер</th>
            <th class="col14">Клиент, ИНН</th>
            <th class="col15">Клиент, Имя</th>
            <th class="col16">Торговый представитель</th>
            <th class="col17">Пункт доставки</th>
            <th class="col18">Склад отправки</th>
            <th class="col19">Текущее подразделение</th>
            <th class="col20">Следующий пункт маршрута</th>
            <th class="col21">Маршрут</th>
            <th class="col22">Водитель</th>
            <th class="col23">Номер ТС</th>
            <th class="col24">Количество паллет</th>
            <th class="col25">Маршрутный лист</th>
            <th class="col26">Время прибытия в следующий пункт</th>
      </tr>
        </thead>

        <!--search containers-->
        <tfoot>
        <tr>
            <th class="col1">ID заявки</th>
            <th class="col2">Номер внутренней заявки</th>
            <th class="col3">Дата заявки</th>
            <th class="col4">Номер накладной</th>
            <th class="col5">Дата накладной</th>
            <th class="col6">Номер документа</th>
            <th class="col7">Дата документа</th>
            <th class="col8">Фирма</th>
            <th class="col9">Зона склада</th>
            <th class="col10">Комментарий</th>
            <th class="col11">Количество коробок</th>
            <th class="col12">Статус заявки</th>
            <th class="col13">Клиент, Номер</th>
            <th class="col14">Клиент, ИНН</th>
            <th class="col15">Клиент, Имя</th>
            <th class="col16">Торговый представитель</th>
            <th class="col17">Пункт доставки</th>
            <th class="col18">Склад отправки</th>
            <th class="col19">Текущее подразделение</th>
            <th class="col20">Следующий пункт маршрута</th>
            <th class="col21">Маршрут</th>
            <th class="col22">Водитель</th>
            <th class="col23">Номер ТС</th>
            <th class="col24">Количество паллет</th>
            <th class="col25">Маршрутный лист</th>
            <th class="col26">Время прибытия в следующий пункт</th>

        </tr>
        </tfoot>
    </table>
</div>
</body>

<style>
    #user-grid_wrapper .dt-buttons{
        position:fixed;
        top:15px;
        z-index:9999999;
    }

    #user-grid_wrapper{
        margin-top:60px;
    }
</style>