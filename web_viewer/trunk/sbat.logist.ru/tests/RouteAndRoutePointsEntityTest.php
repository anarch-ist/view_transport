<?php

include_once __DIR__ . '/../common_files/dao/routeAndRoutePoints/RouteAndRoutePoints.php';
include_once 'AbstractEntityTestClass.php';

class RouteAndRoutePointsEntityTest extends AbstractEntityTestClass
{

    /**
     * @before
     */
    static function openMysqlConnection()
    {
        parent::openMysqlConnection();
    }

    /**
     * @after
     */
    static function closeMysqlConnection()
    {
        parent::closeMysqlConnection();
    }

    /**
     * @afterClass
     */
    static function flushDB()
    {
        parent::flushDB();
    }

    function testCreateRouteAndRoutePoints()
    {
        $this->assertNotNull(\DAO\RouteAndRoutePoints::getInstance());
    }

    /**
     * @dataProvider provideDirectionName
     * @test
     */
    function testGetAllRoutePointsDataForRouteID($routeID, $result)
    {
        $this->assertEquals($result, json_decode(json_encode(\DAO\RouteAndRoutePoints::getInstance()->getAllRoutePointsDataForRouteID($routeID))));
    }

    function provideDirectionName()
    {
        $testResult1 = json_decode(
            '{"daysOfWeek":["monday","tuesday","wednesday","thursday","friday"],
                        "firstPointArrivalTime":"18:00",
                        "routePoints":[{"routePointID":"1","sortOrder":"1","pointName":"point1","tLoading":"120"},{"routePointID":"2","sortOrder":"2","pointName":"point2","tLoading":"40"},{"routePointID":"4","sortOrder":"4","pointName":"point1","tLoading":"230"},{"routePointID":"3","sortOrder":"5","pointName":"point3","tLoading":"230"},{"routePointID":"5","sortOrder":"8","pointName":"point4","tLoading":"120"}],
                        "relationsBetweenRoutePoints":[{"relationID":"1_2","pointNameFirst":"point1","pointNameSecond":"point2","timeForDistance":"0","distance":"400","sortOrder":"1"},{"relationID":"2_4","pointNameFirst":"point2","pointNameSecond":"point1","timeForDistance":"0","distance":"400","sortOrder":"2"},{"relationID":"4_3","pointNameFirst":"point1","pointNameSecond":"point3","timeForDistance":"0","distance":null,"sortOrder":"4"},{"relationID":"3_5","pointNameFirst":"point3","pointNameSecond":"point4","timeForDistance":"0","distance":"340","sortOrder":"5"}]}'
        );
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
 