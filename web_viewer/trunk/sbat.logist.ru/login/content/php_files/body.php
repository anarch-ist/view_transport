<head>
    <meta charset="UTF-8">
    <title>Введите логин и пароль</title>
    <link rel="stylesheet" href="content/css/style.css"/>
    <link rel="stylesheet" href="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.css"/>
    <script type="text/javascript" src="../common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../common_files/media/md5.js"></script>
    <!--custom js-->
    <script type="text/javascript" src="content/js/login.js"></script>
</head>
<body>
<div class="container">
    <div class="main">
        <form class="form" method="post" action="#">
            <label for="loginInput">Логин :</label>
            <input type="text" placeholder="Введите логин" name="login" id="loginInput">

            <label for="passwordInput">Пароль :</label>
            <input type="password" placeholder="Введите пароль" name="password" id="passwordInput">

            <div id="loginErrorContainer"></div>

            <input type="button" name="login" id="loginButton" value="Войти">
        </form>
    </div>
</div>
</body>