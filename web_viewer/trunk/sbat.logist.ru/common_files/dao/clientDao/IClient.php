<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IClientEntity
{
    function selectClients();
    function selectAllClientIdINNPairs();
}