<head>
    <meta charset="UTF-8">
    <title>Введите логин и пароль</title>
    <link rel="stylesheet" href="content/css/style.css"/>
    <script type="text/javascript" src="../common_files/media/js/jquery.js"></script>
    <script type="text/javascript" src="content/js/login.js"></script>
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