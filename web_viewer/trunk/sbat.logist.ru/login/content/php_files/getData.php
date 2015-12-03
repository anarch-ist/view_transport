<?php
include_once '\..\..\common_files\privilegedUser\PrivilegedUser.php';

$login = $_POST['login'];
$password = $_POST['password'];
$_GET['login'] = $login;
$_GET['md5'] = $password;
$dataToSend = ['responseCode'=>'', 'statuses'=>''];
try {

    // FIXME бросает ошибку если после logout производится повторная авторизация
    $pUser = PrivilegedUser::getInstance();

    // User class instances must contain session data. read session data from database inside user DAO
    //$pUser->getUserInfo()->

    // redirect to page like http://sbat.logist.ru/user?login=ivan&role=warehouse_manager
    //header("Location: /?login=ivan&role=warehouse_manager", true, 303);

    // TODO connect with database
    $possibleStatuses = $pUser->getInvoiceEntity()->getInvoiceStatuses($pUser);
    $dataToSend['responseCode'] = '';
    $dataToSend['statuses'] = $possibleStatuses;

} catch (Exception $ex) {
    $dataToSend['responseCode'] = $ex->getMessage();
    $dataToSend['statuses'] = '';
} finally {
    echo(json_encode($dataToSend));
}