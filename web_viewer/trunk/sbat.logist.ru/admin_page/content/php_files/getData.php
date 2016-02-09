<?php
include_once __DIR__ . '/../../../common_files/privilegedUser/PrivilegedUser.php';
try {
    $privUser = PrivilegedUser::getInstance();
    if (isset($_POST['status'])) {
        $action = $_POST['status'];
    } else {
        throw new DataTransferException('Не задан параметр "статус"', __FILE__);
    }
    if ($action === 'getAllPointIdPointNamePairs') {
        getAllPointIdPointNamePairs($privUser);
    } else if ($action === 'getAllUserRoles') {
        getAllUserRoles($privUser);
    } else if ($action === 'getRelationsBetweenRoutePointsDataForRouteID') {
        getRelationsBetweenRoutePointsDataForRouteID($privUser);
    } else if ($action === 'getUsersData') {
//        TODO: check method for select users (getUsers($privUser);)
        getUsers($privUser);

    } else if ($action === 'getAllRouteIdDirectionPairs') {
        getAllRouteIdDirectionPairs($privUser);
    } else if ($action === 'updateStartRouteTime') {
        updateStartRouteTime($privUser);
    } else if ($action === 'updateDaysOfWeek') {
        updateDaysOfWeek($privUser);
    } else if ($action === 'getAllRoutePointsDataForRouteID') {
        getAllRoutePointsDataForRouteID($privUser);
    } else if ($action === 'routeEditing') {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if ($action === 'remove') {
            removeRoutePoint($privUser);
        } else if ($action === 'edit') {
            updateRoutePoints($privUser);
        } else if ($action === 'create') {
            createRoutePoint($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }
    } else if ($action === 'relationsBetweenRoutePoints') {

        $action = $_POST['action'];
        if ($action === 'edit') {
            updateRelationsBetweenRoutePoints($privUser);
        } else {
            throw new DataTransferException('Неверно задан параметр "действие"', __FILE__);
        }

    } else if ($action === 'userEditing') {
        if (!isset($_POST['action'])) {
            throw new DataTransferException('Не задан параметр "действие"', __FILE__);
        }
        $action = $_POST['action'];
        if ($action === 'remove') {
//            TODO: check method for remove
            removeUser($privUser);
        } else if ($action === 'edit') {
            updateUsers($privUser);
//            TODO: check method for edit
        } else if ($action === 'create') {
            createNewUser($privUser);
//            TODO: check method for create
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
    $dataArray = $privUser->getPointEntity()->selectPoints();
    $data = array();
    foreach ($dataArray as $key => $val) {
        if ($val instanceof DAO\PointData) {
            $data[$key]['pointID'] = $val->getData('pointID');
            $data[$key]['pointName'] = $val->getData('pointName');
        }
    }
    echo json_encode($data);
}

function getAllRouteIdDirectionPairs(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getRouteEntity()->selectRoutes();
    $data = array();
    foreach ($dataArray as $key => $val) {
        if ($val instanceof DAO\RouteData) {
            $data[$key]['routeID'] = $val->getData('routeID');
            $data[$key]['directionName'] = $val->getData('directionName');
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

function getUsers(PrivilegedUser $privUser)
{
    $dataArray = $privUser->getUserEntity()->selectUsers($_POST['start'], $_POST['length']);
    $totalData = $privUser->getUserEntity()->getTotalUserCount();
    $totalFiltered = count($dataArray);
    $json_data = array(
        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($totalData),  // total number of records
        "recordsFiltered" => intval($totalFiltered), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => $dataArray   // total data array
    );
    echo json_encode($json_data);


//    $dataArray = json_decode('[{"userID": "1", "firstName":"wefwfe", "lastName":"ewrkbfif", "position": "efewerfw", "patronymic":"ergerge", "phoneNumber": "9055487552",
//            "email": "qwe@qwe.ru", "password":"lewrhbwueu23232", "userRoleRusName":"Диспетчер", "pointName":"point1"}]');
//    $json_data = array(
//        "draw" => intval($_POST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
//        "recordsTotal" => intval(1),  // total number of records
//        "recordsFiltered" => intval(1), // total number of records after searching, if there is no searching then totalFiltered = totalData
//        "data" => $dataArray   // total data array
//    );
//    echo json_encode($json_data);
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
        echo json_encode(array("data" =>array($privUser->getUserEntity()->selectUserByEmail($userInfo->getData('email'))->toArray())));
    }
    else {
        $privUser->getDaoEntity()->rollback();
        throw new DataTransferException('Данные не были добавлены', __FILE__);
    }
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
//        if (!isset($userInfo['sortOrder'])) {
//            throw new DataTablesFieldException('sortOrder','данные отсутствуют',__FILE__);
//        }
//        $sortOrder = $userInfo['sortOrder'];
//        if ($sortOrder<0 || !is_numeric($sortOrder)) {
//            throw new DataTablesFieldException('sortOrder','данные некорректны',__FILE__);
//        }
//        if (!isset($userInfo['tLoading'])) {
//            throw new DataTablesFieldException('tLoading','данные отсутствуют',__FILE__);
//        }
//        $tLoading = $userInfo['tLoading'];
//        if ($tLoading<0 || !is_numeric($tLoading)) {
//            throw new DataTablesFieldException('tLoading','данные некорректны',__FILE__);
//        }
//        if (!isset($userInfo['pointName'])) {
//            throw new DataTablesFieldException('pointName','данные отсутствуют',__FILE__);
//        }
//        $pointName = $userInfo['pointName'];
//        if (!$pointName) {
//            throw new DataTablesFieldException('pointName','данные некорректны',__FILE__);
//        }
        if (!$userEntity->updateUser(new \DAO\UserData($userInfo),$userID)) {
            $privUser->getDaoEntity()->rollback();
            throw new DataTransferException('Данные не были обновлены', __FILE__);
        }
        $serverAnswer['data'][$i] = $userEntity->selectUserByEmail($userInfo['email'])->toArray();
        $i++;
    }
    echo json_encode($serverAnswer);
}