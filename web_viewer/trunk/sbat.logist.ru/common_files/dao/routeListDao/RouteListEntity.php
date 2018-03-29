<?php

namespace DAO;
include_once 'IRouteList.php';
include_once __DIR__ . '/../DAO.php';
include_once 'Data.php';

class RouteListEntity implements IRouteListEntity
{

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
    function getRouteListHistoryByRouteListIdExternal($routeListsIDExternal)
    {
        return $this->DAO->select(new SelectRouteListHistory($routeListsIDExternal));
    }
    function getRouteLists()
    {
        return $this->DAO->select(new SelectAllRouteLists());
    }

    function getRouteListsForTransportCompany()
    {
        return $this->DAO->select(new SelectAllRouteListsForTransportCompany());
    }
    function getRouteListsInFilter()
    {
        return $this->DAO->select(new SelectAllRouteListsForTransportCompany());
    }
    function selectRouteListByRouteListID($routeListID)
    {
        $array = $this->DAO->select(new SelectRouteListByRouteListID ($routeListID));
        return $array[0];
    }
    function getRouteListsForRLPage()
    {
        return $this->DAO->select(new SelectRouteListsForLast3Months());
    }

    function getRouteListByID($id)
    {
        return $this->DAO->select(new SelectRouteListById($this->DAO->checkString($id)));
    }

    function addRouteList($routeData)
    {
        $this->DAO->insert(new InsertRouteList($routeData));
        return $this->DAO->select(new SelectAddedRouteList($routeData));
    }

    function updateRouteList($id, $routeData)
    {
        return $this->DAO->update(new UpdateRouteList($id, $routeData));
    }

    function selectRouteListsByNumber($number)
    {
        return $this->DAO->select(new SelectRouteListsByNumber($number));
    }

    function selectRouteListIdByNumber($number)
    {
        return $this->DAO->select(new SelectRouteListIdByNumber($number));
    }

    function createGenericRouteList($routeId)
    {
        return $this->DAO->insert(new CreateGenericRouteList($routeId));
    }


    function getRouteListForRequestAssignment($routeId)
    {
        $routeList=$this->DAO->select(new GetRouteListForRequestAssignment($routeId));
        if(empty($routeList)){
            if ($this->DAO->insert(new CreateGenericRouteList($routeId))){
                $routeList=$this->DAO->select(new SelectLastAutoInsertedRouteLists(1));
            } else {
                throw new \Exception();
            }
        }
        return $routeList[0];
    }

    function getRouteListForRequestAssignmentWithLimit($routeId, $boxQty)
    {
        $routeList=$this->DAO->select(new GetRouteListForRequestAssignmentWithBoxLimit($routeId,$boxQty));
        if(empty($routeList)){
            if ($this->DAO->insert(new CreateGenericRouteList($routeId))){
                $routeList=$this->DAO->select(new SelectLastAutoInsertedRouteLists(1));
            } else {
                throw new \Exception();
            }
        }
        return $routeList[0];
    }

    function selectLastAutoInsertedRouteLists($quantity){
        return $this->DAO->select(new SelectLastAutoInsertedRouteLists($quantity));
    }

    function assignFreight($routeListId, $freightId){
        $this->DAO->update(new AssignFreight($routeListId,$freightId));

    }

    function selectOne($routeListId){
        return $this->DAO->select(new SelectOne($routeListId))[0];
    }

    function selectOneUnfolded($routeListId){
        return $this->DAO->select(new SelectOneUnfolded($routeListId))[0];
    }

    //Maybe later
//    function updateOwnRequests($routeListId){
//        return
//    }
}

//class UpdateOwnRequests implements IEntityUpdate{
//    private $routeListId;
//
//    /**
//     * UpdateOwnRequests constructor.
//     * @param $routeListId
//     */
//    public function __construct($routeListId)
//    {
//        $this->routeListId = $routeListId;
//    }
//
//    function getUpdateQuery()
//    {
//        $query = "";
//        return $query;
//    }
//
//
//}
class SelectOneUnfolded implements IEntitySelect{
    private $routeListId;

    /**
     * SelectOneUnfolded constructor.
     * @param $routeListId
     */
    public function __construct($routeListId)
    {
        $this->routeListId = $routeListId;
    }

    function getSelectQuery()
    {
        return "SELECT * FROM route_lists LEFT JOIN data_sources ON route_lists.dataSourceID=data_sources.dataSourceID LEFT JOIN route_list_statuses ON route_lists.status = route_list_statuses.routeListStatusID;";
    }


}

class AssignFreight implements IEntityUpdate{
    private $routeListId, $freightId;

    /**
     * AssignFreight constructor.
     * @param $routeListId
     * @param $transportCompanyId
     * @param $driverId
     * @param $vehicleId
     * @param $vehicle2Id
     * @param $vehicle3Id
     * @param $routeId
     */
    public function __construct($routeListId, $freightId)
    {
        $this->routeListId = $routeListId;
        $this->freightId = $freightId;
    }

    function getUpdateQuery()
    {
        $query = "UPDATE route_lists,freight SET route_lists.transport_company_id = freight.transport_company_id, route_lists.driver_id_internal = freight.driver_id, route_lists.vehicle_id=freight.vehicle_id, route_lists.vehicle_2_id=freight.vehicle_2_id, route_lists.vehicle_3_id=freight.vehicle_3_id,route_lists.routeID =freight.route_id, route_lists.freight_id=freight.freight_id  WHERE routeListID = '$this->routeListId' AND freight.freight_id='$this->freightId'";
        return $query;
    }


}

class SelectRouteListsForLast3Months implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT * FROM route_lists LEFT JOIN route_list_statuses ON route_lists.status = route_list_statuses.routeListStatusID JOIN data_sources ON route_lists.dataSourceID = data_sources.dataSourceID LEFT JOIN freight ON route_lists.freight_id = freight.freight_id WHERE route_lists.creationDate >= NOW() - INTERVAL 3 MONTH; ";
    }
}

class SelectAllRouteLists implements IEntitySelect
{
    function getSelectQuery()
    {
        return 'SELECT * FROM `route_lists`';
    }
}
class SelectAllRouteListsForTransportCompany implements IEntitySelect
{
    function getSelectQuery()
    {
//        SELECT * FROM route_lists LEFT JOIN route_list_statuses ON route_lists.status = route_list_statuses.routeListStatusID JOIN data_sources ON route_lists.dataSourceID = data_sources.dataSourceID WHERE route_lists.creationDate >= NOW() - INTERVAL 3 MONTH;
        return 'SELECT routeListID,routeListNumber,departureDate,creationDate, routeListStatusRusName  FROM `route_lists` LEFT JOIN route_list_statuses ON route_lists.status = route_list_statuses.routeListStatusID';
    }
}
class SelectAllRouteListsInFilter implements IEntitySelect
{
    function getSelectQuery()
    {
//        SELECT * FROM route_lists LEFT JOIN route_list_statuses ON route_lists.status = route_list_statuses.routeListStatusID JOIN data_sources ON route_lists.dataSourceID = data_sources.dataSourceID WHERE route_lists.creationDate >= NOW() - INTERVAL 3 MONTH;
        return 'SELECT routeListID,routeListNumber,departureDate,creationDate, routeListStatusRusName  FROM `route_lists` LEFT JOIN route_list_statuses ON route_lists.status = route_list_statuses.routeListStatusID';
    }
}

class SelectRouteListHistory implements IEntitySelect
{
    private $routeListIDExternal;

    function __construct($id)
    {
        $this->routeListIDExternal = $id;
    }

    function getSelectQuery()
    {
        $query = "SELECT * FROM route_list_history WHERE routeListIDExternal='$this->routeListIDExternal';";
        return $query;
    }
}

class SelectOne implements IEntitySelect{
    private $routeListId;

    /**
     * SelectOne constructor.
     * @param $routeListId
     */
    public function __construct($routeListId)
    {
        $this->routeListId = $routeListId;
    }

    function getSelectQuery()
    {
        return "SELECT * FROM route_lists WHERE routeListID = $this->routeListId";
    }


}

class SelectRouteListByRouteListID implements IEntitySelect
{
    private $routeListID;
    private $userID;

    function __construct($routeListID,$userID = -1)
    {
        if ($userID < 1) {
            $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        }
        $this->userID = DAO::getInstance()->checkString($userID);
        $this->routeListIDExternal = DAO::getInstance()->checkString($routeListID);
    }

    function getSelectQuery()
    {
        $query= "SELECT * FROM route_lists JOIN route_list_statuses ON route_lists.status = route_list_statuses.routeListStatusID WHERE routeListIDExternal='$this->routeListIDExternal'";
        return $query;
    }
}

class SelectRouteListById implements IEntitySelect
{
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


class InsertRouteList implements IEntityInsert
{
    private $routeListIdExternal, $dataSourceId, $routeListNumber, $creationDate, $palletsQty,
        $forwarderId, $licensePlate, $status, $routeId;

    public function __construct($routeListData)
    {
        $dao = DAO::getInstance();
        $this->routeListIdExternal = $dao->checkString('LSS-' . $routeListData['routeListNumber']);
        $this->dataSourceId = 'ADMIN_PAGE';
        $this->routeListNumber = $dao->checkString($routeListData['routeListNumber']);
//        $this->creationDate = $dao->checkString($routeListData['creationDate']);
        $this->palletsQty = $dao->checkString($routeListData['palletsQty']);
//        $this->driverId = $dao->checkString($routeListData['driverId']);
//        $this->driverPhoneNumber = $dao->checkString($routeListData['driverPhoneNumber']);
//        $this->driverPhoneNumber = "(SELECT phone FROM `drivers` WHERE id = $this->driverId)";
        $this->forwarderId = $dao->checkString($routeListData['forwarderId']);
        $this->licensePlate = $dao->checkString($routeListData['licensePlate']);
        $this->status = 'CREATED';
        $this->routeId = $dao->checkString($routeListData['routeId']);
    }

    function getInsertQuery()
    {
        $query = "
            INSERT IGNORE INTO `route_lists` (routeListIDExternal, dataSourceID, routeListNumber, creationDate, departureDate, palletsQty,  forwarderId, licensePlate, status, routeID) VALUES " .
            "('$this->routeListIdExternal', '$this->dataSourceId', '$this->routeListNumber', CURDATE(), CURDATE(), $this->palletsQty,  '$this->forwarderId', '$this->licensePlate', '$this->status', $this->routeId);";
        return $query;
    }
}

//It's a workaround. Don't laugh.
class SelectAddedRouteList implements IEntitySelect {
    private $routeListIdExternal;

    /**
     * SelectAddedRouteList constructor.
     * @param $routeListIdExternal
     */
    public function __construct($routeListData)
    {
        $dao = DAO::getInstance();
        $this->routeListIdExternal = $dao->checkString('LSS-' . $routeListData['routeListNumber']);
    }

    /**
     * @return mixed
     */
    function getSelectQuery()
    {
        $query= "SELECT routeListNumber, routeListId from `route_lists` where routeListIDExternal = '$this->routeListIdExternal';";
        return $query;
    }


}



class UpdateRouteList implements IEntityUpdate
{
    private $id, $palletsQty, $driverId, $driverPhoneNumber, $forwarderId, $licensePlate, $status, $routeId, $transportCompanyId, $vehicleId;

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
        $this->transportCompanyId = $dao->checkString($routeListData['transportCompanyId']);
        $this->vehicleId = $dao->checkString($routeListData['vehicleId']);
//        $this->driverId = $dao->checkString($routeListData['driverId']);
    }

    function getUpdateQuery()
    {
        $companyPart = ($this->transportCompanyId == '') ? "" : " transportCompanyId = $this->transportCompanyId, ";
        $vehiclePart = ($this->vehicleId == '') ? "" : " vehicleId = $this->vehicleId, ";
//        $driverPart = ($this->driverId == '') ? "" : " driverId = $this->driverId, ";
        return "UPDATE `route_lists` SET palletsQty = $this->palletsQty, forwarderId = $this->forwarderId, 
            driverID = $this->driverPhoneNumber, licensePlate = $this->licensePlate, status = $this->status, 
            $companyPart 
            $vehiclePart
            routeID = $this->routeId WHERE routeListID = $this->id";
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
        return "SELECT routeID, routeListNumber FROM `route_lists` WHERE routeListNumber LIKE '$this->number%' LIMIT 20";
    }
}

class SelectRouteListIdByNumber implements IEntitySelect
{
    private $number;

    /**
     * SelectRouteListIdByNumber constructor.
     * @param $number
     */
    public function __construct($number)
    {
        $dao = DAO::getInstance();
        $this->number = $dao->checkString($number);
    }

    function getSelectQuery()
    {
        return "SELECT routeListId, routeListNumber FROM `route_lists` where routeListNumber LIKE '$this->number%' LIMIT 20";
    }


}



class GetRouteListsByRoute implements IEntitySelect
{
    private $routeId;

    /**
     * GetRouteListsByRoute constructor.
     * @param $routeId
     */
    public function __construct($routeId)
    {
        $this->routeId = $routeId;
    }

    function getSelectQuery()
    {
        // TODO: Implement getSelectQuery() method.
    }


}

class CreateGenericRouteList implements IEntityInsert
{
    private $routeId;

    /**
     * CreateGenericRouteList constructor.
     * @param $routeId
     */
    public function __construct($routeId)
    {
        $this->routeId = $routeId;
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        $string = "INSERT INTO route_lists (routeListIDExternal,dataSourceID,routeListNumber, creationDate, departureDate, status, routeID) SELECT CONCAT('LSS-',(SELECT COUNT(routeListID) FROM route_lists WHERE dataSourceID='REQUESTS_ASSIGNER')) AS routeListIDExternal , 'REQUESTS_ASSIGNER' AS dataSourceID, CONCAT('LSS-',(SELECT COUNT(routeListID) FROM route_lists WHERE dataSourceID='REQUESTS_ASSIGNER')) AS routeListNumber, NOW() AS creationDate ,(SELECT DATE_ADD(CURDATE(), INTERVAL (9 - IF(DAYOFWEEK(CURDATE())=1, 8, DAYOFWEEK(CURDATE()))) DAY)) AS departureDate, 'CREATED' AS status, '$this->routeId' AS routeId;";
        return $string;
    }
}

class CreateGenericRouteListWithDelay implements IEntityInsert
{
    private $routeId, $interval;

    /**
     * CreateGenericRouteListWithDelay constructor.
     * @param $routeId
     * @param $interval
     */
    public function __construct($routeId, $interval=0)
    {
        $this->routeId = $routeId;
        $this->interval = $interval;
    }


    function getInsertQuery()
    {
        $string = "INSERT INTO route_lists (routeListIDExternal,dataSourceID,routeListNumber, creationDate, departureDate, status, routeID) SELECT CONCAT('LSS-',(SELECT COUNT(routeListID) FROM route_lists WHERE dataSourceID='REQUESTS_ASSIGNER')) AS routeListIDExternal , 'REQUESTS_ASSIGNER' AS dataSourceID, CONCAT('LSS-',(SELECT COUNT(routeListID) FROM route_lists WHERE dataSourceID='REQUESTS_ASSIGNER')) AS routeListNumber, NOW() AS creationDate ,(SELECT DATE_ADD(CURDATE(), INTERVAL (9 - IF(DAYOFWEEK(CURDATE())=1, 8, DAYOFWEEK(CURDATE()))) DAY)) + INTERVAL 7*$this->interval DAY AS departureDate, 'CREATED' AS status, '$this->routeId' AS routeId;";
        return $string;
    }

}

class GetRouteListForRequestAssignment implements IEntitySelect
{
    private $routeId;

    /**
     * getRouteListForRequestAssignment constructor.
     * @param $routeId
     */
    public function __construct($routeId)
    {
        $this->routeId = $routeId;
    }

    function getSelectQuery()
    {
        $query = "SELECT * FROM (SELECT route_lists.routeListID, COUNT(boxQty) AS boxQty, box_limit, routes.routeID, departureDate FROM route_lists LEFT JOIN requests ON route_lists.routeListID = requests.routeListID LEFT JOIN routes ON route_lists.routeID = routes.routeID WHERE route_lists.routeID=$this->routeId AND departureDate>=NOW() - INTERVAL 3 DAY GROUP BY requests.routeListID ORDER BY departureDate ASC LIMIT 1) AS routeLists WHERE boxQty<box_limit;";
        return $query;
    }
}

class GetRouteListForRequestAssignmentWithBoxLimit implements IEntitySelect
{
    private $routeId,$boxLimit;

    /**
     * getRouteListForRequestAssignment constructor.
     * @param $routeId
     */
    public function __construct($routeId,$boxLimit=null)
    {
        $this->routeId = $routeId;
        $this->boxLimit = ($boxLimit==''||$boxLimit==null) ? '' : "+ $boxLimit";
    }

    function getSelectQuery()
    {
        $query = "SELECT * FROM (SELECT route_lists.routeListNumber, routeListIDExternal, route_lists.routeListID, COUNT(boxQty) AS boxQty, box_limit, routes.routeID, departureDate FROM route_lists LEFT JOIN requests ON route_lists.routeListID = requests.routeListID LEFT JOIN routes ON route_lists.routeID = routes.routeID WHERE route_lists.routeID=$this->routeId AND route_lists.dataSourceID='LOGIST_1C' AND departureDate>=NOW() - INTERVAL 3 DAY GROUP BY requests.routeListID ORDER BY departureDate ASC LIMIT 1) AS routeLists WHERE boxQty $this->boxLimit < box_limit;";
        return $query;
    }
}


class SelectLastAutoInsertedRouteLists implements IEntitySelect
{
    private $quantity;

    /**
     * SelectLastAutoInsertedRouteLists constructor.
     * @param $quantity
     */
    public function __construct($quantity)
    {
        $this->quantity = DAO::getInstance()->checkString($quantity);
    }


    function getSelectQuery()
    {
        $query = "SELECT * FROM (SELECT routeListIDExternal, routeListNumber, route_lists.routeListID, '0' AS boxQty, box_limit, routes.routeID, departureDate FROM route_lists LEFT JOIN routes ON route_lists.routeID=routes.routeID WHERE route_lists.dataSourceID = 'REQUESTS_ASSIGNER' ORDER BY routeListID DESC LIMIT $this->quantity) AS routeLists;";
        return $query;
    }

}



?>