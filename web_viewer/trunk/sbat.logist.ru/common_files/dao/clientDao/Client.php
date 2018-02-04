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

//    function selectClients()
//    {
//        return $this->DAO->select(new SelectAllClients());
//    }
    function selectClients($start = 0, $count = 20)
    {
        $array = $this->DAO->multiSelect(new EntitySelectAllClients($start, $count));
        $arrayResult = array();
        $arrayResult['clients'] = $array[0];
        $arrayResult['totalFiltered'] = $array[1][0]['totalFiltered'];
        $arrayResult['totalCount'] = $array[2][0]['totalCount'];
        return $arrayResult;
    }

    function selectClientsForAdminPage()
    {
        return $this->DAO->select(new SelectAllClientsForAdminPage());
    }

    function selectLastInsertedClient(){
        return $this->DAO->select(new SelectLastInserted());
    }

    function removeClient($clientID){
        return $this->DAO->delete(new DeleteClient($clientID));
    }

    function addClient($clientData){
        return $this->DAO->insert(new InsertClient($clientData));
    }

    function updateClient($clientData, $id){
        return $this->DAO->update(new UpdateClient($clientData, $id));
    }

    function selectClient($id){
        return $this->DAO->select(new SelectClient($id));
    }

}

class SelectClient implements IEntitySelect {
    private $clientID;

    /**
     * SelectClient constructor.
     * @param $clientID
     */
    public function __construct($clientID)
    {
        $this->clientID = $clientID;
    }

    function getSelectQuery()
    {
        return "SELECT * FROM clients WHERE clientID=$this->clientID";
    }


}



class UpdateClient implements IEntityUpdate{
    private $INN,$clientName, $KPP, $corAccount, $curAccount, $BIK, $contractNumber, $clientID;

    public function __construct($clientData, $id)
    {
        $this->INN = $clientData['INN'];
        $this->clientName=$clientData['clientName'];
        $this->KPP=$clientData['KPP'];
        $this->corAccount=$clientData['corAccount'];
        $this->curAccount=$clientData['curAccount'];
        $this->BIK=$clientData['BIK'];
        $this->contractNumber=$clientData['contractNumber'];
        $this->clientID=$id;
    }

    function getUpdateQuery()
    {
        $query = "UPDATE clients SET clientName ='$this->clientName', INN ='$this->INN', KPP ='$this->KPP', corAccount='$this->corAccount', curAccount='$this->curAccount', BIK='$this->BIK', contractNumber='$this->contractNumber' WHERE clientID= $this->clientID";
        return $query;
    }


}

class EntitySelectAllClients implements IEntitySelect
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

    function getSelectQuery()
    {
        return "CALL selectClients($this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
    }
}

class DeleteClient implements IEntityDelete{
    private $clientID;

    /**
     * DeleteClient constructor.
     * @param $clientID
     */
    public function __construct($clientID)
    {
        $this->clientID = $clientID;
    }

    function getDeleteQuery()
    {
        $query = "DELETE FROM clients WHERE clientID=$this->clientID";
        return $query;
    }


}

class SelectLastInserted implements IEntitySelect{
    function getSelectQuery()
    {
        return "SELECT clientID, clientName, INN, KPP, corAccount, curAccount, BIK, contractNumber FROM clients WHERE dataSourceID='ADMIN_PAGE' ORDER BY clientID DESC LIMIT 1";
    }

}

class InsertClient implements IEntityInsert{
    private $INN,$clientName, $KPP, $corAccount, $curAccount, $BIK, $contractNumber;

    /**
     * InsertClient constructor.
     */
    public function __construct($clientData)
    {
        $this->INN = $clientData['INN'];
        $this->clientName=$clientData['clientName'];
        $this->KPP=$clientData['KPP'];
        $this->corAccount=$clientData['corAccount'];
        $this->curAccount=$clientData['curAccount'];
        $this->BIK=$clientData['BIK'];
        $this->contractNumber=$clientData['contractNumber'];
    }

    function getInsertQuery()
    {
        $query= "INSERT INTO clients  (clientIDExternal, dataSourceID, clientName, INN, KPP, corAccount, curAccount, BIK, contractNumber) VALUES ((CONCAT('LSS-',(SELECT clientID FROM (SELECT clientID FROM clients WHERE dataSourceID='ADMIN_PAGE' ORDER BY clientID DESC LIMIT 1) AS adminClients))), 'ADMIN_PAGE', '$this->clientName', '$this->INN', '$this->KPP', '$this->corAccount','$this->curAccount','$this->BIK', '$this->contractNumber');";
        return $query;
    }


}

class SelectAllClientsForAdminPage implements IEntitySelect {
    function getSelectQuery()
    {
        return "SELECT clientID, clientName, INN, KPP, corAccount, curAccount, BIK, contractNumber FROM clients;";
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