<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IDriver
{
    function selectAllDrivers();

    function selectDriverById($id);

    function insertDriver($driverInfo);
    
    function selectDriverByCompanyId($companyId);
    
    function selectDriverByVehicleId($vehicleId);

    function selectDriversByRange($start = 0, $length = 20);

    function pseudoRemoveDriver($id);

    function selectDriverByLastInsertedId();

    function updateDriver(DriverData $newDriver, $id);
}