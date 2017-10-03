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



    <!--    Applying JS-->
<!--    <script src="common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js" type="text/javascript"></script>-->
    <!--    <script src="common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>-->
<!--    <script src="common_files/media/Bootstrap-3.3.2/js/bootstrap.min.js" type="text/javascript"></script>-->
<!--    <script src="common_files/media/DataTables-1.10.10/js/jquery.dataTables.min.js" type="text/javascript"></script>-->
<!--    <script src="common_files/media/jquery-mask/dist/jquery.mask.min.js" type="text/javascript"></script>-->

    <!-- Если вы используете API локально, то в URL ресурса необходимо указывать протокол в стандартном виде (http://...)-->
    <script src="http://api-maps.yandex.ru/2.1/?lang=ru-RU" type="text/javascript"></script>
    <script src="http://yandex.st/jquery/2.2.3/jquery.min.js" type="text/javascript"></script>
    <script src="common_files/media/yMap-2.1/pie-chart-clusterer.min.js" type="text/javascript"></script>

    <!--    Custom JS-->
    <script src="content/js/mapPage.js" type="text/javascript"></script>

    <!--    <script src="common_files/media/DataTables-1.10.10/js/dataTables.jqueryui.min.js" type="text/javascript"></script>-->
    <style>
        body, html {
            padding: 0;
            margin: 0;
            width: 100%;
            height: 100%;
        }
        #map {
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
<div id="map"></div>


</body>

</html>
