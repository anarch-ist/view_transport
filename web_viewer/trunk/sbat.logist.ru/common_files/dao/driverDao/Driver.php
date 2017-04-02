<?php
namespace DAO;
include_once __DIR__ . '/IDriver.php';
include_once __DIR__ . '/../DAO.php';

class Driver implements IDriver {
    private static $_instance;
    private $_DAO;

    function selectAllDrivers()
    {
        $array = $this->_DAO->select(new SelectAllDrivers());
        return $array;

    }

    function selectDriverById($id)
    {
        $array = $this->_DAO->select(new SelectDriverById($id));
        return new DriverData($array[0]);
    }

    function insertDriver()
    {
        // TODO: Implement insertDriver() method.
    }

    function selectDriverByCompanyId($companyId)
    {
        $array = $this->_DAO->select(new SelectDriverByCompanyId($companyId));
        return $array;
    }

    function selectDriverByVehicleId($vehicleId)
    {
        $array = $this->_DAO->select(new SelectDriverByVehicleId($vehicleId));
        return $array;
    }


    public function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new Driver();
        return self::$_instance;
    }

}

class SelectAllDrivers implements IEntitySelect {
    function getSelectQuery()
    {
        return "SELECT * FROM `drivers`";
    }

    public function __construct()
    {
    }

}

class SelectDriverById implements IEntitySelect {
    private $id;

    function getSelectQuery()
    {
        return "SELECT * FROM `drivers` WHERE id = $this->id";
    }

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

}

class SelectDriverByCompanyId implements IEntitySelect {
    private $companyId;

    function getSelectQuery()
    {
        return "SELECT * FROM `drivers` WHERE transport_company_id = $this->companyId";
    }

    public function __construct($companyId)
    {
        $this->companyId = DAO::getInstance()->checkString($companyId);
    }
}

class SelectDriverByVehicleId implements IEntitySelect {
    private $vehicleId;

    function getSelectQuery()
    {
        return "SELECT * FROM `drivers` WHERE vehicle_id = $this->vehicleId";
    }

    public function __construct($vehicleId)
    {
        $this->vehicleId = DAO::getInstance()->checkString($vehicleId);
    }
}