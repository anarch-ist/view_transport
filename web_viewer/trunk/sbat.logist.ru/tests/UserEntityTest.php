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
     * @depends      testCreateUserEntity
     * @dataProvider provideUserEmail
     */
    public function testGetUserByEmail($email, $isEmpty, $userID, \DAO\UserEntity $user)
    {
        $userData = $user->selectUserByEmail($email);
        if ($isEmpty) {
            $this->assertEmpty($userData);
        } else {
            $this->assertEquals($userID, $userData->getData('userID'));
        }
    }

    function provideUserEmail()
    {
        $test1 = array('email' => 'andy', 'isEmpty' => true, 'userID' => 1);
        $test2 = array('email' => 'test@test.ru', 'isEmpty' => false, 'userID' => 1);
        return array($test1, $test2);
    }
}
