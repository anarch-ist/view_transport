<?php

namespace DAO;


interface IRoutePointEntity
{
    function selectRoutePointByID($id);

    function selectRoutePointsByRouteID($routeID);

    function updateRelationBetweenRoutePoints($firstRoutePoint, $secondRoutePoint, $newDistance, $newTimeToDestination);

    function updateRoutePoint($routePointID, $sortOrder, $tLoading, $pointName);

    function deleteRoutePoint($routePointID);

    function addRoutePoint($sortOrder, $tLoading, $pointName, $routeID);
}