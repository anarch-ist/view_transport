<head>
    <meta charset="UTF-8">
    <title>Введите логин и пароль</title>
    <link rel="stylesheet" href="content/css/style.css"/>
    <link rel="stylesheet" href="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.css"/>
    <script type="text/javascript" src="../common_files/media/jQuery-2.1.4/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="../common_files/media/jQueryUI-1.11.4/jquery-ui.min.js"></script>
    <script type="text/javascript" src="content/js/login.js"></script>
    <script type="text/javascript" src="content/js/md5.js"></script>
</head>
<body>
<div class="container">
    <div class="main">
        <form class="form" method="post" action="#">

            <h2>Авторизуйтесь для начала работы с системой</h2>

            <label for="loginInput">Логин :</label>
            <input type="text" name="login" id="loginInput">

            <label for="passwordInput">Пароль :</label>
            <input type="password" name="password" id="passwordInput">

            <div id="loginErrorContainer"></div>

            <input type="button" name="login" id="loginButton" value="Войти">
        </form>
    </div>
</div>
</body>