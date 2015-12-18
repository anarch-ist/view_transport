<?php
namespace DAO;

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

    function updateUser($newUser);

    function deleteUser($user);

    function addUser($user);
}