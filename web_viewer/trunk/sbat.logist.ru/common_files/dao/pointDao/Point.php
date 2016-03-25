<?php
namespace DAO;
include_once __DIR__ . '/IPoint.php';
include_once __DIR__ . '/../DAO.php';

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
            $points[$i] = new PointData($array[$i]);
        }
        return $points;
    }

    function selectPointByID($id)
    {
        $array = $this->_DAO->select(new SelectPointByID($id));
        return new PointData($array[0]);
    }

    function updatePoint(PointEntity $newPoint)
    {
        // TODO: Implement updatePoint() method.
    }

    function deletePoint(PointEntity $point)
    {
        // TODO: Implement deletePoint() method.
    }

    function addPoint(PointEntity $point)
    {
        // TODO: Implement addPoint() method.
    }

    function selectPointByUserID($userID)
    {

        $result = $this->_DAO->select(new SelectPointByUserID($userID));
        if (empty($result))
            return '';
        else
            return $result[0]['pointName'];
    }
}

class SelectAllPoints implements IEntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `points`;";
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

class SelectPointByUserID implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "select `points`.`pointName` from `points`, `users` where `users`.`UserID` = '$this->id' AND `points`.`pointID` = `users`.`pointID`";
    }
}