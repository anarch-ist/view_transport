<?php

include_once __DIR__ . '/../common_files/dao/userDao/User.php';

class UserEntityTest extends PHPUnit_Framework_TestCase
{
    public function testCreateUserEntity()
    {
        $user = \DAO\UserEntity::getInstance();
        $this->assertNotNull($user);
        return $user;
    }

    /**
     * @depends testCreateUserEntity
     */
    public function testGetUserRole(\DAO\UserEntity $user)
    {
        $roleRusName = $user->getUserRole(0);
        $this->assertEmpty($roleRusName);
        $roleRusName = $user->getUserRole(1);
        $this->assertNotNull($roleRusName);
    }

    /**
     * @depends testCreateUserEntity
     */
    public function testGetUserByEmail(\DAO\UserEntity $user)
    {
        $userData = $user->selectUserByEmail('andy');
        $this->assertEmpty($userData);
        $userData = $user->selectUserByEmail('\' OR true; select * from `users`; --');
        $this->assertEmpty($userData);
        $userData = $user->selectUserByEmail('" OR true; select * from `users`; --');
        $this->assertEmpty($userData);
        $userData = $user->selectUserByEmail('test@test.ru');
        $this->assertEquals($userData->getData('userID'), 1);
    }
}
