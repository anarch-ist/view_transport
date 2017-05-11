<?php
namespace DAO;
include_once 'IRoute.php';
include_once __DIR__ . '/../DAO.php';
include_once 'Data.php';

class RouteEntity implements IRouteEntity
{
    private static $_instance;
    private $_DAO;

    protected function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new RouteEntity();
        return self::$_instance;
    }

    /**
     * @return array:RouteData
     */
    function selectRoutes()
    {
        $array = $this->_DAO->select(new SelectAllRoutes());
        $routes = array();
        for ($i = 0; $i < count($array); $i++) {
            $routes[$i] = new RouteData($array[$i]);
        }
        return $routes;
    }

    function selectRoutesWithOffset($start = 0, $count = 5)
    {
        $array = $this->_DAO->multiSelect(new SelectRoutesWithOffset($start, $count));
        $arrayResult = array();
        $arrayResult['routes'] = $array[0];
        $arrayResult['totalFiltered'] = $array[1][0]['totalFiltered'];
        $arrayResult['totalCount'] = $array[2][0]['totalCount'];
        return $arrayResult;
    }

    function selectRouteByID($id)
    {
        $array = $this->_DAO->select(new SelectRouteByID($id));
        return new RouteData($array[0]);
    }

    function deleteRoute($routeID)
    {
        return $this->_DAO->delete(new DeleteRoute($routeID));
    }

    function addRoute($route)
    {
        return $this->_DAO->insert(new InsertRoute($route));
    }

    function selectRouteByDirectionName($directionName)
    {
        $array = $this->_DAO->select(new SelectRouteByDirectionName($directionName));
        return new RouteData($array[0]);
    }

    function updateStartRouteTime($routeID, $newTime)
    {
        return $this->_DAO->update(new UpdateStartRouteTime($routeID, $newTime));
    }

    function updateRouteDaysOfWeek($routeID, $days)
    {
        return $this->_DAO->update(new UpdateRouteDaysOfWeek($routeID, $days));
    }

    function updateRoute($routeData, $id)
    {
        return $this->_DAO->update(new UpdateRoute($routeData, $id));
    }

    function updateRoutesTariff($routeID, $tariffID)
    {
        return $this->_DAO->update(new UpdateRoutesTariff($routeID, $tariffID));
    }
}

class SelectAllRoutes implements IEntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `routes`";
    }
}

class SelectRouteByID implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `routes` WHERE `routeID` = $this->id";
    }
}

class SelectRouteByDirectionName implements IEntitySelect
{
    private $directionName;

    function __construct($directionName)
    {
        $this->directionName = DAO::getInstance()->checkString($directionName);
    }

    function getSelectQuery()
    {
        return "SELECT * FROM routes WHERE dataSourceID = 'ADMIN_PAGE' AND directionIDExternal = '$this->directionName';";
    }
}

class UpdateStartRouteTime implements IEntityUpdate {

    /**
     * @return string
     */
    private $routeID, $newTime;
    function __construct($routeID, $newTime) {

        $this->routeID = DAO::getInstance()->checkString($routeID);
        $this->newTime = $this->checkTime($newTime);
    }
    function checkTime($str) {
        if (!($time = strtotime($str) === false)) {
            return $str;
        }
        throw new \DataTransferException('неправильный формат времени', __FILE__);
    }
    function getUpdateQuery()
    {
        return "UPDATE `routes` SET `firstPointArrivalTime` = '$this->newTime' WHERE routeID = $this->routeID";
    }
}

class UpdateRouteDaysOfWeek implements IEntityUpdate {

    /**
     * @return string
     */
    private $routeID, $dayArray;
    function __construct($routeID, $dayArray) {

        $this->routeID = DAO::getInstance()->checkString($routeID);
        $this->dayArray = DAO::getInstance()->checkString(implode(',',$dayArray));
    }
    function getUpdateQuery()
    {
        return "UPDATE `routes` SET `daysOfWeek` = '$this->dayArray' WHERE routeID = $this->routeID";
    }
}

class SelectRoutesWithOffset implements IEntitySelect
{
    private $start, $count, $orderByColumn, $isDesc, $searchString;

    function __construct($start, $count)
    {
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
        $this->isDesc = ($_POST['order'][0]['dir'] === 'desc' ? 'TRUE' : 'FALSE');
        $this->searchString = $_POST['search']['value'];
        $searchArray = $_POST['columns'];
        $this->orderByColumn = $searchArray[$_POST['order'][0]['column']]['name'];
    }

    function getSelectQuery()
    {
        return "CALL selectRoutes($this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
    }
}

class InsertRoute implements IEntityInsert
{
    private $routeName, $tariffID;

    function __construct($data)
    {
        $dao = DAO::getInstance();
        $this->routeName = $dao->checkString($data['routeName']);
        $this->tariffID = $dao->checkString($data['tariffID']);
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        return "INSERT INTO `routes` (routeName, directionIDExternal, dataSourceID, tariffID) VALUE " .
            "('$this->routeName', '$this->routeName', 'ADMIN_PAGE', '$this->tariffID');";
    }
}

class DeleteRoute implements IEntityDelete
{
    private $routeID;

    function __construct($routeID)
    {
        $this->routeID = DAO::getInstance()->checkString($routeID);
    }

    /**
     * @return string
     */
    function getDeleteQuery()
    {
        return "DELETE FROM `routes` WHERE routeID = $this->routeID;";
    }
}

class UpdateRoute implements IEntityUpdate
{

    private $id, $routeName;

    function __construct($routeData, $id)
    {
        $dao = DAO::getInstance();
        $this->id = $id;
        $this->routeName = $dao->checkString($routeData['routeName']);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `routes` SET " .
            "routeName = '$this->routeName'";
        $query = $query . " WHERE routeID = $this->id;";
        return $query;
    }
}

class UpdateRoutesTariff implements IEntityUpdate
{

    private $routeID, $tariffID;

    function __construct($routeID, $tariffID)
    {
        $dao = DAO::getInstance();
        $this->routeID = $routeID;
        $this->tariffID = $dao->checkString($tariffID);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `routes` SET " .
            "tariffID = '$this->tariffID'";
        $query = $query . " WHERE routeID = $this->routeID;";
        return $query;
    }
}