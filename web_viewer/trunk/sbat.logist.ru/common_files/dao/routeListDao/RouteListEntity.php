<?php
namespace DAO;
include_once 'IRouteList.php';
include_once __DIR__ . '/../DAO.php';
include_once 'Data.php';

class RouteListEntity implements IRouteListEntity {

    private static $instance;
    private $DAO;

    protected function __construct()
    {
        $this->DAO = DAO::getInstance();
        self::$instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$instance)) return new RouteListEntity();
        return self::$instance;
    }

    function getRouteLists()
    {
        return $this->DAO->select(new SelectAllRouteLists());
    }

    function getRouteListByID($id)
    {
        return $this->DAO->select(new SelectRouteListById($this->DAO->checkString($id)));
    }

    function addRouteList($routeData)
    {
        return $this->DAO->insert(new InsertRouteList($routeData));
    }

    function updateRouteList($id, $routeData)
    {
        return $this->DAO->update(new UpdateRouteList($id, $routeData));
    }

    function selectRouteListsByNumber($number)
    {
        return $this->DAO->select(new SelectRouteListsByNumber($number));
    }
}

class SelectAllRouteLists implements IEntitySelect {
    function getSelectQuery()
    {
        return 'SELECT * FROM `route_lists`';
    }
}

class SelectRouteListById implements IEntitySelect {
    private $id;

    public function __construct($id)
    {
        $this->id = $id;
    }


    function getSelectQuery()
    {
        return "SELECT * FROM `route_lists` WHERE routeListID = $this->id";
    }
}


class InsertRouteList implements IEntityInsert {
    private $routeListIdExternal, $dataSourceId, $routeListNumber, $creationDate, $palletsQty, $driverId,
        $driverPhoneNumber, $forwarderId, $licensePlate, $status, $routeId;

    public function __construct($routeListData)
    {
        $dao = DAO::getInstance();
        $this->routeListIdExternal = $dao->checkString('LSS-' . $routeListData['routeListNumber']);
        $this->dataSourceId = 'ADMIN_PAGE';
        $this->routeListNumber = $dao->checkString($routeListData['routeListNumber']);
        $this->creationDate = $dao->checkString($routeListData['creationDate']);
        $this->palletsQty = $dao->checkString($routeListData['departureDate']);
        $this->driverId = $dao->checkString($routeListData['driverId']);
        $this->driverPhoneNumber = $dao->checkString($routeListData['driverPhoneNumber']);
        $this->forwarderId = $dao->checkString($routeListData['departureDate']);
        $this->licensePlate = $dao->checkString($routeListData['departureDate']);
        $this->status = 'CREATED';
        $this->routeId = $dao->checkString($routeListData['routeId']);
    }

    function getInsertQuery()
    {
        return "INSERT INTO `route_lists` (routeListIDExternal, dataSourceID, routeListNumber, creationDate, departureDate, palletsQty, driverID, driverPhoneNumber, forwarderId, licensePlate, status, routeID) VALUES " .
            "($this->routeListIdExternal, $this->dataSourceId, $this->routeListNumber, CURDATE(), CURDATE(), $this->palletsQty, $this->driverId, $this->driverPhoneNumber, $this->forwarderId, $this->licensePlate, $this->status, $this->routeId)";
    }
}

class UpdateRouteList implements IEntityUpdate {
    private $id, $palletsQty, $driverId, $driverPhoneNumber, $forwarderId, $licensePlate, $status, $routeId;

    public function __construct($id, $routeListData)
    {
        $dao = DAO::getInstance();
        $this->id = $dao->checkString($id);
        $this->palletsQty = $dao->checkString($routeListData['departureDate']);
        $this->driverId = $dao->checkString($routeListData['driverId']);
        $this->driverPhoneNumber = $dao->checkString($routeListData['driverPhoneNumber']);
        $this->forwarderId = $dao->checkString($routeListData['departureDate']);
        $this->licensePlate = $dao->checkString($routeListData['departureDate']);
        $this->status = $dao->checkString($routeListData['status']);
        $this->routeId = $dao->checkString($routeListData['routeId']);
    }

    function getUpdateQuery()
    {
        return "UPDATE `route_lists` SET palletsQty = $this->palletsQty, forwarderId = $this->forwarderId, " .
            "driverID = $this->driverPhoneNumber, licensePlate = $this->licensePlate, status = $this->status, " .
            "routeID = $this->routeId WHERE routeListID = $this->id";
    }
}

class SelectRouteListsByNumber implements IEntitySelect
{
    private $number;

    /**
     * SelectClientsByInn constructor.
     * @param $number
     */
    public function __construct($number)
    {
        $dao = DAO::getInstance();
        $this->number = $dao->checkString($number);
    }


    /**
     * Select all clients
     * @return string
     */
    function getSelectQuery()
    {
        return "SELECT routeID, routeListNumber FROM `route_lists` WHERE routeListNumber LIKE '$this->number%' LIMIT 10";
    }
}

?>