<?php
include_once __DIR__.'/../dao/userDao/User.php';
include_once __DIR__.'/../dao/pointDao/Point.php';
include_once __DIR__ . '/../dao/invoicesForUser/InvoicesForUser.php';
include_once __DIR__.'/../dao/invoiceDao/Invoice.php';
include_once __DIR__.'/../dao/DAO.php';

use DAO\InvoiceEntity as InvoiceEntity;
use DAO\InvoicesForUserEntity as InvoicesForUserEntity;
use DAO\UserEntity as UserEntity;
use DAO\PointEntity as PointEntity;
use DAO\UserData as UserData;

//use DAO\PointEntity as PointEntity;

abstract class AuthUser
{
    private $user;

    protected function __construct($authVariant)
    {
        $this->user = new UserData(array());
        if (!$this->isValid($authVariant)) {
            throw new AuthException('Ошибка авторизации');
        }
    }

    /**
     * @param $authVariant
     * @throws AuthException
     * @return boolean
     */
    public function isValid($authVariant)
    {
        if ($authVariant === 'check') {
            return ($this->checkAuth());
        } else if ($authVariant === 'auth') {
            return (isset($_POST['login']) && isset($_POST['password']) && $this->authorize($_POST['login'], $_POST['password']));
        } else {
            throw new AuthException('Передан неверный параметр: '.$authVariant);
        }
    }

    /**
     * @param $userID - for user who wants authorize. field from client
     * @param $md5 - field from client
     * @return boolean
     */
    private function checkAuth()
    {
        session_start();
        if (!isset($_COOKIE['SESSION_CHECK_STRING']) || $_COOKIE['SESSION_CHECK_STRING'] !== md5($_SESSION['UserID'].session_id().$_SESSION['md5'])) {
            setcookie('SESSION_CHECK_STRING');
            session_destroy();
            return false;
        }
        $this->user = UserEntity::getInstance()->selectUserByID($_SESSION['UserID']);
        return (!is_null($this->user) && $this->user->getData('passMD5') === $_SESSION['md5']);
    }

    private function authorize($email, $md5)
    {
        $this->user = UserEntity::getInstance()->selectUserByEmail($email);
        if (!is_null($this->user) && $this->user->getData('passMD5') === $md5) {
            session_start();
            $_SESSION['UserID'] = $this->user->getData('userID');
            $_SESSION['md5'] = $this->user->getData('passMD5');
            setcookie('SESSION_CHECK_STRING',md5($_SESSION['UserID'].session_id().$_SESSION['md5']),0,'/');
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
    //private $permissions;

    protected function __construct($authVariant)
    {
        parent::__construct($authVariant);
    }

    public static function getInstance()
    {

        $authVariant = 'check';
        if (func_num_args() === 1) {
            $authVariant = func_get_arg(0);
        }
        if (is_null(self::$instance)) {
            self::$instance = new PrivilegedUser($authVariant);
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
    public function getPointEntity()
    {
        return PointEntity::getInstance();
    }
}