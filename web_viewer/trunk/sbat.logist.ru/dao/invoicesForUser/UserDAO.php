<?php
require_once('IUserDAO.php');
require_once('DAO.class.php');

class UserDAO extends DAO implements IUserDAO
{
    private static $instance;

    protected function __construct()
    {
        parent::__construct();
        self::$instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$instance)) return new UserDAO();
        return self::$instance;
    }

    function selectUsers()
    {
        $array = parent::select(new EntitySelectAllUsers());
        $users = array();
        for ($i = 0; $i < count($array); $i++) {
            $users[$i] = new User($array[$i]);
        }
        return $users;
    }

    function selectUserByID($id)
    {
        $array = parent::select(new UserSelectEntityByID($id));
        return new User($array[0]);
    }

    function selectUserByEmail($email)
    {
        $array = parent::select(new UserSelectEntityByLogin($email));
        return new User($array[0]);
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

class User extends EntityDataObject
{
    private $userID, $firstName, $lastName, $patronymic, $login, $passMD5, $phoneNumber, $email, $userRoleID, $pointID;

    /**
     * @return mixed
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * @param mixed $email
     */
    public function setEmail($email)
    {
        $this->email = $this->prepareSafeString($email);
    }

    /**
     * @return mixed
     */
    public function getFirstName()
    {
        return $this->firstName;
    }

    /**
     * @param mixed $firstName
     */
    public function setFirstName($firstName)
    {
        $this->firstName = $this->prepareSafeString($firstName);
    }

    /**
     * @return mixed
     */
    public function getLastName()
    {
        return $this->lastName;
    }

    /**
     * @param mixed $lastName
     */
    public function setLastName($lastName)
    {
        $this->lastName = $this->prepareSafeString($lastName);
    }

    /**
     * @return mixed
     */
    public function getLogin()
    {
        return $this->login;
    }

    /**
     * @param mixed $login
     */
    public function setLogin($login)
    {
        $this->login = $this->prepareSafeString($login);
    }

    /**
     * @return mixed
     */
    public function getPassMD5()
    {
        return $this->passMD5;
    }

    /**
     * @param mixed $passMD5
     */
    public function setPassMD5($passMD5)
    {
        $this->passMD5 = $this->prepareSafeString($passMD5);
    }

    /**
     * @return mixed
     */
    public function getPatronymic()
    {
        return $this->patronymic;
    }

    /**
     * @param mixed $patronymic
     */
    public function setPatronymic($patronymic)
    {
        $this->patronymic = $this->prepareSafeString($patronymic);
    }

    /**
     * @return mixed
     */
    public function getPhoneNumber()
    {
        return $this->phoneNumber;
    }

    /**
     * @param mixed $phoneNumber
     */
    public function setPhoneNumber($phoneNumber)
    {
        $this->phoneNumber = $this->prepareSafeString($phoneNumber);
    }

    /**
     * @return mixed
     */
    public function getPointID()
    {
        return $this->pointID;
    }

    /**
     * @param mixed $pointID
     */
    public function setPointID($pointID)
    {
        $this->pointID = $this->prepareSafeString($pointID);
    }

    /**
     * @return mixed
     */
    public function getUserRoleID()
    {
        return $this->userRoleID;
    }

    /**
     * @param mixed $userRoleID
     */
    public function setUserRoleID($userRoleID)
    {
        $this->userRoleID = $this->prepareSafeString($userRoleID);
    }

    /**
     * @return mixed
     */
    public function getUserID()
    {
        return $this->userID;
    }

    function __construct($array = null)
    {
        if (!is_null($array)) {
            $this->email = $array['email'];
            $this->firstName = $array['firstName'];
            $this->lastName = $array['lastName'];
            $this->login = $array['login'];
            $this->passMD5 = $array['passMD5'];
            $this->patronymic = $array['patronymic'];
            $this->phoneNumber = $array['phoneNumber'];
            $this->pointID = $array['pointID'];
            $this->userID = $array['userID'];
            $this->userRoleID = $array['userRoleID'];
        } else {
            $this->email = '';
            $this->firstName = '';
            $this->lastName = '';
            $this->login = '';
            $this->passMD5 = '';
            $this->patronymic = '';
            $this->phoneNumber = '';
            $this->pointID = '';
            $this->userID = '';
            $this->userRoleID = '';
        }
    }

    function __toString()
    {
        $string = '';
        foreach ($this as $param) {
            $string .= $param . PHP_EOL;
        }
        return $string;
    }
}

class EntitySelectAllUsers extends EntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "select * from `users`;";
    }
}

// TODO: убрать из бд логин и взамен него использовать эл. почту
class UserSelectEntityByLogin extends EntitySelect
{
    private $email;

    function __construct($email)
    {
        $this->email = $this->prepareSafeString($email);
    }

    function getSelectQuery()
    {
        return "select * from `users` where `email` = '$this->email'";
    }
}

class UserSelectEntityByID extends EntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = $this->prepareSafeString($id);
    }

    function getSelectQuery()
    {
        return "select * from `users` where `userID` = '$this->id'";
    }
}