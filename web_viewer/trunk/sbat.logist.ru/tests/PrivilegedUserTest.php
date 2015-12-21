<?php
ob_start();
include_once __DIR__ . '/../common_files/privilegedUser/PrivilegedUser.php';

class PrivilegedUserTest extends PHPUnit_Framework_TestCase
{

    /**
     * @expectedException AuthException
     */
    function testGetInstanceWithException()
    {
        $pUser = PrivilegedUser::getInstance();
    }
    function testGetInstance()
    {
        $_POST['email'] = 'test@test.ru';
        $_POST['md5'] = md5('test');
        $pUser = PrivilegedUser::getInstance();
    }
}
 