<?php
include_once __DIR__ . '/../../../common_files/privilegedUser/PrivilegedUser.php';
try {
    $privUser = PrivilegedUser::getInstance();
    if (!isset($_POST['status'])) {
        throw new DataTransferException('Не задан параметр "статус"', __FILE__);
    } else if ($_POST['status'] === 'getAllPointIdPointNamePairs') {
        getAllPointIdPointNamePairs($privUser);
    } else if ($_POST['status'] === 'getAllRouteIdDirectionPairs') {
        getAllRouteIdDirectionPairs($privUser);
    } else if ($_POST['status'] === 'getAllRoutePointsDataForRouteID') {
        getAllRoutePointsDataForRouteID($privUser);
    }
} catch (Exception $ex) {
    echo $ex->getMessage();
}
function getAllPointIdPointNamePairs(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getPointEntity()->selectPoints();
    $data = array();
    foreach ($dataArray as $key => $val) {
        if ($val instanceof DAO\PointData)
        {
            $data[$key]['pointID'] = $val->getData('pointID');
            $data[$key]['pointName'] = $val->getData('pointName');
        }
    }
    echo json_encode($data);
    //throw new DataTransferException('need to fill method', __FILE__);
//    $invoiceNumber = $_POST['invoiceNumber'];
//    if (!isset($invoiceNumber) || empty($invoiceNumber)) {
//        throw new DataTransferException('Не задан параметр "номер накладной"',__FILE__);
//    }
//    $data = $privUser->getInvoiceEntity()->getInvoiceHistoryByInvoiceNumber($invoiceNumber);
//    echo json_encode($data);
}

function getAllRouteIdDirectionPairs(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getRouteEntity()->selectRoutes();
    $data = array();
    foreach ($dataArray as $key => $val) {
        if ($val instanceof DAO\RouteData)
        {
            $data[$key]['routeID'] = $val->getData('routeID');
            $data[$key]['directionName'] = $val->getData('directionName');
        }
    }
    echo json_encode($data);
    //throw new DataTransferException('need to fill method',__FILE__);
//    $dataArray = $privUser->getInvoicesForUser()->selectAllData($_POST['start'], $_POST['length']);
//    $totalData = count($dataArray);
//    $totalFiltered = $totalData;
//    $json_data = array(
//        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
//        "recordsTotal" => intval($totalData),  // total number of records
//        "recordsFiltered" => intval($totalFiltered), // total number of records after searching, if there is no searching then totalFiltered = totalData
//        "data" => $dataArray   // total data array
//    );
//    echo json_encode($json_data);
}

function getAllRoutePointsDataForRouteID(PrivilegedUser $privUser)
{
    if (!isset($_POST['routeID'])) {
        throw new DataTransferException('Не задан параметр "идентификатор маршрута"',__FILE__);
    }
    $routeID = $_POST['routeID'];
    $dataArray = $privUser->getRouteEntity()->selectRoutePointsByRouteID($routeID);
    $data = array();
    foreach ($dataArray as $val) {
        if ($val instanceof DAO\RoutePointData)
        {
            $data[] = $val->toArray();
        }
    }
    echo json_encode($data);
    //throw new DataTransferException('need to fill method',__FILE__);
}