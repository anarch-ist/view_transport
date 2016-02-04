<?php
namespace DAO;
include_once __DIR__ . '/IUser.php';
include_once __DIR__ . '/../DAO.php';


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

    function getUserRole($userID)
    {
        $userRoleArray = $this->_DAO->select(new SelectUserRole($userID));
        if (empty($userRoleArray)) {
            return null;
        }
        return $userRoleArray[0]['userRoleRusName'];
    }

    function selectUsers()
    {
        $count = 20;
        $start = 0;
        switch (func_num_args()) {
            case 2:
                $start = func_get_arg(0);
                $count = func_get_arg(1);
                break;
            case 1:
                $start = func_get_arg(0);
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

    function updateUser(UserData $newUser)
    {
        // TODO: Implement updateUser() method.
    }

    function deleteUser(UserData $user)
    {
        // TODO: Implement deleteUser() method.
    }

    function addUser(UserData $user)
    {

        // TODO: Implement addUser() method.
    }
    function getUserRoles() {
        return $this->_DAO->select(new SelectUserRoles());
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

class SelectUserRole implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "select `user_roles`.* from `user_roles`, `users` where `userID` = '$this->id' AND `user_roles`.userRoleID = `users`.userRoleID";
    }
}

class SelectUserRoles implements IEntitySelect
{

    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "select * from `user_roles`;";
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
        $query = '';
        $isFirst = true;
        foreach ($this->obj as $elem) {
            if ($isFirst) {
                $isFirst = false;
                $query .= "0";
            } else {
                $query .= ", '$elem'";
            }
        }
        $query = "insert into `users` values($query)";
        return $query;
    }
}