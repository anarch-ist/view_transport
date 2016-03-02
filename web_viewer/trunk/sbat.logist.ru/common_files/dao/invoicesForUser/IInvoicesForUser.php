<?php
namespace DAO;

interface IInvoicesForUserEntity
{
    function selectAllData(array $columnInformation, $orderColumnNumber);
}