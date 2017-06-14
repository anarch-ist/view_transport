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
    
    function selectDataByRequestId($id){
        $array = $this->_DAO->select(new SelectDataByRequestId($id));
        return $array[0];
    }

    function selectRequestByID($id)
    {
        $array = $this->_DAO->select(new SelectRequestByID($id));
        return new RequestData($array[0]);
//        return $array[0];
    }

    function updateRequest(RequestData $newRequest)
    {
        // TODO: Implement updateRequest() method.
    }

    function updateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $hoursAmount, $transportCompanyId = null, $vehicleId = null, $driverId = null)
    {
        $routeData =  $this->_DAO->select(new SelectRequestByID(str_replace('reqIdExt', '', $requestIDExternal)));
        return $this->_DAO->update2(new UpdateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $routeData[0]['routeListID'], $hoursAmount, $transportCompanyId, $vehicleId, $driverId));
//        $this->_DAO->update(new UpdateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment));
//        return $this->getRequestHistoryByRequestIdExternal($requestIDExternal);
    }

    function updateRequestStatuses($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $hoursAmount=0)
    {
        return $this->_DAO->update2(new UpdateRequestStatuses($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $hoursAmount));
    }

    function updateRequestStatuses2($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $palletsQty, $hoursAmount, $companyId, $vehicleId, $driverId)
    {
        return $this->_DAO->update2(new UpdateRequestStatuses($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $palletsQty, $hoursAmount, $companyId, $vehicleId, $driverId));
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

    function selectRequestByClientIdAndInvoiceNumber($clientId, $invoiceNumber)
    {
        $array = $this->_DAO->select(new SelectRequestByClientIdAndInvoiceNumber($clientId, $invoiceNumber));
        return $array[0];
    }

    function getRequestHistoryByRequestIdExternal($requestIDExternal)
    {
        return $this->_DAO->select(new SelectRequestHistory($requestIDExternal));
    }

    function getRequestsForRouteList($routeListID)
    {
        return $this->_DAO->select(new SelectRequestsByRouteList($routeListID));
    }
    
    
    
    function getVehiclesForCompany($companyId){
        return $this->_DAO->select(new SelectVehicleByCompanyId($companyId));
    }
    
    function getDriversForVehicle($vehicleId){
        return $this->_DAO->select(new SelectDriverByVehicleId($vehicleId));
    }

    function getMarketAgentEmail($requestIDExternal){
        return $this->_DAO->select(new GetMarketAgentEmail($requestIDExternal));
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
        return "SELECT * FROM `requests` WHERE `requestIDExternal` = '$this->id'";
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

class getTransportCompanies implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT * FROM transport_companies";
        // TODO: Implement getSelectQuery() method.
    }

    public function __construct()
    {
    }

}



class SelectDataByRequestId implements IEntitySelect
{
    private $requestIDExternal;
    private $userID;

    function __construct($requestIDExternal, $userID= -1)
    {
        if ($userID < 1) {
            $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        }
        $this->userID = DAO::getInstance()->checkString($userID);
        $this->requestIDExternal = DAO::getInstance()->checkString($requestIDExternal);
    }
    
    function getSelectQuery()
    {
        return "CALL selectDataByRequestId($this->userID,'$this->requestIDExternal')";
    }

}

class SelectRequestByClientIdAndInvoiceNumber implements IEntitySelect
{
    private $clientId, $invoiceNumber;
    private $userID;

    function __construct($clientId, $invoiceNumber, $userID= -1)
    {
        if ($userID < 1) {
            $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        }
        $this->userID = DAO::getInstance()->checkString($userID);
        $this->clientId = DAO::getInstance()->checkString($clientId);
        $this->invoiceNumber = DAO::getInstance()->checkString($invoiceNumber);
    }

    function getSelectQuery()
    {
        return "CALL selectDataByClientIdAndInvoiceNumber($this->userID,'$this->clientId','$this->invoiceNumber')";
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
    private $vehicleNumber;
    private $routeID;
    private $hoursAmount;
    private $transportCompanyId;
    private $vehicleId;
    private $driverId;


    function __construct($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $routeID, $hoursAmount, $transportCompanyId, $vehicleId, $driverId)
    {
        $dao = DAO::getInstance();

        $this->userID = $dao->checkString($userID);
        $this->requestIDExternal = $dao->checkString($requestIDExternal);
        $this->newRequestStatus = $dao->checkString($newRequestStatus);
        $this->datetime = $dao->checkString($datetime);
        $this->comment = $dao->checkString($comment);
        $this->vehicleNumber = $dao->checkString($vehicleNumber);
        $this->routeID = $dao->checkString($routeID);
        $this->hoursAmount = $dao->checkString($hoursAmount);
        $this->transportCompanyId = $dao->checkString($transportCompanyId);
        $this->vehicleId = $dao->checkString($vehicleId);
        $this->driverId = $dao->checkString($driverId);

    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $companyPart = ($this->transportCompanyId == 'NULL') ? "" : ", `transportCompanyId` = $this->transportCompanyId ";
        $vehiclePart = ($this->vehicleId == 'NULL') ? "" : ", `vehicleId` = $this->vehicleId ";
        $driverPart = ($this->driverId == 'NULL') ? "" : ", `driverId` = $this->driverId ";
        $String = "UPDATE `route_lists` SET `licensePlate` = '$this->vehicleNumber' WHERE `routeListID` = '$this->routeID';UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i:%s'), `hoursAmount` = $this->hoursAmount $companyPart $vehiclePart $driverPart WHERE `requestIDExternal` = '$this->requestIDExternal';";
        return $String;
    }
}

class UpdateRequestStatuses implements IEntityUpdate
{
    private $requests;
    private $userID;
    private $newRequestStatus;
    private $datetime;
    private $comment;
    private $vehicleNumber;
    private $palletQuantity;
    private $routeListID;
    private $hoursAmount;
    private $companyId;
    private $vehicleId;
    private $driverId;

    function __construct($userID, $routeListID, $requestListArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $palletQuantity, $hoursAmount, $companyId, $vehicleId, $driverId)
    {
        $dao = DAO::getInstance();

        //It doesn't work like that, see http://stackoverflow.com/questions/22789770/imploding-an-array-into-a-mysql-query
        //$requestListArray = implode(", ",$requestListArray);
//
//        $this->requests = $dao->checkString($requestListArray);
        $this->requests = $requestListArray;
        $this->userID = $dao->checkString($userID);
        $this->newRequestStatus = $dao->checkString($newRequestStatus);
        $this->datetime = $dao->checkString($datetime);
        $this->comment = $dao->checkString($comment);
        $this->vehicleNumber = $dao->checkString($vehicleNumber);
        $this->palletQuantity = $dao->checkString($palletQuantity);
        $this->routeListID = $dao->checkString($routeListID);
        $this->hoursAmount = $dao->checkString($hoursAmount);
        $this->companyId = $dao->checkString($companyId);
        $this->vehicleId = $dao->checkString($vehicleId);
        $this->driverId = $dao->checkString($driverId);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {

        //Why don't we do this? This looks awesome!!
        $hoursPart = ($this->hoursAmount==0) ? "" : ", `hoursAmount` = $this->hoursAmount ";
        $companyPart = ($this->companyId==0) ? "" : ", `transportCompanyId` = $this->companyId ";
        $vehiclePart = ($this->vehicleId==0) ? "" : ", `vehicleId` = $this->vehicleId ";
        $driverPart = ($this->driverId==0) ? "" : ", `driverId` = $this->driverId ";

        if ($this->newRequestStatus === 'DEPARTURE') {
            // добавить запрос на обновление паллет в routeLists
            $query = "UPDATE transmaster_transport_db.`route_lists` SET `palletsQty` = '$this->palletQuantity', `licensePlate` = '$this->vehicleNumber' WHERE `routeListID` = '$this->routeListID'; UPDATE transmaster_transport_db.`requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') $hoursPart $companyPart $vehiclePart $driverPart WHERE `requestIDExternal` IN ('".implode("', '",$this->requests)."');";
        } else {
            $query  = "UPDATE transmaster_transport_db.`route_lists` SET `licensePlate` = '$this->vehicleNumber' WHERE `routeListID` = '$this->routeListID'; UPDATE transmaster_transport_db.`requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') $hoursPart $companyPart $vehiclePart $driverPart  WHERE `requestIDExternal` IN ('".implode("', '",$this->requests)."');";
        }

        return $query;
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
        return "SELECT r.requestID, r.requestIDExternal, r.invoiceNumber, r.requestStatusID, s.requestStatusRusName  FROM `requests` AS r LEFT JOIN `request_statuses` AS s ON r.requestStatusID = s.requestStatusID WHERE routeListID='$this->routeListID';";
    }
}

class GetMarketAgentEmail implements IEntitySelect
{
    private $requestIDExternal;

    function __construct($requestIDExternal)
    {
        $this->requestIDExternal = DAO::getInstance()->checkString($requestIDExternal);
    }

    function getSelectQuery()
    {
        return "SELECT email FROM users WHERE userID = (SELECT marketAgentUserID FROM requests WHERE requestIDExternal = '$this->requestIDExternal') LIMIT 1";
        // TODO: Implement getSelectQuery() method.
    }
}
