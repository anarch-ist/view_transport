<?php

namespace DAO;;
include_once __DIR__ . '/../DAO.php';


class Freight{
    private static $instance;
    private $DAO;

    protected function __construct()
    {
        $this->DAO = DAO::getInstance();
        self::$instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$instance)) return new Freight();
        return self::$instance;
    }

    public function createFreight($freightData){
        $this->DAO->insert(new CreateFreight($freightData['transportCompanyId'],
            $freightData['driverId'],
            $freightData['vehicleId'],
            $freightData['vehicle2Id'],
            $freightData['vehicle3Id'],
            $freightData['routeId'],
            $freightData['distance'],
            $freightData['continuance'],
            $freightData['stallHours'],
            $freightData['speedReadingsEnd'],
            $freightData['freightNumber'],
            $freightData['fuelConsumption']));
        $lastInsertedFreight =$this->DAO->select(new SelectLastInsertedFreight())[0];
        $this->updateOwnRouteLists($lastInsertedFreight['freight_id']);
        return $lastInsertedFreight;

    }

    private function updateOwnRouteLists($freightId){
        $this->DAO->update(new UpdateOwnRouteLists($freightId));
    }

}
class UpdateOwnRouteLists implements IEntityUpdate {
    private $freightId;

    /**
     * UpdateOwnRouteLists constructor.
     * @param $freightId
     */
    public function __construct($freightId)
    {
        $this->freightId = $freightId;
    }

    function getUpdateQuery()
    {
        return "UPDATE route_lists,freight SET route_lists.driver_id_internal = freight.driver_id, route_lists.vehicle_id = freight.vehicle_id, route_lists.vehicle_2_id=freight.vehicle_2_id, route_lists.vehicle_3_id = freight.vehicle_3_id, route_lists.routeID=freight.route_id";
    }


}


class SelectLastInsertedFreight implements IEntitySelect
{
    function getSelectQuery()
    {
        return "SELECT * FROM freight ORDER BY freight_id DESC LIMIT 1;";
    }

}


class CreateFreight implements IEntityInsert {
    private $transportCompanyId,
        $driverId,
        $vehicleId,
        $vehicle2Id,
        $vehicle3Id,
        $routeId,
        $distance,
        $continuance,
        $stallHours,
        $speedReadingsEnd,
        $freightNumber,
        $fuelConsumption;

    /**
     * CreateFreight constructor.
     * @param $transportCompanyId
     * @param $driverId
     * @param $vehicleId
     * @param $vehicle2Id
     * @param $vehicle3Id
     * @param $routeId
     * @param $distance
     * @param $continuance
     * @param $stallHours
     * @param $speedReadingEnd
     * @param $freightNumber
     */
    public function __construct($transportCompanyId, $driverId, $vehicleId, $vehicle2Id, $vehicle3Id, $routeId, $distance, $continuance, $stallHours, $speedReadingsEnd, $freightNumber,$fuelConsumption)
    {
        $this->transportCompanyId = $transportCompanyId;
        $this->driverId = $driverId;
        $this->vehicleId = $vehicleId;
        $this->vehicle2Id = $vehicle2Id;
        $this->vehicle3Id = $vehicle3Id;
        $this->routeId = $routeId;
        $this->distance = $distance;
        $this->continuance = $continuance;
        $this->stallHours = $stallHours;
        $this->speedReadingsEnd = $speedReadingsEnd;
        $this->freightNumber = $freightNumber;
        $this->fuelConsumption = $fuelConsumption;
    }


    function getInsertQuery()
    {
        $query= "INSERT INTO freight (transport_company_id, driver_id, vehicle_id, vehicle_2_id, vehicle_3_id, route_id, distance, continuance, stall_hours,  speed_readings_end,freight_number, status_id, fuel_consumption) VALUE ('$this->transportCompanyId','$this->driverId', '$this->vehicleId', '$this->vehicle2Id', '$this->vehicle3Id', '$this->routeId', '$this->distance', '$this->continuance', '$this->stallHours', '$this->speedReadingsEnd', '$this->freightNumber', 'CREATED','$this->fuelConsumption');";
        return $query;
    }


}