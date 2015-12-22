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

    function selectRoutes()
    {
        $array = $this->_DAO->select(new SelectAllRoutes());
        $Routes = array();
        for ($i = 0; $i < count($array); $i++) {
            $Routes[$i] = new RouteData($array[$i]);
        }
        return $Routes;
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

    function selectRoutePointsByDirectionName($directionName)
    {
        $array = $this->_DAO->select(new SelectRoutePointsByDirectionName($directionName));
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
        return "SELECT * FROM `routes`;";
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

class SelectRoutePointsByDirectionName implements IEntitySelect
{
    private $directionName;

    function __construct($directionName)
    {
        $this->directionName = DAO::getInstance()->checkString($directionName);
    }

    function getSelectQuery()
    {
        throw new \Exception('Запрос не написан');
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
        return "CALL getRoutePointsByDirectionName('$this->directionName');";
    }
}