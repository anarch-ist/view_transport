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
    function  getUserRole($userID);

    function selectUserByID($id);

    function selectUserByEmail($email);

    function updateUser(UserData $newUser);

    function deleteUser(UserData $user);

    function addUser(UserData $user);
}