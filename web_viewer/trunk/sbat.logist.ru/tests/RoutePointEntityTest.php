<?php

include_once __DIR__.'/../common_files/dao/routePointDao/RoutePoint.php';

class RoutePointEntityTest extends PHPUnit_Framework_TestCase
{
    function testCreateRoutePointEntity()
    {
        $routePointEntity = \DAO\RoutePointEntity::getInstance();
        $this->assertNotNull($routePointEntity);
        return $routePointEntity;
    }

//    /**
//     * @depends      testCreateRoutePointEntity
//     * @dataProvider routePointsForRemovingProvider
//     */
//    function testRemoveRoutePoint(\DAO\RoutePointEntity $routePointEntity)
//    {
//        $routePointEntity->deleteRoutePoint()
//    }
//
//    function routePointsForRemovingProvider()
//    {
//        return array();
//    }

    /**
     * @depends      testCreateRoutePointEntity
     * @dataProvider routePointsForUpdatingProvider
     */
    function testUpdateRoutePoint($routePointID, $sortOrder, $pointName, $tLoading, $result, \DAO\RoutePointEntity $routePointEntity)
    {
        $this->assertTrue($routePointEntity->updateRoutePoint($routePointID, $sortOrder, $tLoading, $pointName));
        $resultRoutePoint = $routePointEntity->selectRoutePointByID($routePointID);
        if ($result) {
            $this->assertEquals($result, json_decode(json_encode($resultRoutePoint->toArray())));
        }
        else {
            $this->assertNull($resultRoutePoint);
        }

    }

    function routePointsForUpdatingProvider()
    {
        //$dataTest0 = array('routePointID' => 3, 'sortOrder' => 3,'pointName' => 3, 'tLoading' => 60, 'result' => json_decode('{"routePointID": "3", "sortOrder": "3", "timeForLoadingOperations": "60", "pointID": "3", "routeID": "1"}'));
        //$dataTest2 = array('routePointID' => 2, 'sortOrder' => 3,'pointName' => 3, 'tLoading' => 60, 'result' => json_decode('{"routePointID": "2", "sortOrder": "3", "timeForLoadingOperations": "60", "pointID": "3", "routeID": "1"}'));
        $dataTest1 = array('routePointID' => 30, 'sortOrder' => 3,'pointName' => 3, 'tLoading' => 60, 'result' => false);
        return array($dataTest1);//, $dataTest0, $dataTest2);
    }
}
 