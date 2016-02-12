<?php

abstract class AbstractEntityTestClass extends PHPUnit_Framework_TestCase
{

    private static $dao;

    static function closeMysqlConnection()
    {
        echo 'closeMysqlConnection' . PHP_EOL;
        if (!(self::$dao instanceof \DAO\IDAO)) {
            self::$dao = \DAO\DAO::getInstance();
            self::$dao->closeConnection();
        } else if (self::$dao instanceof \DAO\IDAO) {
            self::$dao->rollback();
            self::$dao->closeConnection();
        }
    }

    static function openMysqlConnection()
    {
        echo 'openMysqlConnection' . PHP_EOL;
        if (!(self::$dao instanceof \DAO\IDAO)) {
            self::$dao = \DAO\DAO::getInstance();
        } else {
            self::$dao->startConnection();
        }
    }

    static function flushDB()
    {
        echo 'flushDB' . PHP_EOL;
        $connection = new mysqli('localhost', 'root', '', 'transmaster_transport_db');
        mysqli_set_charset($connection, "utf8");
        $connection->begin_transaction();
        $connection->multi_query(file_get_contents(__DIR__ . '/../common_files/dao/SQL/test_inserts.sql'));
        while ($connection->more_results()) {
            $connection->next_result();
        }
        $connection->commit();
        $connection->close();
        unset($connection);
    }

}
 