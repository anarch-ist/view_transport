<?php
namespace DAO;
include_once __DIR__ . '/IEntityData.php';

abstract class EntityData implements IEntityData
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

