<?php

include_once __DIR__ . '/../common_files/dao/invoiceDao/Invoice.php';

use DAO\InvoiceEntity as InvoiceEntity;

/**
 * @group DB_flush_data
 * @covers \DAO\InvoiceEntity
 */
class InvoiceEntityTest extends PHPUnit_Framework_TestCase
{
    /**
     * @beforeClass
     */
    static function initializeDB()
    {
        $connection = new mysqli('localhost', 'root', '', 'transmaster_transport_db');
        $connection->multi_query(file_get_contents(__DIR__.'/insertsForInvoiceEntityTest.sql'));
        while($connection->more_results()) $connection->next_result();
        $connection->close();
        unset($connection);
        \DAO\DAO::getInstance()->commit();
    }

    /**
     * @afterClass
     */
    static function restoreDB()
    {
        $connection = new mysqli('localhost', 'root', '', 'transmaster_transport_db');
        $connection->multi_query(file_get_contents(__DIR__.'/../common_files/dao/SQL/test_inserts.sql'));
        while($connection->more_results()) $connection->next_result();
        $connection->close();
    }

    public function testSelects()
    {
        $totalCount = 3;
        $invoices = InvoiceEntity::getInstance()->selectInvoices();
        $this->assertEquals($totalCount, count($invoices));
    }

    /**
     * @test
     */
    function testCreateInvoiceEntity()
    {
        $invoice = InvoiceEntity::getInstance();
        $this->assertNotNull($invoice);
        $history = $invoice->getInvoiceHistoryByInvoiceNumber('qwd22345');
        $this->assertNotFalse($history);
        $this->assertEquals(1, count($history));
        $this->assertEquals('Внутренняя заявка добавлена в БД', $history[0]['invoiceStatusRusName']);

    }

    /**
     * @depends      testSelects
     * @dataProvider updateInvoiceStatusDataProvider
     */
    public function testUpdateInvoiceStatus($userID, $invoiceNumber, $newStatus, $historyExpected, $datetime, $count)
    {
        $invoice = InvoiceEntity::getInstance();
        $this->assertNotFalse($invoice->updateInvoiceStatus($userID, $invoiceNumber, $newStatus, $datetime));
        $history = $invoice->getInvoiceHistoryByInvoiceNumber($invoiceNumber);
        $this->assertNotFalse($history);
        $this->assertEquals($count, count($history));
        $this->assertEquals($historyExpected, json_decode(json_encode($history)));
    }

    function updateInvoiceStatusDataProvider()
    {
        $historyExpected = array();
        $historyExpected[] = json_decode('{
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
        $historyExpected[] = json_decode(' {
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
            'userID' => '1',
            'invoiceNumber' => 'qwd22345',
            'newStatus' => 'ARRIVED',
            'historyExpected' => $historyExpected,
            'datetime' => $date,
            'count' => 2
        );
        $date = date('d.m.Y H:i:s', strtotime($date) + 1);
        $historyExpected[] = json_decode('{
  "pointName": "point1",
  "firstName": "ivan",
  "lastName": "ivanov",
  "patronymic": "ivanovich",
  "invoiceStatusRusName": "На контроле",
  "lastStatusUpdated": "' . date('Y-m-d H:i:s', strtotime($date)) . '",
  "routeListNumber": "1455668",
  "palletsQty": 3,
  "boxQty": 4
 }');
        $test2 = array
        (
            'userID' => '1',
            'invoiceNumber' => 'qwd22345',
            'newStatus' => 'CHECK',
            'historyExpected' => $historyExpected,
            'datetime' => $date,
            'count' => 3
        );
        $date = date('d.m.Y H:i:s', strtotime($date) + 1);
        $historyExpected[] = json_decode('{
  "pointName": "point1",
  "firstName": "ivan",
  "lastName": "ivanov",
  "patronymic": "ivanovich",
  "invoiceStatusRusName": "Внутренняя заявка удалена из БД",
  "lastStatusUpdated": "' . date('Y-m-d H:i:s', strtotime($date)) . '",
  "routeListNumber": "1455668",
  "palletsQty": 3,
  "boxQty": 4
 }');
        $test3 = array
        (
            'userID' => '1',
            'invoiceNumber' => 'qwd22345',
            'newStatus' => 'DELETED',
            'historyExpected' => $historyExpected,
            'datetime' => $date,
            'count' => 4
        );

        return array($test1, $test2, $test3);
    }
}
