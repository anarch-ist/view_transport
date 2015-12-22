<?php

namespace SessionAndCookieWork;

/**
 * Interface ISessionAndCookieWork
 *
 * реализации данного интерфейса используются в
 * @package SessionAndCookieWork
 */
interface ISessionAndCookieWork
{
    /**
     * @param $name
     * @param $value
     * @return void
     */
    function setCookie($name, $value);

    /**
     * @param $name
     * @return string|false The cookie's text or false if not exists
     */
    function getCookie($name);

    /**
     * @param $name
     * @return void
     */
    function removeCookie($name);

    /**
     * @return true, false
     */
    function startSession();

    /**
     * @param $salt
     * @return boolean
     */
    function sessionIsValid($salt);

    /**
     * @param $salt
     * @return void
     */
    function encodeSessionID($salt);

    /**
     * @return mixed
     */
    function closeSession();

    /**
     * @param $name
     * @return mixed
     */
    function getSessionParameter($name);

    /**
     * @param $name
     * @param $value
     * @return mixed
     */
    function setSessionParameter($name, $value);

    /**
     * @param $name
     * @return mixed
     */
    function unsetSessionParameter($name);
}