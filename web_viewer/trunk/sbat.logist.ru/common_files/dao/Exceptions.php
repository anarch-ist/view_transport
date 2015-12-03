<?php

class DataEntityException extends Exception
{
    function __construct($string)
    {
        parent::__construct('Обращение к несуществующему полю');
    }
}

class MysqlException extends Exception
{
    public function __construct($str)
    {
        parent::__construct('Ошибка базы данных');
    }
}

class AuthException extends Exception
{
    public function __construct($str)
    {
        parent::__construct('Ошибка авторизации - неверные имя пользователя или пароль');
    }
}

class NoStatusException extends Exception
{
    public function __construct($str)
    {
        parent::__construct('Не задан параметр "статус"');
    }
}