<?php
namespace DAO;
include_once __DIR__ . '/IExchaneLog.php';
include_once __DIR__ . '/../DAO.php';

class ExchangeLog implements IExchaneLog
{
    private static $_instance;
    private $_DAO;

    protected function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new ExchangeLog();
        return self::$_instance;
    }

    function selectLastTenTransactions()
    {
        return $this->_DAO->select(new SelectLastTenTransactions());
    }

    function selectLastTransaction()
    {
        return $this->_DAO->select(new SelectLastTransaction());
    }

    function selectLastXTransactions($howMany)
    {
        return $this->_DAO->select(new SelectLastXTransactions($howMany));
    }



}

class SelectLastTenTransactions implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT * FROM exchange_log ORDER BY entry_id DESC LIMIT 10;";
    }
}

class SelectLastTransaction implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT * FROM exchange_log ORDER BY entry_id DESC LIMIT 1;";
    }
}

class SelectLastXTransactions implements IEntitySelect
{
    private $howMany;

    function getSelectQuery()
    {
        return "SELECT * FROM exchange_log ORDER BY entry_id DESC LIMIT $this->howMany;";
    }

    public function __construct($howMany)
    {
        $this->howMany = DAO::getInstance()->checkString($howMany);
    }

}