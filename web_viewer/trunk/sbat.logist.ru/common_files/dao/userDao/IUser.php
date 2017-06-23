<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IUserEntity
{
    function selectUsers();

    /**
     * @param $userID
     * @return string
     */
    function getUserRole($userID);  //covered

    function selectUserByID($id); //covered

    function selectUserByLogin($login); //covered

    function updateUser(UserData $newUser, $id); //covered

    function deleteUser($userID); //covered

    function addUser(UserData $user); //covered

    function getUsersTotalCount();

    function getMarketAgentsByName($name);
}