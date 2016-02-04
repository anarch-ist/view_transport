<?php
namespace DAO;

interface IEntityData
{
    function getData($field);

    function toArray();
}