<?php
include_once __DIR__ . '/../common_files/dao/DAO.php';

class DAOTest extends PHPUnit_Framework_TestCase
{
    public function testCommitTransaction()
    {
        $dao = \DAO\DAO::getInstance();
        $this->assertEquals($dao::AUTO_START_TRANSACTION , $dao->commit());
        $this->assertFalse($dao->commit());
        $dao->closeConnection();
        $this->assertFalse($dao->commit());
        $dao->startConnection();
        $this->assertEquals($dao::AUTO_START_TRANSACTION , $dao->commit());
        unset($dao);
    }

    /**
     * @depends testCommitTransaction
     */
    public function testRollbackTransaction()
    {
        $dao = \DAO\DAO::getInstance();
        $dao->closeConnection();
        $dao->startConnection();
        $this->assertEquals($dao::AUTO_START_TRANSACTION , $dao->rollback());
        $this->assertFalse($dao->rollback());
        $dao->closeConnection();
        $this->assertFalse($dao->rollback());
        $dao->startConnection();
        $this->assertEquals($dao::AUTO_START_TRANSACTION , $dao->rollback());
        unset($dao);
    }

    /**
     * @depends testRollbackTransaction
     */
    public function testConnection()
    {
        $dao = \DAO\DAO::getInstance();
        $this->assertNotNull($dao);
        return $dao;
    }

    /**
     * @depends testConnection
     * @expectedException MysqlException
     */
    public function testQuery(\DAO\DAO $dao)
    {
        $this->assertNotEmpty($dao->query('SELECT * FROM `users` LIMIT 0, 20;'));
        $dao->query($this->flushDataBase());
        unset($dao);
    }

    private function flushDataBase()
    {
        return file_get_contents(__DIR__ . '/../common_files/dao/SQL/test_inserts.sql');
    }

    /**
     * @depends testConnection
     */
    public function testCloseConnection(\DAO\DAO $dao)
    {
        $this->assertTrue($dao->closeConnection());
        $this->assertFalse($dao->closeConnection());
        return $dao;
    }

    /**
     * @depends testCloseConnection
     */
    public function testStartConnection(\DAO\DAO $dao)
    {
        $this->assertTrue($dao->startConnection());
        $this->assertFalse($dao->startConnection());
        unset($dao);
    }

    /**
     * @depends testStartConnection
     */
    public function testStartTransaction()
    {
        $dao = \DAO\DAO::getInstance();
        $this->assertTrue($dao->startTransaction() xor $dao::AUTO_START_TRANSACTION);
        if (!$dao::AUTO_START_TRANSACTION) {
            $this->assertTrue(!$dao->startTransaction());
        }
        unset($dao);
    }
}