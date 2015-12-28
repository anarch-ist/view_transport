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
    <div>
        <label for="routeSelect">Направление: </label>
        <select id="routeSelect">
            <option value="routeId1">Направление1</option>
            <option value="routeId2">Направление2</option>
            <option value="routeId3">Направление3</option>
        </select>
    </div>
    <table id="routePointsTable">
        <thead>
        <tr>
            <th>Порядок</th>
            <th>Пункт</th>
            <th>Время разгрузки</th>
            <th>Время до следующего пункта</th>
            <th>Расстояние, км.</th>
        </tr>
        </thead>
    </table>
</div>
</body>