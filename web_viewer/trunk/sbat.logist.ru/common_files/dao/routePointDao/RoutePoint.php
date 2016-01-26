<?php

namespace DAO;
include_once __DIR__ . '/IRoutePoint.php';
include_once __DIR__ . '/../DAO.php';
include_once __DIR__ . '/Data.php';

class RoutePointEntity implements IRoutePointEntity
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
        if (is_null(self::$_instance)) return new RoutePointEntity();
        return self::$_instance;
    }

    function selectRoutePointByID($id)
    {
        // TODO: Implement selectRoutePointByID() method.
    }

    function selectRoutePointsByRouteID($routeID)
    {
        $routePoints = array();
        $routePoints['routePoints'] = $this->_DAO->select(new SelectRoutePointsByRouteID($routeID));
        $routePoints['relationsBetweenRoutePoints'] = $this->_DAO->select(new SelectRelationsBetweenRoutePoints($routeID));
        return new RoutePointsData($routePoints);
    }

    function updateRelationBetweenRoutePoints($firstRoutePoint, $secondRoutePoint, $newDistance, $newTimeToDestination)
    {
        // TODO: Implement updateRelationBetweenRoutePoints() method.
    }

    function updateRoutePoint($routePointID, $sortOrder, $tLoading, $pointName)
    {
        // TODO: check updateRoutePoint() method.
        return $this->_DAO->update(new UpdateRoutePoint($routePointID, $sortOrder, $tLoading, $pointName));
    }

    function deleteRoutePoint($RoutePointID)
    {
        // TODO: Implement deleteRoutePoint() method.
        return $this->_DAO->delete(new DeleteRoutePointFromRoute($RoutePointID));
    }

    function addRoutePoint($Route)
    {
        // TODO: Implement addRoutePoint() method.
    }
}

class DeleteRoutePointFromRoute implements IEntityDelete
{
    private $routePointID;

    function __construct($routePointID)
    {
        $this->routePointID = DAO::getInstance()->checkString($routePointID);
    }

    function getDeleteQuery()
    {
        throw new \MysqlException('запрос не написан для DeleteRoutePointFromRoute::getDeleteQuery');
        //return "DELETE FROM route_points WHERE routePointID = '$this->routePointID';";
    }
}

class UpdateRoutePoint implements IEntityUpdate
{
    private $routePointID, $sortOrder, $tLoading, $pointName;

    function __construct($routePointID, $sortOrder, $tLoading, $pointName)
    {
        $this->routePointID = DAO::getInstance()->checkString($routePointID);
        $this->sortOrder = DAO::getInstance()->checkString($sortOrder);
        $this->tLoading = DAO::getInstance()->checkString($tLoading);
        $this->pointName = DAO::getInstance()->checkString($pointName);
    }

    function getUpdateQuery()
    {
        return "UPDATE route_points SET sortOrder = $this->sortOrder, pointID = getPointIDByName($this->pointName), timeForLoadingOperations = $this->tLoading WHERE routePointID = 2;";
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
        return "SELECT `routePointID`, `sortOrder`, `pointName`, `timeForLoadingOperations` as `tLoading`
FROM `route_points`
  JOIN `points` ON `points`.`PointID` = `route_points`.`PointID`
WHERE `RouteID` = $this->routeID
ORDER BY sortOrder;";
    }
}

class SelectRoutePointByID implements IEntitySelect
{
    private $routePointID;

    function __construct($routePointID)
    {
        $this->routePointID = DAO::getInstance()->checkString($routePointID);
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `route_points` WHERE `RoutePointID` = $this->routePointID";
    }
}

class SelectRelationsBetweenRoutePoints implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "CALL getRelationsBetweenRoutePoints($this->id);";
    }
}