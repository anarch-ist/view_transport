<?php
namespace DAO;
include_once __DIR__ . '/IUser.php';
include_once __DIR__ . '/../DAO.php';


class UserEntity implements IUserEntity
{
    private static $_instance;
    private $_DAO;

    protected function __construct(IDAO $dao)
    {
        $this->_DAO = $dao;
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new UserEntity(DAO::getInstance());
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

    function selectUsers($start = 0, $count = 20)
    {
//        $count = 20;
//        $start = 0;
//        switch (func_num_args()) {
//            case 2:
//                $start = func_get_arg(0);
//                $count = func_get_arg(1);
//                break;
//            case 1:
//                $start = func_get_arg(0);
//                break;
//        }
        $array = $this->_DAO->select(new EntitySelectAllUsers($start, $count));
        return $array;
//        $users = array();
//        for ($i = 0; $i < count($array); $i++) {
//            $users[$i] = new UserData($array[$i]);
//        }
//        return $users;
    }

    function selectUserByID($id)
    {
        $array = $this->_DAO->select(new SelectUserByID($id));
        if (!count($array)) {
            return null;
        }
        return new UserData($array[0]);
    }

    function getUsersTotalCount()
    {
        return $this->_DAO->select(new SelectTotalUserCount())[0]['count'];
    }

    function selectUserByEmail($email)
    {
        $array = $this->_DAO->select(new SelectUserByEmail($email));
        if (!count($array)) {
            return null;
        }
        return new UserData($array[0]);
    }

    function updateUser(UserData $newUser, $id)
    {
        return $this->_DAO->update(new UpdateUser($newUser, $id));
    }

    function deleteUser($userID)
    {
        return $this->_DAO->delete(new DeleteUser($userID));
    }

    function addUser(UserData $user)
    {
        return $this->_DAO->insert(new InsertUser($user));
    }

    function getUserRoles()
    {
        return $this->_DAO->select(new SelectUserRoles());
    }
}

class SelectTotalUserCount implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT count(*) AS count FROM `users`;";
    }
}

class EntitySelectAllUsers implements IEntitySelect
{
    private $start, $count, $orderByColumn, $isDesc, $searchString;

    function __construct($start, $count)
    {
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
        $this->isDesc = ($_POST['order'][0]['dir'] === 'desc' ? 'TRUE' : 'FALSE');
        $this->searchString = '';
        $searchArray = $_POST['columns'];
        for ($i = 0; $i < count($searchArray); $i++) {
            if ($searchArray[$i]['search']['value'] !== '') {
                $this->searchString .= $searchArray[$i]['name'] . ',' . $searchArray[$i]['search']['value'] . ';';
            }
        }
        $this->orderByColumn = $searchArray[$_POST['order'][0]['column']]['name'];
    }

    function getSelectQuery()
    {
        return "CALL selectUsers($this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
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
        return "SELECT * FROM `user_roles`;";
    }
}

class DeleteUser implements IEntityDelete
{
    private $userID;

    function __construct($userID)
    {
        $this->userID = DAO::getInstance()->checkString($userID);
    }

    /**
     * @return string
     */
    function getDeleteQuery()
    {
        return "DELETE FROM `users` WHERE userID = $this->userID;";
    }
}

class InsertUser implements IEntityInsert
{
    private $firstName, $lastName, $login, $patronymic, $position, $passMD5, $phoneNumber, $email, $userRoleID, $pointID;

    function __construct(UserData $user)
    {
        $dao = DAO::getInstance();
        $this->firstName = $dao->checkString($user->getData('firstName'));
        $this->lastName = $dao->checkString($user->getData('lastName'));
        $this->patronymic = $dao->checkString($user->getData('patronymic'));
        $this->position = $dao->checkString($user->getData('position'));
        $this->passMD5 = $dao->checkString($user->getData('password'));
        $this->phoneNumber = $dao->checkString($user->getData('phoneNumber'));
        $this->email = $dao->checkString($user->getData('email'));
        $this->login = $dao->checkString($user->getData('email'));
        $this->userRoleID = $dao->checkString($user->getData('userRoleRusName'));
        $this->pointID = $dao->checkString($user->getData('pointName'));
    }

    /**
     * @return string
     */
    function getInsertQuery()
    {
        $salt = substr(md5(rand(0, 100000000)), 0, 16);
        $passAndSalt = md5($this->passMD5 . $salt);
//        echo "INSERT INTO `users` (firstName, lastName, patronymic, position, salt, passAndSalt, phoneNumber, email, userRoleID, pointID) VALUE ($this->firstName, $this->lastName, $this->patronymic, $this->position, $salt, $passAndSalt, $this->phoneNumber, $this->email, $this->userRoleID, $this->pointID);";
        return "INSERT INTO `users` (firstName, lastName, patronymic, login, position, salt, passAndSalt, phoneNumber, email, userRoleID, pointID) VALUE " .
        "('$this->firstName', '$this->lastName', '$this->patronymic', '$this->login', '$this->position', '$salt', '$passAndSalt', '$this->phoneNumber', '$this->email', '$this->userRoleID', $this->pointID);";
    }
}

class UpdateUser implements IEntityUpdate
{
    private $firstName, $lastName, $patronymic, $position, $passMD5, $phoneNumber, $email, $userRoleID, $pointID;
    private $userID;

    function __construct(UserData $user, $id)
    {
        $dao = DAO::getInstance();
        $this->userID = $dao->checkString($id);
        $this->firstName = $dao->checkString($user->getData('firstName'));
        $this->lastName = $dao->checkString($user->getData('lastName'));
        $this->patronymic = $dao->checkString($user->getData('patronymic'));
        $this->position = $dao->checkString($user->getData('position'));
//        $this->passMD5 = $dao->checkString($user->getData('password'));
        $this->phoneNumber = $dao->checkString($user->getData('phoneNumber'));
        $this->email = $dao->checkString($user->getData('email'));
        $this->userRoleID = $dao->checkString($user->getData('userRoleRusName'));
        $this->pointID = $dao->checkString($user->getData('pointName'));
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
//        $salt = substr(md5(rand(0,100000000)),0,16);
//        $passAndSalt = md5($this->passMD5.$salt);
        return "UPDATE `users` SET " .
        "firstName = '$this->firstName', " .
        "lastName = '$this->lastName', " .
        "patronymic = '$this->patronymic', " .
        "position = '$this->position', " .
        "phoneNumber = '$this->phoneNumber', " .
        "email = '$this->email', " .
        "userRoleID = '$this->userRoleID', " .
        "pointID = $this->pointID " .
        "WHERE userID = $this->userID;";
//        return "UPDATE `users` SET
//            firstName = '$this->firstName',
//            lastName = '$this->lastName',
//            patronymic = '$this->patronymic',
//            position = '$this->position',
//            salt = '$salt',
//            passAndSalt = '$passAndSalt',
//            phoneNumber = '$this->phoneNumber',
//            email = '$this->email',
//            userRoleID = '$this->userRoleID',
//            pointID = $this->pointID
//            WHERE userID = this->userID;";
    }
}