<?php
namespace DAO;
include_once __DIR__ . '/IInvoice.php';
include_once __DIR__ . '/../DAO.php';

class InvoiceEntity implements IInvoiceEntity
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
        if (is_null(self::$_instance)) return new InvoiceEntity();
        return self::$_instance;
    }

    function selectInvoices($start=0, $count=20)
    {
        $array = $this->_DAO->select(new SelectAllInvoices($start, $count));
        $invoices = array();
        for ($i = 0; $i < count($array); $i++) {
            $invoices[$i] = new InvoiceData($array[$i]);
        }
        return $invoices;
    }

    function selectInvoiceByID($id)
    {
        $array = $this->_DAO->select(new SelectInvoiceByID($id));
        return new InvoiceData($array[0]);
    }

    function updateInvoice(InvoiceData $newInvoice)
    {
        // TODO: Implement updateInvoice() method.
    }

    function updateInvoiceStatus($userID, $invoiceNumber, $newInvoiceStatus, $datetime, $comment)
    {
        return $this->_DAO->update(new UpdateInvoiceStatus($userID, $invoiceNumber, $newInvoiceStatus, $datetime, $comment));
//        $this->_DAO->update(new UpdateInvoiceStatus($userID, $invoiceNumber, $newInvoiceStatus, $datetime, $comment));
//        return $this->getInvoiceHistoryByInvoiceNumber($invoiceNumber);
    }

    function updateInvoiceStatuses($userID, $routeListID, $newInvoiceStatus, $datetime, $comment)
    {
        return $this->_DAO->update(new UpdateInvoiceStatuses($userID, $routeListID, $newInvoiceStatus, $datetime, $comment));
    }

    function deleteInvoice(InvoiceData $Invoice)
    {
        // TODO: Implement deleteInvoice() method.
    }

    function addInvoice(InvoiceData $Invoice)
    {
        // TODO: Implement addInvoice() method.
    }

    function getInvoiceStatuses(\PrivilegedUser $pUser)
    {
        return $this->_DAO->select(new SelectInvoiceStatuses($pUser->getUserInfo()->getData('userRoleID')));
    }

    function getInvoiceHistoryByInvoiceNumber($invoiceNumber)
    {
        return $this->_DAO->select(new SelectInvoiceHistory($invoiceNumber));
    }

    function getInvoicesForRouteList($routeListID)
    {
        return $this->_DAO->select(new SelectInvoicesByRouteList($routeListID));
    }
}


class SelectAllInvoices implements IEntitySelect
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
        return "SELECT * FROM `invoices` LIMIT $this->start, $this->count;";
    }
}

class SelectInvoiceByID implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `invoices` WHERE `InvoiceID` = $this->id";
    }
}

class SelectInvoiceStatuses implements IEntitySelect
{
    private $role;

    function __construct($role)
    {
        $this->role = $role;
    }

    function getSelectQuery()
    {
        return "SELECT `invoice_statuses`.`invoiceStatusID`, `invoiceStatusRusName` from `invoice_statuses`, `invoice_statuses_for_user_role` where `invoice_statuses`.`invoiceStatusID` = `invoice_statuses_for_user_role`.`invoiceStatusID` AND userRoleID = '$this->role'";
    }
}

class SelectInvoiceHistory implements IEntitySelect
{
    private $invoiceNumber;

    function __construct($id)
    {
        $this->invoiceNumber = $id;
    }

    function getSelectQuery()
    {
        return "CALL selectRequestStatusHistory('$this->invoiceNumber');";
    }
}

class UpdateInvoiceStatus implements IEntityUpdate
{
    private $invoiceNumber;
    private $userID;
    private $newInvoiceStatus;
    private $datetime;
    private $comment;

    function __construct($userID, $invoiceNumber, $newInvoiceStatus, $datetime, $comment)
    {
        $dao = DAO::getInstance();

        $this->userID = $dao->checkString($userID);
        $this->invoiceNumber = $dao->checkString($invoiceNumber);
        $this->newInvoiceStatus = $dao->checkString($newInvoiceStatus);
        $this->datetime = $dao->checkString($datetime);
        $this->comment = $dao->checkString($comment);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        return "UPDATE `invoices` SET `invoiceStatusID` = '$this->newInvoiceStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i:%s') WHERE `invoiceIDExternal` = '$this->invoiceNumber';";
    }
}

class UpdateInvoiceStatuses implements IEntityUpdate
{
    private $invoices;
    private $userID;
    private $newInvoiceStatus;
    private $datetime;
    private $comment;
    private $palletQuantity;

    function __construct($userID, $invoiceListArray, $newInvoiceStatus, $datetime, $comment, $palletQuantity=0)
    {
        $dao = DAO::getInstance();

        $invoiceListArray = implode(", ",$invoiceListArray);
        $this->invoices = $dao->checkString($invoiceListArray);
        $this->userID = $dao->checkString($userID);
        $this->newInvoiceStatus = $dao->checkString($newInvoiceStatus);
        $this->datetime = $dao->checkString($datetime);
        $this->comment = $dao->checkString($comment);
        $this->palletQuantity = $dao->checkString($palletQuantity);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        if ($this->newInvoiceStatus === 'DEPARTURE') {
            return "UPDATE `invoices` SET `invoiceStatusID` = '$this->newInvoiceStatus', `boxQty` = $this->palletQuantity,`lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') WHERE `invoiceID` IN ($this->invoices);";
        }
        return "UPDATE `invoices` SET `invoiceStatusID` = '$this->newInvoiceStatus', `lastModifiedBy` = '$this->userID', `commentForStatus` = '$this->comment', `lastStatusUpdated` = STR_TO_DATE('$this->datetime', '%d.%m.%Y %H:%i%:%s') WHERE `invoiceID` IN ($this->invoices);";
    }
}


class SelectInvoicesByRouteList implements IEntitySelect
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
        return "SELECT invoiceID, invoiceIDExternal FROM `invoices` WHERE routeListID='$this->routeListID';";
    }
}
