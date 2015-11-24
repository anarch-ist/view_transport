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
        return $this->_DAO->select(new EntitySelectAllData());
    }

    /**
     * @param $keyword
     * @return array
     */
    function selectDataByKey($keyword)
    {
        // TODO: Implement selectDataByKey() method.
        return $this->_DAO->select(new EntitySelectAllData());
    }
}

class EntitySelectAllData implements IEntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "CALL selectData();";
    }
}
