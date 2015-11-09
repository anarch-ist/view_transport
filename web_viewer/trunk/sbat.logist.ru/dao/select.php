<html>
<head>
</head>
<body>
<?php
include_once('./userDao/UserDAO.php');
$userDAO = UserDAO::getInstance();
$users = $userDAO->selectUsers();
foreach ($users as $user) {
    echo $user . '<br>' . PHP_EOL;
}
?>
<button onclick="document.location='add.php'">добавить</button>
</button><button onclick="">изменить</button>
</body>
</html>