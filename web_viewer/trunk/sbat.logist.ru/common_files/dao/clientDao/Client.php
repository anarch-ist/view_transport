<?php
namespace DAO;
include_once __DIR__ . "/IClient.php";
include_once __DIR__ . '/../DAO.php';
include_once __DIR__ . '/Data.php';

class ClientEntity implements IClientEntity
{
    private static $_instance;
    private $DAO;

    protected function __construct()
    {
        $this->DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new ClientEntity();
        return self::$_instance;
    }

    function selectClients()
    {
        $array = $this->DAO->select(new SelectAllClients());
        return $array;
    }
}

class SelectAllClients implements IEntitySelect
{

    /**
     * Select all clients
     * @return string
     */
    function getSelectQuery()
    {
        return "SELECT clientID,clientName FROM `clients`";
    }
}
