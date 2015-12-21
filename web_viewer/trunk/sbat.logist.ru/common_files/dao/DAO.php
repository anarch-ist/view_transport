<?php
namespace DAO;

define('PHP_NEWLINE', '<br>' . PHP_EOL);

use mysqli;

include_once __DIR__ . '/IDAO.php';
include_once __DIR__ . '/Exceptions.php';

class DAO implements IDAO
{
    const AUTO_START_TRANSACTION = false;
    private static $_instance;
    private $_transactionStarted = false;
    private $_connection;

    private function __construct()
    {
        $this->_connection = $this->privateStartConnection();
        if ($this->_connection && $this::AUTO_START_TRANSACTION) {
            $this->startTransaction();
        }
    }

    private function privateStartConnection()
    {
        if (!$this->_connection) {
            $config = parse_ini_file('db_connection.ini');
            $connection = @new mysqli('localhost', $config['username'], $config['password'], $config['dbname']);
            if ($connection->connect_errno) {
                throw new \MysqlException('Connection error - ' . $connection->connect_error);
            }
            mysqli_set_charset($connection, "utf8"); // fixed encoding error
            return $connection;
        }
        return false;
    }

    public function startTransaction()
    {
        if (!$this->_transactionStarted) {
            return $this->_transactionStarted = $this->_connection->query('START TRANSACTION;');
        }
        return false;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) {
            self::$_instance = new DAO();
        }
        return self::$_instance;
    }

    public function checkString($str)
    {
        return $this->_connection->real_escape_string($str);
    }

    public function startConnection()
    {
        $connection = $this->privateStartConnection();
        if ($connection) {
            $this->_connection = $connection;
            if ($this::AUTO_START_TRANSACTION) {
                return $this->startTransaction();
            }
        }
        return !($connection === false);
    }

    public function rollback()
    {
        if ($this->_transactionStarted) {
            $this->_transactionStarted = !$this->_connection->query('ROLLBACK;');
            return !$this->_transactionStarted;
        }
        return false;

    }

    function __destruct()
    {
        $this->commit();
        $this->closeConnection();
    }

    public function commit()
    {
        if ($this->_transactionStarted) {
            $this->_transactionStarted = !$this->_connection->query('COMMIT;');
            return !$this->_transactionStarted;
        }
        return false;
    }

    function closeConnection()
    {
        if (!$this->_connection) {
            return false;
        }
        $this->_transactionStarted = false;
        $this->_connection->close();
        $this->_connection = false;
        return true;
    }

    function select(IEntitySelect $selectObj)
    {
        $result = $this->query($selectObj->getSelectQuery());
        $array = array();
        $count = 0;
        while ($row = $result->fetch_assoc()) {
            $array[$count] = $row;
            $count++;
        }
        return $array;
    }

    function query($sql)
    {
        $result = @$this->_connection->query($sql);
        if ($this->_connection->errno) {
            throw new \MysqlException('Ошибка в запросе: ' . $this->_connection->error . '. Запрос: ' . $sql);
        }
        return $result;
    }

    function update(IEntityUpdate $newObj, IEntityInsert $updateTable = null)
    {
        // TODO: Check update() method.
        if (!is_null($updateTable)) {
            $this->query($updateTable->getInsertQuery());
        }
        return $this->query($newObj->getUpdateQuery());
    }

    function insert(IEntityInsert $obj, IEntityInsert $updateTable = null)
    {
        // TODO: Check insert() method.
        if (!is_null($updateTable)) {
            $this->query($updateTable->getInsertQuery());
        }
        return $this->query($obj->getInsertQuery());
    }

    function delete(IEntityDelete $obj, IEntityInsert $updateTable = null)
    {
        // TODO: Check delete() method.
        if (!is_null($updateTable)) {
            $this->query($updateTable->getInsertQuery());
        }
        return $this->query($obj->getDeleteQuery());
    }
}

class UserAction implements IEntityInsert
{
    private $table;
    private $action;

    function __construct($table, $action)
    {
        $this->table = DAO::getInstance()->checkString($table);
        $this->action = DAO::getInstance()->checkString($action);
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        // TODO: Implement getUpdateQuery() method.
        return "INSERT INTO `user_action_history`(`userID`, `tableID`, `action`) VALUES ($userID,'$this->table','$this->action');";
    }
}