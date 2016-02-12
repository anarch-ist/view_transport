<?php

namespace DAO;


interface IRoutePointEntity
{
    function selectRoutePointByID($id);
    //unnecessary

    function selectRoutePointsByRouteID($routeID);
    //covered

    function updateRelationBetweenRoutePoints($firstRoutePoint, $secondRoutePoint, $newTimeToDestination);
    //covered

    function updateRoutePoint($routePointID, $sortOrder, $tLoading, $pointName);
    //covered

    function deleteRoutePoint($routePointID); //covered

    function addRoutePoint($sortOrder, $tLoading, $pointName, $routeID);
    //covered
}