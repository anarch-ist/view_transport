<?php
include_once __DIR__ . '/../../common_files/privilegedUser/PrivilegedUser.php';
try {
    $privUser = PrivilegedUser::getInstance();
    if (!isset($_POST['status'])) {
        throw new DataTransferException('Не задан параметр "статус"', __FILE__);
    } else if (strcasecmp($_POST['status'],'getRequestsForUser')===0) {
        echo getRequestsForUser($privUser);
    } else if (strcasecmp($_POST['status'],'changeStatusForRequest')===0) {
        echo changeStatusForRequest($privUser);
    } else if (strcasecmp($_POST['status'],'changeStatusForSeveralRequests')===0) {
        echo changeStatusForSeveralRequests($privUser);
    } else if (strcasecmp($_POST['status'],'getStatusHistory')===0) {
        echo getStatusHistory($privUser);
    } else if (strcasecmp($_POST['status'],'getRequestsForRouteList')===0) {
        echo getRequestsForRouteList($privUser);
    }
} catch (Exception $ex) {
    echo $ex->getMessage();
}
function getStatusHistory(PrivilegedUser $privUser)
{
    $requestIDExternal = $_POST['requestIDExternal'];
    if (!isset($requestIDExternal) || empty($requestIDExternal)) {
        throw new DataTransferException('Не задан параметр "номер заявки"', __FILE__);
    }
    $data = $privUser->getRequestEntity()->getRequestHistoryByRequestIdExternal($requestIDExternal);
    //exit(print_r($data));
    return json_encode($data);
}
function getRequestsForRouteList(PrivilegedUser $privUser)
{
    if (!isset($_POST['routeListID'])) {
        throw new DataTransferException('Не задан параметр "маршрутный лист"', __FILE__);
    }
    $routelist = $_POST['routeListID'];
    if (empty($routelist)) {
        throw new DataTransferException('Не задан параметр "номер заявки"', __FILE__);
    }
    $data = $privUser->getRequestEntity()->getRequestsForRouteList($routelist);
    return json_encode($data);
}

function getRequestsForUser(PrivilegedUser $privUser)
{
    //exit(print_r($_POST['order']));

    $start = (int)$_POST['start'];
    $count = (int)$_POST['length'];
    $columnInformation = $_POST['columns'];
    $orderColumnNumber = $_POST['order'][0]['column'];
    $dataArray = $privUser->getRequestsForUser()->selectAllData($columnInformation, $orderColumnNumber, $start, $count);
    // remove date seconds from
    // $dataArray['requestDate'] = explode(' ', $dataArray['requestDate'])[0];
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => $dataArray['requests']   // total data array
    );
    foreach($dataArray['requests'] as $key => $value){
        $json_data['data'][$key]['requestDate'] = date('d-m-Y', strtotime($value['requestDate']));
        $json_data['data'][$key]['invoiceDate'] = date('d-m-Y', strtotime($value['invoiceDate']));
        $json_data['data'][$key]['documentDate'] = date('d-m-Y', strtotime($value['documentDate']));
        $json_data['data'][$key]['arrivalTimeToNextRoutePoint'] = date('d-m-Y H:i:s', strtotime($value['arrivalTimeToNextRoutePoint']));
    }
    return json_encode($json_data);
}

function changeStatusForRequest(PrivilegedUser $privUser)
{
    $requestIDExternal = $_POST['requestIDExternal'];
    $newStatusID = $_POST['newStatusID'];
    $vehicleNumber = $_POST['vehicleNumber'];
    $comment = $_POST['comment'];
    $datetime = $_POST['date'];
    $userID = $privUser->getUserInfo()->getData('userID');
    return $privUser->getRequestEntity()->updateRequestStatus($userID, $requestIDExternal, $newStatusID, $datetime, $comment, $vehicleNumber);
}

function changeStatusForSeveralRequests(PrivilegedUser $privUser)
{
    $requests = $_POST['requestIDExternalArray'];
    $routeListID = $_POST['routeListID'];
    $newStatusID = $_POST['newStatusID'];
    $vehicleNumber = $_POST['vehicleNumber'];
    $comment = $_POST['comment'];
    $datetime = $_POST['date'];
    $userID = $privUser->getUserInfo()->getData('userID');
    if (isset($_POST['palletsQty'])) {
        return $privUser->getRequestEntity()->updateRequestStatuses2($userID, $routeListID, $requests, $newStatusID, $datetime, $comment, $vehicleNumber, $_POST['palletsQty']);
    }
    return $privUser->getRequestEntity()->updateRequestStatuses($userID, $routeListID, $requests, $newStatusID, $datetime, $comment, $vehicleNumber);
}