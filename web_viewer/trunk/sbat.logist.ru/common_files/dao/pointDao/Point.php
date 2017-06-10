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

    function selectAllPointIDAndPointName()
    {
        return $this->_DAO->select(new SelectAllPointIDAndPointName());
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

    function selectPointsByName($name) {
        return $this->_DAO->select(new SelectPointsByName($this->_DAO->checkString($name)));
    }
}

class SelectAllPointIDAndPointName implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT DISTINCT p.pointID, p.pointName FROM `points` p JOIN `route_points` ON p.pointID = route_points.pointID";
    }
}


class SelectPointsByName implements IEntitySelect
{
    private $name;

    /**
     * SelectPointsByName constructor.
     * @param $name
     */
    public function __construct($name)
    {
        $this->name = $name;
    }

    function getSelectQuery()
    {
        return "SELECT pointID, pointName FROM `points` WHERE pointName LIKE '$this->name%' LIMIT 10";
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