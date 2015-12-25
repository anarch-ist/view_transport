<title>логистика</title>
<head>
    <meta charset="UTF-8"/>

    <link rel="stylesheet" type="text/css" href="common_files/media/jQueryUI-1.11.4/jquery-ui.min.css"/>
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

    <!--dateTimePicker-->
    <link rel="stylesheet" type="text/css" href="common_files/media/dateTimePicker/jquery-ui-timepicker-addon.css"/>
    <script type="text/javascript" src="common_files/media/dateTimePicker/jquery-ui-timepicker-addon.js"></script>
    <!--jqueryCookiePlugin-->
    <script src="common_files/media/jqueryCookie-1.4.1/jquery.cookie.js"></script>
    <!--custom js-->
    <script type="text/javascript" language="javascript" src="content/js/invoiceHistoryDialog.js"></script>
    <script type="text/javascript" language="javascript" src="content/js/main.js"></script>

</head>
<body>

<div class="container">

    <div id="menu" style="z-index:1;width: 100%;position: fixed;height: 41px;margin-left: -10px;margin-top: -5px;">
        <div id="buttonsContainer" style="text-align: right;padding: 2px;">
            <div style="color: #0070a3;font-size: 1.1em;font-weight: bold;display: inline-block;margin-right: 15px;">
                <?php
                $data = $privUser->getUserInfo()->toArray();
                echo $data['lastName'] . ' ' . $data['firstName'] . ' ' . $data['patronymic'] . ' | ' . $privUser->getUserEntity()->getUserRole($data['userID']) . ' | ' . $privUser->getPointEntity()->selectPointByUserID($data['userID']);
                ?>
            </div>
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
            <th>...</th>
            <th>...</th>
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
            <th>...</th>
            <th>...</th>
        </tr>
        </tfoot>
    </table>
</div>
</body>