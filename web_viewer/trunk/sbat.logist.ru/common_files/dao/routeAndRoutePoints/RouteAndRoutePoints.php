<?php

namespace DAO;
include_once __DIR__ . '/../routeDao/Route.php';
include_once __DIR__ . '/../routePointDao/RoutePoint.php';

class RouteAndRoutePoints
{
    private static $instance = null;

    protected function __construct()
    {
    }

    static function getInstance()
    {
        if (is_null(self::$instance)) {
            self::$instance = new RouteAndRoutePoints();
        }
        return self::$instance;
    }

    function getAllRoutePointsDataForRouteID($routeID)
    {
        $routeInfo = array();
        $routeInfoData = RouteEntity::getInstance()->selectRouteByID($routeID);
        $routeInfo['daysOfWeek'] = explode(',', $routeInfoData->getData('daysOfWeek'));
        $routeInfo['firstPointArrivalTime'] = substr($routeInfoData->getData('firstPointArrivalTime'), 0, 5);

        $routePointEntity = RoutePointEntity::getInstance()->selectRoutePointsByRouteID($routeID);
        $routeInfo['routePoints'] = $routePointEntity->getData('routePoints');
        $routeInfo['relationsBetweenRoutePoints'] = $routePointEntity->getData('relationsBetweenRoutePoints');
        return $routeInfo;

//        $routeInfo0 = $this->_DAO->select(new SelectRouteByID($routeID))[0];
//
//        $routeInfo = array();
//        $routeInfo['daysOfWeek'] = explode(',', $routeInfo0['daysOfWeek']);
//        $routeInfo['firstPointArrivalTime'] = substr($routeInfo0['firstPointArrivalTime'], 0, 5);
//
//        $routeInfo['routePoints'] = $this->_DAO->select(new SelectRoutePointsByRouteID($routeID));
//        $routeInfo['relationsBetweenRoutePoints'] = $this->_DAO->select(new SelectRelationsBetweenRoutePoints($routeID));
//
//        return $routeInfo;

//        throw new \MysqlException('запрос не написан для routeAndRoutePoints::getAllRoutePointsDataForRouteID');
    }
} 