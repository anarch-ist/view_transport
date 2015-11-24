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
    function update(IEntityUpdate $newObj);

    /**
     * @param $obj
     * @return string
     */
    function insert(IEntityInsert $obj);

    /**
     * @param IEntityDelete $obj
     * @return string
     */
    function delete(IEntityDelete $obj);
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

interface IEntityData {
    function getData($field);
    function toArray();
}
?>