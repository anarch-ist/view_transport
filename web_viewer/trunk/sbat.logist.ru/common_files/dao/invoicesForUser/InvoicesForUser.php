<?php
namespace DAO;
require_once 'IInvoicesForUser.php';
include_once '/../DAO.php';


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
    function selectAllData()
    {
        $count = 25;
        $start = 0;
        switch (func_num_args()) {
            case 2:
                $start = func_get_arg(1);
                $count = func_get_arg(0);
                break;
            case 1:
                $count = func_get_arg(0);
                break;
        }
        return $this->_DAO->select(new EntitySelectAllData($start,$count));
    }

    /**
     * @param $keyword
     * @return array
     */
    function selectDataByKey($keyword)
    {
        $count = 25;
        $start = 0;
        switch (func_num_args()) {
            case 2:
                $start = func_get_arg(1);
                $count = func_get_arg(0);
                break;
            case 1:
                $count = func_get_arg(0);
                break;
        }
        return $this->_DAO->select(new EntitySelectAllData($start,$count));
    }
}

class EntitySelectAllData implements IEntitySelect
{
    private $start, $count;
    function __construct($start, $count) {
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
    }
    function getSelectQuery()
    {
        $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        return "CALL selectData($userID,$this->start,$this->count);";
    }
}
