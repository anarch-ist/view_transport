<?php
include_once __DIR__ . '/../../../common_files/privilegedUser/PrivilegedUser.php';
try {
    $privUser = PrivilegedUser::getInstance();
    if (isset($_POST['status'])) {
        $action = $_POST['status'];
    } else {
        throw new DataTransferException('Не задан параметр "статус"', __FILE__);
    }
    if (strcasecmp($action,'getAllPointIdPointNamePairs')===0) {
        getAllPointIdPointNamePairs($privUser);
    } else if (strcasecmp($action, 'getPointsByName')===0) {
        getPointsByName($privUser);
    } else if (strcasecmp($action,'getAllUserRoles')===0) {
        getAllUserRoles($privUser);
    } else if (strcasecmp($action,'getRelationsBetweenRoutePointsDataForRouteID')===0) {
        getRelationsBetweenRoutePointsDataForRouteID($privUser);
    } else if (strcasecmp($action,'getUsersData')===0) {
        getUsers($privUser);
    } else if (strcasecmp($action, 'getRoutesData')===0) {
        getRoutes($privUser);
    } else if (strcasecmp($action,'getClientsByINN')===0) {
        getClientsByINN($privUser);
    } else if (strcasecmp($action,'getClients')===0) {
        getClients($privUser);
    } else if (strcasecmp($action,'getAllRouteIdDirectionPairs')===0) {
        getAllRouteIdDirectionPairs($privUser);
    } else if (strcasecmp($action,'updateStartRouteTime')===0) {
        updateStartRouteTime($privUser);
    } else if (strcasecmp($action,'updateDaysOfWeek')===0) {
        updateDaysOfWeek($privUser);
    } else if (strcasecmp($action,'getAllRoutePointsDataForRouteID')===0) {
        getAllRoutePointsDataForRouteID($privUser);
    } else if (strcasecmp($action,'getTransportCompaniesData')===0) {
        getTransportCompanies($privUser);
    } else if (strcasecmp($action,'getVehiclesData')===0) {
        getVehicles($privUser);
    } else if (strcasecmp($action,'getDriversData')===0) {
        getDrivers($privUser);
    } else if (strcasecmp($action,'routeEditing')===0) {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if (strcasecmp($action,'remove')===0) {
            removeRoutePoint($privUser);
        } else if (strcasecmp($action,'edit')===0) {
            updateRoutePoints($privUser);
        } else if (strcasecmp($action,'create')===0) {
            createRoutePoint($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else if (strcasecmp($action,'relationsBetweenRoutePoints')===0) {
        $action = $_POST['action'];
        if (strcasecmp($action,'edit')===0) {
            updateRelationsBetweenRoutePoints($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else if (strcasecmp($action,'routeEditingOnly')===0) {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if (strcasecmp($action,'remove')===0) {
            removeRoute($privUser);
        } else if (strcasecmp($action,'edit')===0) {
            updateRoute($privUser);
        } else if (strcasecmp($action,'create')===0) {
            createRoute($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else if (strcasecmp($action,'userEditing')===0) {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if (strcasecmp($action,'remove')===0) {
            removeUser($privUser);
        } else if (strcasecmp($action,'edit')===0) {
            updateUsers($privUser);
        } else if (strcasecmp($action,'create')===0) {
            createNewUser($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else if (strcasecmp($action,'transportCompaniesEditing')===0) {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if (strcasecmp($action, 'remove') === 0) {
            removeTransportCompany($privUser);
        } else if (strcasecmp($action, 'edit') === 0) {
            updateTransportCompany($privUser);
        } else if (strcasecmp($action, 'create') === 0) {
            addTransportCompany($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else if (strcasecmp($action,'vehiclesEditing')===0) {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if (strcasecmp($action, 'remove') === 0) {
            removeVehicle($privUser);
        } else if (strcasecmp($action, 'edit') === 0) {
            updateVehicle($privUser);
        } else if (strcasecmp($action, 'create') === 0) {
            addVehicle($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else if (strcasecmp($action,'driversEditing')===0) {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if (strcasecmp($action, 'remove') === 0) {
            removeDrivers($privUser);
        } else if (strcasecmp($action, 'edit') === 0) {
            updateDrivers($privUser);
        } else if (strcasecmp($action, 'create') === 0) {
            addDriver($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else {
        throw new DataTransferException('Неверно задан параметр "статус"', __FILE__);
    }
} catch (Exception $ex) {
    echo $ex->getMessage();
}

function getAllPointIdPointNamePairs(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getPointEntity()->selectAllPointIDAndPointName();
    echo json_encode($dataArray);
}

function getPointsByName(PrivilegedUser $privUser)
{
    $name = $_POST['name'];
    $dataArray = $privUser->getPointEntity()->selectPointsByName($name);
    echo json_encode($dataArray);
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

function getAllRoutePointsDataForRouteID(PrivilegedUser $privUser)
{
    if (!isset($_POST['routeID'])) {
        throw new DataTransferException('Не задан параметр "идентификатор маршрута"', __FILE__);
    }
    $routeID = $_POST['routeID'];
    $dataArray = $privUser->getRouteAndRoutePointsEntity()->getAllRoutePointsDataForRouteID($routeID);
    echo json_encode($dataArray);
}

function getAllUserRoles(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getUserEntity()->getUserRoles();
    echo json_encode($dataArray);
}

function removeRoutePoint(PrivilegedUser $privUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    foreach ($dataSourceArray as $routePointID => $dataSourceElem) {
        $privUser->getRoutePointEntity()->deleteRoutePoint($routePointID);
    }
    echo '{ }';
}

function createRoutePoint(PrivilegedUser $privUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'][0];
    $serverAnswer = array();
    $routeID = $dataSourceArray['routeID'];
    $sortOrder = $dataSourceArray['sortOrder'];
    if ($privUser->getRoutePointEntity()->addRoutePoint($sortOrder, $dataSourceArray['tLoading'], $dataSourceArray['pointName'], $routeID)) {
        $serverAnswer['data'][0] = $privUser->getRoutePointEntity()->selectRoutePointsByRouteID($routeID)->getData('routePoints')[$sortOrder - 1];
        echo json_encode($serverAnswer);
    }
}

function updateDaysOfWeek(PrivilegedUser $privUser)
{
    if (!isset($_POST['routeID'])) {
        throw new DataTransferException('Не задан параметр "идентификатор маршрута"', __FILE__);
    }
    if (!isset($_POST['daysOfWeek'])) {
        throw new DataTransferException('Не задан параметр "Дни недели"', __FILE__);
    }
    if ($privUser->getRouteEntity()->updateRouteDaysOfWeek($_POST['routeID'], $_POST['daysOfWeek'])) {
        echo json_encode($_POST['daysOfWeek']);
    } else {
        throw new DataTransferException('нет данных для updateDaysOfWeek', __FILE__);
    }
}

function updateStartRouteTime(PrivilegedUser $privUser)
{
    if (!isset($_POST['routeID'])) {
        throw new DataTransferException('Не задан параметр "идентификатор маршрута"', __FILE__);
    }
    if (!isset($_POST['firstPointArrivalTime'])) {
        throw new DataTransferException('Не задан параметр "Старт маршрута"', __FILE__);
    }
    $startTime = $_POST['firstPointArrivalTime'];
    if (!preg_match('/^([01][0-9]|2[0-3]):[0-5][0-9]$/', $startTime)) {
        throw new DataTablesFieldException('firstPointArrivalTime','неверные данные',__FILE__);
    }
    if ($privUser->getRouteEntity()->updateStartRouteTime($_POST['routeID'], $startTime . ':00')) {
        echo $_POST['firstPointArrivalTime'];
    } else {
        throw new DataTransferException('нет данных для updateStartRouteTime', __FILE__);
    }
}

function updateRelationsBetweenRoutePoints(PrivilegedUser $privUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "данные"', __FILE__);
    }
    $routeID = $_POST['routeID'];
    foreach ($_POST['data'] as $ids => $elem) {
        $id = explode('_', $ids);
        $timeForDistance = $elem['timeForDistance'];
        if ($timeForDistance<0 || !is_numeric($timeForDistance)) {
            throw new DataTablesFieldException('timeForDistance','неверные данные',__FILE__);
        }
        $privUser->getRoutePointEntity()->updateRelationBetweenRoutePoints($id[0], $id[1], $timeForDistance);
    }
    $dataArray = $privUser->getRouteAndRoutePointsEntity()->getAllRoutePointsDataForRouteID($routeID)['relationsBetweenRoutePoints'];
    echo json_encode(array('data' => $dataArray));
}

function updateRoutePoints(PrivilegedUser $privUser)
{
    $serverAnswer = array();
    $serverAnswer['data'] = array();
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    $routePointEntity = $privUser->getRoutePointEntity();
    $i = 0;
    foreach ($dataSourceArray as $routePointID => $dataSourceElem) {
        if (!isset($dataSourceElem['sortOrder'])) {
            throw new DataTablesFieldException('sortOrder','данные отсутствуют',__FILE__);
        }
        $sortOrder = $dataSourceElem['sortOrder'];
        if ($sortOrder<0 || !is_numeric($sortOrder)) {
            throw new DataTablesFieldException('sortOrder','данные некорректны',__FILE__);
        }
        if (!isset($dataSourceElem['tLoading'])) {
            throw new DataTablesFieldException('tLoading','данные отсутствуют',__FILE__);
        }
        $tLoading = $dataSourceElem['tLoading'];
        if ($tLoading<0 || !is_numeric($tLoading)) {
            throw new DataTablesFieldException('tLoading','данные некорректны',__FILE__);
        }
        if (!isset($dataSourceElem['pointName'])) {
            throw new DataTablesFieldException('pointName','данные отсутствуют',__FILE__);
        }
        $pointName = $dataSourceElem['pointName'];
        if (!$pointName) {
            throw new DataTablesFieldException('pointName','данные некорректны',__FILE__);
        }
        if (!$routePointEntity->updateRoutePoint($routePointID, $sortOrder, $tLoading, $pointName)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $serverAnswer['data'][$i] = $routePointEntity->selectRoutePointsByRouteID($dataSourceArray[$routePointID]['routeID'])->getData('routePoints')[$sortOrder - 1];
        $serverAnswer['data'][$i]['pointID'] = $dataSourceArray[$routePointID]['pointName'];
        $i++;
    }
    echo json_encode($serverAnswer);
}

function getRelationsBetweenRoutePointsDataForRouteID(PrivilegedUser $privUser)
{
    if (!isset($_POST['routeID'])) {
        throw new DataTransferException('Не задан параметр "идентификатор маршрута"', __FILE__);
    }
    $routeID = $_POST['routeID'];
    $dataArray = $privUser->getRouteAndRoutePointsEntity()->getAllRoutePointsDataForRouteID($routeID)['relationsBetweenRoutePoints'];
    echo json_encode($dataArray);
}


function getClients(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getClientEntity()->selectAllClientIdINNPairs();
    $data = array();
    foreach ($dataArray as $key => $val) {
        if ($val instanceof DAO\ClientData) {
            $data[$key]['clientID'] = $val->getData('clientID');
            $data[$key]['clientName'] = $val->getData('clientName');
        }
    }
    echo json_encode($dataArray);
}

function getClientsByINN(PrivilegedUser $privUser)
{
    $inn = $_POST['inn'];
    $dataArray = $privUser->getClientEntity()->selectClientsByINN($inn);
    echo json_encode($dataArray);
}

function getUsers(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getUserEntity()->selectUsers($_POST['start'], $_POST['length']);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => is_null($dataArray['users']) ? "" : $dataArray['users']  // total data array
    );
    echo json_encode($json_data);
}

function getTransportCompanies(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getTransportCompanyEntity()->selectByRange($_POST['start'], $_POST['length']);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => is_null($dataArray['transportCompanies']) ? "" : $dataArray['transportCompanies']   // total data array
    );
    echo json_encode($json_data);
}

function getVehicles(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getVehicleEntity()->selectVehiclesByRange($_POST['start'], $_POST['length']);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => is_null($dataArray['vehicles']) ? "" : $dataArray['vehicles']   // total data array
    );
    echo json_encode($json_data);
}

function getDrivers(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getDriverEntity()->selectDriversByRange($_POST['start'], $_POST['length']);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => is_null($dataArray['drivers']) ? "" : $dataArray['drivers']   // total data array
    );
    echo json_encode($json_data);
}

function addTransportCompany(PrivilegedUser $privilegedUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "данные"', __FILE__);
    }
    $transportCompanyInfo = $_POST['data'][0];
    if ($privilegedUser->getTransportCompanyEntity()->insertCompany($transportCompanyInfo)) {
        echo json_encode(
            array(
                "data" =>array(
                    $privilegedUser->getTransportCompanyEntity()->selectLastInsertedId()->toArray()
                )
            )
        );
    } else {
        $privilegedUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
}

function addVehicle(PrivilegedUser $privilegedUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "данные"', __FILE__);
    }
    $vehicleInfo = $_POST['data'][0];
    if ($privilegedUser->getVehicleEntity()->insertVehicle($vehicleInfo)) {
        echo json_encode(
            array(
                "data" =>array(
                    $privilegedUser->getVehicleEntity()->selectVehicleByLastInsertedId()->toArray()
                )
            )
        );
    } else {
        $privilegedUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
}

function addDriver(PrivilegedUser $privilegedUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "данные"', __FILE__);
    }
    $driverInfo = $_POST['data'][0];
    if ($privilegedUser->getDriverEntity()->insertDriver($driverInfo)) {
        echo json_encode(
            array(
                "data" =>array(
                    $privilegedUser->getDriverEntity()->selectDriverByLastInsertedId()->toArray()
                )
            )
        );
    } else {
        $privilegedUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
}

function removeVehicle(PrivilegedUser $privilegedUser) {
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    foreach ($dataSourceArray as $id => $userData) {
        $privilegedUser->getVehicleEntity()->pseudoRemoveVehicle($id);
        $privilegedUser->getDriverEntity()->pseudoRemoveDriverByVehicle($id);
    }
    echo '{ }';
}

function removeTransportCompany(PrivilegedUser $privilegedUser) {
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    foreach ($dataSourceArray as $id => $userData) {
        $privilegedUser->getTransportCompanyEntity()->pseudoRemoveCompany($id);
        $privilegedUser->getDriverEntity()->pseudoRemoveDriverByTransportCompany($id);
        $privilegedUser->getVehicleEntity()->presudoRemoveDriverByTransportCompany($id);
    }
    echo '{ }';
}

function removeDrivers(PrivilegedUser $privilegedUser) {
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    foreach ($dataSourceArray as $id => $userData) {
        $privilegedUser->getDriverEntity()->pseudoRemoveDriver($id);
    }
    echo '{ }';
}

function getRoutes(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getRouteEntity()->selectRoutesWithOffset($_POST['start'], $_POST['length']);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($dataArray['totalCount']),  // total number of records
        "recordsFiltered" => intval($dataArray['totalFiltered']), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => is_null($dataArray['routes']) ? "" : $dataArray['routes'] // total data array
    );
    echo json_encode($json_data);
}

function removeRoute(PrivilegedUser $privUser) {
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    foreach ($dataSourceArray as $routeID => $routeData) {
        $privUser->getRouteEntity()->deleteRoute($routeID);
    }
    echo '{ }';
}

function createRoute(PrivilegedUser $privUser) {
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "данные"', __FILE__);
    }
    $routeInfo = $_POST['data'][0];
    if ($privUser->getTariffEntity()->insertTariff($routeInfo)) {
        $lastID = $privUser->getTariffEntity()->getLastInsertedID();
        $routeInfo['tariffID'] = $lastID[0]['tariffID'];
        if ($privUser->getRouteEntity()->addRoute($routeInfo)) {
            $insertedRoute = $privUser->getRouteEntity()->selectRouteByDirectionName($routeInfo['routeName']);
            echo json_encode(array("data" =>array($insertedRoute->toArray())));
        }
    } else {
        $privUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
}

function removeUser(PrivilegedUser $privUser) {
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    foreach ($dataSourceArray as $userID => $userData) {
        $privUser->getUserEntity()->deleteUser($userID);
    }
    echo '{ }';
}

function createNewUser(PrivilegedUser $privUser)
{
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "данные"', __FILE__);
    }
    $userInfo = new \DAO\UserData($_POST['data'][0]);
    if ($privUser->getUserEntity()->addUser($userInfo)) {
        echo json_encode(array("data" =>array($privUser->getUserEntity()->selectUserByLogin($userInfo->getData('login'))->toArray())));
    }
    else {
        $privUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
}

function updateDrivers(PrivilegedUser $privUser)
{
    $serverAnswer = array();
    $serverAnswer['data'] = array();
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    $driverEntity = $privUser->getDriverEntity();
    $i = 0;
    foreach ($dataSourceArray as $id => $driverInfo) {
        if (!$driverEntity->updateDriver(new \DAO\DriverData($driverInfo), $id)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $serverAnswer['data'][$i] = $driverEntity->selectDriverById($id)->toArray();
        $i++;
    }
    echo json_encode($serverAnswer);
}

function updateUsers(PrivilegedUser $privUser)
{
    $serverAnswer = array();
    $serverAnswer['data'] = array();
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    $userEntity = $privUser->getUserEntity();
    $i = 0;
    foreach ($dataSourceArray as $userID => $userInfo) {
        if (!$userEntity->updateUser(new \DAO\UserData($userInfo),$userID)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $serverAnswer['data'][$i] = $userEntity->selectUserByID($userID)->toArray();
        $i++;
    }
    echo json_encode($serverAnswer);
}

function updateVehicle(PrivilegedUser $privUser)
{
    $serverAnswer = array();
    $serverAnswer['data'] = array();
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    $vehicleEntity = $privUser->getVehicleEntity();
    $i = 0;
    foreach ($dataSourceArray as $vehicleId => $vehicleInfo) {
        if (!$vehicleEntity->updateVehicle(new \DAO\VehicleData($vehicleInfo), $vehicleId)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $serverAnswer['data'][$i] = $vehicleEntity->selectVehicleById($vehicleId)->toArray();
        $i++;
    }
    echo json_encode($serverAnswer);
}

function updateTransportCompany(PrivilegedUser $privUser)
{
    $serverAnswer = array();
    $serverAnswer['data'] = array();
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    $transportCompanyEntity = $privUser->getTransportCompanyEntity();
    $i = 0;
    foreach ($dataSourceArray as $companyId => $companyInfo) {
        if (!$transportCompanyEntity->updateCompany(new \DAO\TransportCompanyData($companyInfo), $companyId)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $serverAnswer['data'][$i] = $transportCompanyEntity->selectCompanyById($companyId)->toArray();
        $i++;
    }
    echo json_encode($serverAnswer);
}

function updateRoute(PrivilegedUser $privUser)
{
    $serverAnswer = array();
    $serverAnswer['data'] = array();
    if (!isset($_POST['data'])) {
        throw new DataTransferException('Не задан параметр "data"', __FILE__);
    }
    $dataSourceArray = $_POST['data'];
    $routeEntity = $privUser->getRouteEntity();
    $tariffEntity = $privUser->getTariffEntity();
    $i = 0;
    foreach ($dataSourceArray as $routeId => $routeInfo) {
        if (!$routeEntity->updateRoute($routeInfo, $routeId)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $routeInfoAfter = $routeEntity->selectRouteByID($routeId);
        if (!empty($routeInfo['tariffID'])) {
            if(!$tariffEntity->updateTariff($routeInfo, $routeInfoAfter->getData('tariffID'))) {
                $privUser->getDaoEntity()->rollback();
                throw new DataTransferException('Данные не были обновлены', __FILE__);
            }

            $tariffInfoAfter = $tariffEntity->getTariffById($routeInfoAfter->getData('tariffID'));

            $updatedRoute = array();
            $updatedRoute['routeName'] = $routeInfoAfter->getData('routeName');
            $updatedRoute['cost'] = $tariffInfoAfter->getData('cost');
            $updatedRoute['cost_per_point'] = $tariffInfoAfter->getData('cost_per_point');
            $updatedRoute['cost_per_hour'] = $tariffInfoAfter->getData('cost_per_hour');
            $updatedRoute['cost_per_box'] = $tariffInfoAfter->getData('cost_per_box');
            $serverAnswer['data'][$i] = $updatedRoute;
            $i++;
        } else {
            if ($privUser->getTariffEntity()->insertTariff($routeInfo)) {
                $lastID = $privUser->getTariffEntity()->getLastInsertedID();
                if($routeEntity->updateRoutesTariff($routeId, $lastID[0]['tariffID'])) {
                    $tariffInfoAfter = $tariffEntity->getTariffById($lastID[0]['tariffID']);
                    $updatedRoute['routeName'] = $routeInfoAfter->getData('routeName');
                    $updatedRoute['cost'] = $tariffInfoAfter->getData('cost');
                    $updatedRoute['cost_per_point'] = $tariffInfoAfter->getData('cost_per_point');
                    $updatedRoute['cost_per_hour'] = $tariffInfoAfter->getData('cost_per_hour');
                    $updatedRoute['cost_per_box'] = $tariffInfoAfter->getData('cost_per_box');
                    $serverAnswer['data'][$i] = $updatedRoute;
                } else {

                }
            }
        }
    }
    echo json_encode($serverAnswer);
}