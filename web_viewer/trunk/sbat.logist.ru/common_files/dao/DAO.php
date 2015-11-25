<?php
namespace DAO;

define('PHP_NEWLINE','<br>'.PHP_EOL);

use mysqli;
use Exception;

include_once 'IDAO.php';
include_once 'Exceptions.php';

class DAO implements IDAO
{
    const AUTO_START_TRANSACTION = true;
    private static $_instance;
    private $_connection;

    public static function getInstance()
    {
        if (is_null(self::$_instance)) {
            self::$_instance = new DAO();
        }
        return self::$_instance;
    }

    private function __construct()
    {
        $this->_connection = @new mysqli('localhost', 'andy', 'andyandy', 'project_database');
        if ($this->_connection->connect_errno) {
            throw new \MysqlException('Connection error - ' . $this->_connection->connect_error);
        }
        mysqli_set_charset($this->_connection, "utf8"); // fixed encoding error
        if ($this::AUTO_START_TRANSACTION) {
            $this->startTransaction();
        }
    }

    public function checkString($str)
    {
        return $this->_connection->real_escape_string($str);
    }


    function query($sql)
    {
        return $this->_connection->query($sql);
    }

    function startConnection()
    {
        $this->_connection = @new mysqli('localhost', 'andy', 'andyandy', 'project_database');
        if ($this->_connection->connect_errno) {
            throw new \MysqlException('Connection error - ' . $this->_connection->connect_error);
        }

    }

    function closeConnection()
    {
        return $this->_connection->close();
    }

    public function startTransaction()
    {
        return $this->_connection->begin_transaction();
    }

    public function commit()
    {
        return $this->_connection->commit();
    }

    public function rollback()
    {
        return $this->_connection->rollback();
    }

    function __destruct()
    {
        $this->commit();
        $this->closeConnection();
    }

    function select(IEntitySelect $selectObj)
    {
        $result = self::query($selectObj->getSelectQuery());
        if (!$result) {
            throw new Exception('ошибка в выборочном запросе - ' . $this->_connection->error);
        }
        $array = array();
        $count = 0;
        while ($row = $result->fetch_assoc()) {
            $array[$count] = $row;
            $count++;
        }
        return $array;
    }

    function update(IEntityUpdate $newObj)
    {
        // TODO: Check update() method.
        return $this->query($newObj->getUpdateQuery());
    }

    function insert(IEntityInsert $obj)
    {
        // TODO: Check insert() method.
        return $this->query($obj->getInsertQuery());
    }

    function delete(IEntityDelete $obj)
    {
        // TODO: Check delete() method.
        return $this->query($obj->getDeleteQuery());
    }
}

?>