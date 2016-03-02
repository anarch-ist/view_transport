<?php
ob_start();
//include 'E:\web\www\sbat.logist.ru\common_files\sessionAndCookieWork\SessionAndCookieWork.php';
//$sacw = new SessionAndCookieWork\SessionAndCookieWork();
//$sacw->startSession();
//$_COOKIE['SESSION_CHECK_STRING'] = md5(session_id() . '2');
//$sacw->setSessionParameter('UserID', '2');
//$sacw->setSessionParameter('md5', md5('test'));
//$_POST['status']='getInvoicesForRouteList';
//$_POST['routelist'] = 1;
//include 'E:\web\www\sbat.logist.ru\content\php_files\getData.php';

$connection = @new mysqli('localhost', 'andy', 'andyandy', 'transmaster_transport_db');
$array = array();
$arrayCount = 0;
if ($connection->multi_query("CALL selectUsers(0,20,'position',true,'');")) {
    do {
        /* получаем первый результирующий набор */
        if ($result = $connection->store_result()) {
            $count = 0;
            while ($row = $result->fetch_assoc()) {
                $array[$arrayCount][$count] = $row;
                $count++;
            }
            $arrayCount++;
            $result->free();
        }
    } while ($connection->more_results() && $connection->next_result());
}
$array = selectUsers($array);
print_r(intval($array['totalFiltered'][0]['totalFiltered']));


function selectUsers($res)
{
    $array = $res;
    $arrayResult = array();
    $arrayResult['users'] = $array[0];
    $arrayResult['totalFiltered'] = $array[1];
    $arrayResult['totalCount'] = $array[2];
    return $arrayResult;
}