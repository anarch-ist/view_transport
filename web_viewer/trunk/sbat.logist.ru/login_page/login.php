<?php
include_once '\..\common_files\privilegedUser\PrivilegedUser.php';

$login = $_POST['login']; // Fetching Values from URL.
$password = sha1($_POST['password']); // Password Encryption, If you like you can also leave sha1.
$_GET['login'] = $login;
$_GET['md5'] = $password;
try {
    $pUser = PrivilegedUser::getInstance();
    // User class instances must contain session data. read session data from database inside user DAO
    //$pUser->getUserInfo()->
    // redirect to page like http://sbat.logist.ru/user?login=ivan&role=warehouse_manager
    //header("Location: /user?login=ivan&role=warehouse_manager", true, 303);
} catch (Exception $logEx) {
    echo $logEx->getMessage();
}