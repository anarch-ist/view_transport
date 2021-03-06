<?php
namespace DAO;
require_once 'IRequestsForUser.php';
include_once __DIR__ . '/../DAO.php';
ini_set('display_errors','Off');
error_reporting('E_ALL');

class RequestsForUserEntity implements IRequestsForUserEntity
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
        if (is_null(self::$_instance)) return new RequestsForUserEntity();
        return self::$_instance;
    }

    /**
     * @return array
     */
    function selectAllData(array $columnInformation, $orderColumnNumber, $start = 0, $count = 20, $userID = -1)
    {
        if ($userID < 1) {
            $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        }
        $array = $this->_DAO->multiSelect(new EntitySelectAllRequestsForUser($start, $count, $userID, $columnInformation, $orderColumnNumber));
        $arrayResult = array();
        // TODO возникает ошибка, если в запросе после фильтрации нет данных
        if(empty($array[0]) || $array[0] == '' || !isset($array[0]) ){
            $array[0] = array();
        }
        $arrayResult['requests'] = $array[0];
        $arrayResult['totalFiltered'] = $array[1][0]['totalFiltered'];
        $arrayResult['totalCount'] = $array[2][0]['totalCount'];
        return $arrayResult;
    }

    function selectClientSideData($userID= -1){
        if ($userID < 1) {
            $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        }
        $array = $this->_DAO->select(new SelectCliendSideDataForUser($userID));
        return $array;
    }

}

class SelectCliendSideDataForUser implements IEntitySelect {
    private $userID;

    function __construct($userID)
    {
        $this->userID=$userID;
    }

    function getSelectQuery()
    {
        return "CALL selectClientSideData($this->userID);";
    }
}

class EntitySelectAllRequestsForUser implements IEntitySelect
{
    private $start, $count, $orderByColumn, $isDesc, $searchString, $userID;

    function __construct($start, $count, $userID, $columnInformation, $orderColumn, $order = -1)
    {
        $this->userID = $userID;
        //TODO: remake this shit
        if ($order === -1) {
            $order = $_POST['order'][0]['dir'];
        }
        if ($orderColumn === -1) {
            $orderColumn = $_POST['order'][0]['column'];
        }
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
        $this->isDesc = ($order === 'desc' ? 'TRUE' : 'FALSE');
        $this->searchString = '';
        $searchArray = $columnInformation;
        for ($i = 0; $i < count($searchArray); $i++) {
            if ($searchArray[$i]['search']['value'] !== '') {
                $this->searchString .= $searchArray[$i]['name'] . ',' . $searchArray[$i]['search']['value'] . ';';
            }
        }
        $this->orderByColumn = $searchArray[$orderColumn]['name'];
    }

    function getSelectQuery()
    {
        return "CALL selectData($this->userID,$this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
    }
}