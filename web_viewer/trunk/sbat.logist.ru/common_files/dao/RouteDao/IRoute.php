<?php
namespace DAO;

interface IRouteEntity
{
    function selectRoutes();

    function selectRouteByID($id);

    function selectRouteByDirectionName($directionName);

    function selectRoutePointsByDirectionName($directionName);

    function updateRoute($newRoute);

    function deleteRoute($Route);

    function addRoute($Route);
}