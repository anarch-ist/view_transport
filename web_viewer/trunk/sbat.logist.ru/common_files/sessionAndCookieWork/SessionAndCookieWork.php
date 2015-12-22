<?php

namespace SessionAndCookieWork;
include_once __DIR__ . '/ISessionAndCookieWork.php';


class SessionAndCookieWork implements ISessionAndCookieWork
{

    function getCookie($name)
    {
        if (isset($_COOKIE[$name])) {
            return $_COOKIE[$name];
        }
        return false;
    }

    function removeCookie($name)
    {
        setcookie($name, null, -1, '/');
    }

    function startSession()
    {
        return session_start();
    }

    function closeSession()
    {
        foreach( $_SESSION as $key ) {
            $this->unsetSessionParameter($key);
        }
        $this->removeCookie('SESSION_CHECK_STRING');
        $this->removeCookie('PHPSESSID');
        return session_destroy();
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
        return $this->getCookie('SESSION_CHECK_STRING') === md5( session_id() . $salt);
    }

    function encodeSessionID($salt)
    {
        $this->setCookie('SESSION_CHECK_STRING', md5( session_id() . $salt));
    }

    function setCookie($name, $value)
    {
        setcookie($name, $value, 0, '/');
    }

    function unsetSessionParameter($name)
    {
        unset($_SESSION[$name]);
    }
}
