<?php

namespace DAO;
include_once __DIR__ . '/IPretension.php';
include_once __DIR__ . '/../DAO.php';

class PretensionEntity implements IPretension
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
        if (is_null(self::$_instance)) return new PretensionEntity();
        return self::$_instance;
    }


    /**
     * @param $pretensionID
     * @param $requestIDExternal
     * @return bool|\mysqli_result|string
     */
    function closePretension($pretensionID, $requestIDExternal){
        return $this->_DAO->update(new closePretension($pretensionID,$requestIDExternal));
    }

    /**
     * @param $pretensionID
     * @param $requestIDExternal
     * @param $pretensionComment
     * @param $pretensionCathegory
     * @param $pretensionPositionNumber
     * @param $pretensionSum
     * @return bool|\mysqli_result|string
     */
    function updatePretension($pretensionID, $requestIDExternal, $pretensionComment, $pretensionCathegory, $pretensionPositionNumber, $pretensionSum){
        return $this->_DAO->update(new updatePretension($pretensionID,$requestIDExternal,$pretensionComment,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum));
    }

    /**
     * @param $requestIDExternal
     * @param $pretensionComment
     * @param $pretensionStatus
     * @param $pretensionCathegory
     * @param $pretensionPositionNumber
     * @param $pretensionSum
     * @return bool|\mysqli_result|string
     */
    function addPretension($requestIDExternal, $pretensionComment, $pretensionStatus, $pretensionCathegory, $pretensionPositionNumber, $pretensionSum){
        return $this->_DAO->insert(new addPretension($requestIDExternal,$pretensionComment,$pretensionStatus,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum));
    }

    /**
     * @param $requestIDExternal
     * @return array|string
     */
    function getPretensions($requestIDExternal){
        return $this->_DAO->select(new getPretensions($requestIDExternal));
    }

}

class GetPretensions implements IEntitySelect
{
    private $requestIDExternal;
    function __construct($requestIDExternal)
    {
        $this->requestIDExternal=$requestIDExternal;
    }

    function getSelectQuery()
    {
        return "CALL getPretensionsByReqIdExt('$this->requestIDExternal')";

    }
}

class ClosePretension implements IEntityUpdate
{
    private $pretensionID, $requestIDExternal;

    function __construct($pretensionID, $requestIDExternal)
    {
        $this->pretensionID=$pretensionID;
        $this->requestIDExternal=$requestIDExternal;
    }
    function getUpdateQuery()
    {
        return "CALL deletePretension($this->pretensionID,'$this->requestIDExternal')";

    }
}

class UpdatePretension implements IEntityUpdate
{
    private $pretensionID, $requestIDExternal, $pretensionComment, $pretensionCathegory, $pretensionPositionNumber, $pretensionSum;

    function __construct($pretensionID, $requestIDExternal, $pretensionComment, $pretensionCathegory, $pretensionPositionNumber, $pretensionSum)
    {
        $this->pretensionID=DAO::getInstance()->checkString($pretensionID);
        $this->requestIDExternal=DAO::getInstance()->checkString($requestIDExternal);
        $this->pretensionComment=DAO::getInstance()->checkString($pretensionComment);
        $this->pretensionCathegory=DAO::getInstance()->checkString($pretensionCathegory);
        $this->pretensionPositionNumber=DAO::getInstance()->checkString($pretensionPositionNumber);
        $this->pretensionSum=DAO::getInstance()->checkString($pretensionSum);
    }

    function getUpdateQuery()
    {
        return "CALL updatePretension($this->pretensionID, '$this->requestIDExternal', '$this->pretensionCathegory', '$this->pretensionComment','$this->pretensionPositionNumber',$this->pretensionSum)";

    }
}

class AddPretension implements IEntityInsert
{
    private $requestIDExternal,$pretensionComment,$pretensionStatus,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum;

    function __construct($requestIDExternal,$pretensionComment,$pretensionStatus,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum)
    {
        $this->requestIDExternal=DAO::getInstance()->checkString($requestIDExternal);
        $this->pretensionComment=DAO::getInstance()->checkString($pretensionComment);
        $this->pretensionStatus=DAO::getInstance()->checkString($pretensionStatus);
        $this->pretensionCathegory=DAO::getInstance()->checkString($pretensionCathegory);
        $this->pretensionPositionNumber=DAO::getInstance()->checkString($pretensionPositionNumber);
        $this->pretensionSum=DAO::getInstance()->checkString($pretensionSum);
    }

    function getInsertQuery()
    {
        return "CALL insert_pretension('$this->requestIDExternal','$this->pretensionComment','$this->pretensionStatus','$this->pretensionCathegory','$this->pretensionPositionNumber',$this->pretensionSum)";
    }
}