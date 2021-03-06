<?php

namespace DAO;
include_once __DIR__ . '/IRequest.php';
include_once __DIR__ . '/../DAO.php';

class RequestEntity implements IRequestEntity
{
    private static $_instance;
    private $_DAO;

    function getRoutesForEmptyRequests($warehousePointId)
    {
        $array = $this->_DAO->select(new GetRoutesForEmptyRequests(DAO::getInstance()->checkString($warehousePointId)));
        return $array;
    }

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

    function selectRequests($start = 0, $count = 20)
    {
        $array = $this->_DAO->select(new SelectAllRequests($start, $count));
        $requests = array();
        for ($i = 0; $i < count($array); $i++) {
            $requests[$i] = new RequestData($array[$i]);
        }
        return $requests;
    }

    function selectDataByRequestId($id)
    {
        $array = $this->_DAO->select(new SelectDataByRequestId($id));
        return $array[0];
    }

    function selectLastInserted()
    {
        $array = $this->_DAO->select(new SelectLastInserted());
    }

    function selectRequestByID($id)
    {
        $array = $this->_DAO->select(new SelectRequestByID($id));
//        return new RequestData($array[0]);
        return $array[0];
    }


    function updateRequest($newRequest)
    {
        return $this->_DAO->update(new EditRequest($newRequest));
    }

    function updateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $hoursAmount, $goodCost ,$transportCompanyId = null, $vehicleId = null, $driverId = null)
    {
        $routeData = $this->_DAO->select(new SelectRequestByID(str_replace('reqIdExt', '', $requestIDExternal)));
        return $this->_DAO->update2(new UpdateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $routeData[0]['routeListID'], $hoursAmount, $goodCost));
//        $this->_DAO->update(new UpdateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment));
//        return $this->getRequestHistoryByRequestIdExternal($requestIDExternal);
    }

    function updateRequestStatuses($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $hoursAmount = 0)
    {
        return $this->_DAO->update2(new UpdateRequestStatuses($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $hoursAmount));
    }

    function updateRequestStatuses2($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $palletsQty, $goodCost,$hoursAmount, $companyId, $vehicleId, $driverId,$vehicle2Id="", $vehicle3Id="")
    {
        return $this->_DAO->update2(new UpdateRequestStatuses($userID, $routeListID, $requestIdExternalArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $palletsQty, $goodCost,$hoursAmount, $companyId, $vehicleId, $driverId,$vehicle2Id,$vehicle3Id));
    }

    function deleteRequest($requestIDExternal)
    {
        return $this->_DAO->delete(new DeleteRequest($requestIDExternal));
    }

    function addRequest($requestData)
    {
        return $this->_DAO->insert(new addRequest($requestData));
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


    function getVehiclesForCompany($companyId)
    {
        return $this->_DAO->select(new SelectVehicleByCompanyId($companyId));
    }

    function getDriversForVehicle($vehicleId)
    {
        return $this->_DAO->select(new SelectDriverByVehicleId($vehicleId));
    }

    function getMarketAgentEmail($requestIDExternal)
    {
        return $this->_DAO->select(new GetMarketAgentEmail($requestIDExternal));
    }

    function getEmptyRequestsByWarehousePointIdAndRouteId($wpid, $rid)
    {
        $array = $this->_DAO->select(new GetEmptyRequestsByWarehouseIdAndRouteId($rid, $wpid));
        return $array;
    }

    function assignRequests($requests,$routeListId=null)
    {
        if($routeListId!=null){
            $array = $this->_DAO->update(new AssignRequests($requests,$routeListId));
        } else {
            $array = $this->_DAO->update(new AssignRequests($requests));
        }
        return $array;
    }



}

class addRequest implements IEntityInsert
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
    private $requestNumber;
    private $clientId;
    private $routeListId;
    private $marketAgentUserId;
    private $invoiceNumber;
    private $documentNumber;
    private $firma;
    private $storage;
    private $contactName;
    private $contactPhone;
    private $deliveryDate;
    private $warehousePointId;
    private $boxQty;


    /**
     * addRequest constructor.
     * @param Associative array
     */
    function __construct($requestData)
//        $userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $routeID, $hoursAmount, $transportCompanyId, $vehicleId, $driverId)
    {
        $dao = DAO::getInstance();

        $this->userID = $dao->checkString($requestData['userID']);
        $this->requestIDExternal = $dao->checkString($requestData['requestIDExternal']);
        $this->newRequestStatus = $dao->checkString($requestData['nrewRequestStatus']);
        $this->datetime = ($dao->checkString($requestData['datetime'])=='') ? ' NOW() ' : "STR_TO_DATE('".$requestData['datetime']."', '%d%m%Y %H:%i:%s'),";
        $this->comment = $dao->checkString($requestData['comment']);
        $this->vehicleNumber = $dao->checkString($requestData['vehicleNumber']);
        $this->routeID = $dao->checkString($requestData['routeID']);
//        $this->hoursAmount = $dao->checkString();
        $this->transportCompanyId = $dao->checkString($requestData['transportCompanyId']);
        $this->vehicleId = $dao->checkString($requestData['vehicleId']);
        $this->driverId = $dao->checkString($requestData['driverId']);
        $this->requestNumber = $dao->checkString($requestData['requestNumber']);
        $this->clientId = $dao->checkString($requestData['clientID']);
        $this->routeListId = $dao->checkString($requestData['routeListID']);
        $this->marketAgentUserId = $dao->checkString($requestData['marketAgentUserId']);
        $this->invoiceNumber = $dao->checkString($requestData['invoiceNumber']);
        $this->documentNumber = $dao->checkString($requestData['documentNumber']);
        $this->firma = $dao->checkString($requestData['firma']);
        $this->contactName = $dao->checkString($requestData['contactName']);
        $this->contactPhone = $dao->checkString($requestData['contactPhone']);
        $this->warehousePointId = $dao->checkString($requestData['warehousePointId']);
        $this->storage = $dao->checkString($requestData['storage']);
//        $date = date_create_from_format('d-m-Y',$dao->checkString($requestData['deliveryDate']));
//        $this->deliveryDate = date_format($date,'Y-m-d H:i:s');

        $this->deliveryDate = $dao->checkString($requestData['deliveryDate']);
        $this->boxQty = $dao->checkString($requestData['boxQty']);
//        date_format($date, 'Y-m-d H:i:s');

    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        $companyPart = ($this->transportCompanyId == '') ? "NULL" : $this->transportCompanyId;
        $vehiclePart = ($this->vehicleId == '') ? "NULL" : $this->vehicleId;
        $driverPart = ($this->driverId == '') ? "NULL" : $this->driverId;
        $String = "INSERT INTO requests (requestIDExternal,
 dataSourceID, 
 requestNumber, 
 requestDate, 
 clientID, 
 destinationPointID, 
 marketAgentUserID, 
 invoiceNumber, 
 invoiceDate, 
 documentNumber, 
 documentDate, 
 firma, 
 storage, 
 requestStatusID, 
 warehousePointID, 
 routeListID, 
 transportCompanyId, 
 vehicleId, 
 driverId,
 deliveryDate,
 boxQty
 )  
VALUES (
(SELECT CONCAT('LSS-',requestID) FROM requests AS rqs ORDER BY requestID DESC LIMIT 1),
'ADMIN_PAGE',
(SELECT CONCAT('LSS-',requestID) FROM requests AS rqs ORDER BY requestID DESC LIMIT 1),
CURDATE(),
$this->clientId,
(SELECT pointID FROM route_points WHERE routeID = (SELECT routeID FROM route_lists WHERE routeListID = $this->routeListId) ORDER BY sortOrder DESC LIMIT 1),
$this->marketAgentUserId,
'$this->invoiceNumber',
CURDATE(),
'$this->documentNumber',
CURDATE(),
'$this->firma',
'$this->storage',
'CREATED',
$this->warehousePointId,
$this->routeListId,
$companyPart,
$vehiclePart,
$driverPart,
$this->datetime,
'$this->boxQty'
);";
        //STR_TO_DATE($this->deliveryDate, '%d%m%Y %H:%i:%s'),
        return $String;
    }
}

class GetRequestsInTransit implements IEntitySelect
{
    public function getSelectQuery()
    {
        return "SELECT DISTINCT vehicleId,destinationPointID,x,y FROM requests INNER JOIN requests ON points.pointID = requests.destinationPointID WHERE requestStatusID='DEPARTURE' AND vehicleId IS NOT NULL;";
    }
}

class SetRequestsToDeliveredByVehicleId implements IEntityUpdate
{
    private $vehicleId;
    private $destinationPointId;

    /**
     * SetRequestsToDeliveredByVehicleId constructor.
     * @param $vehicleId
     */
    public function __construct($vehicleId, $destinationPointId)
    {
        $this->vehicleId = $vehicleId;
        $this->destinationPointId = $destinationPointId;
    }


    public function getUpdateQuery()
    {
        return "UPDATE requests SET requests.requestStatusID='DELIVERED' WHERE requests.destinationPointID=$this->$this->destinationPointId AND requests.vehicleId=$this->vehicleId AND requests.requestStatusID='DEPARTURE';";
    }
}

class DeleteRequest implements IEntityDelete
{
    private $requestIDExternal;
//    private $dao

    /**
     * DeleteRequest constructor.
     * @param string
     * @return DeleteRequest
     */
    public function __construct($requestIDExternal)
    {
        $dao = DAO::getInstance();
        $this->requestIDExternal = $dao->checkString($requestIDExternal);
    }

    /**
     * @return string
     */
    function getDeleteQuery()
    {
        $query = "DELETE FROM requests WHERE requestIDExternal = '$this->requestIDExternal' LIMIT 1;";
        return $query;
    }


}

class EditRequest implements IEntityUpdate
{
    private $requestIDExternal;
    private $userID;
    private $newRequestStatus;
//    private $datetime;
    private $comment;
    private $vehicleNumber;
    private $routeID;
//    private $hoursAmount;
    private $transportCompanyId;
    private $vehicleId;
    private $driverId;
    private $requestNumber;
    private $clientId;
    private $routeListId;
    private $marketAgentUserId;
    private $invoiceNumber;
    private $documentNumber;
    private $firma;
    private $storage;
    private $contactName;
    private $contactPhone;
    private $deliveryDate;
    private $warehousePointId;
    private $boxQty;

    function __construct($requestData)
//        $userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $routeID, $hoursAmount, $transportCompanyId, $vehicleId, $driverId)
    {
        $dao = DAO::getInstance();

//        $this->userID = $dao->checkString($requestData['userID']);
        $this->requestIDExternal = $dao->checkString($requestData['requestIDExternal']);
        $this->newRequestStatus = $dao->checkString($requestData['newRequestStatus']);
        $this->datetime = $dao->checkString($requestData['datetime']);
        $this->comment = $dao->checkString($requestData['comment']);
        $this->vehicleNumber = $dao->checkString($requestData['vehicleNumber']);
        $this->routeID = $dao->checkString($requestData['routeID']);
        $this->transportCompanyId = $dao->checkString($requestData['transportCompanyId']);
        $this->vehicleId = $dao->checkString($requestData['vehicleId']);
        $this->driverId = $dao->checkString($requestData['driverId']);
        $this->requestNumber = $dao->checkString($requestData['requestNumber']);
        $this->clientId = $dao->checkString($requestData['clientID']);
        $this->clientId = ($this->clientId == 'dummy') ? '' : " clientId = $this->clientId, ";
        $this->routeListId = $dao->checkString($requestData['routeListID']);
        $this->routeListId = ($this->routeListId == 'dummy') ? '' : " routeListId = $this->routeListId, destinationPointID=(SELECT pointID FROM route_points WHERE routeID = (SELECT routeID FROM route_lists WHERE routeListID = $this->routeListId) ORDER BY sortOrder DESC LIMIT 1),";
        $this->marketAgentUserId = $dao->checkString($requestData['marketAgentUserId']);
        $this->marketAgentUserId = ($this->marketAgentUserId == 'dummy') ? '' : " marketAgentUserId = $this->marketAgentUserId, ";
        $this->invoiceNumber = $dao->checkString($requestData['invoiceNumber']);
        $this->documentNumber = $dao->checkString($requestData['documentNumber']);
        $this->firma = $dao->checkString($requestData['firma']);
        $this->contactName = $dao->checkString($requestData['contactName']);
        $this->contactPhone = $dao->checkString($requestData['contactPhone']);
        $this->warehousePointId = $dao->checkString($requestData['warehousePointId']);
        $this->warehousePointId = ($this->warehousePointId == 'dummy') ? '' : " warehousePointId = $this->warehousePointId, ";
        $this->storage = $dao->checkString($requestData['storage']);
//        $date = date_create_from_format('d-m-Y',$dao->checkString($requestData['deliveryDate']));
//        $this->deliveryDate = date_format($date,'Y-m-d H:i:s');
        $this->deliveryDate = $dao->checkString($requestData['deliveryDate']);
        $this->boxQty = $dao->checkString($requestData['boxQty']);
//        date_format($date, 'Y-m-d H:i:s');

    }

    function getUpdateQuery()
    {
        $companyPart = ($this->transportCompanyId == '') ? "" : " transportCompanyId = $this->transportCompanyId, ";
        $vehiclePart = ($this->vehicleId == '') ? "" : " vehicleId = $this->vehicleId, ";
        $driverPart = ($this->driverId == '') ? "" : " driverId = $this->driverId, ";
        $String = "UPDATE requests
        SET  
        invoiceNumber = '$this->invoiceNumber', 
        $this->clientId
        $this->warehousePointId
        $this->marketAgentUserId
        $this->routeListId
        firma = '$this->firma', 
        storage = '$this->storage',  
        $companyPart 
        $vehiclePart
        $driverPart
        boxQty = '$this->boxQty' WHERE requestIDExternal = '$this->requestIDExternal' LIMIT 1;";
        return $String;
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

class SelectLastInserted implements IEntitySelect
{
    /**
     * @return mixed
     */
    function getSelectQuery()
    {
        return "SELECT * FROM `requests` WHERE dataSourceID = 'ADMIN_PAGE' AND requestID = (SELECT MAX(requestID) FROM requests)";
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

    function __construct($requestIDExternal, $userID = -1)
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

    function __construct($clientId, $invoiceNumber, $userID = -1)
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
        $query= "CALL selectDataByClientIdAndInvoiceNumber($this->userID,'$this->clientId','$this->invoiceNumber')";
        return $query;
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
    private $goodCost;
    private $hoursAmount;
    private $transportCompanyId;
    private $vehicleId;
    private $driverId;


    function __construct($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $routeID,$goodCost, $hoursAmount, $transportCompanyId=null, $vehicleId=null, $driverId=null)
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
        $this->goodCost = $dao->checkString($goodCost);
//        $this->transportCompanyId = $dao->checkString($transportCompanyId);
//        $this->vehicleId = $dao->checkString($vehicleId);
//        $this->driverId = $dao->checkString($driverId);

    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
//        $companyPart = ($this->transportCompanyId == 'NULL') ? "" : ", `transportCompanyId` = $this->transportCompanyId ";
//        $vehiclePart = ($this->vehicleId == 'NULL') ? "" : ", `vehicleId` = $this->vehicleId ";
//        $driverPart = ($this->driverId == 'NULL') ? "" : ", `driverId` = $this->driverId ";
//        $String = "UPDATE `route_lists` SET `licensePlate` = '$this->vehicleNumber' WHERE `routeListID` = '$this->routeID';UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i:%s'), `hoursAmount` = $this->hoursAmount $companyPart $vehiclePart $driverPart WHERE `requestIDExternal` = '$this->requestIDExternal';";
        $String = "UPDATE `route_lists` SET `licensePlate` = '$this->vehicleNumber' WHERE `routeListID` = '$this->routeID';";
        $String.= "UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i:%s'),`goodsCost` = $this->goodCost, `hoursAmount` = $this->hoursAmount WHERE `requestIDExternal` = '$this->requestIDExternal';";
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
    private $goodCost;
    private $companyId;
    private $vehicleId;
    private $vehicle2Id;
    private $vehicle3Id;
    private $driverId;

    function __construct($userID, $routeListID, $requestListArray, $newRequestStatus, $datetime, $comment, $vehicleNumber, $palletQuantity,$goodCost, $hoursAmount, $companyId, $vehicleId, $driverId, $vehicle2Id="", $vehicle3Id="")
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
        $this->goodCost = $dao->checkString($goodCost);
        $this->hoursAmount = $dao->checkString($hoursAmount);
        $this->companyId = $dao->checkString($companyId);
        $this->vehicleId = $dao->checkString($vehicleId);
        $this->vehicle2Id = $dao->checkString($vehicle2Id);
        $this->vehicle3Id = $dao->checkString($vehicle3Id);
        $this->driverId = $dao->checkString($driverId);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {

        //Why don't we do this? This looks awesome!!
//        $goodCost = ($this->goodCost == 0) ? "" : ", `goodCost` = $this->goodCost ";
        $hoursPart = ($this->hoursAmount == 0) ? "" : ", `hoursAmount` = $this->hoursAmount ";
        $companyPart = ($this->companyId == 0) ? "" : ", `transport_company_id` = $this->companyId ";
        $vehiclePart = ($this->vehicleId == 0) ? "" : ", `vehicle_id` = $this->vehicleId ";
        $driverPart = ($this->driverId == 0) ? "" : ", `driver_id_internal` = $this->driverId ";

        if ($this->newRequestStatus === 'DEPARTURE') {
            // добавить запрос на обновление паллет в routeLists
            $query = "UPDATE `route_lists` SET `palletsQty` = '$this->palletQuantity', `licensePlate` = '$this->vehicleNumber', vehicle_2_id='$this->vehicle2Id', vehicle_3_id = '$this->vehicle3Id' $companyPart $vehiclePart $driverPart WHERE `routeListID` = '$this->routeListID'; 
          UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `goodsCost` = $this->goodCost,`lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') $hoursPart   WHERE `requestIDExternal` IN ('" . implode("', '", $this->requests) . "');";
        } else {
            $query = "UPDATE `route_lists` SET `licensePlate` = '$this->vehicleNumber', vehicle_2_id='$this->vehicle2Id', vehicle_3_id = '$this->vehicle3Id' $companyPart $vehiclePart $driverPart WHERE `routeListID` = '$this->routeListID'; 
          UPDATE `requests` SET `requestStatusID` = '$this->newRequestStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment',`goodsCost` = $this->goodCost, `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') $hoursPart    WHERE `requestIDExternal` IN ('" . implode("', '", $this->requests) . "');";
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
        return "SELECT r.requestID, r.requestIDExternal, r.invoiceNumber, r.requestStatusID, r.clientID, s.requestStatusRusName, r.deliveryDate, r.documentNumber, r.boxQty  FROM `requests` AS r LEFT JOIN `request_statuses` AS s ON r.requestStatusID = s.requestStatusID WHERE routeListID='$this->routeListID';";
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

class GetVehicleWialonId implements IEntitySelect
{
    private $requestIDExternal;

    function __construct($requestIDExternal)
    {
        $this->requestIDExternal = DAO::getInstance()->checkString($requestIDExternal);
    }

    function getSelectQuery()
    {
        return "SELECT vehicles.wialon_id FROM vehicles WHERE vehicles.id = (SELECT vehicleId FROM requests WHERE requestIDExternal = $this->requestIDExternal)";
    }
}

class GetRoutesForEmptyRequests implements IEntitySelect
{
    private $warehousePointId;

    /**
     * GetEmptyRequestsByPointId constructor.
     * @param $warehousePointId
     */
    public function __construct($warehousePointId)
    {
        $this->warehousePointId = $warehousePointId;
    }


    function getSelectQuery()
    {
        $string = "SELECT DISTINCT routes.routeId, box_limit FROM (SELECT lastWeekRequests.warehousePointID, lastWeekRequests.storage, lastWeekRequests.requestID, delivery_route_points.routeId, lastWeekRequests.destinationPointID, lastWeekRequests.deliveryDate FROM (SELECT * FROM requests WHERE requestDate >= NOW() - INTERVAL 31 DAY AND  routeListID IS NULL AND requestStatusID IN ('CHECK_PASSED','READY') AND (warehousePointID  = $this->warehousePointId OR storage = (SELECT storage FROM storages_to_points WHERE point_id = $this->warehousePointId LIMIT 1)) GROUP BY requestID) AS lastWeekRequests INNER JOIN delivery_route_points ON lastWeekRequests.destinationPointID = delivery_route_points.pointId GROUP BY requestID) AS requestsAndRoutes INNER JOIN routes ON requestsAndRoutes.routeId = routes.routeID;";
        return $string;
    }

}


class GetEmptyRequestsByWarehouseIdAndRouteId implements IEntitySelect
{
    private $routeId, $warehousePointId;

    /**
     * GetEmptyRequestsByWarehouseIdAndRouteId constructor.
     * @param $routeId
     * @param $warehousePointId
     */
    public function __construct($routeId, $warehousePointId)
    {
        $this->routeId = $routeId;
        $this->warehousePointId = $warehousePointId;
    }


    function getSelectQuery()
    {
        $string = "SELECT requestID, boxQty FROM (SELECT lastWeekRequests.requestID, delivery_route_points.routeId, lastWeekRequests.destinationPointID, lastWeekRequests.deliveryDate, lastWeekRequests.boxQty FROM (SELECT * FROM requests WHERE requestDate >= NOW() - INTERVAL 31 DAY AND  routeListID IS NULL AND requestStatusID IN ('CHECK_PASSED','READY') AND (warehousePointID = $this->warehousePointId OR storage = (SELECT storage FROM storages_to_points WHERE point_id = $this->warehousePointId LIMIT 1)) GROUP BY requestID ORDER BY deliveryDate ASC) AS lastWeekRequests INNER JOIN delivery_route_points ON lastWeekRequests.destinationPointID = delivery_route_points.pointId GROUP BY requestID) AS requestsAndRoutes WHERE routeId = $this->routeId ORDER BY boxQty DESC;";
        return $string;
        // TODO: Implement getSelectQuery() method.
    }

}

class AssignRequests implements IEntityUpdate
{
    private $requests, $routeListId;

    /**
     * AssignRequests constructor.
     * @param $requests
     * @param $routeListId
     */
    public function __construct($requests, $routeListId = " (SELECT routeListID FROM route_lists WHERE dataSourceID = 'REQUESTS_ASSIGNER' ORDER BY routeListID DESC LIMIT 1) ")
    {
        $this->requests = "(";
        foreach ($requests as $request) {
            $this->requests = $this->requests . $request['requestID'] . ',';
        }
        $this->requests = rtrim($this->requests,","). ') ';
        $this->routeListId = $routeListId;
    }


    function getUpdateQuery()
    {
        $query = "UPDATE requests SET routeListID=$this->routeListId, requestStatusID='ROUTE_LIST_MADE' WHERE requests.requestID IN $this->requests";
        return $query;
        // TODO: Implement getUpdateQuery() method.
    }


}