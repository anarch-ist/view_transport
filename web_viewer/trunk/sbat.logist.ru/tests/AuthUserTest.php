<?php
include_once __DIR__ . '/../common_files/privilegedUser/PrivilegedUser.php';

class UserTest extends AuthUser
{
    function __construct($state, $sess)
    {
        parent::__construct($state, $sess);
        $sess->sessStarted = false;
    }
}

class SessionAndCookieWorkTest implements \SessionAndCookieWork\ISessionAndCookieWork
{
    public $cookie;
    public $session;
    public $sessStarted;
    public $sessId;

    function __construct()
    {
        $this->cookie = array();
        $this->session = array();
        $this->sessStarted = false;
        $this->sessId = null;
    }

    function startSession()
    {
        if (!$this->sessStarted) {
            $this->sessStarted = true;
            if (!$this->getCookie('PHPSESSID')) {
                $this->sessId = md5(rand(0, 100));
                $this->setCookie('PHPSESSID', $this->sessId);
            } else {
                $this->sessId = $this->getCookie('PHPSESSID');
            }
            return true;
        }
        throw new Exception('session already started');
    }

    /**
     * @param $name
     * @return string|false The cookie's text or false if not exists
     */
    function getCookie($name)
    {
        if (isset($this->cookie[$name])) {
            return $this->cookie[$name];
        }
        return false;
    }

    function setCookie($name, $value)
    {
        $this->cookie[$name] = $value;
    }

    function closeSession()
    {
        if ($this->sessStarted) {
            foreach ($this->session as $key) {
                $this->unsetSessionParameter($key);
            }
            $this->removeCookie('SESSION_CHECK_STRING');
            $this->removeCookie('PHPSESSID');
            $this->sessStarted = false;
            $this->sessId = null;
            return true;
        }
        throw new Exception('session already closed');
    }

    function unsetSessionParameter($name)
    {
        unset($this->session[$name]);
    }

    function removeCookie($name)
    {
        unset($this->cookie[$name]);
    }

    function getSessionParameter($name)
    {
        if (isset($_SESSION[$name])) {
            return $_SESSION[$name];
        }
        return false;
    }

    function setSessionParameter($name, $value)
    {
        $_SESSION[$name] = $value;
    }

    function sessionIsValid($salt)
    {
        return $this->getCookie('SESSION_CHECK_STRING') === md5($this->sessId . $salt);
    }

    function encodeSessionID($salt)
    {
        $this->setCookie('SESSION_CHECK_STRING', md5($this->sessId . $salt));
    }
}

class AuthUserTest extends PHPUnit_Framework_TestCase
{

    /**
     * @expectedException AuthException
     */
    function testGetInstanceWithException()
    {
        new UserTest('auth', new SessionAndCookieWorkTest());
    }

    function testIsValid()
    {
        $_POST['login'] = 'test@test.ru';
        $_POST['password'] = md5('test');
        $sess = new SessionAndCookieWorkTest();
        $user = new UserTest('auth', $sess);
        $this->assertEquals($user->getUserInfo()->getData('email'), $_POST['login']);
        $sessId = $sess->sessId;
        $this->assertNotNull($sessId);
        $user = new UserTest('check', $sess);
        $this->assertEquals($user->getUserInfo()->getData('email'), $_POST['login']);
        $this->assertEquals($sessId, $sess->sessId);

    }

    /**
     * @dataProvider providerAuth
     */
    function testAuth($login, $password, SessionAndCookieWorkTest $sess, $isRight)
    {
        $_POST['login'] = $login;
        $_POST['password'] = md5($password);
        $sessId = '';
        try {
            $user = new UserTest('auth', $sess);
            $sessId = $sess->sessId;
            $this->assertEquals($user->getUserInfo()->getData('email'), $_POST['login']);
            $this->assertNotNull($sessId);
        } catch (AuthException $ex) {
            $this->assertFalse($isRight);
        }
        try {
            $user = new UserTest('check', $sess);
            $this->assertEquals($user->getUserInfo()->getData('userID'), $sess->getSessionParameter('UserID'));
            $this->assertEquals($sessId, $sess->sessId);
        } catch (AuthException $ex) {
            $this->assertFalse($isRight);
        }

    }

    function providerAuth()
    {
        return array(
            array(
                'login' => 'test@test.ru',
                'password' => 'test',
                'sess' => new SessionAndCookieWorkTest(),
                'right' => true
            ),
            array(
                'login' => '',
                'password' => '',
                'sess' => new SessionAndCookieWorkTest(),
                'right' => false
            ),
            array(
                'login' => 'test@test.ru',
                'password' => 'test1',
                'sess' => new SessionAndCookieWorkTest(),
                'right' => false
            )
        );
    }
}
