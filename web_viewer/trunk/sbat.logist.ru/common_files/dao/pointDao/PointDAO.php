<?php
namespace DAO;
include_once 'IPointDAO.php';
include_once '/../DAO.php';

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
    private $pointID = '';
    private $pointName = '';
    private $region = '';
    private $timeZone = '';
    private $docs = '';
    private $comments = '';
    private $openTime = '';
    private $closeTime = '';
    private $district = '';
    private $locality = '';
    private $mailIndex = '';
    private $address = '';
    private $email = '';

    /**
     * @return mixed
     */
    public function getAddress()
    {
        return $this->address;
    }

    /**
     * @return mixed
     */
    public function getCloseTime()
    {
        return $this->closeTime;
    }

    /**
     * @return mixed
     */
    public function getComments()
    {
        return $this->comments;
    }

    /**
     * @return mixed
     */
    public function getDistrict()
    {
        return $this->district;
    }

    /**
     * @return mixed
     */
    public function getDocs()
    {
        return $this->docs;
    }

    /**
     * @return string
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * @return mixed
     */
    public function getLocality()
    {
        return $this->locality;
    }

    /**
     * @return mixed
     */
    public function getOpenTime()
    {
        return $this->openTime;
    }

    /**
     * @return mixed
     */
    public function getMailIndex()
    {
        return $this->mailIndex;
    }

    /**
     * @return string
     */
    public function getPhoneNumber()
    {
        return $this->phoneNumber;
    }

    /**
     * @return string
     */
    public function getPointID()
    {
        return $this->pointID;
    }

    /**
     * @return mixed
     */
    public function getPointName()
    {
        return $this->pointName;
    }

    /**
     * @return mixed
     */
    public function getPointTypeID()
    {
        return $this->pointTypeID;
    }

    /**
     * @return mixed
     */
    public function getRegion()
    {
        return $this->region;
    }

    /**
     * @return mixed
     */
    public function getTimeZone()
    {
        return $this->timeZone;
    }

    private $phoneNumber;
    private $pointTypeID;

    /**
     * @param mixed $pointRoleID
     */
    public function setPointRoleID($pointRoleID)
    {
        $this->pointRoleID = $this->prepareSafeString($pointRoleID);
    }

    /**
     * @param mixed $address
     */
    public function setAddress($address)
    {
        $this->address = $this->prepareSafeString($address);
    }

    /**
     * @param mixed $closeTime
     */
    public function setCloseTime($closeTime)
    {
        $this->closeTime = $this->prepareSafeString($closeTime);
    }

    /**
     * @param mixed $comments
     */
    public function setComments($comments)
    {
        $this->comments = $this->prepareSafeString($comments);
    }

    /**
     * @param mixed $district
     */
    public function setDistrict($district)
    {
        $this->district = $this->prepareSafeString($district);
    }

    /**
     * @param mixed $docs
     */
    public function setDocs($docs)
    {
        $this->docs = $this->prepareSafeString($docs);
    }

    /**
     * @param mixed $email
     */
    public function setEmail($email)
    {
        $this->email = $this->prepareSafeString($email);
    }

    /**
     * @param mixed $locality
     */
    public function setLocality($locality)
    {
        $this->locality = $this->prepareSafeString($locality);
    }

    /**
     * @param mixed $mailIndex
     */
    public function setMailIndex($mailIndex)
    {
        $this->mailIndex = $this->prepareSafeString($mailIndex);
    }

    /**
     * @param mixed $openTime
     */
    public function setOpenTime($openTime)
    {
        $this->openTime = $this->prepareSafeString($openTime);
    }

    /**
     * @param mixed $phoneNumber
     */
    public function setPhoneNumber($phoneNumber)
    {
        $this->phoneNumber = $this->prepareSafeString($phoneNumber);
    }

    /**
     * @param mixed $pointName
     */
    public function setPointName($pointName)
    {
        $this->pointName = $this->prepareSafeString($this->prepareSafeString($pointName));
    }

    /**
     * @param mixed $pointTypeID
     */
    public function setPointTypeID($pointTypeID)
    {
        $this->pointTypeID = $this->prepareSafeString($pointTypeID);
    }

    /**
     * @param mixed $region
     */
    public function setRegion($region)
    {
        $this->region = $this->prepareSafeString($region);
    }

    /**
     * @param mixed $timeZone
     */
    public function setTimeZone($timeZone)
    {
        $this->timeZone = $this->prepareSafeString($timeZone);
    }

    function __construct($array = null)
    {
        if (!is_null($array)) {
            $this->pointID = $array['pointID'];
            $this->pointName = $array['pointName'];
            $this->region = $array['region'];
            $this->timeZone = $array['timeZone'];
            $this->docs = $array['docs'];
            $this->comments = $array['comments'];
            $this->openTime = $array['openTime'];
            $this->closeTime = $array['closeTime'];
            $this->district = $array['district'];
            $this->locality = $array['locality'];
            $this->mailIndex = $array['mailIndex'];
            $this->address = $array['address'];
            $this->email = $array['email'];
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

class EntitySelectAllPoints extends EntityDataObject implements IEntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "select * from `points`;";
    }
}

class PointSelectEntityByID extends EntityDataObject implements IEntitySelect
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