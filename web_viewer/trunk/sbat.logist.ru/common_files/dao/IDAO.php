<?php
namespace DAO;

interface IDAO
{
    // Function for checking string for correct format
    /**
     * @param $selectObj
     * @return string
     */
    function select(IEntitySelect $selectObj);

    /**
     * @param $newObj
     * @return string
     */
    function update(IEntityUpdate $newObj, IEntityInsert $updateTable=null);

    /**
     * @param $obj
     * @return string
     */
    function insert(IEntityInsert $obj, IEntityInsert $updateTable=null);

    /**
     * @param IEntityDelete $obj
     * @return string
     */
    function delete(IEntityDelete $obj, IEntityInsert $updateTable=null);
}

interface IEntitySelect
{
    /**
     * this function contains query text
     * @return string
     */
    function getSelectQuery();
}

interface IEntityUpdate
{
    /**
     * @return string
     */
    function getUpdateQuery();
}

interface IEntityInsert
{
    /**
     * @return string
     */
    function getInsertQuery();
}

interface IEntityDelete
{
    /**
     * @return string
     */
    function getDeleteQuery();
}

interface IEntityCanAct
{
    //function hasPermission(PermissionsData $permissions);
}

?>