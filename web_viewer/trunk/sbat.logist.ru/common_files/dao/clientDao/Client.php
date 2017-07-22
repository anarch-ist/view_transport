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

    function selectAllClientIdINNPairs()
    {
        return $this->DAO->select(new SelectAllClientIdINNPairs());
    }

    function selectClientsByINN($inn)
    {
        return $this->DAO->select(new SelectClientsByInn($this->DAO->checkString($inn)));
    }

    function selectClientsByInnOrName($search)
    {
        return $this->DAO->select(new SelectClientByInnOrName($this->DAO->checkString($search)));
    }

    function selectClients()
    {
        return $this->DAO->select(new SelectAllClients());
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
        return "SELECT * FROM `clients`";
    }
}

class SelectAllClientIdINNPairs implements IEntitySelect
{

    /**
     * Select all clients
     * @return string
     */
    function getSelectQuery()
    {
        return "SELECT clientID, INN FROM `clients`";
    }
}

class SelectClientByInnOrName implements IEntitySelect
{
    private $search;

    /**
     * SelectClientByInnOrName constructor.
     * @param $inn
     * @param $name
     */
    public function __construct($search)
    {
        $this->search=$search;
    }

    /**
     * @return mixed
     */
    function getSelectQuery()
    {
        return "SELECT clientID,INN,clientName FROM `clients` WHERE (INN LIKE '$this->search%' OR clientName LIKE '$this->search%') AND INN <>'' AND clientName IS NOT NULL LIMIT 20";
    }

    /**
     * SelectClientByInnOrName constructor.
     */



}

class SelectClientsByInn implements IEntitySelect
{
    private $inn;

    /**
     * SelectClientsByInn constructor.
     * @param $inn
     */
    public function __construct($inn)
    {
        $this->inn = $inn;
    }


    /**
     * Select all clients
     * @return string
     */
    function getSelectQuery()
    {
        return "SELECT clientID, INN, clientName FROM `clients` WHERE INN LIKE '$this->inn%' AND INN <> '' AND clientName IS NOT NULL LIMIT 10";
    }
}