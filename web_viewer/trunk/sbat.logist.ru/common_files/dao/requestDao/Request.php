<?php
namespace DAO;
include_once __DIR__ . '/IRequest.php';
include_once __DIR__ . '/../DAO.php';

class RequestEntity implements IRequestEntity
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
        if (is_null(self::$_instance)) return new RequestEntity();
        return self::$_instance;
    }

    function selectRequests($start=0, $count=20)
    {
        $array = $this->_DAO->select(new SelectAllRequests($start, $count));
        $requests = array();
        for ($i = 0; $i < count($array); $i++) {
            $requests[$i] = new RequestData($array[$i]);
        }
        return $requests;
    }

    function selectRequestByID($id)
    {
        $array = $this->_DAO->select(new SelectRequestByID($id));
        return new RequestData($array[0]);
    }

    function updateRequest(RequestData $newRequest)
    {
        // TODO: Implement updateRequest() method.
    }

    function updateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment)
    {
        return $this->_DAO->update(new UpdateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment));
//        $this->_DAO->update(new UpdateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment));
//        return $this->getRequestHistoryByRequestIdExternal($requestIDExternal);
    }

    function updateRequestStatuses($userID, $routeListID, $newRequestStatus, $datetime, $comment)
    {

        return $this->_DAO->update(new UpdateRequestStatuses($userID, $routeListID, $newRequestStatus, $datetime, $comment));
    }

    function updateRequestStatuses2($userID, $routeListID, $newRequestStatus, $datetime, $comment, $palletsQty)
    {
        $routeData =  $this->_DAO->select(new SelectRequestByID($routeListID[0]));
        return $this->_DAO->update2(new UpdateRequestStatuses($userID, $routeListID, $newRequestStatus, $datetime, $comment, $palletsQty, $routeData[0]['routeListID']));
    }

    function deleteRequest(RequestData $Request)
    {
        // TODO: Implement deleteRequest() method.
    }

    function addRequest(RequestData $Request)
    {
        // TODO: Implement addRequest() method.
    }

    function getRequestStatuses(\PrivilegedUser $pUser)
    {
        return $this->_DAO->select(new SelectRequestStatuses($pUser->getUserInfo()->getData('userRoleID')));
    }

    function getRequestHistoryByRequestIdExternal($requestIDExternal)
    {
        return $this->_DAO->select(new SelectRequestHistory($requestIDExternal));
    }

    function getRequestsForRouteList($routeListID)
    {
        return $this->_DAO->select(new SelectRequestsByRouteList($routeListID));
    }
}


class SelectAllRequests implements IEntitySelect
{
    private $start;
    private $count;

    function __construct($start, $count)
    {
        $this->start = $start;
        $this->count = $count;
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `requests` LIMIT $this->start, $this->count;";
    }
}

class SelectRequestByID implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `requests` WHERE `requestID` = $this->id";
    }
}

class SelectRequestStatuses implements IEntitySelect
{
    private $role;

    function __construct($role)
    {
        $this->role = $role;
    }

    function getSelectQuery()
    {
        return "SELECT `request_statuses`.`requestStatusID`, `requestStatusRusName` from `request_statuses`, `request_statuses_for_user_role` where `request_statuses`.`requestStatusID` = `request_statuses_for_user_role`.`requestStatusID` AND userRoleID = '$this->role'";
    }
}

class SelectRequestHistory implements IEntitySelect
{
    private $requestIDExternal;

    function __construct($id)
    {
        $this->requestIDExternal = $id;
    }

    function getSelectQuery()
    {
        return "CALL selectRequestStatusHistory('$this->requestIDExternal');";
    }
}

class UpdateRequestStatus implements IEntityUpdate
{
    private $requestIDExternal;
    private $userID;
    private $newRequestStatus;
    private $datetime;
    private $comment;

    function __construct($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment)
    {
        $dao = DAO::getInstance();

        $this->userID = $dao->checkString($userID);
        $this->requestIDExternal = $dao->checkString($requestIDExternal);
        $this->newRequestStatus = $dao->checkString($newRequestStatus);
        $this->datetime = $dao->checkString($datetime);
        $this->comment = $dao->checkString($comment);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        return "UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i:%s') WHERE `requestIDExternal` = '$this->requestIDExternal';";
    }
}

class UpdateRequestStatuses implements IEntityUpdate
{
    private $requests;
    private $userID;
    private $newRequestStatus;
    private $datetime;
    private $comment;
    private $palletQuantity;
    private $routeID;

    function __construct($userID, $requestListArray, $newRequestStatus, $datetime, $comment, $palletQuantity=0, $routeID)
    {
        $dao = DAO::getInstance();

        $requestListArray = implode(", ",$requestListArray);
        $this->requests = $dao->checkString($requestListArray);
        $this->userID = $dao->checkString($userID);
        $this->newRequestStatus = $dao->checkString($newRequestStatus);
        $this->datetime = $dao->checkString($datetime);
        $this->comment = $dao->checkString($comment);
        $this->palletQuantity = $dao->checkString($palletQuantity);
        $this->routeID = $dao->checkString($routeID);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {


        if ($this->newRequestStatus === 'DEPARTURE') {
            // добавить запрос на обновление паллет в routeLists
            return "UPDATE `route_lists` SET `palletsQty` = '$this->palletQuantity' WHERE `routeListID` = '$this->routeID';UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') WHERE `requestID` IN ($this->requests);";
        }
        return "UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') WHERE `requestID` IN ($this->requests);";
        
    }
}


class SelectRequestsByRouteList implements IEntitySelect
{
    private $routeListID;

    function __construct($routeListID)
    {
        $this->routeListID = DAO::getInstance()->checkString($routeListID);
    }

    /**
     * @return string
     */
    function getSelectQuery()
    {
        return "SELECT requestID, requestIDExternal FROM `requests` WHERE routeListID='$this->routeListID';";
    }
}
