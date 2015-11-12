<?php

interface IDAO
{
    // This is the root interface for realizing DAO-pattern
    // Static functions for interaction with data source
    // Function for starting connection with data source
    static function startConnection();

    /**
     * @return bool
     */
    static function closeConnection();

    // Function for checking string for correct format
    function select($selectObj);

    function update($newObj);

    function insert($obj);

    function delete($obj);
}

interface IEntitySelect
{
    /**
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

/**
 * Interface IEntityDataCheck
 */
interface IEntityDataCheck
{
    /**
     * @param string
     * @return string
     */
    function prepareSafeString($string);
}

?>