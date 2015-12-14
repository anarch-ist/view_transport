<?php
include_once __DIR__.'/../../../common_files/privilegedUser/PrivilegedUser.php';

$dataToSend = ['responseCode'=>'', 'statuses'=>''];
try {

    // FIXME бросает ошибку если после logout производится повторная авторизация
    $pUser = PrivilegedUser::getInstance('auth');

    // User class instances must contain session data. read session data from database inside user DAO
    //$pUser->getUserInfo()->

    // redirect to page like http://sbat.logist.ru/user?login=ivan&role=warehouse_manager
    //header("Location: /?login=ivan&role=warehouse_manager", true, 303);

    // TODO connect with database
    $possibleStatuses = $pUser->getInvoiceEntity()->getInvoiceStatuses($pUser);
    $dataToSend['responseCode'] = '';
    $dataToSend['statuses'] = $possibleStatuses;
    echo(json_encode($dataToSend));

} catch (Exception $ex) {
    $dataToSend['responseCode'] = $ex->getMessage();
    $dataToSend['statuses'] = '';
    echo(json_encode($dataToSend));
} // finally {
//
//}