<?php

include_once __DIR__ . '/../common_files/dao/routeDao/route.php';

class RouteEntityTest extends PHPUnit_Framework_TestCase
{
    function testCreateRouteEntity()
    {
        $this->assertNotNull(\DAO\RouteEntity::getInstance());
    }

    /**
     * @dataProvider provideDirectionName
     */
    function testGetAllRoutePointsDataForRouteID($routeID, $result)
    {
        $this->assertEquals(json_decode(json_encode(\DAO\RouteEntity::getInstance()->selectRoutePointsByRouteID($routeID))), $result);
    }

    function provideDirectionName()
    {
        $testResult1 = json_decode(
            '{
                        daysOfWeek:["monday","tuesday","wednesday","thursday","friday"],
                        firstPointArrivalTime: "18:00",
                        routePoints: [
                            {routePointID: 10, sortOrder:1, pointName:"point1", tLoading: 120},
                            {routePointID: 11, sortOrder:2, pointName:"point2", tLoading: 40},
                            {routePointID: 12, sortOrder:3, pointName:"point3", tLoading: 230}
                        ],
                        relationsBetweenRoutePoints: [
                            {relationID:"10_11", pointNameFirst:"point1", pointNameSecond:"point2", distance: 400, timeForDistance: 120},
                            {relationID:"11_12", pointNameFirst:"point2", pointNameSecond:"point3", distance: 230, timeForDistance: 200}
                        ]
                    }'
        );
        $testResult2 = json_decode(
            '{
                        daysOfWeek:["monday","tuesday","wednesday","thursday","friday"],
                        firstPointArrivalTime: "14:30",
                        routePoints: [],
                        relationsBetweenRoutePoints: []
                    }'
        );
        return array(array('routeID' => '1', 'result' => $testResult1), array('routeID' => '2', 'result' => $testResult2));
    }
}
 