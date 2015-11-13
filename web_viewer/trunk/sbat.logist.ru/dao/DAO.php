<?php
require_once 'IDAO.php';

abstract class DAO implements IDAO
{
    const AUTO_START_TRANSACTION = true;
    private static $connection;
    private static $countConnectings = 0;

    protected function __construct()
    {
        if (is_null(self::$connection)) {
            self::startConnection();
        }
        if (!self::$countConnectings && self::AUTO_START_TRANSACTION) {
            self::startTransaction();
        }
        self::$countConnectings++;
    }

    static function getConnection()
    {
        return self::$connection;
    }

    private static function query($sql)
    {
        return self::$connection->query($sql);
    }

    static function startConnection()
    {
        self::$connection = new mysqli('localhost', 'andy', 'andyandy', 'project_database');
        if (mysqli_connect_errno())
            throw new Exception('Ошибка соединения: ' . mysqli_connect_error());
    }

    static function closeConnection()
    {
        return self::$connection->close();
    }

    private static function startTransaction()
    {
        return self::$connection->begin_transaction();
    }

    private static function commit()
    {
        return self::$connection->commit();
    }

    private static function rollback()
    {
        return self::$connection->rollback();
    }

    function __destruct()
    {
        self::$countConnectings--;
        if (!self::$countConnectings) {
            self::commit();
            self::closeConnection();
        }
    }

    function select($selectObj)
    {
        $result = self::query($selectObj->getSelectQuery());
        $array = array();
        $count = 0;
        while ($row = $result->fetch_assoc()) {
            $array[$count] = $row;
            $count++;
        }
        return $array;
    }

    function update($newObj)
    {
        // TODO: Check update() method.
        return $this->query($newObj->getUpdateQuery());
    }

    function insert($obj)
    {
        // TODO: Check insert() method.
        return $this->query($obj->getInsertQuery());
    }

    function delete($obj)
    {
        // TODO: Check delete() method.
        return $this->query($obj->getDeleteQuery());
    }
}

abstract class EntityDataObject implements IEntityDataCheck
{
    function prepareSafeString($string)
    {
        return DAO::getConnection()->real_escape_string($string);
    }
}

?>