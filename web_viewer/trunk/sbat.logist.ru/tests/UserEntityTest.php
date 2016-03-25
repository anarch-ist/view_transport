<?php

include_once __DIR__ . '/../common_files/dao/userDao/User.php';
include_once 'AbstractEntityTestClass.php';

class UserEntityTest extends AbstractEntityTestClass
{
    /**
     * @before
     */
    static function openMysqlConnection()
    {
        parent::openMysqlConnection();
    }

    /**
     * @after
     */
    static function closeMysqlConnection()
    {
        parent::closeMysqlConnection();
    }

    /**
     * @afterClass
     */
    static function flushDB()
    {
        parent::flushDB();
    }

    /**
     * @covers \DAO\UserEntity::getInstance
     * @test
     */
    public function testCreateUserEntity()
    {
        $user = \DAO\UserEntity::getInstance();
        $this->assertNotNull($user);
        return $user;
    }

    /**
     * @depends testCreateUserEntity
     * @covers  \DAO\UserEntity::getUserRole
     * @test
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
     * @covers       \DAO\UserEntity::selectUserByLogin
     * @test
     */
    public function testGetUserByEmail($email, $isEmpty, $userID, \DAO\UserEntity $user)
    {
        $userData = $user->selectUserByLogin($email);
        if ($isEmpty) {
            $this->assertEmpty($userData);
        } else {
            $this->assertEquals($userID, $userData->getData('userID'));
        }
    }

    function provideUserEmail()
    {
        $test1 = array('email' => 'andy', 'isEmpty' => true, 'userID' => 1);
        $test2 = array('email' => 'test@test.ru', 'isEmpty' => false, 'userID' => 2);
        return array($test1, $test2);
    }

    /**
     * @depends      testCreateUserEntity
     * @covers       \DAO\UserEntity::addUser
     * @test
     */
    function addUserTest()
    {
        $data = array();
        $data['firstName'] = 'name';
        $data['lastName'] = 'lname';
        $data['patronymic'] = 'patronymic';
        $data['position'] = 'position';
        $data['phoneNumber'] = '1111111111';
        $data['email'] = 'andy-92@mail.ru';
        $data['password'] = md5('andyandy');
        $data['userRoleRusName'] = 'ADMIN';
        $data['pointName'] = 1;
        $this->assertTrue(\DAO\UserEntity::getInstance()->addUser(new \DAO\UserData($data)));
    }

    /**
     * @depends      testCreateUserEntity
     * @covers       \DAO\UserEntity::updateUser
     * @covers       \DAO\UserEntity::selectUserByID
     * @test
     */
    function updateUserTest()
    {
        $data = array();
        $data['firstName'] = 'name';
        $data['lastName'] = 'lname';
        $data['patronymic'] = 'patronymic';
        $data['position'] = 'position';
        $data['phoneNumber'] = '1111111111';
        $data['email'] = 'andy-92@mail.ru';
        $data['salt'] = substr(md5('1'), 0, 16);
        $data['passAndSalt'] = md5(md5('test@test.ru') . $data['salt']);
        $data['userRoleRusName'] = 'ADMIN';
        $data['pointName'] = 2;
        $data = new \DAO\UserData($data);
        $this->assertTrue(\DAO\UserEntity::getInstance()->updateUser($data, 1));
        $this->assertEquals($data, \DAO\UserEntity::getInstance()->selectUserByID(1));
    }

    /**
     * @depends      testCreateUserEntity
     * @covers       \DAO\UserEntity::deleteUser
     * @test
     */
    function removeUserTest()
    {
        $this->assertTrue(\DAO\UserEntity::getInstance()->deleteUser('0'));
        $this->assertTrue(\DAO\UserEntity::getInstance()->deleteUser('1'));
        $this->assertNull(\DAO\UserEntity::getInstance()->selectUserByID(1));
        $this->assertFalse(\DAO\UserEntity::getInstance()->deleteUser('1'));
    }

    /**
     * @dgepends      testCreateUserEntity
     * @test
     */
    function getAllUsersTest()
    {
        $dataArray = \DAO\UserEntity::getInstance()->selectUsers(0, 20);
        $tc = intval($dataArray['totalCount'][0]['totalCount']);
        $this->assertEquals(6,$tc);
        $cf = intval($dataArray['totalFiltered'][0]['totalFiltered']);
        $this->assertEquals(6,$cf);
        $data = $dataArray['users'];   // total data array
        $this->assertNotNull($data);
    }
}