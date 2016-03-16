<?php

include_once __DIR__ . '/../common_files/dao/requestDao/Request.php';
include_once __DIR__ . '/../common_files/dao/requestsForUser/RequestsForUser.php';
include_once 'AbstractEntityTestClass.php';

use DAO\RequestEntity as InvoiceEntity;

class InvoiceEntityTest extends AbstractEntityTestClass
{

    /**
     * @before
     */
    static function openMysqlConnection() {
        parent::openMysqlConnection();
    }

    /**
     * @after
     */
    static function closeMysqlConnection() {
        parent::closeMysqlConnection();
    }

    /**
     * @afterClass
     */
    static function flushDBAfterClass() {
        parent::flushDB();
    }

    /**
     * @beforeClass
     */
    static function flushDBBeforeClass() {
        parent::flushDB();
    }
    /**
     * @test
     * @covers InvoiceEntity::selectInvoices
     */
    public function testSelects()
    {
        $totalCount = 3;
        $invoices = RequestEntity::getInstance()->selectInvoices();
        $this->assertEquals($totalCount, count($invoices));
    }

    /**
     * @test
     * @depends      testSelects
     * @dataProvider updateInvoiceStatusDataProvider
     * @covers InvoiceEntity::updateInvoiceStatus()
     * @covers InvoiceEntity::getInvoiceHistoryByInvoiceNumber
     */
    public function testUpdateInvoiceStatus($userID, $invoiceNumber, $newStatus, $historyExpected, $datetime, $comment)
    {
        $invoice = RequestEntity::getInstance();
        $this->assertNotFalse($invoice->updateInvoiceStatus($userID, $invoiceNumber, $newStatus, $datetime, $comment));
        $history = $invoice->getInvoiceHistoryByInvoiceNumber($invoiceNumber);
        $this->assertNotFalse($history);
        $this->assertEquals($historyExpected, json_decode(json_encode($history)));
    }

    /**
     * @test
     * @covers InvoiceEntity::getInvoicesForRouteList
     */
    public function testGetinvoicesForRouteList()
    {
        $invoice = RequestEntity::getInstance();
        $this->assertEquals('[{"invoiceID":"1","invoiceIDExternal":"invIdExt1"},{"invoiceID":"2","invoiceIDExternal":"invIdExt2"}]',json_encode($invoice->getInvoicesForRouteList('1')));
    }

    function updateInvoiceStatusDataProvider()
    {
        $historyExpected = array();
        $historyExpected[0] = json_decode('{
  "pointName": "point1",
  "firstName": "ivan",
  "lastName": "ivanov",
  "patronymic": "ivanovich",
  "invoiceStatusRusName": "Внутренняя заявка добавлена в БД",
  "lastStatusUpdated": null,
  "routeListNumber": "1455668",
  "palletsQty": 3,
  "boxQty": 4
 }');
        $date = date('d.m.Y H:i:s');
        $historyExpected[1] = json_decode(' {
  "pointName": "point1",
  "firstName": "ivan",
  "lastName": "ivanov",
  "patronymic": "ivanovich",
  "invoiceStatusRusName": "Накладная прибыла в пункт",
  "lastStatusUpdated": "' . date('Y-m-d H:i:s', strtotime($date)) . '",
  "routeListNumber": "1455668",
  "palletsQty": 3,
  "boxQty": 4
 }');
        $test1 = array
        (
            'userID' => '2',
            'invoiceNumber' => 'invIdExt1',
            'newStatus' => 'ARRIVED',
            'historyExpected' => $historyExpected,
            'datetime' => $date,
            'comment' => md5(rand(1, 100))
        );
//        $date = date('d.m.Y H:i:s', strtotime($date) + 1);
//        $historyExpected[1] = json_decode('{
//  "pointName": "point1",
//  "firstName": "ivan",
//  "lastName": "ivanov",
//  "patronymic": "ivanovich",
//  "invoiceStatusRusName": "На контроле",
//  "lastStatusUpdated": "' . date('Y-m-d H:i:s', strtotime($date)) . '",
//  "routeListNumber": "1455668",
//  "palletsQty": 3,
//  "boxQty": 4
// }');
//        $test2 = array
//        (
//            'userID' => '1',
//            'invoiceNumber' => 'invIdExt1',
//            'newStatus' => 'CHECK',
//            'historyExpected' => $historyExpected,
//            'datetime' => $date,
//            'comment' => md5(rand(1, 100))
//        );
//        $date = date('d.m.Y H:i:s', strtotime($date) + 1);
//        $historyExpected[1] = json_decode('{
//  "pointName": "point1",
//  "firstName": "ivan",
//  "lastName": "ivanov",
//  "patronymic": "ivanovich",
//  "invoiceStatusRusName": "Внутренняя заявка удалена из БД",
//  "lastStatusUpdated": "' . date('Y-m-d H:i:s', strtotime($date)) . '",
//  "routeListNumber": "1455668",
//  "palletsQty": 3,
//  "boxQty": 4
// }');
//        $test3 = array
//        (
//            'userID' => '1',
//            'invoiceNumber' => 'invIdExt1',
//            'newStatus' => 'DELETED',
//            'historyExpected' => $historyExpected,
//            'datetime' => $date,
//            'comment' => md5(rand(1, 100))
//        );

        return array($test1);//, $test2, $test3);
    }
    
    function testGetAllData() {
        $columns = array();
        $columns['0']['data']= 'requestNumber';
        $columns['0']['name']= 'requestNumber';
        $columns['0']['searchable']= 'true';
        $columns['0']['orderable']= 'true';
        $columns['0']['search']['value']= '';
        $columns['0']['search']['regex']= 'false';
        $columns['1']['data']= 'insiderRequestNumber';
        $columns['1']['name']= 'insiderRequestNumber';
        $columns['1']['searchable']= 'true';
        $columns['1']['orderable']= 'true';
        $columns['1']['search']['value']= '';
        $columns['1']['search']['regex']= 'false';
        $columns['2']['data']= 'invoiceNumber';
        $columns['2']['name']= 'invoiceNumber';
        $columns['2']['searchable']= 'true';
        $columns['2']['orderable']= 'true';
        $columns['2']['search']['value']= '';
        $columns['2']['search']['regex']= 'false';
        $columns['3']['data']= 'INN';
        $columns['3']['name']= 'INN';
        $columns['3']['searchable']= 'true';
        $columns['3']['orderable']= 'true';
        $columns['3']['search']['value']= '';
        $columns['3']['search']['regex']= 'false';
        $columns['4']['data']= 'deliveryPoint';
        $columns['4']['name']= 'deliveryPoint';
        $columns['4']['searchable']= 'true';
        $columns['4']['orderable']= 'true';
        $columns['4']['search']['value']= '';
        $columns['4']['search']['regex']= 'false';
        $columns['5']['data']= 'warehousePoint';
        $columns['5']['name']= 'warehousePoint';
        $columns['5']['searchable']= 'true';
        $columns['5']['orderable']= 'true';
        $columns['5']['search']['value']= '';
        $columns['5']['search']['regex']= 'false';
        $columns['6']['data']= 'lastName';
        $columns['6']['name']= 'lastName';
        $columns['6']['searchable']= 'true';
        $columns['6']['orderable']= 'true';
        $columns['6']['search']['value']= '';
        $columns['6']['search']['regex']= 'false';
        $columns['7']['data']= 'invoiceStatusRusName';
        $columns['7']['name']= 'invoiceStatusRusName';
        $columns['7']['searchable']= 'true';
        $columns['7']['orderable']= 'true';
        $columns['7']['search']['value']= '';
        $columns['7']['search']['regex']= 'false';
        $columns['8']['data']= 'boxQty';
        $columns['8']['name']= 'boxQty';
        $columns['8']['searchable']= 'true';
        $columns['8']['orderable']= 'true';
        $columns['8']['search']['value']= '';
        $columns['8']['search']['regex']= 'false';
        $columns['9']['data']= 'driver';
        $columns['9']['name']= 'driver';
        $columns['9']['searchable']= 'true';
        $columns['9']['orderable']= 'true';
        $columns['9']['search']['value']= '';
        $columns['9']['search']['regex']= 'false';
        $columns['10']['data']= 'licensePlate';
        $columns['10']['name']= 'licensePlate';
        $columns['10']['searchable']= 'true';
        $columns['10']['orderable']= 'true';
        $columns['10']['search']['value']= '';
        $columns['10']['search']['regex']= 'false';
        $columns['11']['data']= 'palletsQty';
        $columns['11']['name']= 'palletsQty';
        $columns['11']['searchable']= 'true';
        $columns['11']['orderable']= 'true';
        $columns['11']['search']['value']= '';
        $columns['11']['search']['regex']= 'false';
        $columns['12']['data']= 'routeListNumber';
        $columns['12']['name']= 'routeListNumber';
        $columns['12']['searchable']= 'true';
        $columns['12']['orderable']= 'true';
        $columns['12']['search']['value']= '';
        $columns['12']['search']['regex']= 'false';
        $columns['13']['data']= 'directionName';
        $columns['13']['name']= 'directionName';
        $columns['13']['searchable']= 'true';
        $columns['13']['orderable']= 'true';
        $columns['13']['search']['value']= '';
        $columns['13']['search']['regex']= 'false';
        $columns['14']['data']= 'currentPoint';
        $columns['14']['name']= 'currentPoint';
        $columns['14']['searchable']= 'true';
        $columns['14']['orderable']= 'true';
        $columns['14']['search']['value']= '';
        $columns['14']['search']['regex']= 'false';
        $columns['15']['data']= 'nextPoint';
        $columns['15']['name']= 'nextPoint';
        $columns['15']['searchable']= 'true';
        $columns['15']['orderable']= 'true';
        $columns['15']['search']['value']= '';
        $columns['15']['search']['regex']= 'false';
        $columns['16']['data']= 'arrivalTime';
        $columns['16']['name']= 'arrivalTime';
        $columns['16']['searchable']= 'true';
        $columns['16']['orderable']= 'true';
        $columns['16']['search']['value']= '';
        $columns['16']['search']['regex']= 'false';
        $columns['17']['data']= 'invoiceStatusID';
        $columns['17']['name']= 'invoiceStatusID';
        $columns['17']['searchable']= 'false';
        $columns['17']['orderable']= 'true';
        $columns['17']['search']['value']= '';
        $columns['17']['search']['regex']= 'false';
        $columns['18']['data']= 'routeListID';
        $columns['18']['name']= 'routeListID';
        $columns['18']['searchable']= 'false';
        $columns['18']['orderable']= 'true';
        $columns['18']['search']['value']= '';
        $columns['18']['search']['regex']= 'false';
        $_POST['columns'] = $columns;
        $order = array();
        $order['0']['column']= '0';
        $order['0']['dir']= 'asc';
        $_POST['order'] = $order;
        $start= '0';
        $length= '10';
        $search = array();
        $search['value']= '';
        $search['regex']= 'false';
        $_POST['search'] = $search;
        $userID = '2';
        $expectedResult = json_decode('
[
{"requestNumber":"123356","insiderRequestNumber":"no data","invoiceNumber":"invIdExt1","INN":"1234567890","deliveryPoint":"point2","warehousePoint":"point1","lastName":"rtgrgg","invoiceStatusID":"CREATED","invoiceStatusRusName":"Внутренняя заявка добавлена в БД","boxQty":"4","driver":"водила1","licensePlate":"екх123","palletsQty":"3","routeListNumber":"1455668","routeListID":"1","directionName":"route1","currentPoint":"point1","nextPoint":"point2","arrivalTime":null},
{"requestNumber":"123356","insiderRequestNumber":"no data","invoiceNumber":"invIdExt2","INN":"1234567890","deliveryPoint":"point2","warehousePoint":"point1","lastName":"rtgrgg","invoiceStatusID":"CREATED","invoiceStatusRusName":"Внутренняя заявка добавлена в БД","boxQty":"2","driver":"водила1","licensePlate":"екх123","palletsQty":"3","routeListNumber":"1455668","routeListID":"1","directionName":"route1","currentPoint":"point2","nextPoint":"point1","arrivalTime":null},
{"requestNumber":"123356","insiderRequestNumber":"no data","invoiceNumber":"invIdExt3","INN":"1234567890","deliveryPoint":"point2","warehousePoint":"point1","lastName":"rtgrgg","invoiceStatusID":"CREATED","invoiceStatusRusName":"Внутренняя заявка добавлена в БД","boxQty":"10","driver":null,"licensePlate":null,"palletsQty":null,"routeListNumber":null,"routeListID":null,"directionName":null,"currentPoint":null,"nextPoint":null,"arrivalTime":null},
{"requestNumber":"859458","insiderRequestNumber":"no data","invoiceNumber":null,"INN":null,"deliveryPoint":null,"warehousePoint":null,"lastName":null,"invoiceStatusID":null,"invoiceStatusRusName":null,"boxQty":null,"driver":null,"licensePlate":null,"palletsQty":null,"routeListNumber":null,"routeListID":null,"directionName":null,"currentPoint":null,"nextPoint":null,"arrivalTime":null},
{"requestNumber":"er9458","insiderRequestNumber":"no data","invoiceNumber":null,"INN":null,"deliveryPoint":null,"warehousePoint":null,"lastName":null,"invoiceStatusID":null,"invoiceStatusRusName":null,"boxQty":null,"driver":null,"licensePlate":null,"palletsQty":null,"routeListNumber":null,"routeListID":null,"directionName":null,"currentPoint":null,"nextPoint":null,"arrivalTime":null}
]
        ');
        $this->assertEquals($expectedResult,json_decode(json_encode(\DAO\RequestsForUserEntity::getInstance()->selectAllData($start,$length,$userID))));
    }
}
