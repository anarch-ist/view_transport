<?php
namespace DAO;
require_once 'IInvoicesForUser.php';
include_once __DIR__ . '/../DAO.php';


class InvoicesForUserEntity implements IInvoicesForUserEntity
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
        if (is_null(self::$_instance)) return new InvoicesForUserEntity();
        return self::$_instance;
    }

    /**
     * @return array
     */
    function selectAllData($start = 0, $count = 20, $userID = -1)
    {
        if ($userID < 1) {
            $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        }
        $array = $this->_DAO->select(new EntitySelectAllInvoicesForUser($start, $count, $userID));
        return $array;
    }

}

class EntitySelectAllInvoicesForUser implements IEntitySelect
{
    private $start, $count, $orderByColumn, $isDesc, $searchString, $userID;

    function __construct($start, $count, $userID, $order = -1, $orderColumn = -1, $cols = -1)
    {
        $this->userID = $userID;
        //TODO: remake this shit
        if ($order === -1) {
            $order = $_POST['order'][0]['dir'];
        }
        if ($cols === -1) {
            $cols = $_POST['columns'];
        }
        if ($orderColumn === -1) {
            $orderColumn = $_POST['order'][0]['column'];
        }
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
        $this->isDesc = ($order === 'desc' ? 'TRUE' : 'FALSE');
        $this->searchString = '';
        $searchArray = $cols;
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