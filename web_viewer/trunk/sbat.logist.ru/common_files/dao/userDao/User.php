<?php
namespace DAO;
include_once 'IUser.php';
include_once '/../DAO.php';


class UserEntity implements IUserEntity
{
    private static $_instance;
    private $_DAO;

    protected function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new UserEntity();
        return self::$_instance;
    }

    function selectUsers()
    {
        $count = 25;
        $start = 0;
        switch (func_num_args()) {
            case 2:
                $start = func_get_arg(1);
                $count = func_get_arg(0);
                break;
            case 1:
                $count = func_get_arg(0);
                break;
        }
        $array = $this->_DAO->select(new SelectAllUsers($start, $count));
        $users = array();
        for ($i = 0; $i < count($array); $i++) {
            $users[$i] = new UserData($array[$i]);
        }
        return $users;
    }

    function selectUserByID($id)
    {
        $array = $this->_DAO->select(new SelectUserByID($id));
        if (!count($array)) {
            return null;
        }
        return new UserData($array[0]);
    }

    function selectUserByEmail($email)
    {
        $array = $this->_DAO->select(new SelectUserByEmail($email));
        if (!count($array)) {
            return null;
        }
        return new UserData($array[0]);
    }

    function updateUser($newUser)
    {
        // TODO: Implement updateUser() method.
    }

    function deleteUser($user)
    {
        // TODO: Implement deleteUser() method.
    }

    function addUser($user)
    {

        // TODO: Implement addUser() method.
    }
}

class UserData implements IEntityData
{
    private $array;

    function __construct($array)
    {
        $this->array = $array;
    }

    public function getData($index)
    {
        if (!isset($this->array[$index])) {
            throw new \DataEntityException('Field doesn`t exist: ' . $index . ' - in ' . get_class($this));
        } else {
            return $this->array[$index];
        }
    }

    public function toArray()
    {
        return $this->array;
    }
}

class SelectAllUsers implements IEntitySelect
{
    private $start;
    private $count;

    function __construct($start, $count)
    {
        $this->start = $start;
        $this->count = $count;
    }

    function getSelectQuery()
    {
        return "SELECT * FROM `users` LIMIT $this->start, $this->count;";
    }
}

// TODO: убрать из бд логин и взамен него использовать эл. почту
class SelectUserByEmail implements IEntitySelect
{
    private $email;

    function __construct($email)
    {
        $this->email = DAO::getInstance()->checkString($email);
    }

    function getSelectQuery()
    {
        return "select * from `users` where `email` = '$this->email'";
    }
}

class SelectUserByID implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "select * from `users` where `userID` = '$this->id'";
    }
}

class UserUpdateEntity implements IEntityUpdate
{
    private $obj;

    function __construct($obj)
    {
        $this->$obj = $obj;
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "insert into `users` values(";
        $isFirst = true;
        foreach ($this->obj as $elem) {
            if ($isFirst) {
                $isFirst = false;
                $query .= "0";
            } else {
                $query .= ", '$elem'";
            }
        }
        $query .= ")";
        return $query;
    }
}