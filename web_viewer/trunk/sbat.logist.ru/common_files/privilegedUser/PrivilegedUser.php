<?php
include_once __DIR__.'/../dao/userDao/User.php';
//include_once __DIR__.'/../dao/pointDao/Point.php';
include_once __DIR__.'/../dao/invoicesForUser/InvoicesForUser.php';
include_once __DIR__.'/../dao/invoiceDao/Invoice.php';
include_once __DIR__.'/../dao/DAO.php';

use DAO\InvoiceEntity as InvoiceEntity;
use DAO\InvoicesForUserEntity as InvoicesForUserEntity;
use DAO\UserEntity as UserEntity;
use DAO\UserData as UserData;

//use DAO\PointEntity as PointEntity;

abstract class AuthUser
{
    private $user;

    protected function __construct()
    {
        $this->user = new UserData(array());
        if (!$this->isValid()) {
            throw new AuthException('Ошибка авторизации');
        }
    }

    public function isValid()
    {
        if (isset($_COOKIE['UserID']) && isset($_COOKIE['md5']) && $this->checkAuth($_COOKIE['UserID'], $_COOKIE['md5'])) {
            return true;
        }
        //TODO: change GET to POST
        if (isset($_POST['login']) && isset($_POST['password']) && $this->authorize($_POST['login'], $_POST['password'])) {
            return true;
        }
        return false;
    }

    /**
     * @param $userID - for user who wants authorize. field from client
     * @param $md5 - field from client
     * @return boolean
     */
    private function checkAuth($userID, $md5)
    {
//        if (!isset($userID) || !isset($md5)) return false;
        $this->user = UserEntity::getInstance()->selectUserByID($userID);
        return (!is_null($this->user) && $this->user->getData('passMD5') === $md5);
    }

    private function authorize($email, $md5)
    {
//        if (!isset($email) || !isset($md5)) return false;
        $this->user = UserEntity::getInstance()->selectUserByEmail($email);
        if (!is_null($this->user) && $this->user->getData('passMD5') === $md5) {
            setcookie('UserID', $this->user->getData('userID'), 0, '/');
            setcookie('md5', $this->user->getData('passMD5'), 0, '/');
            return true;
        }
        return false;
    }

    public function getUserInfo()
    {
        return $this->user;
    }
}

class PrivilegedUser extends AuthUser
{
    private static $instance;
    private $permissions;

    protected function __construct()
    {
        parent::__construct();
    }

    public static function getInstance()
    {
        if (is_null(self::$instance)) {
            self::$instance = new PrivilegedUser();
        }
        return self::$instance;
    }

    public function getUserEntity()
    {
        return UserEntity::getInstance();
    }

    public function getInvoicesForUser()
    {
        return InvoicesForUserEntity::getInstance();
    }

    public function getInvoiceEntity()
    {
        return InvoiceEntity::getInstance();
    }
}