<?php

class RoutePointEntityTest extends PHPUnit_Framework_TestCase
{
    function testCreateRoutePointEntity()
    {
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertNotNull($routePointEntity);
        return $routePointEntity;
    }

    /**
     * @depends      testCreateRoutePointEntity
     * @dataProvider getChangesForRoutePointProvider
     */
    function testRemoveRoutePoint(\DAO\RoutePointEntity $routePointEntity)
    {
//        $routePointEntity->deleteRoutePoint()
    }

    function routePointsForRemovingProvider()
    {
        return array();
    }
}
 