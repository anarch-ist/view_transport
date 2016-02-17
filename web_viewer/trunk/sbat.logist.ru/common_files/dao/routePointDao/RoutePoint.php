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
        // TODO: check selectRoutePointByID() method.
        $answer = $this->_DAO->select(new SelectRoutePointByID($id));
        if (!isset($answer[0])) return null;
        return new RoutePointData($answer[0]);
    }

    function selectRoutePointsByRouteID($routeID)
    {
        $routePoints = array();
        $routePoints['routePoints'] = $this->_DAO->select(new SelectRoutePointsByRouteID($routeID));
        $routePoints['relationsBetweenRoutePoints'] = $this->_DAO->select(new SelectRelationsBetweenRoutePoints($routeID));
        return new RoutePointsData($routePoints);
    }

    function updateRelationBetweenRoutePoints($firstRoutePoint, $secondRoutePoint, $newTimeToDestination)
    {
        return $this->_DAO->update(new UpdateRelationBetweenRoutePoints($firstRoutePoint, $secondRoutePoint, $newTimeToDestination));
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

    function addRoutePoint($sortOrder, $tLoading, $pointID, $routeID)
    {
        // TODO: Implement addRoutePoint() method.
        return $this->_DAO->insert(new AddRoutePoint($sortOrder, $tLoading, $pointID, $routeID));
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
        return "DELETE FROM route_points WHERE routePointID = $this->routePointID;";
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
        return "UPDATE route_points SET sortOrder = $this->sortOrder, pointID = $this->pointName, timeForLoadingOperations = $this->tLoading WHERE routePointID = $this->routePointID;";
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

class AddRoutePoint implements IEntityInsert
{
    private $sortOrder, $tLoading, $pointID, $routeID;

    function __construct($sortOrder, $tLoading, $pointID, $routeID)
    {
        $dao = DAO::getInstance();
        $this->sortOrder = $dao->checkString($sortOrder);
        $this->tLoading = $dao->checkString($tLoading);
        $this->pointID = $dao->checkString($pointID);
        $this->routeID = $dao->checkString($routeID);
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        // TODO: check getInsertQuery() method.
        return "INSERT INTO route_points (sortOrder, timeForLoadingOperations, pointID, routeID) VALUES ($this->sortOrder, $this->tLoading, $this->pointID, $this->routeID);";
    }
}

class UpdateRelationBetweenRoutePoints implements IEntityUpdate
{
    private $routePoint1, $routePoint2, $time;

    function __construct($rp1, $rp2, $t)
    {
        $this->routePoint1 = $rp1;
        $this->routePoint2 = $rp2;
        $this->time = $t;
    }


    /**
     * @return string
     */
    function getUpdateQuery()
    {
        return "UPDATE `relations_between_route_points` set `timeForDistance` = $this->time where routePointIDFirst = $this->routePoint1 AND routePointIDSecond = $this->routePoint2";
    }
}