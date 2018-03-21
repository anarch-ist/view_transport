<?php
namespace DAO;
include_once __DIR__ . '/IVehicle.php';
include_once __DIR__ . '/../DAO.php';

class Vehicle implements IVehicle {
    private static $_instance;
    private $_DAO;

    function getVehicleByWialonId($wialonId){
        $vehicle = $this->_DAO->select(new SelectVehicleByWialonId($wialonId));
        return (!empty($vehicle)) ? $vehicle[0] : false;
    }

    function getTCpageVehicles(){
        return $this->_DAO->select(new SelectVehiclesForTransportCompanyPage());
    }
    function getVehicleWialonID($vehicleId)
    {

        $value = $this->_DAO->select(new GetVehicleWialonId($vehicleId))[0];
        return $value;
    }

    function selectAllVehicles()
    {
        $array = $this->_DAO->select(new SelectAllVehicles());
        return $array;

    }

    function selectVehicleById($id)
    {
        $array = $this->_DAO->select(new SelectVehicleById($id));
        return $array[0];
    }
    function selectVehicleByIdForTransportCompany ($id)
    {
        $array = $this->_DAO->select(new selectVehicleByIdForTransportCompany ($id));
        return $array[0];
    }
    function insertVehicleForTransportCompany($vehicleInfo)
    {
    return $this->_DAO->insert(new insertVehicleForTransportCompany($vehicleInfo));
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
    function TCPageRemoveVehicle($id)
    {
        return $this->_DAO->update(new TCPageRemoveVehicle($id));
    }
    function pseudoRemoveVehicle($id)
    {
        return $this->_DAO->update(new RemoveVehicle($id));
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
    function selectVehicleByLastInsertedIdForTC()
    {
        $array = $this->_DAO->select(new SelectLastInsertedVehicleIdForTC());
        return new VehicleData($array[0]);
    }
    function updateVehicle(VehicleData $newVehicle, $id)
    {
        return $this->_DAO->update(new UpdateVehicle($newVehicle, $id));
    }
    function TCPageUpdateVehicle(VehicleData $newVehicle, $id)
    {
        return $this->_DAO->update(new UpdateVehicleForTransportCompanyPage($newVehicle, $id));
    }
    function presudoRemoveDriverByTransportCompany($id) {
        return $this->_DAO->update(new RemoveVehicleByTransportCompany($id));
    }
}
class SelectVehiclesForTransportCompanyPage implements IEntitySelect {
    function getSelectQuery()
    {
        return "SELECT id,license_number,model,carrying_capacity,volume,loading_type,pallets_quantity,type,wialon_id FROM `vehicles`";
    }

    public function __construct()
    {
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

class SelectVehicleByWialonId implements IEntitySelect{
    private $wialonId;

    /**
     * SelectVehicleByWialonId constructor.
     * @param $wialonId
     */
    public function __construct($wialonId)
    {
        $this->wialonId = DAO::getInstance()->checkString($wialonId);
    }

    function getSelectQuery()
    {
        return "SELECT * FROM vehicles WHERE wialon_id = $this->wialonId AND deleted=0 LIMIT 1";
    }


}
class SelectVehicleForTCPage implements IEntitySelect {
    private $id;

    function getSelectQuery()
    {
        return "SELECT * FROM `vehicles` WHERE id = $this->id";
    }

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

}class selectVehicleByIdForTransportCompany implements IEntitySelect {
    private $id;

    function getSelectQuery()
    {
        return "SELECT id,license_number,model,carrying_capacity,volume,loading_type,pallets_quantity,type,wialon_id FROM `vehicles` WHERE id = $this->id";
    }

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
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
class InsertVehicleForTransportCompany implements IEntityInsert{
    private $transport_company_id, $license_number, $model, $carrying_capacity, $volume, $loading_type, $pallets_quantity, $type, $wialon_id;

    public function __construct($vehicleData)
    {
        $dao = DAO::getInstance();
        $this->transport_company_id = $dao->checkString($vehicleData['transport_company_id']);
        $this->license_number = $dao->checkString($vehicleData['license_number']);
        $this->model = $dao->checkString($vehicleData['model']);
        $this->carrying_capacity = $dao->checkString($vehicleData['carrying_capacity']);
        $this->volume = $dao->checkString($vehicleData['volume']);
        $this->loading_type = $dao->checkString($vehicleData['loading_type']);
        $this->pallets_quantity = ($dao->checkString($vehicleData['pallets_quantity'])=='') ? 0 : $dao->checkString($vehicleData['pallets_quantity']);
        $this->type = $dao->checkString($vehicleData['type']);
        $this->wialon_id = $dao->checkString($vehicleData['wialon_id']);
    }

    function getInsertQuery()
    {
        $query = "INSERT INTO `vehicles` (transport_company_id, vehicle_id_external, license_number, model, carrying_capacity, volume, loading_type, pallets_quantity, type, wialon_id) VALUE " .
            "($this->transport_company_id, CONCAT('LSS-',(SELECT COUNT(*) FROM (SELECT * FROM vehicles WHERE data_source_id='TCPage') as subVehicles)), '$this->license_number', '$this->model', '$this->carrying_capacity', '$this->volume', '$this->loading_type', '$this->pallets_quantity', '$this->type', '$this->wialon_id');";
        return $query;
    }
}


class InsertVehicle implements IEntityInsert{
    private $transport_company_id, $license_number, $model, $carrying_capacity, $volume, $loading_type, $pallets_quantity, $type, $wialon_id;

    public function __construct($vehicleData)
    {
        $dao = DAO::getInstance();
        $this->transport_company_id = $dao->checkString($vehicleData['transport_company_id']);
        $this->license_number = $dao->checkString($vehicleData['license_number']);
        $this->model = $dao->checkString($vehicleData['model']);
        $this->carrying_capacity = $dao->checkString($vehicleData['carrying_capacity']);
        $this->volume = $dao->checkString($vehicleData['volume']);
        $this->loading_type = $dao->checkString($vehicleData['loading_type']);
        $this->pallets_quantity = ($dao->checkString($vehicleData['pallets_quantity'])=='') ? 0 : $dao->checkString($vehicleData['pallets_quantity']);
        $this->type = $dao->checkString($vehicleData['type']);
        $this->wialon_id = $dao->checkString($vehicleData['wialon_id']);
    }

    function getInsertQuery()
    {
        $query = "INSERT INTO `vehicles` (transport_company_id, vehicle_id_external, license_number, model, carrying_capacity, volume, loading_type, pallets_quantity, type, wialon_id) VALUE " .
            "($this->transport_company_id, CONCAT('LSS-',(SELECT COUNT(*) FROM (SELECT * FROM vehicles WHERE data_source_id='ADMIN_PAGE') as subVehicles)), '$this->license_number', '$this->model', '$this->carrying_capacity', '$this->volume', '$this->loading_type', '$this->pallets_quantity', '$this->type', '$this->wialon_id');";
        return $query;
    }
}



//class GetVehicleWialonId implements IEntitySelect {
//    private $vehicleId;
//
//    /**
//     * @return mixed
//     */
//    function getSelectQuery()
//    {
//        return "SELECT vehicles.wialon_id FROM vehicles WHERE vehicles.id = $this->vehicleId";
//    }
//
//    /**
//     * GetVehicleWialonId constructor
//     */
//    public function __construct($vehicleId)
//    {
//        $this->vehicleId = $vehicleId;
//    }
//
//
//}

class RemoveVehicle implements IEntityUpdate {
    private $id;

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        return "UPDATE `vehicles` SET deleted = TRUE WHERE id = $this->id";
    }
}
class TCPageRemoveVehicle implements IEntityUpdate {
    private $id;

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        return "UPDATE `vehicles` SET deleted = TRUE WHERE id = $this->id";
    }
}
class RemoveVehicleByTransportCompany implements IEntityUpdate {
    private $id;

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        return "UPDATE `vehicles` SET deleted = TRUE WHERE transport_company_id = $this->id";
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
class SelectLastInsertedVehicleIdForTC implements IEntitySelect {
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
    private $id, $transport_company_id, $license_number, $model, $volume, $loading_type, $pallets_quantity, $type, $wialon_id;

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
        $this->wialon_id = $dao->checkString($user->getData('wialon_id'));
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `vehicles` SET " .
            "transport_company_id = '$this->transport_company_id', " .
            "license_number = '$this->license_number', " .
            "model = '$this->model', " .
            "volume = '$this->volume', " .
            "loading_type = '$this->loading_type', " .
            "pallets_quantity = '$this->pallets_quantity', " .
            "type = '$this->type', " .
            "wialon_id = '$this->wialon_id'";
        $query = $query . " WHERE id = $this->id;";
        return $query;
    }
}
class UpdateVehicleForTransportCompanyPage implements IEntityUpdate
{
    private $id, $transport_company_id, $license_number, $model, $volume, $loading_type, $pallets_quantity, $type, $wialon_id;

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
        $this->wialon_id = $dao->checkString($user->getData('wialon_id'));
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `vehicles` SET " .
            "transport_company_id = '$this->transport_company_id', " .
            "license_number = '$this->license_number', " .
            "model = '$this->model', " .
            "volume = '$this->volume', " .
            "loading_type = '$this->loading_type', " .
            "pallets_quantity = '$this->pallets_quantity', " .
            "type = '$this->type', " .
            "wialon_id = '$this->wialon_id'";
        $query = $query . " WHERE id = $this->id;";
        return $query;
    }
}