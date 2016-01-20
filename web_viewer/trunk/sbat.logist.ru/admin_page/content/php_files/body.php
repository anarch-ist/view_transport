<head>
    <meta charset="UTF-8">
    <title></title>

    <link rel="stylesheet" type="text/css" href="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Selectize/css/selectize.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/DataTables-1.10.10/css/dataTables.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Buttons-1.1.0/css/buttons.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/Editor-1.5.4/css/editor.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../common_files/media/FieldType-Selectize/editor.selectize.min.css"/> <!--z-index:999-->
    <link rel="stylesheet" type="text/css" href="../common_files/media/Select-1.1.0/css/select.jqueryui.min.css"/>

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

    <!--custom-->
    <script src="content/js/routePoints.js"></script>

</head>
<body>
<div>
    <label for="routeSelect">Направление: </label>
    <select id="routeSelect"></select>
    <div>
        <div style="border: 3px solid lightcoral">
<!--            <label for="daysOfWeekSelect">Дни недели: </label>-->
            <dt data-editor-label="daysOfWeekSelect">Дни недели:</dt>
            <dd data-editor-field="daysOfWeekSelect"></dd>

            <dt data-editor-label="startRouteTimeInput">Старт маршрута:</dt>
            <dd data-editor-field="startRouteTimeInput">ertert</dd>
<!--            <div id="daysOfWeekSelect" data-editor-field="daysOfWeekSelect">-->
<!--                <input type="checkbox" id="monCheck"><label for="monCheck">ПН</label>-->
<!--                <input type="checkbox" id="tueCheck"><label for="tueCheck">ВТ</label>-->
<!--                <input type="checkbox" id="wedCheck"><label for="wedCheck">СР</label>-->
<!--                <input type="checkbox" id="thuCheck"><label for="thuCheck">ЧТ</label>-->
<!--                <input type="checkbox" id="friCheck"><label for="friCheck">ПТ</label>-->
<!--                <input type="checkbox" id="satCheck"><label for="satCheck">СБ</label>-->
<!--                <input type="checkbox" id="sunCheck"><label for="sunCheck">ВС</label>-->
<!--            </div>-->
<!--            <label for="startRouteTimeInput">Старт маршрута: </label>-->

<!--            <input id="startRouteTimeInput" data-editor-field="startRouteTimeInput">-->




            <div >
                <div style="width: 49%; display: inline-block">
                    <table id="routePointsTable" >
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
                    <table id="relationsBetweenRoutePointsTable" >
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
</body>