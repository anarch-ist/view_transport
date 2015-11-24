<?php
namespace DAO;
include_once 'IPoint.php';
include_once '/../DAO.php';

class PointEntity implements IPointEntity
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
        if (is_null(self::$_instance)) return new PointEntity();
        return self::$_instance;
    }

    function selectPoints()
    {
        $array = $this->_DAO->select(new SelectAllPoints());
        $points = array();
        for ($i = 0; $i < count($array); $i++) {
            $points[$i] = new Point($array[$i]);
        }
        return $points;
    }

    function selectPointByID($id)
    {
        $array = $this->_DAO->select(new SelectPointByID($id));
        return new Point($array[0]);
    }

    function updatePoint($newPoint)
    {
        // TODO: Implement updatePoint() method.
    }

    function deletePoint($point)
    {
        // TODO: Implement deletePoint() method.
    }

    function addPoint($point)
    {
        // TODO: Implement addPoint() method.
    }
}

class Point implements IEntityData
{
    private $array;

    function __construct($array)
    {
        $this->array = $array;
    }

    public function getData($index)
    {
        if (!isset($this->array[$index])) {
            throw new \DataEntityException('Field doesn`t exist: '.$index.' - in '.get_class($this));
        } else {
            return $this->array[$index];
        }
    }

    public function toArray()
    {
        return $this->array;
    }
}

class SelectAllPoints implements IEntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "select * from `points`;";
    }
}

class SelectPointByID implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "select * from `points` where `pointID` = '$this->id'";
    }
}