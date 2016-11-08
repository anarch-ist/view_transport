<?php
namespace DAO;

define('PHP_NEWLINE', '<br>' . PHP_EOL);

use mysqli;

include_once __DIR__ . '/IDAO.php';
include_once __DIR__ . '/Exceptions.php';

class DAO implements IDAO
{
    const AUTO_START_TRANSACTION = true;
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
//            $connection = @new mysqli('185.75.182.94', 'root', 'aftR179Kp', $config['dbname']);
            if ($connection->connect_errno) {
                throw new \MysqlException('Connection error - ' . $connection->connect_error);
            }
            //$connection->set_charset()
            mysqli_set_charset($connection, "utf8"); // fixed encoding error
            return $connection;
        }
        return false;
    }

    public function startTransaction()
    {
        if (!$this->_transactionStarted) {
            return $this->_transactionStarted = $this->query('START TRANSACTION;');
        }
        return false;
    }

    function query($sql)
    {
        $connection = $this->getConnection();
        if (!$connection) throw new \MysqlException('There is no connections');
        while ($connection->more_results()) $connection->next_result();
        $result = $connection->query($sql);
        if ($connection->errno) {
            throw new \MysqlException('Ошибка в запросе: ' . $connection->error . '. Запрос: ' . $sql);
        }
        return $result;
//        if($this->_connection->multi_query($sql)) {
//            $result = '';
//            do {
//                $result = $this->_connection->store_result();
//            } while($this->_connection->next_result());
//            return $result;
//        }
//        else {
//            throw new \MysqlException('Ошибка в запросе: ' . $this->_connection->error . '. Запрос: ' . $sql);
//        }
    }

    function getConnection()
    {
        if (!$this->_connection) throw new \MysqlException('There is no connections');
        return $this->_connection;
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
        $connection = $this->getConnection();
        return $connection->real_escape_string($str);
    }

    public function startConnection()
    {
        $this->_connection = $this->privateStartConnection();
        if ($this->_connection && $this::AUTO_START_TRANSACTION) {
            return $this->startTransaction();
        }
        return !($this->_connection === false);
//        $connection = $this->privateStartConnection();
//        if ($connection) {
//            $this->_connection = $connection;
//            if ($this::AUTO_START_TRANSACTION) {
//                return $this->startTransaction();
//            }
//        }
//        return !($connection === false);
    }

    public function rollback()
    {
        if ($this->_transactionStarted) {
            $this->_transactionStarted = !$this->query('ROLLBACK;');
            return !$this->_transactionStarted;
        }
        return false;

    }

    function __destruct()
    {
        $this->closeConnection();
    }

    function closeConnection()
    {
        if (!$this->_connection) {
            return false;
        }
        $this->commit();
        $this->_connection->close();
        $this->_connection = false;
        return true;
    }

    public function commit()
    {
        if ($this->_transactionStarted) {
            $this->query('COMMIT');
            $this->_transactionStarted = false;
            return true;
        }
        return false;
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

    function multiSelect(IEntitySelect $selectObj)
    {
        $connection = $this->_connection;
        $array = array();
        $arrayCount = 0;
        if ($connection->multi_query($selectObj->getSelectQuery())) {
            do {
                /* получаем первый результирующий набор */
                if ($result = $connection->store_result()) {
                    $count = 0;
                    while ($row = $result->fetch_assoc()) {
                        $array[$arrayCount][$count] = $row;
                        $count++;
                    }
                    $arrayCount++;
                    $result->free();
                }
            } while ($connection->more_results() && $connection->next_result());
        }
        return $array;
    }

    function update(IEntityUpdate $newObj, IEntityInsert $updateTable = null)
    {
        // TODO: Check update() method.
        if (!is_null($updateTable)) {
            $this->query($updateTable->getInsertQuery());
        }
        $result = $this->query($newObj->getUpdateQuery());
        return $result;
    }
    

    function update2(IEntityUpdate $newObj, IEntityInsert $updateTable = null){
         // TODO: Check update() method.

        $strs = explode(';', $newObj->getUpdateQuery());
        if (!is_null($updateTable)) {
            $this->query($updateTable->getInsertQuery());
        }
        $result1 = $this->query($strs[0]);
        $result2 = $this->query($strs[1]);
        return $result2;
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
    private $userID;
    private $action;

    function __construct($userID, $table, $action)
    {
        $this->table = DAO::getInstance()->checkString($table);
        $this->userID = DAO::getInstance()->checkString($userID);
        $this->action = DAO::getInstance()->checkString($action);
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        // TODO: Implement getUpdateQuery() method.
        return "INSERT INTO `user_action_history`(`userID`, `tableID`, `action`) VALUES ($this->userID,'$this->table','$this->action')";
    }
}