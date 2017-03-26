<?php
namespace DAO;
include_once __DIR__ . '/ITransportCompany.php';
include_once __DIR__ . '/../DAO.php';

class TransportCompany implements ITransportCompany {
    private static $_instance;
    private $_DAO;
    
    function selectAllCompanies()
    {
        $array = $this->_DAO->select(new SelectAllCompanies());
        return $array;
        
    }

    function selectCompanyById($id)
    {
        $array = $this->_DAO->select(new SelectCompanyById($id));
        return new TransportCompanyData($array[0]);
    }

    function insertCompany()
    {
        // TODO: Implement insertCompany() method.
    }

    public function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

}

class SelectAllCompanies implements IEntitySelect {
    function getSelectQuery()
    {
        return "SELECT * FROM transport_companies";
    }

    public function __construct()
    {
    }

}

class SelectCompanyById implements IEntitySelect{
    var $id;
    function getSelectQuery()
    {
        return "SELECT * FROM transport_companies WHERE id = $this->id ";
    }

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

}

class InsertCompany implements IEntityInsert{
    function getInsertQuery()
    {
        // TODO: Implement getInsertQuery() method.
    }

    public function __construct()
    {
    }


}