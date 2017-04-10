<?php
namespace DAO;
include_once 'ITariff.php';
include_once __DIR__ . '/../DAO.php';
include_once 'Data.php';

class TariffEntity implements ITariffEntity
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
        if (is_null(self::$_instance)) return new TariffEntity();
        return self::$_instance;
    }

    function insertNewTariff($data)
    {
        return $this->_DAO->insert(new InsertTariff($data));
    }

    function getLastInsertedID()
    {
        return $this->_DAO->select(new SelectLastInsertedID());
    }
}

class InsertTariff implements IEntityInsert
{
    private $cost, $cost_per_point, $cost_per_hour;

    function __construct($data)
    {
        $dao = DAO::getInstance();
        $this->cost = $dao->checkString($data['cost']);
        $this->cost_per_hour = $dao->checkString($data['cost_per_hour']);
        $this->cost_per_point = $dao->checkString($data['cost_per_point']);
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        return "INSERT INTO `tariffs` (cost, cost_per_hour, cost_per_point) VALUE " .
            "('$this->cost', '$this->cost_per_hour', '$this->cost_per_point');";
    }
}

class SelectLastInsertedID implements IEntitySelect
{
    /**
     * this function contains query text
     * @return string
     */
    function getSelectQuery()
    {
        return 'SELECT tariffID FROM `tariffs` WHERE tariffID = LAST_INSERT_ID();';
    }
}