<?php
namespace DAO;

interface IRouteListEntity
{
    function getRouteLists();

    function getRouteListByID($id);

    function addRouteList($routeData);

    function updateRouteList($id, $routeData);
}