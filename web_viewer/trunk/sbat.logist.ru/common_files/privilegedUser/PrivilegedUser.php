<?php
include_once __DIR__ . '/../dao/userDao/User.php';
include_once __DIR__ . '/../dao/requestsForUser/RequestsForUser.php';
include_once __DIR__ . '/../sessionAndCookieWork/SessionAndCookieWork.php';

use SessionAndCookieWork\ISessionAndCookieWork as ISessionAndCookieWork;
use SessionAndCookieWork\SessionAndCookieWork as SessionAndCookieWork;


abstract class AuthUser
{
    private $user;
    private $sessCookieWork;

    protected function __construct($authVariant, ISessionAndCookieWork $sessionAndCookieWork)
    {
        $this->sessCookieWork = $sessionAndCookieWork;
        $this->user = new \DAO\UserData(array());
        $this->isValid($authVariant);
    }

    /**
     * @param $authVariant
     * @throws AuthException
     * @return void
     */
    public function isValid($authVariant)
    {
        if ($authVariant === 'auth') {
            if (!(isset($_POST['login']) && isset($_POST['password']) && $this->authorize($_POST['login'], $_POST['password']))) {
                $login = '';
                if (isset($_POST['login'])) $login = $_POST['login'];
                $string = 'login: ' . $login;
                throw new AuthException('Ошибка авторизации. ' . $string);
            }
        } else if ($authVariant === 'check') {
            if (!$this->checkAuth()) {
            if(isset($_GET['clientId']) & isset($_GET['md5'])){
                $this->authorize($_GET['clientId'],$_GET['md5']);
            } else {
                throw new AuthException('Проверка не пройдена');
            }
        }
        } else {
            throw new AuthException('Передан неверный параметр: ' . $authVariant);
        }
    }

    /**
     * @param $userID - for user who wants authorize. field from client
     * @param $md5 - field from client
     * @return boolean
     */
    private function checkAuth()
    {
        $this->sessCookieWork->startSession();
        if ($this->sessCookieWork->sessionIsValid($this->sessCookieWork->getSessionParameter('UserID'))) {
            $this->user = \DAO\UserEntity::getInstance()->selectUserByID($this->sessCookieWork->getSessionParameter('UserID'));
            return (!is_null($this->user) && $this->user->getData('passAndSalt') === md5($this->sessCookieWork->getSessionParameter('md5') . $this->user->getData('salt')));
        } else {
            $this->sessCookieWork->closeSession();
            return false;
        }
    }

    private function authorize($login, $md5)
    {
        $this->user = \DAO\UserEntity::getInstance()->selectUserByLogin($login);
        if (!is_null($this->user) && $this->user->getData('passAndSalt') === md5($md5 . $this->user->getData('salt'))) {
            $this->sessCookieWork->startSession();
            $this->sessCookieWork->setSessionParameter('UserID', $this->user->getData('userID'));
            $this->sessCookieWork->setSessionParameter('md5', $md5);
            $this->sessCookieWork->encodeSessionID($this->user->getData('userID'));
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
        parent::__construct($authVariant, new SessionAndCookieWork());
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
        return \DAO\UserEntity::getInstance();
    }

    public function getDaoEntity()
    {
        return \DAO\DAO::getInstance();
    }

    public function getRequestsForUser()
    {
        return \DAO\RequestsForUserEntity::getInstance();
    }

    public function getRequestEntity()
    {
        include_once __DIR__ . '/../dao/requestDao/Request.php';
        return \DAO\RequestEntity::getInstance();
    }

    public function getPointEntity()
    {
        include_once __DIR__ . '/../dao/pointDao/Point.php';
        return \DAO\PointEntity::getInstance();
    }

    public function getRouteEntity()
    {
        include_once __DIR__ . '/../dao/routeDao/Route.php';
        return \DAO\RouteEntity::getInstance();
    }

    public function getRouteAndRoutePointsEntity()
    {
        include_once __DIR__ . '/../dao/routeAndRoutePoints/RouteAndRoutePoints.php';
        return \DAO\RouteAndRoutePoints::getInstance();
    }

    public function getRoutePointEntity()
    {
        include_once __DIR__ . '/../dao/routePointDao/RoutePoint.php';
        return \DAO\RoutePointEntity::getInstance();
    }

    public function getClientEntity()
    {
        include_once __DIR__ . '/../dao/clientDao/Client.php';
        return \DAO\ClientEntity::getInstance();
    }
}