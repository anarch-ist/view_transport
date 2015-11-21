<?php
//get instance of userDAO
$userDao = \DAO\UserDAO::getInstance();

$login = $_POST['login']; // Fetching Values from URL.
$password = sha1($_POST['password']); // Password Encryption, If you like you can also leave sha1.

try {
    $result = $userDao->selectUserByLoginAndPassword($login, $password);
    // User class instances must contain session data. read session data from database inside user DAO

    // redirect to page like http://sbat.logist.ru/user?login=ivan&role=warehouse_manager
    header('Location: /user?login=ivan&role=warehouse_manager', true, 303);
} catch (NoSuchLoginException $logEx) {
    echo $logEx->getMessage();
} catch (IllegalPasswordException $passEx) {
    echo $passEx->getMessage();
}