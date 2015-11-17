<?php
namespace DAO;

interface IUserDAO
{
    function selectUsers();

    function selectUserByID($id);

    function selectUserByEmail($email);

    function updateUser($newUser);

    function deleteUser($user);

    function addUser($user);
}