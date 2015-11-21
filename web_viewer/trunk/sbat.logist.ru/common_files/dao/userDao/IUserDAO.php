<?php
namespace DAO;

interface IUserDAO
{
    function selectUsers();

    function selectUserByID($id);

    function selectUserByEmail($email);

    function selectUserByLoginAndPassword($login, $password);

    function updateUser($newUser);

    function deleteUser($user);

    function addUser($user);
}