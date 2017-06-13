<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IVehicle
{
    function selectAllVehicles();

    function selectVehicleById($id);
    
    function selectVehicleByCompanyId($companyId);

    function insertVehicle($vehicleInfo);

    function pseudoRemoveVehicle($id);

    function selectVehiclesByRange($start, $length);

    function selectVehicleByLastInsertedId();

    function updateVehicle(VehicleData $newUser, $id);
}