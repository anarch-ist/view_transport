<?php
include_once __DIR__ . '/../dao/userDao/User.php';
include_once __DIR__ . '/../dao/invoicesForUser/InvoicesForUser.php';
include_once __DIR__ . '/../sessionAndCookieWork/SessionAndCookieWork.php';

use SessionAndCookieWork\SessionAndCookieWork as SessionAndCookieWork;
use SessionAndCookieWork\ISessionAndCookieWork as ISessionAndCookieWork;


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
        if ($authVariant === 'check') {
            if (!$this->checkAuth()) {
                throw new AuthException('Проверка не пройдена');
            }
        } else if ($authVariant === 'auth') {
            if (!(isset($_POST['login']) && isset($_POST['password']) && $this->authorize($_POST['login'], $_POST['password']))) {
                $login = '';
                if (isset($_POST['login'])) $login = $_POST['login'];
                $password = '';
                if (isset($_POST['password'])) $password = $_POST['password'];
                $string = 'login: '.$login.' | password: '.$password;
                throw new AuthException('Ошибка авторизации. '.$string);
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
            return (!is_null($this->user) && $this->user->getData('passAndSalt') === md5($_SESSION['md5'] . $this->user->getData('salt')));
        } else {
            $this->sessCookieWork->closeSession();
            return false;
        }
    }

    private function authorize($email, $md5)
    {
        $this->user = \DAO\UserEntity::getInstance()->selectUserByEmail($email);
        if (!is_null($this->user) && $this->user->getData('passAndSalt') === md5($md5 . $this->user->getData('salt'))) {
            $this->sessCookieWork->startSession();
            $this->sessCookieWork->setSessionParameter('UserID',$this->user->getData('userID'));
            $this->sessCookieWork->setSessionParameter('md5',$md5);
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

    public function getInvoicesForUser()
    {
        return \DAO\InvoicesForUserEntity::getInstance();
    }

    public function getInvoiceEntity()
    {
        include_once __DIR__ . '/../dao/invoiceDao/Invoice.php';
        return \DAO\InvoiceEntity::getInstance();
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
}