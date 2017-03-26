<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IDriver
{
    function selectAllDrivers();

    function selectDriverById($id);

    function insertDriver();
    
    function selectDriverByCompanyId($companyId);
    
    function selectDriverByVehicleId($vehicleId);
}