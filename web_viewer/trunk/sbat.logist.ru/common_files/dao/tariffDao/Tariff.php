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

    function insertTariff($data)
    {
        return $this->_DAO->insert(new InsertTariff($data));
    }

    function getLastInsertedID()
    {
        return $this->_DAO->select(new SelectLastInsertedID());
    }

    function getTariffById($id)
    {
        $array = $this->_DAO->select(new SelectTariffById($id));
        return new TariffData($array[0]);
    }

    function updateTariff($tariffData, $id)
    {
        return $this->_DAO->update(new UpdateTariff($tariffData, $id));
    }
}

class InsertTariff implements IEntityInsert
{
    private $cost, $cost_per_point, $cost_per_hour, $cost_per_box;

    function __construct($data)
    {
        $dao = DAO::getInstance();

        $this->cost = $dao->checkString($data['cost']);
        $this->cost_per_hour = $dao->checkString($data['cost_per_hour']);
        $this->cost_per_point = $dao->checkString($data['cost_per_point']);
        $this->cost_per_box = $dao->checkString($data['cost_per_box']);
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        return "INSERT INTO `tariffs` (cost, cost_per_hour, cost_per_point, cost_per_box) VALUE " .
            "('$this->cost', '$this->cost_per_hour', '$this->cost_per_point', '$this->cost_per_box');";
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

class SelectTariffById implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `tariffs` WHERE `tariffID` = $this->id";
    }
}

class UpdateTariff implements IEntityUpdate
{

    private $id, $cost, $cost_per_hour, $cost_per_point, $cost_per_box;

    function __construct($data, $id)
    {
        $dao = DAO::getInstance();
        $this->id = $id;

        $this->cost = $dao->checkString($data['cost']);
        $this->cost_per_hour = $dao->checkString($data['cost_per_hour']);
        $this->cost_per_point = $dao->checkString($data['cost_per_point']);
        $this->cost_per_box = $dao->checkString($data['cost_per_box']);

    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `tariffs` SET " .
            "cost = '$this->cost', " .
            "cost_per_hour = '$this->cost_per_hour', " .
            "cost_per_point = '$this->cost_per_point'".
            "cost_per_box = '$this->cost_per_box'";
        $query = $query . " WHERE tariffID = $this->id;";
        return $query;
    }
}