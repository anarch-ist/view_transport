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

    function insertVehicle($vehicleInfo)
    {
        return $this->_DAO->insert(new InsertVehicle($vehicleInfo));
    }

    function selectVehicleByCompanyId($companyId)
    {
        return $this->_DAO->select(new SelectVehicleByCompanyId($companyId));
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

    function removeVehicle($id)
    {
        return $this->_DAO->delete(new RemoveVehicle($id));
    }

    function selectVehiclesByRange($start = 0, $length = 20)
    {
        $array = $this->_DAO->multiSelect(new SelectVehiclesByRange($start, $length));
        $arrayResult = array();
        $arrayResult['vehicles'] = $array[0];
        $arrayResult['totalFiltered'] = $array[1][0]['totalFiltered'];
        $arrayResult['totalCount'] = $array[2][0]['totalCount'];
        return $arrayResult;
    }

    function selectVehicleByLastInsertedId()
    {
        $array = $this->_DAO->select(new SelectLastInsertedVehicleId());
        return new VehicleData($array[0]);
    }

    function updateVehicle(VehicleData $newVehicle, $id)
    {
        return $this->_DAO->update(new UpdateVehicle($newVehicle, $id));
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
    }

    public function __construct($companyId)
    {
        $this->companyId = DAO::getInstance()->checkString($companyId);
    }
}

class SelectVehiclesByRange implements IEntitySelect {
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
        return "CALL selectVehicles($this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
    }
}


class InsertVehicle implements IEntityInsert{
    private $transport_company_id, $license_number, $model, $carrying_capacity, $volume, $loading_type, $pallets_quantity, $type;

    public function __construct($companyData)
    {
        $dao = DAO::getInstance();
        $this->transport_company_id = $dao->checkString($companyData['transport_company_id']);
        $this->license_number = $dao->checkString($companyData['license_number']);
        $this->model = $dao->checkString($companyData['model']);
        $this->carrying_capacity = $dao->checkString($companyData['carrying_capacity']);
        $this->volume = $dao->checkString($companyData['volume']);
        $this->loading_type = $dao->checkString($companyData['loading_type']);
        $this->pallets_quantity = $dao->checkString($companyData['pallets_quantity']);
        $this->type = $dao->checkString($companyData['type']);
    }

    function getInsertQuery()
    {
        return "INSERT INTO `vehicles` (transport_company_id, license_number, model, carrying_capacity, volume, loading_type, pallets_quantity, type) VALUE " .
            "('$this->transport_company_id', '$this->license_number', '$this->model', '$this->carrying_capacity', '$this->volume', '$this->loading_type', '$this->pallets_quantity', '$this->type');";
    }
}

class RemoveVehicle implements IEntityDelete {
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
        return "DELETE FROM `vehicles` WHERE id = $this->id";
    }
}


class SelectLastInsertedVehicleId implements IEntitySelect {
    /**
     * this function contains query text
     * @return string
     */
    function getSelectQuery()
    {
        return 'SELECT * FROM `vehicles` WHERE id = LAST_INSERT_ID()';
    }
}

class UpdateVehicle implements IEntityUpdate
{
    private $id, $transport_company_id, $license_number, $model, $volume, $loading_type, $pallets_quantity, $type;

    function __construct(VehicleData $user, $id)
    {
        $dao = DAO::getInstance();
        $this->id = $dao->checkString($id);
        $this->transport_company_id = $dao->checkString($user->getData('transport_company_id'));
        $this->license_number = $dao->checkString($user->getData('license_number'));
        $this->model = $dao->checkString($user->getData('model'));
        $this->volume = $dao->checkString($user->getData('volume'));
        $this->loading_type = $dao->checkString($user->getData('loading_type'));
        $this->pallets_quantity = $dao->checkString($user->getData('pallets_quantity'));
        $this->type = $dao->checkString($user->getData('type'));
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `vehicles` SET " .
            "transport_company_id = '$this->transport_company_id', " .
            "license_number = $this->license_number, " .
            "model = '$this->model', " .
            "volume = '$this->volume', " .
            "loading_type = '$this->loading_type', " .
            "pallets_quantity = '$this->pallets_quantity', " .
            "type = '$this->type'";
        $query = $query . " WHERE id = $this->id;";
        return $query;
    }
}