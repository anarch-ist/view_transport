<?php
include_once __DIR__ . '/../../common_files/privilegedUser/PrivilegedUser.php';
require_once __DIR__ . '/../../common_files/utility/MakeshiftWialonAPI.php';
//    error_reporting(E_ALL);
//    ini_set('display_errors', 1);
try {
    $privUser = PrivilegedUser::getInstance();
    $action = $_POST['status'];
    if (!isset($action)) {
        throw new DataTransferException('Не задан параметр "статус"', __FILE__);
    } else if (strcasecmp($action, 'getRequestsForUser') === 0) {
        echo getRequestsForUser($privUser);
    } else if (strcasecmp($action, 'changeStatusForRequest') === 0) {
        echo changeStatusForRequest($privUser);
    } else if (strcasecmp($action, 'changeStatusForSeveralRequests') === 0) {
        echo changeStatusForSeveralRequests($privUser);
    } else if (strcasecmp($action, 'getStatusHistory') === 0) {
        echo getStatusHistory($privUser);
    } else if (strcasecmp($action, 'getRequestsForRouteList') === 0) {
        echo getRequestsForRouteList($privUser);
    } else if (strcasecmp($action, 'getRequestByClientIdAndInvoiceNumber') === 0) {
        echo getRequestByClientIdAndInvoiceNumber($privUser);
    } else if (strcasecmp($action, 'addPretension') === 0) {
        echo addPretension($privUser);
    } else if (strcasecmp($action, 'getPretensions') === 0) {
        echo getPretensions($privUser);
    } else if (strcasecmp($action, 'updatePretension') === 0) {
        echo updatePretension($privUser);
    } else if (strcasecmp($action, 'deletePretension') === 0) {
        echo deletePretension($privUser);
    } else if (strcasecmp($action, 'getCompanies') === 0) {
        echo getCompanies($privUser);
    } else if (strcasecmp($action, 'getVehicles') === 0) {
        echo getVehiclesForCompany($privUser);
    } else if (strcasecmp($action, 'getDrivers') === 0) {
        echo getDriversForVehicle($privUser);
    } else if (strcasecmp($action, 'getRequestById') === 0) {
        echo getRequestById($privUser);
    } else if (strcasecmp($action, 'getDocuments') === 0) {
        echo getDocuments();
    } else if (strcasecmp($action, 'getClientsByINN') === 0) {
        echo getClientsByINN($privUser);
    } else if (strcasecmp($action, 'uploadDocuments') === 0) {
        echo uploadDocuments();
    } else if (strcasecmp($action, 'addGroup') === 0) {
        createDocumentGroup();
    } else if (strcasecmp($action, 'getPointsByName') === 0) {
        echo getPointsByName($privUser);
    } else if (strcasecmp($action, 'getRouteListsByNumber') === 0) {
        echo getRouteListsByNumber($privUser);
    } else if (strcasecmp($action, 'getMarketAgentsByName') === 0) {
        echo getMarketAgentByName($privUser);
    } else if (strcasecmp($action, 'addRouteList') === 0) {
        echo addRouteList($privUser);
    } else if (strcasecmp($action, 'getAllRouteIdDirectionPairs') === 0) {
        getAllRouteIdDirectionPairs($privUser);
    } else if (strcasecmp($action, 'addRequest') === 0) {
        addRequest($privUser);
    } else if (strcasecmp($action, 'deleteRequest') === 0) {
        deleteRequest($privUser);
    } else if (strcasecmp($action, 'editRequest') === 0) {
        echo editRequest($privUser);
    } else if (strcasecmp($action, 'getAllPointsCoordinates') === 0) {
        echo getAllPointsCoordinates($privUser);
    } else if (strcasecmp($action, 'getPointsCoordinatesForRequest') === 0) {
        echo getPointsCoordinatesForRequest($privUser);
    } else if (strcasecmp($action, 'getVehicleData') === 0) {
        echo getVehicleData($privUser);
    } else if (strcasecmp($action, 'updateRequestsViaWialonApi') === 0) {
        echo updateRequestsViaWialonApi($privUser);
    } else if (strcasecmp($action, 'getRouteListsData') === 0) {
        echo getRouteListsData($privUser);
    } else if (strcasecmp($action, 'getRouteListData') === 0) {
        echo getRouteListData($privUser);
    } else if (strcasecmp($action, 'getRouteListById') === 0) {
        echo getRouteListById($privUser);
    } else if (strcasecmp($action, 'getAllWialonResources') === 0) {
        echo getAllWialonResources($privUser);
    } else if (strcasecmp($action, 'getTCPageRouteLists') === 0) {
        echo getTCPageRouteLists($privUser);
    }else if (strcasecmp($action, 'getTCPageDrivers') === 0) {
        echo getTCPageDrivers($privUser);
    }else if (strcasecmp($action, 'getTCPageVehicles') === 0) {
        echo getTCPageVehicles($privUser);
    }else if (strcasecmp($action, 'TCPageVehiclesEditing') === 0) {
            if (!isset($_POST['action'])) {
                throw new DataTransferException('Не задан параметр "действие"', __FILE__);
            }
            $action = $_POST['action'];
            if (strcasecmp($action, 'remove') === 0) {
                removeVehicleForTransportCompany($privUser);
            } else if (strcasecmp($action, 'edit') === 0) {
                updateVehicleForTransportCompany($privUser);
            } else if (strcasecmp($action, 'create') === 0) {
                addVehicleForTransportCompany($privUser);
            } else {
                throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
            }
    }  else if (strcasecmp($action, 'addVehicleFromVMap') === 0) {
        echo addVehicleFromVMap($privUser);
    } else if (strcasecmp($action, 'getClientSideRequestsForUser')===0){
        echo getClientSideRequestsForUser($privUser);
    }
} catch (Exception $ex) {
    echo $ex->getMessage();
}

function addVehicleFromVMap(PrivilegedUser $privilegedUser){

    $data = $_POST['data'];
    $response=$privilegedUser->getVehicleEntity()->insertVehicle($data);
    if ($response){
        return true;
    } else {
        return json_encode($response);
    }
}

function getAllWialonResources(PrivilegedUser $privUser)
{
    $wialonResources = new AllVehiclesData();
    $vehiclesData = $wialonResources->getVehicles();
    $vehiclePlacemarks = [];


    function getHtmlForm($WialonId){
    $formHtml='<div class="openAccordion" onclick="openAccordion()" ; >Добавить в базу данных</div>' .
    '        <div class="accordion" style="display:none;">' .
    '            <form class="form-group" method="post" action="content/getData.php" style="">' .
    '                <label class="form-group">Номер лицензии' .
    '                    <input type="text" name="license_number" class="inputLicense form-control"' .
    '                           placeholder="Номер лицензии ТС"></label>' .
    '                <label class="form-group">Модель ТС' .
    '                    <input type="text" name="model" class="form-control"' .
    '                           placeholder="Модель ТС"></label>' .
    '                <label class="form-group">Тип ТС' .
    '                        <select class="form-control" name="type">' .
    '                            <option>Тент</option>' .
    '                            <option>Термос</option>' .
    '                            <option>Рефрижератор</option>' .
    '                        </select>' .
    '                </label>' .
    '                <label class="form-group">Тип погрузки' .
    '                    <select class="form-control" name="loading_type">' .
    '                        <option>Задняя</option>' .
    '                        <option>Боковая</option>' .
    '                        <option>Верхняя</option>' .
    '                    </select>' .
    '                </label>' .
    '                <label class="form-group">Объем M<sup>3</sup>' .
    '' .
    '                    <input type="text" class="form-control" placeholder="123" onkeypress="return isNumberKey(event)"' .
    '                           name="volume">' .
    '                </label>' .
    '                <label class="form-group">Кол-во паллет' .
    '                    <input type="text" class="form-control" placeholder="123" onkeypress="return isNumberKey(event)"' .
    '                           name="pallets_quantity">' .
    '                </label>' .
    '                <label class="form-group">Лимит загрузки КГ' .
    '                    <input type="text" class="form-control" placeholder="123" onkeypress="return isNumberKey(event)"' .
    '                           name="carrying_capacity">' .
    '                </label>' .
    '                <label class="form-group" style="display: none">WialonID' .
    '                    <input type="text" name="wialon_id"  class="form-control"' .
    '                           placeholder="" value="'.$WialonId.'">' .
    '                </label>' .
        '<input type="text" name="status" style="display:none" value="addVehicleFromVMap">'.
        '<input type="text" name="transport_company_id" style="display:none" value="null">'.
    '                <div class="form-group">' .
    '                    <input type="submit"  class="btn btn-default" value="Отправить">' .
    '                </div>' .
    '            </form>' .
    '        </div>';

    return $formHtml;
    }


    foreach ($vehiclesData as $vehicle) {
        if ($vehicle->getWialonId() != null) {
            $vehiclePlacemarkOptions = new YandexApiOptions();
            $vehiclePlacemarkProperties = new YandexApiProperties();
            $coordinates = $vehicle->getCoordinates();
            $vehiclePlacemark = new YandexApiPlacemark($coordinates->x, $coordinates->y, $vehiclePlacemarkProperties, $vehiclePlacemarkOptions);
            $registeredVehicle = $privUser->getVehicleEntity()->getVehicleByWialonId($vehicle->getWialonId());
            if ($registeredVehicle) {
                $vehiclePlacemarkOptions->createProperty('preset', 'islands#darkGreenDeliveryIcon');
                $vehiclePlacemarkProperties->createProperty('balloonContent',
                    "WialonId: <b>" . $vehicle->getWialonId() . "</b><br>" .
                    "Номер лицензии: " . $registeredVehicle['license_number'] . "<br>" .
                    "Модель: " . $registeredVehicle['model'] . "<br>" .
                    "Тип: " . $registeredVehicle['type'] . "<br>" .
                    "Тип погрузки: " . $registeredVehicle['loading_type']);
                $vehiclePlacemarkProperties->createProperty('balloonContentFooter', "В скором времени здесь будет показываться информация о компании-владельце автомобиля и его водителях");
            } else {
                $vehiclePlacemarkOptions->createProperty('preset', 'islands#violetDeliveryIcon');
                $vehiclePlacemarkOptions->createProperty('balloonMinWidth',300);
//                $vehiclePlacemarkOptions->createProperty('balloonMinHeight',200);
                $vehiclePlacemarkProperties->createProperty('balloonContent', "WialonId: <b>" . $vehicle->getWialonId() . "</b><br><div class='form-placement'>".getHtmlForm($vehicle->getWialonId())."</div>");
            }
            $vehiclePlacemarkProperties->createProperty('balloonContentHeader', "Транспорт: " . $vehicle->getName());
            array_push($vehiclePlacemarks, $vehiclePlacemark);
        }
    }

    return json_encode($vehiclePlacemarks);
}


function getRouteListById(PrivilegedUser $privilegedUser)
{
    return json_encode($privilegedUser->getRouteListEntity()->getRouteListByID($_POST['routeListID']));
}

function getRouteListData(PrivilegedUser $privilegedUser)
{
    $jsonData = [];
    $routeListId = $_POST['routeListID'];

    $jsonData['data'] = $privilegedUser->getRequestEntity()->getRequestsForRouteList($routeListId);
    return json_encode($jsonData);
}

function getRouteListsData(PrivilegedUser $privilegedUser)
{
    $jsonData = [];
    $jsonData['data'] = $privilegedUser->getRouteListEntity()->getRouteListsForRLPage();
    return json_encode($jsonData);
}

function updateRequestsViaWialonApi(PrivilegedUser $privilegedUser)
{
//    sqrt(((2-5)^2)+((1-5)^2));

}

function getVehicleData(PrivilegedUser $privilegedUser)
{


    $requestIDExternal = $_POST['requestIDExternal'];
    $requestData = $privilegedUser->getRequestEntity()->selectRequestByID($requestIDExternal);
    if ($requestData['vehicleId'] != null) {
        $vehicleData = $privilegedUser->getVehicleEntity()->selectVehicleById($requestData['vehicleId']);
        if ($vehicleData['wialon_id'] != null) {
            $wialonVehicle = new WialonVehicle($vehicleData['wialon_id']);
            $coordinates = $wialonVehicle->getCoordinates();
            $vehiclePlacemarkOptions = new YandexApiOptions();
            $vehiclePlacemarkOptions->createProperty('preset', 'islands#violetAutoIcon');
            $vehiclePlacemarkProperties = new YandexApiProperties();
            $vehiclePlacemarkProperties->createProperty('balloonContentHeader', "Транспортное средство");
            $vehiclePlacemarkProperties->createProperty('balloonContent', "Транспортное средство: <b>" . $vehicleData['model'] . "</b><br>Номер: <b>" . $vehicleData['license_number'] . "</b>");
            $vehiclePlacemark = new YandexApiPlacemark($coordinates->x, $coordinates->y, $vehiclePlacemarkProperties, $vehiclePlacemarkOptions);
            $vehicleData['vehiclePlacemark'] = $vehiclePlacemark;
        }

        return json_encode($vehicleData);
    }
    return json_encode(false);

}

function getPointsCoordinatesForRequest(PrivilegedUser $privilegedUser)
{

    $requestIDExternal = $_POST['requestIDExternal'];
    $requestData = $privilegedUser->getRequestEntity()->selectRequestByID($requestIDExternal);
    $routePoints = array();
    if ($requestData['warehousePointID'] != null) {
        $warehousePoint = $privilegedUser->getPointEntity()->getPointCoordinatesByPointId($requestData['warehousePointID'])[0];
        if ($warehousePoint != null) {


//        $routePoints['warehousePoint']=[floatval($warehousePoint[0]['y']),floatval($warehousePoint[0]['x'])];
            $properties = new YandexApiProperties();
            $properties->createProperty('balloonContentHeader', '<b>Склад отправки</b><br>');
            $properties->createProperty('balloonContent', $warehousePoint['pointName']);


            $routePoints['warehousePoint'] = new YandexApiPlacemark($warehousePoint['x'], $warehousePoint['y'], $properties, new YandexApiOptions());
//        array_push($routePoints,new YandexApiRoutePoint($warehousePoint[0]['x'],$warehousePoint[0]['y'],'wayPoint',$warehousePoint[0]['address']));
//        array_push($routePoints, [floatval($warehousePoint[0]['y']),floatval($warehousePoint[0]['x'])]);
        }
    }
    if ($requestData['lastVisitedRoutePointID'] != null) {
        $transitPoint = $privilegedUser->getPointEntity()->getPointCoordinatesByPointId($requestData['lastVisitedRoutePointID'])[0];
        if ($transitPoint != null) {
            $properties = new YandexApiProperties();
            $properties->createProperty('balloonContentHeader', '<b>Последняя посещеная точка</b><br>');
            $properties->createProperty('balloonContent', $transitPoint['pointName']);

            $routePoints['lastVisitedPoint'] = new YandexApiPlacemark($transitPoint['x'], $transitPoint['y'], $properties, new YandexApiOptions());
        }

    }
    if ($requestData['destinationPointID'] != null) {
        $destinationPoint = $privilegedUser->getPointEntity()->getPointCoordinatesByPointId($requestData['destinationPointID'])[0];
        if ($destinationPoint != null) {

            $options = new YandexApiOptions();
            $options->createProperty('preset', 'islands#blueGovernmentIcon');
//        $routePoints['destinationPoint']=[floatval($destinationPoint[0]['y']),floatval($destinationPoint[0]['x'])];
            $properties = new YandexApiProperties();
            $properties->createProperty('balloonContentHeader', '<b>Точка назначения</b><br>');
            $properties->createProperty('balloonContent', $destinationPoint['pointName']);
            $routePoints['destinationPoint'] = new YandexApiPlacemark($destinationPoint['x'], $destinationPoint['y'], $properties, $options);
//        array_push($routePoints,new YandexApiRoutePoint($destinationPoint[0]['x'],$destinationPoint[0]['y'],'wayPoint',$destinationPoint[0]['address']));
//        array_push($routePoints, [floatval($destinationPoint[0]['y']),floatval($destinationPoint[0]['x'])]);
        }
    }
    return json_encode($routePoints);
//    return json_encode([[55.755786, 37.117633], [55.155786, 37.617633]]);
}

function getAllPointsCoordinates(PrivilegedUser $privilegedUser)
{
    if($managerId = $privilegedUser->getUserInfo()->getData('userRoleID')=='ADMIN'){
        $data = $privilegedUser->getPointEntity()->getAllDistinctPointCoordinates();
    } else {
        $managerId = $privilegedUser->getUserInfo()->getData('userID');
        $data = $privilegedUser->getPointEntity()->getDistinctPointCoordinatesForManager($managerId);
    }
    $avg_count = $privilegedUser->getPointEntity()->getAverageRequestsCount()['avg_count'];
    $featureCollection = new YandexApiFeatureCollection();
    $options = new YandexApiOptions();
    $id = 0;
    foreach ($data as $row) {
        $geometryPoint = new YandexApiGeometryPoint($row['y'], $row['x']);
        $properties = new YandexApiProperties();
        $options = new YandexApiOptions();
        if ($row['requests'] > ($avg_count * 2)) $options->createProperty('preset', 'islands#redIcon');
        elseif ($row['requests'] > $avg_count) $options->createProperty('preset', 'islands#orangeIcon');
        elseif ($row['requests'] > 0) $options->createProperty('preset', 'islands#darkGreenIcon');
        else $options->createProperty('preset', 'islands#grayIcon');

        $properties->createProperty('balloonContentHeader', $row['pointName']);
        $properties->createProperty('balloonContentBody', $row['address']);
        $properties->createProperty('balloonContent', $row['pointName']);
        $properties->createProperty('hintContent', $row['pointName']);
        $properties->createProperty('clusterCaption', $row['pointName']);
        $properties->createProperty('balloonContentFooter', 'Кол-во заказов: ' . $row['requests']);
        $feature = new YandexApiFeature($geometryPoint, $properties, $options);
        $feature->id = $id;
        $id++;
        $featureCollection->addFeature($feature);
    }
    $json = json_encode($featureCollection);
    return $json;
}

function editRequest(PrivilegedUser $privilegedUser)
{
    $data = reset($_POST['data']);
    $data['requestIDExternal'] = $_POST['requestIDExternal'];
    if ($privilegedUser->getRequestEntity()->updateRequest($data)) {
        return json_encode(array("data" => array($privilegedUser->getRequestEntity()->selectRequestByID($data['requestIDExternal']))));
    } else return false;
//    return $privilegedUser->getRequestEntity()->updateRequest($data);
}

function deleteRequest(PrivilegedUser $privilegedUser)
{
    $requestIdExternal = $_POST['requestIDExternal'];
    if (isset($requestIdExternal)) {
        return $privilegedUser->getRequestEntity()->deleteRequest($requestIdExternal);
    } else return null;
}

function addRequest(PrivilegedUser $privUser)
{
    if ($privUser->getRequestEntity()->addRequest($_POST['data'][0])) {
        echo json_encode(array("data" => array($privUser->getRequestEntity()->selectLastInserted())));
    } else {
        $privUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
//    echo json_encode($privUser->getRequestEntity()->addRequest($_POST['data'][0]));
}

function getAllRouteIdDirectionPairs(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getRouteEntity()->selectRoutes();
    $data = array();
    foreach ($dataArray as $key => $val) {
        if ($val instanceof DAO\RouteData) {
            $data[$key]['routeID'] = $val->getData('routeID');
            $data[$key]['directionName'] = $val->getData('routeName');
        }
    }
    echo json_encode($data);
}

function addRouteList(PrivilegedUser $privUser)
{
    $data = $_POST['data'];
    $returnData = json_encode($privUser->getRouteListEntity()->addRouteList($data));
    return $returnData;

}

function getMarketAgentByName(PrivilegedUser $privUser)
{
    $name = $_POST['name'];
    $dataArray = $privUser->getUserEntity()->getMarketAgentsByName($name);
    return json_encode($dataArray);
}

function getRouteListsByNumber(PrivilegedUser $privUser)
{
    $number = $_POST['number'];
    $dataArray = $privUser->getRouteListEntity()->selectRouteListIdByNumber($number);
    return json_encode($dataArray);
}

function getClientsByINN(PrivilegedUser $privUser)
{
    $inn = $_POST['inn'];
    $dataArray = $privUser->getClientEntity()->selectClientsByInnOrName($inn);
    return json_encode($dataArray);
}

function getPointsByName(PrivilegedUser $privUser)
{
    $name = $_POST['name'];
    $dataArray = $privUser->getPointEntity()->selectPointsByName($name);
    return json_encode($dataArray);
}

function createDocumentGroup()
{
    $requestId = $_POST['requestIDExternal'];
    $docGroupName = $_POST['docGroupName'];
    if (!isset($requestId)) {
        throw new DataTransferException('Не задан ID заявки', __FILE__);
    }
    $path = "../common_files/media/Other/docs/xml/$requestId.xml";
    $pathToDocs = '../common_files/media/Other/docs/files/';
    if (file_exists($path)) {

        $xml = simplexml_load_file($path);
        $groups = $xml->groups[0];
        $newGroup = $xml->addChild('group');
        $newGroup->addAttribute('tit', $docGroupName);
        $xml->asXML($path);
    } else {

        $xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
                        <groups>
                            <group tit=\"$docGroupName\">
                            </group>
                        </groups>";
        $xml = new SimpleXMLElement($xmlString);
        $xml->asXML($path);
    }

}

function uploadDocuments()
{

    $requestId = $_POST['requestIDExternal'];
    $groupName = $_POST['groupName'];
    mkdir("../common_files/media/Other/docs/files/uploads/$requestId", 0777);
    $docDir = realpath("../common_files/media/Other/docs/files/uploads/$requestId") . '/';

//    $file_ary = array();
//    $file_count = count($_FILES['userfile']);
//    $file_keys = array_keys($_FILES['userfile']);
//
//    for ($i=0; $i<$file_count; $i++) {
//        foreach ($file_keys as $key) {
//            $file_ary[$i][$key] = $_FILES['userfile'][$key][$i];
//        }
//    }


    if (!isset($requestId)) {
        throw new DataTransferException('Не задан ID заявки', __FILE__);
    }
    if (!isset($groupName)) {
        throw new DataTransferException('Не задана группа документа', __FILE__);
    }
    $pathToXml = "../common_files/media/Other/docs/xml/$requestId.xml";

    if (file_exists($pathToXml)) {
        $xml = simplexml_load_file($pathToXml);
        $groupNode = false;
        foreach ($xml->group as $group) {
            if ($group['tit'] == $groupName) {
                $groupNode = $group;
            }
        }
        if ($groupNode) {
            for ($i = 0; $i < count($_FILES['docFiles']['name']); $i++) {
                $message = 'Error uploading file';
                switch ($_FILES['docFiles']['error'][$i]) {
                    case UPLOAD_ERR_OK:
                        $message = false;;
                        break;
                    case UPLOAD_ERR_INI_SIZE:
                        $message .= ' - file too large (limit of ' . ini_get("upload_max_filesize") . ')';
                        break;
                    case UPLOAD_ERR_FORM_SIZE:
                        $message .= ' - file too large (limit of ' . ini_get("upload_max_filesize") . ')';
                        break;
                    case UPLOAD_ERR_PARTIAL:
                        $message .= ' - file upload was not completed.';
                        break;
                    case UPLOAD_ERR_NO_FILE:
                        $message .= ' - zero-length file uploaded.';
                        break;
                    default:
                        $message .= ' - internal error #' . $_FILES['newfile']['error'];
                        break;
                }


                $basename = basename($_FILES['docFiles']['name'][$i]);
                $ext = explode('.', basename($basename));
                $fileName = md5(uniqid()) . "." . $ext[count($ext) - 1];
                $target_path = $docDir . $fileName;
                if (!move_uploaded_file($_FILES['docFiles']['tmp_name'][$i], $target_path)) {
                    return "$message";
                } else {
                    $file = $groupNode->addChild('file');
                    $file['name'] = "uploads/$requestId/$fileName";
                    $file['tit'] = $_FILES['docFiles']['name'][$i];
                }
            }
            $xml->asXML($pathToXml);
        }
    } else {
        throw new DataTransferException("Не удалось найти файл $pathToXml", __FILE__);
    }


    return 0;
}
function addVehicleForTransportCompany(PrivilegedUser $privilegedUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "данные"', __FILE__);
    }
    $vehicleInfo = $_POST['data'][0];
    if ($privilegedUser->getVehicleEntity()->insertVehicleForTransportCompany($vehicleInfo)) {
        echo json_encode(
            array(
                "data" => array(
                    $privilegedUser->getVehicleEntity()->selectVehicleByLastInsertedIdForTC()->toArray()
                )
            )
        );
    } else {
        $privilegedUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
}
function removeVehicleForTransportCompany (PrivilegedUser $privilegedUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    foreach ($dataSourceArray as $id => $userData) {
        $privilegedUser->getVehicleEntity()->TCPageRemoveVehicle($id);
        $privilegedUser->getDriverEntity()->TCPageRemoveDriverByVehicle($id);
    }
    echo '{ }';
}
function updateVehicleForTransportCompany(PrivilegedUser $privUser)
{
    $serverAnswer = array();
    $serverAnswer['data'] = array();
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    $VehicleEntity = $privUser->getVehicleEntity();
    $i = 0;
    foreach ($dataSourceArray as $vehicleId => $vehicleInfo) {
        if (!$VehicleEntity->UpdateVehicle(new \DAO\VehicleData($vehicleInfo), $vehicleId)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $serverAnswer['data'][$i] = $VehicleEntity->selectVehicleByIdForTransportCompany ($vehicleId)->toArray();
        $i++;
    }
    echo json_encode($serverAnswer);
}
function getDocuments()
{
    $requestId = $_POST['requestIDExternal'];
    if (!isset($requestId)) {
        throw new DataTransferException('Не задан ID заявки', __FILE__);
    }
    $path = "../common_files/media/Other/docs/xml/$requestId.xml";
    $pathToDocs = '../common_files/media/Other/docs/files/';
    if (file_exists($path)) {
        $xml = simplexml_load_file($path);
        $answer = [];
        foreach ($xml->group as $value) {
            $files = [];
            $group = ['title' => (string)$value['tit']];

            foreach ($value->file as $file) {
                array_push($files, ['name' => (string)$file['tit'], 'file' => (string)$pathToDocs . $file['name']]);
            }
            $group['documents'] = $files;
            array_push($answer, $group);
        }
        return (json_encode($answer));
    } else {
        throw new DataTransferException('Не удалось найти файл', __FILE__);
    }
}

function getCompanies(PrivilegedUser $privUser)
{
//    $data = $privUser->getRequestEntity()->getAllTransportCompanies();
    $data = $privUser->getTransportCompanyEntity()->selectAllCompanies();

    return (json_encode($data));
}

function getVehiclesForCompany(PrivilegedUser $privUser)
{
    $companyId = (int)$_POST['companyId'];
    if (!isset($companyId)) {
        throw new DataTransferException('Не задан ID компании', __FILE__);
    }
    $data = $privUser->getVehicleEntity()->selectVehicleByCompanyId($companyId);
    return (json_encode($data));
}

function getDriversForVehicle(PrivilegedUser $privUser)
{
    $vehicleId = $_POST['vehicleId'];
    if (isset($vehicleId)) {
        $data = $privUser->getDriverEntity()->selectDriverByVehicleId($vehicleId);
        return (json_encode($data));
    } else return null;

}

function deletePretension(PrivilegedUser $privUser)
{
    $requestIDExternal = $_POST['requestIDExternal'];
    $pretensionID = $_POST['pretensionID'];
    if (!isset($requestIDExternal) || empty($requestIDExternal)) {
        throw new DataTransferException('Не задан requestIDExternal', __FILE__);
    } elseif (!isset($pretensionID) || empty($pretensionID)) {
        throw new DataTransferException('Не задан номер претензии', __FILE__);
    }
    $data = $privUser->getPretensionEntity()->closePretension($pretensionID, $requestIDExternal);
    return (json_encode($data));
}

function updatePretension(PrivilegedUser $privUser)
{
    $commentRequired = ($_POST['commentRequired'] == 'true') ? True : false;
    $pretensionID = $_POST['pretensionID'];
    $requestIDExternal = $_POST['requestIDExternal'];
    $pretensionComment = $_POST['pretensionComment'];
////    $pretensionStatus = $_POST['pretensionStatus'];
    $pretensionCathegory = $_POST['pretensionCathegory'];
    $pretensionSum = $_POST['pretensionSum'];
    $pretensionPositionNumber = $_POST['pretensionPositionNumber'];
    if (!isset($requestIDExternal) || empty($requestIDExternal)) {
        throw new DataTransferException('Не задана заявка', __FILE__);
    } elseif (!isset($pretensionCathegory) || empty($pretensionCathegory)) {
        throw new DataTransferException('Не задана категория претензии', __FILE__);
    } elseif (!isset($pretensionPositionNumber) || empty($pretensionPositionNumber)) {
        throw new DataTransferException('Не задан код позиции', __FILE__);
    } elseif ((!isset($pretensionComment) || empty($pretensionComment)) && $commentRequired) {
        throw new DataTransferException("Не задан комментарий претензии $commentRequired", __FILE__);
    } elseif (!isset($pretensionSum) || empty($pretensionSum)) {
//        throw new DataTransferException('Не задана сумма', __FILE__);
        $pretensionSum = 0;
    }
//    $data = $privUser->getRequestEntity()->addPretension($requestIDExternal,$pretensionStatus,$pretensionComment,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum);
    $data = $privUser->getPretensionEntity()->updatePretension($pretensionID, $requestIDExternal, $pretensionComment, $pretensionCathegory, $pretensionPositionNumber, $pretensionSum);
    return (json_encode($data));
}

function getPretensions(PrivilegedUser $privUser)
{
    $requestIDExternal = $_POST['requestIDExternal'];
    if (!isset($requestIDExternal) || empty($requestIDExternal)) {
        throw new DataTransferException('Не задана заявка', __FILE__);
    }
    $data = $privUser->getPretensionEntity()->getPretensions($requestIDExternal);
    return json_encode($data);

}

function addPretension(PrivilegedUser $privUser)
{
//    error_reporting(E_ALL);
//    ini_set('display_errors',1);
    $commentRequired = ($_POST['commentRequired'] == 'true') ? True : false;
    $requestIDExternal = $_POST['requestIDExternal'];
    $pretensionComment = $_POST['pretensionComment'];
    $pretensionStatus = $_POST['pretensionStatus'];
    $pretensionCathegory = $_POST['pretensionCathegory'];
    $pretensionSum = $_POST['pretensionSum'];
    $pretensionPositionNumber = $_POST['pretensionPositionNumber'];
    $invoiceNumber = $_POST['invoiceNumber'];
    if (!isset($requestIDExternal) || empty($requestIDExternal)) {
        throw new DataTransferException('Не задана заявка', __FILE__);
    } elseif (!isset($pretensionCathegory) || empty($pretensionCathegory)) {
        throw new DataTransferException('Не задана категория претензии', __FILE__);
    } elseif (!isset($pretensionPositionNumber) || empty($pretensionPositionNumber)) {
        throw new DataTransferException('Не задан код позиции', __FILE__);
    } elseif ((!isset($pretensionComment) || empty($pretensionComment)) && $commentRequired) {
        throw new DataTransferException("Не задан комментарий претензии $commentRequired", __FILE__);
    } elseif (!isset($pretensionSum) || empty($pretensionSum)) {
//        throw new DataTransferException('Не задана сумма', __FILE__);
        $pretensionSum = 0;
    }

    $linkToRequest = $_POST['linkToRequest'];
    utf8mail($privUser->getRequestEntity()->getMarketAgentEmail($requestIDExternal)[0]['email'],
        "Создана претензия по накладной $invoiceNumber", wordwrap("Категория претензии: <br>
            \r\n $pretensionCathegory \r\n
            <br><br> Текст претензии:
            <br> $pretensionComment" .
            (isset($linkToRequest) ? "<br><br> <a href='$linkToRequest'>Ссылка на заявку" : ""), 70, "\r\n"));
    $data = $privUser->getPretensionEntity()->addPretension($requestIDExternal, $pretensionStatus, $pretensionComment, $pretensionCathegory, $pretensionPositionNumber, $pretensionSum);


    return json_encode($data);
}

//A complicated function that works around many mailing systems
function utf8mail($to, $s, $body, $from_name = "Logicsmart", $from_a = "info@logicsmart.ru", $reply = "info@logicsmart.ru")
{

    $mail = new PHPMailer(true);
    $mail->isSMTP();
    try {
        $mail->CharSet = "UTF-8";
        $mail->Host = "smtp.mail.ru";
        $mail->SMTPDebug = 0;
        $mail->SMTPAuth = true;
        $mail->Port = 465;
        $mail->Username = "testsmpt@bk.ru";
        $mail->Password = "testtest12345";
        $mail->addReplyTo("info@logicsmart.ru");
        $mail->replyTo = "info@logicsmart.ru";
        $mail->setFrom("testsmpt@bk.ru");
        $mail->addAddress($to);
        $mail->Subject = htmlspecialchars($s);
        $mail->msgHTML($body);
        $mail->SMTPSecure = 'ssl';
        $mail->send();
//    echo "Message sent Ok!</p>\n";
    } catch (phpmailerException $e) {
        echo $e->errorMessage();
    } catch (Exception $e) {
        echo $e->getMessage();
    }

}

function getRequestById(PrivilegedUser $privUser)
{
    $reqIdExt = $_POST['requestIDExternal'];
    if (!isset($reqIdExt)) {
        throw new DataTransferException('Не задан параметр "номер заявки"', __FILE__);
    }
    $data = $privUser->getRequestEntity()->selectDataByRequestId($reqIdExt);
    return json_encode($data);
//    return $reqIdExt;
}

function getRequestByClientIdAndInvoiceNumber(PrivilegedUser $privUser)
{
    $clientId = $_POST['clientId'];
    $invoiceNumber = $_POST['invoiceNumber'];
    if (!isset($clientId) || empty($clientId) || !isset($invoiceNumber) || empty($invoiceNumber)) {
//        throw new DataTransferException('Не задан параметр "номер заявки"', __FILE__);
        return '0';
    }
    $data = $privUser->getRequestEntity()->selectRequestByClientIdAndInvoiceNumber($clientId, $invoiceNumber);
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

function getClientSideRequestsForUser(PrivilegedUser $privilegedUser){
    $data['data'] = $privilegedUser->getRequestsForUser()->selectClientSideData();
    return json_encode($data);
}


function getRequestsForUser(PrivilegedUser $privUser)
{
    //exit(print_r($_POST['order']));

    $start = (int)$_POST['start'];
    $count = (int)$_POST['length'];
    foreach ($_POST['columns'] as $key => $value) {
        if ($value['data'] == 'requestDate' || $value['data'] == 'invoiceDate' || $value['data'] == 'documentDate' || $value['data'] == 'arrivalTimeToNextRoutePoint') {
            if ($value['search']['value'] != '') {
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
    foreach ($dataArray['requests'] as $key => $value) {
        $json_data['data'][$key]['requestDate'] = date('d-m-Y', strtotime($value['requestDate']));
        $json_data['data'][$key]['invoiceDate'] = $value['invoiceDate'] != 0 ? date('d-m-Y', strtotime($value['invoiceDate'])) : "";
        $json_data['data'][$key]['documentDate'] = $value['documentDate'] != 0 ? date('d-m-Y', strtotime($value['documentDate'])) : "";
        $json_data['data'][$key]['arrivalTimeToNextRoutePoint'] = $value['arrivalTimeToNextRoutePoint'] != 0 ? date('d-m-Y H:i:s', strtotime($value['arrivalTimeToNextRoutePoint'])) : "";


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
    $hoursAmount = !empty($_POST['hoursAmount']) ? $_POST['hoursAmount'] : 0;
    $companyId = !empty($_POST['companyId']) ? $_POST['companyId'] : "NULL";
    $vehicleId = !empty($_POST['vehicleId']) ? $_POST['vehicleId'] : "NULL";
    $driverId = !empty($_POST['driverId']) ? $_POST['driverId'] : "NULL";
    $userID = $privUser->getUserInfo()->getData('userID');
    return $privUser->getRequestEntity()->updateRequestStatus($userID, $requestIDExternal, $newStatusID, $datetime, $comment, $vehicleNumber, $hoursAmount, $companyId, $vehicleId, $driverId);
//    if (!isset($hoursAmount) || empty($hoursAmount)) {
//        return $privUser->getRequestEntity()->updateRequestStatus($userID, $requestIDExternal, $newStatusID, $datetime, $comment, $vehicleNumber, 0, $companyId, $vehicleId, $driverId);
//    } else {
//
//    }

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
    $palletsQty = !empty($_POST['palletsQty']) ? $_POST['palletsQty'] : 0;
    $hoursAmount = !empty($_POST['hoursAmount']) ? $_POST['hoursAmount'] : 0;
    $companyId = !empty($_POST['companyId']) ? $_POST['companyId'] : 0;
    $vehicleId = !empty($_POST['vehicleId']) ? $_POST['vehicleId'] : 0;
    $driverId = !empty($_POST['driverId']) ? $_POST['driverId'] : 0;
//        if (isset($_POST['palletsQty'])) {
    return $privUser->getRequestEntity()->updateRequestStatuses2($userID, $routeListID, $requests, $newStatusID, $datetime, $comment, $vehicleNumber, $palletsQty, $hoursAmount, $companyId, $vehicleId, $driverId);
//        }
//        return $privUser->getRequestEntity()->updateRequestStatuses($userID, $routeListID, $requests, $newStatusID, $datetime, $comment, $vehicleNumber, $hoursAmount);

}
function getTCPageRouteLists (PrivilegedUser $privilegedUser){

    $data['data'] = $privilegedUser->getRouteListEntity()->getRouteListsForTransportCompany();
    return json_encode($data);

}
function getTCPageVehicles (PrivilegedUser $privilegedUser){
    $data['data'] = $privilegedUser -> getVehicleEntity() -> getTCpageVehicles();
    return json_encode($data);
}
function getTCPageDrivers (PrivilegedUser $privilegedUser){
    $data['data'] = $privilegedUser -> getDriverEntity() -> getTCPageDrivers();
    return json_encode($data);
}