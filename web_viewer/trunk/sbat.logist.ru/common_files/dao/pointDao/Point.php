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

    function getWarehouses(){
        return $this->_DAO->select(new SelectWarehouses());
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

    function getAllDistinctPointCoordinates(){
        $result = $this->_DAO->select(new GetAllDistinctPointCoordinates());
        return $result;
    }

    function getDistinctPointCoordinatesForManager($managerId){
        $result = $this->_DAO->select(new GetDistinctPointCoordinatesForManager($managerId));
        return $result;
    }

    function getAverageRequestsCount(){
        return $this->_DAO->select(new GetAverageRequestCount())[0];
    }

    function getPointCoordinatesByPointId($pointId){
        return $this->_DAO->select(new GetPointCoordinatesByPointId($pointId));
    }
}

class SelectWarehouses implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT pointID, pointName FROM points WHERE pointTypeID='WAREHOUSE'";
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

class GetAllDistinctPointCoordinates implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT DISTINCT points.x, points.y, points.pointName, points.address,  points.requests_count AS requests from points WHERE x<>0.0 ;";
//        return "SELECT DISTINCT points.x, points.y, points.pointName, points.address from points WHERE x<>0.0";
    }
}

class GetDistinctPointCoordinatesForManager implements IEntitySelect
{
    private $manager_id;

    /**
     * GetDistinctPointCoordinatesForManager constructor.
     * @param $manager_id
     */
    public function __construct($manager_id)
    {
        $this->manager_id = $manager_id;
    }


    function getSelectQuery()
    {
        return "SELECT DISTINCT points.x, points.y, points.pointName, points.address,  points.requests_count AS requests from points INNER JOIN points_to_managers ON points_to_managers.point_id=points.pointID WHERE x<>0.0 AND manager_id=$this->manager_id;";
//        return "SELECT DISTINCT points.x, points.y, points.pointName, points.address from points WHERE x<>0.0";
    }
}

class GetAverageRequestCount implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT AVG(points.requests_count) AS avg_count FROM points WHERE x<>0 AND requests_count>0";
    }
}

class GetPointCoordinatesByPointId implements IEntitySelect
{
    private $pointId;

    /**
     * GetPointCoordinatesByPointId constructor.
     */
    public function __construct($pointId)
    {
        $this->pointId = $pointId;
    }


    function getSelectQuery()
    {
        return "SELECT points.x, points.y, points.pointName, points.address from points WHERE pointID=$this->pointId AND x<>0  LIMIT 1;";
    }
}

