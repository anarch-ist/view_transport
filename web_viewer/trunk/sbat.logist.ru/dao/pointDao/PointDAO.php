<?php
require_once('IPointDAO.php');
require_once('DAO.class.php');

class PointDAO extends DAO implements IPointDAO
{
    private static $instance;

    protected function __construct()
    {
        parent::__construct();
        self::$instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$instance)) return new PointDAO();
        return self::$instance;
    }

    function selectPoints()
    {
        $array = parent::select(new EntitySelectAllPoints());
        $points = array();
        for ($i = 0; $i < count($array); $i++) {
            $points[$i] = new Point($array[$i]);
        }
        return $points;
    }

    function selectPointByID($id)
    {
        $array = parent::select(new PointSelectEntityByID($id));
        return new Point($array[0]);
    }

    function selectPointByEmail($email)
    {
        $array = parent::select(new PointSelectEntityByLogin($email));
        return new Point($array[0]);
    }

    function updatePoint($newPoint)
    {
        // TODO: Implement updatePoint() method.
    }

    function deletePoint($point)
    {
        // TODO: Implement deletePoint() method.
    }

    function addPoint($point)
    {
        // TODO: Implement addPoint() method.
    }
}

class Point extends EntityDataObject
{
    private $pointID, $firstName, $lastName, $patronymic, $login, $passMD5, $phoneNumber, $email, $pointRoleID;

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
    public function getPointRoleID()
    {
        return $this->pointRoleID;
    }

    /**
     * @param mixed $pointRoleID
     */
    public function setPointRoleID($pointRoleID)
    {
        $this->pointRoleID = $this->prepareSafeString($pointRoleID);
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
            $this->pointRoleID = $array['pointRoleID'];
        } else {
            $this->email = '';
            $this->firstName = '';
            $this->lastName = '';
            $this->login = '';
            $this->passMD5 = '';
            $this->patronymic = '';
            $this->phoneNumber = '';
            $this->pointID = '';
            $this->pointRoleID = '';
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

class EntitySelectAllPoints extends EntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "select * from `points`;";
    }
}

class PointSelectEntityByID extends EntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = $this->prepareSafeString($id);
    }

    function getSelectQuery()
    {
        return "select * from `points` where `pointID` = '$this->id'";
    }
}