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
    }  else if (strcasecmp($_POST['status'],'getRequestByClientIdAndInvoiceNumber')===0) {
        echo getRequestByClientIdAndInvoiceNumber($privUser);
    } else if (strcasecmp($_POST['status'],'addPretension')===0) {
        echo addPretension($privUser);
    } else if (strcasecmp($_POST['status'],'getPretensions')===0){
        echo getPretensions($privUser);
    } else if (strcasecmp($_POST['status'], 'updatePretension')===0){
        echo updatePretension($privUser);
    } else if (strcasecmp($_POST['status'], 'deletePretension')===0){
        echo deletePretension($privUser);
    } else if (strcasecmp($_POST['status'], 'getCompanies')===0){
        echo getCompanies($privUser);
    } else if (strcasecmp($_POST['status'], 'getVehicles')===0){
        echo getVehiclesForCompany($privUser);
    } else if (strcasecmp($_POST['status'], 'getDrivers')===0){
        echo getDriversForVehicle($privUser);
    }
} catch (Exception $ex) {
    echo $ex->getMessage();
}

function getCompanies(PrivilegedUser $privUser){
//    $data = $privUser->getRequestEntity()->getAllTransportCompanies();
    $data = $privUser->getTransportCompanyEntity()->selectAllCompanies();
    
    return(json_encode($data));
}

function getVehiclesForCompany(PrivilegedUser $privUser){
    $companyId = (int) $_POST['companyId'];
    if(!isset($companyId)){
        throw new DataTransferException('Не задан ID компании', __FILE__);
    }
    $data = $privUser->getVehicleEntity()->selectVehicleByCompanyId($companyId);
    return(json_encode($data));
}
function getDriversForVehicle(PrivilegedUser $privUser){
    $vehicleId = $_POST['vehicleId'];
    if(isset($vehicleId)){
        $data = $privUser->getDriverEntity()->selectDriverByVehicleId($vehicleId);
        return(json_encode($data));    
    } else return null;
    
}

function deletePretension(PrivilegedUser $privUser){
    $requestIDExternal = $_POST['requestIDExternal'];
    $pretensionID = $_POST['pretensionID'];
    if(!isset($requestIDExternal) || empty($requestIDExternal)){
        throw new DataTransferException('Не задан requestIDExternal', __FILE__);
    } elseif (!isset($pretensionID) || empty($pretensionID)){
        throw new DataTransferException('Не задан номер претензии', __FILE__);
    }
    $data = $privUser->getRequestEntity()->closePretension($pretensionID,$requestIDExternal);
    return(json_encode($data));
}

function updatePretension(PrivilegedUser $privUser){
    $commentRequired = ($_POST['commentRequired'] == 'true') ? True : false ;
    $pretensionID = $_POST['pretensionID'];
    $requestIDExternal = $_POST['requestIDExternal'];
    $pretensionComment = $_POST['pretensionComment'];
////    $pretensionStatus = $_POST['pretensionStatus'];
    $pretensionCathegory = $_POST['pretensionCathegory'];
    $pretensionSum = $_POST['pretensionSum'];
    $pretensionPositionNumber= $_POST['pretensionPositionNumber'];
    if(!isset($requestIDExternal) || empty($requestIDExternal)){
        throw new DataTransferException('Не задана заявка', __FILE__);
    } elseif (!isset($pretensionCathegory) || empty($pretensionCathegory)){
        throw new DataTransferException('Не задана категория претензии',__FILE__);
    }  elseif (!isset($pretensionPositionNumber) || empty($pretensionPositionNumber)){
        throw new DataTransferException('Не задан код позиции', __FILE__);
    } elseif ((!isset($pretensionComment) || empty($pretensionComment))&& $commentRequired){
        throw new DataTransferException("Не задан комментарий претензии $commentRequired",__FILE__);
    }  elseif (!isset($pretensionSum) || empty($pretensionSum)){
//        throw new DataTransferException('Не задана сумма', __FILE__);
        $pretensionSum=0;
    }
//    $data = $privUser->getRequestEntity()->addPretension($requestIDExternal,$pretensionStatus,$pretensionComment,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum);
    $data = $privUser->getRequestEntity()->updatePretension($pretensionID,$requestIDExternal,$pretensionComment,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum);
    return(json_encode($data));
}

function getPretensions(PrivilegedUser $privUser){
    $requestIDExternal = $_POST['requestIDExternal'];
    if(!isset($requestIDExternal) || empty($requestIDExternal)){
        throw new DataTransferException('Не задана заявка', __FILE__);
    }
    $data = $privUser->getRequestEntity()->getPretensions($requestIDExternal);
    return json_encode($data);

}

function addPretension(PrivilegedUser $privUser){
    $commentRequired = ($_POST['commentRequired'] == 'true') ? True : false ;
    $requestIDExternal = $_POST['requestIDExternal'];
    $pretensionComment = $_POST['pretensionComment'];
    $pretensionStatus = $_POST['pretensionStatus'];
    $pretensionCathegory = $_POST['pretensionCathegory'];
    $pretensionSum = $_POST['pretensionSum'];
    $pretensionPositionNumber= $_POST['pretensionPositionNumber'];
    if(!isset($requestIDExternal) || empty($requestIDExternal)){
        throw new DataTransferException('Не задана заявка', __FILE__);
    } elseif (!isset($pretensionCathegory) || empty($pretensionCathegory)){
        throw new DataTransferException('Не задана категория претензии',__FILE__);
    }  elseif (!isset($pretensionPositionNumber) || empty($pretensionPositionNumber)){
        throw new DataTransferException('Не задан код позиции', __FILE__);
    } elseif ((!isset($pretensionComment) || empty($pretensionComment))&& $commentRequired){
        throw new DataTransferException("Не задан комментарий претензии $commentRequired",__FILE__);
    }  elseif (!isset($pretensionSum) || empty($pretensionSum)){
//        throw new DataTransferException('Не задана сумма', __FILE__);
        $pretensionSum=0;
    }
    $data = $privUser->getRequestEntity()->addPretension($requestIDExternal,$pretensionStatus,$pretensionComment,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum);
    return json_encode($data);
}

function getRequestByClientIdAndInvoiceNumber(PrivilegedUser $privUser){
    $clientId = $_POST['clientId'];
    $invoiceNumber = $_POST['invoiceNumber'];
    if(!isset($clientId) || empty($clientId) || !isset($invoiceNumber) || empty($invoiceNumber)){
        throw new DataTransferException('Не задан параметр "номер заявки"', __FILE__);
    }
    $data = $privUser->getRequestEntity()->selectRequestByClientIdAndInvoiceNumber($clientId,$invoiceNumber);
    return json_encode($data);
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
    foreach($_POST['columns'] as $key => $value){
        if($value['data'] == 'requestDate' || $value['data'] == 'invoiceDate' || $value['data'] == 'documentDate' || $value['data'] == 'arrivalTimeToNextRoutePoint'){
            if($value['search']['value'] != ''){
                $_POST['columns'][$key]['search']['value'] = date('Y-m-d', strtotime($value['search']['value']));
            }
        }
    }
    $columnInformation = $_POST['columns'];
    //exit(print_r($_POST['columns']));
    $orderColumnNumber = $_POST['order'][0]['column'];
    $dataArray = $privUser->getRequestsForUser()->selectAllData($columnInformation, $orderColumnNumber, $start, $count);
    //echo(print_r($data['dataArray']));
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
        $json_data['data'][$key]['arrivalTimeToNextRoutePoint'] = $value['arrivalTimeToNextRoutePoint']!=0 ? date('d-m-Y H:i:s', strtotime($value['arrivalTimeToNextRoutePoint'])) : "";

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
    $hoursAmount = $_POST['hoursAmount'];
    $companyId = !empty($_POST['companyId']) ? $_POST['companyId'] : "NULL";
    $vehicleId = !empty($_POST['vehicleId']) ? $_POST['vehicleId'] : "NULL";
    $driverId = !empty($_POST['driverId']) ? $_POST['driverId'] : "NULL";
    $userID = $privUser->getUserInfo()->getData('userID');
    if (!isset($hoursAmount) || empty($hoursAmount)) {
        return $privUser->getRequestEntity()->updateRequestStatus($userID, $requestIDExternal, $newStatusID, $datetime, $comment, $vehicleNumber, 0, $companyId, $vehicleId, $driverId);
    } else {
        return $privUser->getRequestEntity()->updateRequestStatus($userID, $requestIDExternal, $newStatusID, $datetime, $comment, $vehicleNumber, $hoursAmount, $companyId, $vehicleId, $driverId);
    }

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
    $palletsQty = !empty($_POST['palletsQty']) ? $_POST['palletsQty'] : 0 ;
    $hoursAmount = !empty($_POST['hoursAmount']) ? $_POST['hoursAmount'] : 0;
    $companyId = !empty($_POST['companyId']) ? $_POST['companyId'] : 0;
    $vehicleId = !empty($_POST['vehicleId']) ? $_POST['vehicleId'] : 0;
    $driverId = !empty($_POST['driverId']) ? $_POST['driverId'] : 0;
//        if (isset($_POST['palletsQty'])) {
            return $privUser->getRequestEntity()->updateRequestStatuses2($userID, $routeListID, $requests, $newStatusID, $datetime, $comment, $vehicleNumber, $palletsQty, $hoursAmount, $companyId, $vehicleId, $driverId);
//        }
//        return $privUser->getRequestEntity()->updateRequestStatuses($userID, $routeListID, $requests, $newStatusID, $datetime, $comment, $vehicleNumber, $hoursAmount);

}