<?php
namespace DAO;

define('PHP_NEWLINE', '<br>' . PHP_EOL);

use Exception;
use mysqli;

include_once __DIR__.'/IDAO.php';
include_once __DIR__.'/Exceptions.php';

class DAO implements IDAO
{
    const AUTO_START_TRANSACTION = true;
    private static $_instance;
    private $_connection;

    private function __construct()
    {
        $config = parse_ini_file('db_connection.ini');
        $this->_connection = @new mysqli('localhost', $config['username'], $config['password'], $config['dbname']);
        if ($this->_connection->connect_errno) {
            throw new \MysqlException('Connection error - ' . $this->_connection->connect_error);
        }
        mysqli_set_charset($this->_connection, "utf8"); // fixed encoding error
        if ($this::AUTO_START_TRANSACTION) {
            $this->startTransaction();
        }
    }

    public function startTransaction()
    {
        return $this->_connection->query('START TRANSACTION;');
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

    function startConnection()
    {
        $this->_connection = @new mysqli('localhost', 'andy', 'andyandy', 'project_database');
        if ($this->_connection->connect_errno) {
            throw new \MysqlException('Connection error - ' . $this->_connection->connect_error);
        }

    }

    public function rollback()
    {
        return $this->_connection->query('ROLLBACK;');

    }

    function __destruct()
    {
        $this->commit();
        $this->closeConnection();
    }

    public function commit()
    {
        return $this->_connection->query('COMMIT;');
    }

    function closeConnection()
    {
        return $this->_connection->close();
    }

    function select(IEntitySelect $selectObj)
    {
        $result = @self::query($selectObj->getSelectQuery());
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

    function query($sql)
    {
        return $this->_connection->query($sql);
    }

    function update(IEntityUpdate $newObj, IEntityInsert $updateTable=null)
    {
        // TODO: Check update() method.
        if (!is_null($updateTable)) {
            $this->query($updateTable->getInsertQuery());
        }
        return $this->query($newObj->getUpdateQuery());
    }

    function insert(IEntityInsert $obj, IEntityInsert $updateTable=null)
    {
        // TODO: Check insert() method.
        if (!is_null($updateTable)) {
            $this->query($updateTable->getInsertQuery());
        }
        return $this->query($obj->getInsertQuery());
    }

    function delete(IEntityDelete $obj, IEntityInsert $updateTable=null)
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