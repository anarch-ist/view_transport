<?php
namespace DAO;
include_once __DIR__ . '/IDriver.php';
include_once __DIR__ . '/../DAO.php';

class Driver implements IDriver
{
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

    function insertDriver($driverInfo)
    {
        return $this->_DAO->insert(new InsertDriver($driverInfo));
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

    function selectDriversByRange($start = 0, $length = 20)
    {
        $array = $this->_DAO->multiSelect(new SelectDriversByRange($start, $length));
        $arrayResult = array();
        $arrayResult['drivers'] = $array[0];
        $arrayResult['totalFiltered'] = $array[1][0]['totalFiltered'];
        $arrayResult['totalCount'] = $array[2][0]['totalCount'];
        return $arrayResult;
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

    function removeDriver($id)
    {
        return $this->_DAO->delete(new RemoveDriver($id));
    }

    function selectDriverByLastInsertedId()
    {
        $array = $this->_DAO->select(new SelectLastInsertedDriverId());
        return new DriverData($array[0]);
    }

    function updateDriver(DriverData $newDriver, $id)
    {
        return $this->_DAO->update(new UpdateDriver($newDriver, $id));
    }
}

class SelectAllDrivers implements IEntitySelect
{
    public function __construct()
    {
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `drivers`";
    }

}

class SelectDriverById implements IEntitySelect
{
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

class SelectDriverByCompanyId implements IEntitySelect
{
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

class SelectDriverByVehicleId implements IEntitySelect
{
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

class SelectDriversByRange implements IEntitySelect
{
    private $start, $count, $orderByColumn, $isDesc, $searchString;

    function __construct($start, $count)
    {
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
        $this->isDesc = ($_POST['order'][0]['dir'] === 'desc' ? 'TRUE' : 'FALSE');
        $this->searchString = $_POST['search']['value'];
        $searchArray = $_POST['columns'];
        $this->orderByColumn = $searchArray[$_POST['order'][0]['column']]['name'];
    }

    /**
     * this function contains query text
     * @return string
     */
    function getSelectQuery()
    {
        return "CALL selectDrivers($this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
    }
}


class InsertDriver implements IEntityInsert
{
    private $vehicle_id, $transport_company_id, $full_name, $passport, $phone, $license;

    public function __construct($companyData)
    {
        $dao = DAO::getInstance();
        $this->vehicle_id = $dao->checkString($companyData['vehicle_id']);
        $this->transport_company_id = $dao->checkString($companyData['transport_company_id']);
        $this->full_name = $dao->checkString($companyData['full_name']);
        $this->passport = $dao->checkString($companyData['passport']);
        $this->phone = $dao->checkString($companyData['phone']);
        $this->license = $dao->checkString($companyData['license']);
    }

    function getInsertQuery()
    {
        return "INSERT INTO `drivers` (vehicle_id, transport_company_id, full_name, passport, phone, license) VALUE " .
            "('$this->vehicle_id', '$this->transport_company_id', '$this->full_name', '$this->passport', '$this->phone', '$this->license');";
    }
}

class RemoveDriver implements IEntityDelete
{
    private $id;

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    /**
     * @return string
     */
    function getDeleteQuery()
    {
        return "DELETE FROM `drivers` WHERE id = $this->id";
    }
}


class SelectLastInsertedDriverId implements IEntitySelect
{
    /**
     * this function contains query text
     * @return string
     */
    function getSelectQuery()
    {
        return 'SELECT * FROM `drivers` WHERE id = LAST_INSERT_ID()';
    }
}


class UpdateDriver implements IEntityUpdate
{
    private $id, $vehicle_id, $transport_company_id, $full_name, $passport, $phone, $license;

    function __construct(DriverData $user, $id)
    {
        $dao = DAO::getInstance();
        $this->id = $dao->checkString($id);
        $this->transport_company_id = $dao->checkString($user->getData('transport_company_id'));
        $this->full_name = $dao->checkString($user->getData('full_name'));
        $this->passport = $dao->checkString($user->getData('passport'));
        $this->phone = $dao->checkString($user->getData('phone'));
        $this->license = $dao->checkString($user->getData('license'));
        $this->vehicle_id = $dao->checkString($user->getData('vehicle_id'));
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `drivers` SET " .
            "vehicle_id = $this->vehicle_id, " .
            "transport_company_id = '$this->transport_company_id', " .
            "full_name = '$this->full_name', " .
            "passport = '$this->passport', " .
            "phone = '$this->phone', " .
            "license = '$this->license'";
        $query = $query . " WHERE id = $this->id;";
        return $query;
    }
}