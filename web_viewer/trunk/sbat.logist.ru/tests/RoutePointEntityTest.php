<?php

include_once __DIR__ . '/../common_files/dao/routePointDao/RoutePoint.php';
include_once __DIR__ . '/../common_files/dao/routeAndRoutePoints/RouteAndRoutePoints.php';
include_once 'AbstractEntityTestClass.php';

class RoutePointEntityTest extends AbstractEntityTestClass
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

    /**
     * @test
     */
    function testCreateRoutePointEntity()
    {
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertNotNull($routePointEntity);
    }

    /**
     * @covers       RoutePointEntity::deleteRoutePoint
     * @test
     */
    function testRemoveRoutePoint()
    {
        $expectResult = '{"daysOfWeek":["monday","tuesday","wednesday","thursday","friday"],"firstPointArrivalTime":"18:00","routePoints":[{"routePointID":"1","sortOrder":"1","pointName":"point1","tLoading":"120"},{"routePointID":"2","sortOrder":"2","pointName":"point2","tLoading":"40"},{"routePointID":"4","sortOrder":"4","pointName":"point1","tLoading":"230"},{"routePointID":"5","sortOrder":"8","pointName":"point4","tLoading":"120"}],"relationsBetweenRoutePoints":[{"relationID":"1_2","pointNameFirst":"point1","pointNameSecond":"point2","timeForDistance":"0","distance":"400","sortOrder":"1"},{"relationID":"2_4","pointNameFirst":"point2","pointNameSecond":"point1","timeForDistance":"0","distance":"400","sortOrder":"2"},{"relationID":"1_4","pointNameFirst":"point1","pointNameSecond":"point4","timeForDistance":"0","distance":null,"sortOrder":"4"}]}';
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertTrue($routePointEntity->deleteRoutePoint(3));
        $this->assertEquals(json_decode($expectResult),json_decode(json_encode(\DAO\RouteAndRoutePoints::getInstance()->getAllRoutePointsDataForRouteID(1))));
    }

    /**
     * @dataProvider routePointsForUpdatingProvider
     * @test
     */
    function testUpdateRoutePoint($routePointID, $sortOrder, $pointName, $tLoading, $result)
    {
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertTrue($routePointEntity->updateRoutePoint($routePointID, $sortOrder, $tLoading, $pointName));
        $resultRoutePoint = $routePointEntity->selectRoutePointByID($routePointID);
        if ($result) {
            $this->assertEquals($result, json_decode(json_encode($resultRoutePoint->toArray())));
        } else {
            $this->assertNull($resultRoutePoint);
        }

    }

    function routePointsForUpdatingProvider()
    {
        $dataTest0 = array('routePointID' => 30, 'sortOrder' => 3, 'pointName' => 3, 'tLoading' => 60, 'result' => false);
        $dataTest1 = array('routePointID' => 3, 'sortOrder' => 3, 'pointName' => 3, 'tLoading' => 60, 'result' => json_decode('{"routePointID": "3", "sortOrder": "3", "timeForLoadingOperations": "60", "pointID": "3", "routeID": "1"}'));
        $dataTest2 = array('routePointID' => 2, 'sortOrder' => 3, 'pointName' => 3, 'tLoading' => 60, 'result' => json_decode('{"routePointID": "2", "sortOrder": "3", "timeForLoadingOperations": "60", "pointID": "3", "routeID": "1"}'));
        return array($dataTest0, $dataTest1, $dataTest2);
    }

    /**
     * @dataProvider routePointsForAdditingProvider
     * @covers       RoutePointEntity::addRoutePoint
     * @test
     */
    function testAddRoutePoint($sortOrder, $pointName, $tLoading, $routeID, $result)
    {
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertEquals($result,$routePointEntity->addRoutePoint($sortOrder, $tLoading, $pointName, $routeID));
    }

    function routePointsForAdditingProvider()
    {
        $dataTest0 = array('sortOrder' => 6, 'pointName' => 2, 'tLoading' => 60, 'routeID' => 1, 'result' => true);
        $dataTest1 = array('sortOrder' => 6, 'pointName' => 3, 'tLoading' => 60, 'routeID' => 1, 'result' => false);
//        $dataTest2 = array('sortOrder' => 3, 'pointName' => 3, 'tLoading' => 60, 'routeID' => 1, 'result' => false);
        return array($dataTest0, $dataTest1);//, $dataTest2);
    }

    /**
     * @covers       RoutePointEntity::selectRoutePointsByRouteID
     * @test
     */
    function testSelectRoutePointsByRouteID()
    {
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertEquals(5,count($routePointEntity->selectRoutePointsByRouteID(1)->getData('routePoints')));
    }

    /**
     * @covers       RoutePointEntity::updateRelationBetweenRoutePoints
     * @test
     */
    function testUpdateRelationBetweenRoutePoints()
    {
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertTrue($routePointEntity->updateRelationBetweenRoutePoints(1,2,10));
    }
}
 