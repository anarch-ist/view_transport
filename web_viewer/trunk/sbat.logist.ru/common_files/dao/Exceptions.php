<?php

class DataEntityException extends Exception
{
    function __construct($string)
    {
        parent::__construct('Обращение к несуществующему полю');
        writeInErrorlog("DataEntityException: " . $string);
    }
}

class MysqlException extends Exception
{
    public function __construct($str)
    {
        parent::__construct('Ошибка базы данных');
        writeInErrorlog("MysqlException: " . $str);
    }
}

class AuthException extends Exception
{
    public function __construct($str)
    {
        parent::__construct('Ошибка авторизации - неверные имя пользователя или пароль');
        if ($str !== 'Проверка не пройдена') {
            writeInErrorlog("AuthException: " . $str);
        }
    }
}

class DataTransferException extends Exception
{
    public function __construct($str, $page)
    {
        parent::__construct($str);
        writeInErrorlog("DataTransferException: " . $str.' на странице '.$page);
    }
}

function writeInErrorlog($string)
{
    $file = fopen(__DIR__ . '/logs/error.log', 'a');
    fprintf($file, "%s\n", $string);
    fclose($file);
}