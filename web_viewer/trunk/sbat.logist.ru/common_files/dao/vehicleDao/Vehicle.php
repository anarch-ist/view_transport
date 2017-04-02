<?php
namespace DAO;
include_once __DIR__ . '/IVehicle.php';
include_once __DIR__ . '/../DAO.php';

class Vehicle implements IVehicle {
    private static $_instance;
    private $_DAO;

    function selectAllVehicles()
    {
        $array = $this->_DAO->select(new SelectAllVehicles());
        return $array;

    }

    function selectVehicleById($id)
    {
        $array = $this->_DAO->select(new SelectVehicleById($id));
        return new VehicleData($array[0]);
    }

    function insertVehicle()
    {
        // TODO: Implement insertCompany() method.
    }

    function selectVehicleByCompanyId($companyId)
    {
        return $this->_DAO->select(new SelectVehicleByCompanyId($companyId));
        // TODO: Implement selectVehicleByCompanyId() method.
    }


    public function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new Vehicle();
        return self::$_instance;
    }

    function getAllVehicles(){
        return $this->_DAO->select(new SelectAllVehicles());
    }

    function getVehicleById($id){
        return $this->_DAO->select(new SelectVehicleById($id));
    }

    function getVehicleByCompanyId($companyId){
        return $this->_DAO->select(new SelectVehicleByCompanyId($companyId));
    }
}

class SelectAllVehicles implements IEntitySelect {
    function getSelectQuery()
    {
        return "SELECT * FROM `vehicles`";
    }

    public function __construct()
    {
    }

}

class SelectVehicleById implements IEntitySelect {
    private $id;

    function getSelectQuery()
    {
        return "SELECT * FROM `vehicles` WHERE id = $this->id";
    }

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

}

class SelectVehicleByCompanyId implements IEntitySelect {
    private $companyId;

    function getSelectQuery()
    {
        return "SELECT * FROM `vehicles` WHERE transport_company_id = $this->companyId";
//        return "SELECT * FROM `vehicles` WHERE transport_company_id = 1";
    }

    public function __construct($companyId)
    {
        $this->companyId = DAO::getInstance()->checkString($companyId);
    }
}