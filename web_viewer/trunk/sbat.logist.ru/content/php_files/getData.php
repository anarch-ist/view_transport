<?php
include_once __DIR__.'/../../common_files/privilegedUser/PrivilegedUser.php';
try {
    $privUser = PrivilegedUser::getInstance();
    if (!isset($_POST['status'])) {
        throw new NoStatusException('');
    } else if ($_POST['status'] === 'getInvoicesForUser') {
        getInvoicesForUser($privUser);
    } else if ($_POST['status'] === 'changeStatusForInvoice') {
        changeStatusForInvoice($privUser);
    } else if ($_POST['status'] === 'changeStatusForSeveralInvoices') {
        changeStatusForSeveralInvoices($privUser);
    }
} catch (Exception $ex) {
    echo $ex->getMessage();
}
function getInvoicesForUser(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getInvoicesForUser()->selectAllData($_POST['start'],$_POST['length']);
    $totalData = count($dataArray);
    $totalFiltered = $totalData;
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($totalData),  // total number of records
        "recordsFiltered" => intval($totalFiltered), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => $dataArray   // total data array
    );
    echo json_encode($json_data);
}

function changeStatusForInvoice(PrivilegedUser $privUser)
{
    $invoiceID = $_POST['invoiceID'];
    $newStatusID = $_POST['newStatusID'];
    $datetime = $_POST['date'];
    echo $privUser->getInvoiceEntity()->updateInvoiceStatus($invoiceID, $newStatusID,$datetime);
}
function changeStatusForSeveralInvoices(PrivilegedUser $privUser)
{
    $routeListID = $_POST['routeListID'];
    $newStatusID = $_POST['newStatusID'];
    $datetime = $_POST['date'];
    echo $privUser->getInvoiceEntity()->updateInvoiceStatuses($routeListID, $newStatusID,$datetime);
}