<?php
namespace DAO;
include_once 'IRouteList.php';
include_once __DIR__ . '/../DAO.php';
include_once 'Data.php';

class RouteList implements IRouteListEntity {
    /**
     * @return mixed
     */
    function getRouteLists()
    {
        // TODO: Implement getRouteLists() method.
    }

    /**
     * @param $id
     * @return mixed
     */
    function getRouteListByID($id)
    {
        // TODO: Implement getRouteListByID() method.
    }

    /**
     * @param $routeData
     * @return mixed
     */
    function addRouteList($routeData)
    {
        // TODO: Implement addRouteList() method.
    }

    /**
     * @param $id
     * @param $routeData
     * @return mixed
     */
    function updateRouteList($id, $routeData)
    {
        // TODO: Implement updateRouteList() method.
    }

    /**
     * RouteList constructor.
     */
    public function __construct()
    {
    }

}
?>