<?php
namespace DAO;
include_once __DIR__ . '/IRoute.php';
include_once __DIR__ . '/../DAO.php';
include_once __DIR__ . '/Data.php';

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

    function selectRouteByID($id)
    {
        $array = $this->_DAO->select(new SelectRouteByID($id));
        return new RouteData($array[0]);
    }

    function updateRoute($newRoute)
    {
        // TODO: Implement updateRoute() method.
    }

    function deleteRoute($Route)
    {
        // TODO: Implement deleteRoute() method.
    }

    function addRoute($Route)
    {
        // TODO: Implement addRoute() method.
    }

    function selectRouteByDirectionName($directionName)
    {
        $array = $this->_DAO->select(new SelectRouteByDirectionName($directionName));
        return new RouteData($array[0]);
    }

    function selectRoutePointsByRouteID($directionName)
    {
        $array = $this->_DAO->select(new SelectRoutePointsByRouteID($directionName));
        $RoutePoints = array();
        for ($i = 0; $i < count($array); $i++) {
            $RoutePoints[$i] = new RoutePointData($array[$i]);
        }
        return $RoutePoints;
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

class SelectRoutePointsByRouteID implements IEntitySelect
{
    private $routeID;

    function __construct($routeID)
    {
        $this->routeID = DAO::getInstance()->checkString($routeID);
    }

    function getSelectQuery()
    {
        return "SELECT routePointID, sortOrder, pointName, tLoading, timeToNextPoint, distanceToNextPoint FROM route_points, points WHERE route_points.pointID = points.pointID AND routeID = '$this->routeID' ORDER BY `sortOrder`;";
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
        throw new \MysqlException('Запрос не написан');
    }
}