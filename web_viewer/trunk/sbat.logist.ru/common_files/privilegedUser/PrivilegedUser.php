<?php
include_once '\..\dao\userDao\User.php';
//include_once '\..\dao\pointDao\Point.php';
include_once '\..\dao\invoicesForUser\InvoicesForUser.php';
include_once '\..\dao\DAO.php';
use DAO\UserEntity as UserEntity;
//use DAO\PointEntity as PointEntity;
use DAO\InvoicesForUserEntity as InvoicesForUserEntity;
abstract class AuthUser {
    private $user;
    protected function __construct() {
        if(!$this->isValid()) {
            throw new AuthException('Ошибка авторизации');
        }
    }

    /**
     * @param $userID - for user who wants authorize. field from client
     * @param $md5 - field from client
     * @return boolean
     */
    private function checkAuth($userID, $md5) {
//        if (!isset($userID) || !isset($md5)) return false;
        $this->user = UserEntity::getInstance()->selectUserByID($userID);
        return (!is_null($this->user) && $this->user->getData('passMD5') === $md5);
    }
    private function authorize($email, $md5) {
//        if (!isset($email) || !isset($md5)) return false;
        $this->user = UserEntity::getInstance()->selectUserByEmail($email);
        if (!is_null($this->user) && $this->user->getData('passMD5') === $md5) {
            setcookie('UserID',$this->user->getData('userID'),0,'/');
            setcookie('md5',$this->user->getData('passMD5'),0,'/');
            return true;
        }
        return false;
    }
    public function isValid() {
        if (isset($_COOKIE['UserID'])&& isset($_COOKIE['md5']) && $this->checkAuth($_COOKIE['UserID'],$_COOKIE['md5'])) {
            return true;
        }
        //TODO: change GET to POST
        if (isset($_GET['login'])&& isset($_GET['md5']) && $this->authorize($_GET['login'],$_GET['md5'])) {
            return true;
        }
        return false;
    }
    public function getUserInfo() {
        return $this->user;
    }
}
class PrivilegedUser extends AuthUser {
    private static $instance;
    private $permissions;
    protected function __construct() {
        parent::__construct();
    }

    public static function getInstance() {
        if (is_null(self::$instance)) {
            self::$instance = new PrivilegedUser();
        }
        return self::$instance;
    }
    public function getUserEntity() {
        return UserEntity::getInstance();
    }
    public function getInvoicesForUser() {
        return InvoicesForUserEntity::getInstance();
    }
}