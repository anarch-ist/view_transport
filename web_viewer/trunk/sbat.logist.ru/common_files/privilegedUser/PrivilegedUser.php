<?php
include_once '\..\dao\userDao\UserDAOEntity.php';
include_once '\..\dao\pointDao\PointDAO.php';
include_once '\..\dao\invoicesForUser\InvoicesForUserDAO.php';
include_once '\..\dao\DAO.php';
use DAO\DAO as DAO;
use DAO\UserDAO as UserDAO;
use DAO\PointDAO as PointDAO;
use DAO\InvoicesForUserDAO as InvoicesForUserDAO;
class AuthUser extends DAO {
    private static $instance;
    private $user;
    protected function __construct() {
    }

    /**
     * @param $userID - for user who wants authorize. field from client
     * @param $md5 - field from client
     */
    private function checkAuth($userID, $md5) {
        if (!isset($userID) || !isset($md5)) return false;
        $this->user = UserDAO::getInstance()->selectUserByID($userID);
        return (!is_null($this->user) && $this->user->getPassMD5() === $md5);
    }
    private function authorize($login, $md5) {
        if (!isset($login) || !isset($md5)) return false;
        $this->user = UserDAO::getInstance()->selectUserByEmail($login);
        return (!is_null($this->user) && $this->user->getPassMD5() === $md5);
    }
    public function isValid() {
        $isAuth = false;
        if (isset($_COOKIE['UserID'])&& isset($_COOKIE['md5'])) {
            $isAuth = $this->checkAuth($_COOKIE['UserID'],$_COOKIE['md5']);
        }
        //TODO: change GET to POST
        if (isset($_GET['login'])&& isset($_GET['md5'])) {
            $isAuth = $isAuth || $this->authorize($_GET['login'],$_GET['md5']);
        }
        return $isAuth;
    }
    public function getUserInfo() {
        return $this->user;
    }
    public static function getInstance() {
        if (is_null(self::$instance)) {
            self::$instance = new AuthUser();
        }
        return self::$instance;
    }
}
class PrivilegedUser extends DAO {
    private static $instance;
    private $permissions;
    private $user;
    protected function __construct($user) {
        $this->user = $user;
    }

    public static function getInstance() {
        if (is_null(self::$instance)) {
            if(!AuthUser::getInstance()->isValid()) {
                self::$instance = null;
                throw new Exception('Ошибка авторизации');
            }
            self::$instance = new PrivilegedUser(AuthUser::getInstance()->getUserInfo());
        }
        return self::$instance;
    }
    public function getUserEntity() {
        return UserDAO::getInstance();
    }
    public function getInvoicesForUser() {
        return InvoicesForUserDAO::getInstance();
    }
}