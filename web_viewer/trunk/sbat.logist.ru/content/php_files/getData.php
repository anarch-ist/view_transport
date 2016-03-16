<?php
include_once __DIR__ . '/../../common_files/privilegedUser/PrivilegedUser.php';
try {
    $privUser = PrivilegedUser::getInstance();
    if (!isset($_POST['status'])) {
        throw new DataTransferException('Не задан параметр "статус"', __FILE__);
    } else if (strcasecmp($_POST['status'],'getInvoicesForUser')===0) {
        echo getInvoicesForUser($privUser);
    } else if (strcasecmp($_POST['status'],'changeStatusForInvoice')===0) {
        echo changeStatusForInvoice($privUser);
    } else if (strcasecmp($_POST['status'],'changeStatusForSeveralInvoices')===0) {
        echo changeStatusForSeveralInvoices($privUser);
    } else if (strcasecmp($_POST['status'],'getStatusHistory')===0) {
        echo getStatusHistory($privUser);
    } else if (strcasecmp($_POST['status'],'getInvoicesForRouteList')===0) {
        echo getInvoicesForRouteList($privUser);
    }
} catch (Exception $ex) {
    echo $ex->getMessage();
}
function getStatusHistory(PrivilegedUser $privUser)
{
    $invoiceNumber = $_POST['invoiceNumber'];
    if (!isset($invoiceNumber) || empty($invoiceNumber)) {
        throw new DataTransferException('Не задан параметр "номер накладной"', __FILE__);
    }
    $data = $privUser->getInvoiceEntity()->getInvoiceHistoryByInvoiceNumber($invoiceNumber);
    return json_encode($data);
}
function getInvoicesForRouteList(PrivilegedUser $privUser)
{
    if (!isset($_POST['routeListID'])) {
        throw new DataTransferException('Не задан параметр "маршрутный лист"', __FILE__);
    }
    $routelist = $_POST['routeListID'];
    if (empty($routelist)) {
        throw new DataTransferException('Не задан параметр "номер накладной"', __FILE__);
    }
    $data = $privUser->getInvoiceEntity()->getInvoicesForRouteList($routelist);
    return json_encode($data);
}

function getInvoicesForUser(PrivilegedUser $privUser)
{
    $start = (int)$_POST['start'];
    $count = (int)$_POST['length'];
    $columnInformation = $_POST['columns'];
    $orderColumnNumber = $_POST['order'][0]['column'];
    $dataArray = $privUser->getInvoicesForUser()->selectAllData($columnInformation, $orderColumnNumber, $start, $count);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => $dataArray['invoices']   // total data array
    );
    return json_encode($json_data);
}

function changeStatusForInvoice(PrivilegedUser $privUser)
{
    $invoiceNumber = $_POST['invoiceNumber'];
    $newStatusID = $_POST['newStatusID'];
    $comment = $_POST['comment'];
    $datetime = $_POST['date'];
    $userID = $privUser->getUserInfo()->getData('userID');
    return $privUser->getInvoiceEntity()->updateInvoiceStatus($userID, $invoiceNumber, $newStatusID, $datetime, $comment);
}

function changeStatusForSeveralInvoices(PrivilegedUser $privUser)
{
    $invoices = $_POST['invEdExt'];
    $newStatusID = $_POST['newStatusID'];
    $comment = $_POST['comment'];
    $datetime = $_POST['date'];
    $userID = $privUser->getUserInfo()->getData('userID');
    if (isset($_POST['palletsQty'])) {
        return $privUser->getInvoiceEntity()->updateInvoiceStatuses($userID, $invoices, $newStatusID, $datetime, $comment, $_POST['palletsQty']);
    }
    return $privUser->getInvoiceEntity()->updateInvoiceStatuses($userID, $invoices, $newStatusID, $datetime, $comment);
}