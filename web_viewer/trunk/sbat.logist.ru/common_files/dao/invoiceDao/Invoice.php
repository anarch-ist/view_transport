<?php
namespace DAO;
include_once 'IInvoice.php';
include_once '/../DAO.php';

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

    function selectInvoices()
    {
        $count = 25;
        $start = 0;
        switch(func_num_args()) {
            case 2:
                $start = func_get_arg(1);
                $count = func_get_arg(0);
                break;
            case 1:
                $count = func_get_arg(0);
                break;
        }
        $array = $this->_DAO->select(new SelectAllInvoices($start, $count));
        $invoices = array();
        for ($i = 0; $i < count($array); $i++) {
            $invoices[$i] = new Invoice($array[$i]);
        }
        return $invoices;
    }

    function selectInvoiceByID($id)
    {
        $array = $this->_DAO->select(new SelectInvoiceByID($id));
        return new Invoice($array[0]);
    }

    function updateInvoice($newInvoice)
    {
        // TODO: Implement updateInvoice() method.
    }

    function deleteInvoice($Invoice)
    {
        // TODO: Implement deleteInvoice() method.
    }

    function addInvoice($Invoice)
    {
        // TODO: Implement addInvoice() method.
    }

    function getInvoiceStatuses()
    {
        return $this->_DAO->select(new SelectInvoiceStatuses());
    }
}

class Invoice implements IEntityData
{
    private $array;

    function __construct($array)
    {
        $this->array = $array;
    }

    public function getData($index)
    {
        if (!isset($this->array[$index])) {
            throw new \DataEntityException('Field doesn`t exist: ' . $index . ' - in ' . get_class($this));
        } else {
            return $this->array[$index];
        }
    }

    public function toArray()
    {
        return $this->array;
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
        return "select * from `invoices` LIMIT $this->start, $this->count;";
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
        return "select * from `invoices` where `InvoiceID` = $this->id";
    }
}

class SelectInvoiceStatuses implements IEntitySelect
{
    function getSelectQuery()
    {
        return "select * from `invoice_statuses`";
    }
}
