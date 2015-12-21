<?php
namespace DAO;
include_once __DIR__ . '/ITemplate.php';
include_once __DIR__ . '/../DAO.php';

class TemplateEntity implements ITemplateEntity
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
        if (is_null(self::$_instance)) return new TemplateEntity();
        return self::$_instance;
    }

    function selectTemplates()
    {
        $array = $this->_DAO->select(new SelectAllTemplates());
        $Templates = array();
        for ($i = 0; $i < count($array); $i++) {
            $Templates[$i] = new Template($array[$i]);
        }
        return $Templates;
    }

    function selectTemplateByID($id)
    {
        $array = $this->_DAO->select(new SelectTemplateByID($id));
        return new Template($array[0]);
    }

    function updateTemplate($newTemplate)
    {
        // TODO: Implement updateTemplate() method.
    }

    function deleteTemplate($Template)
    {
        // TODO: Implement deleteTemplate() method.
    }

    function addTemplate($Template)
    {
        // TODO: Implement addTemplate() method.
    }
}

class Template implements IEntityData
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

class SelectAllTemplates implements IEntitySelect
{
    function __construct()
    {
    }

    function getSelectQuery()
    {
        return "select * from `Templates`;";
    }
}

class SelectTemplateByID implements IEntitySelect
{
    private $id;

    function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    function getSelectQuery()
    {
        return "select * from `Templates` where `TemplateID` = '$this->id'";
    }
}