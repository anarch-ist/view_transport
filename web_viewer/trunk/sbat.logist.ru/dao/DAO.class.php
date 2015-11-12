<?php
require_once 'IDAO.php';

abstract class DAO implements IDAO
{
    const AUTO_START_TRANSACTION = true;
    private static $connection;

    protected function __construct()
    {
        if (is_null(self::$connection)) {
            self::startConnection();
        }
//            if (self::AUTO_START_TRANSACTION) {
//                self::startTransaction();
//            }
    }

    private static function query($sql)
    {
        return self::$connection->query($sql);
    }

    static function startConnection()
    {
        self::$connection = new mysqli('localhost', 'andy', 'andyandy', 'project_database');
        if (mysqli_connect_errno())
            die('Ошибка соединения: ' . mysqli_connect_error());
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
        self::commit();
        self::closeConnection();
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
        //return $this->query($newObj->getSelectQuery());
    }

    function insert($obj)
    {
        // TODO: Implement insert() method.
    }

    function delete($obj)
    {
        // TODO: Implement delete() method.
    }
}

abstract class EntityDataObject implements IEntityDataCheck
{
    function prepareSafeString($string)
    {
        return $string;
    }
}

?>