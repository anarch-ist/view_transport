<?php
namespace DAO;
require_once 'IInvoicesForUserDAO.php';
include_once '/../DAO.php';


class InvoicesForUserDAO extends DAO implements IInvoicesForUserDAO
{
    private static $instance;

    protected function __construct()
    {
        parent::__construct();
        self::$instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$instance)) return new InvoicesForUserDAO();
        return self::$instance;
    }

    /**
     * @return array
     */
    function selectAllData()
    {
        return parent::select(new EntitySelectAllData());
    }

    /**
     * @param $keyword
     * @return array
     */
    function selectDataByKey($keyword)
    {
        // TODO: Implement selectDataByKey() method.
        return parent::select(new EntitySelectAllData());
    }
}

class EntitySelectAllData extends EntityDataObject implements IEntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "CALL selectData();";
    }
}
