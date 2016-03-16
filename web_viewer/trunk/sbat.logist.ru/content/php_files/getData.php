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
    $start = (int)$_POST['start'];
    $count = (int)$_POST['length'];
    $columnInformation = $_POST['columns'];
    $orderColumnNumber = $_POST['order'][0]['column'];

    $dataArray = $privUser->getRequestsForUser()->selectAllData($columnInformation, $orderColumnNumber, $start, $count);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData

        "data" => $dataArray['requests']   // total data array
    );
    return json_encode($json_data);
}


function changeStatusForRequest(PrivilegedUser $privUser)
{

    $requestIDExternal = $_POST['requestIDExternal'];
    $newStatusID = $_POST['newStatusID'];
    $comment = $_POST['comment'];
    $datetime = $_POST['date'];
    $userID = $privUser->getUserInfo()->getData('userID');

    return $privUser->getRequestEntity()->updateRequestStatus($userID, $requestIDExternal, $newStatusID, $datetime, $comment);
}


// TODO get collection of requests from $requestIdExternalArray
function changeStatusForSeveralRequests(PrivilegedUser $privUser)
{
    
    $requests = $_POST['invEdExt'];
    $newStatusID = $_POST['newStatusID'];
    $comment = $_POST['comment'];
    $datetime = $_POST['date'];
    $userID = $privUser->getUserInfo()->getData('userID');
    if (isset($_POST['palletsQty'])) {
        return $privUser->getRequestEntity()->updateRequestStatuses($userID, $requests, $newStatusID, $datetime, $comment, $_POST['palletsQty']);
    }
    return $privUser->getRequestEntity()->updateRequestStatuses($userID, $requests, $newStatusID, $datetime, $comment);
}