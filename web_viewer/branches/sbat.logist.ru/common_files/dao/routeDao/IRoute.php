<?php
namespace DAO;

interface IRouteEntity
{
    function selectRoutes();

    function selectRouteByID($id);

    function selectRouteByDirectionName($directionName);

    function updateStartRouteTime($routeID, $newTime);

    function updateRouteDaysOfWeek($routeID, $days);

    function deleteRoute($Route);

    function addRoute($Route);
}