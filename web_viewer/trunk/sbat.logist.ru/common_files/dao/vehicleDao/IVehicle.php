<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IVehicle
{
    function selectAllVehicles();

    function selectVehicleById($id);
    
    function selectVehicleByCompanyId($companyId);

    function insertVehicle();
}