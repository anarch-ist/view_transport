<?php

include_once __DIR__ . '/../common_files/dao/routeAndRoutePoints/RouteAndRoutePoints.php';

class RouteAndRoutePointsEntityTest extends PHPUnit_Framework_TestCase
{
    function testCreateRouteAndRoutePoints()
    {
        $this->assertNotNull(\DAO\RouteAndRoutePoints::getInstance());
    }

    /**
     * @dataProvider provideDirectionName
     */
    function testGetAllRoutePointsDataForRouteID($routeID, $result)
    {
        $this->assertEquals($result, json_decode(json_encode(\DAO\RouteAndRoutePoints::getInstance()->getAllRoutePointsDataForRouteID($routeID))));
    }

    function provideDirectionName()
    {
        $testResult1 = json_decode('{"daysOfWeek":["monday","tuesday","wednesday","thursday","friday"],
                        "firstPointArrivalTime": "18:00",
                        "routePoints": [
                            {"routePointID": "1", "sortOrder":"1", "pointName":"point1", "tLoading": "120"},
                            {"routePointID": "2", "sortOrder":"2", "pointName":"point2", "tLoading": "40"},
                            {"routePointID": "3", "sortOrder":"3", "pointName":"point3", "tLoading": "230"}
                        ],
                        "relationsBetweenRoutePoints": [
                            {"relationID":"1_2", "pointNameFirst":"point1", "pointNameSecond":"point2", "distance": "400", "timeForDistance": "120"},
                            {"relationID":"2_3", "pointNameFirst":"point2", "pointNameSecond":"point3", "distance": "230", "timeForDistance": "200"}
                        ]}');
        $testResult2 = json_decode(
            '{"daysOfWeek":["monday","tuesday","wednesday","thursday","friday"],
                        "firstPointArrivalTime": "14:30",
                        "routePoints": [],
                        "relationsBetweenRoutePoints": []
                    }'
        );
        return array(array('routeID' => '1', 'result' => $testResult1), array('routeID' => '2', 'result' => $testResult2));
    }
}
 