<?php
namespace DAO;

interface IRequestsForUserEntity
{
    function selectAllData(array $columnInformation, $orderColumnNumber);
}