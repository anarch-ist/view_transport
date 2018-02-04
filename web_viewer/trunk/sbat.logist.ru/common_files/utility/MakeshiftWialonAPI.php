<?php
//include_once __DIR__ . '../privilegedUser/PrivilegedUser.php';
//$privUser = PrivilegedUser::getInstance();
//$json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=token/login&params={"token":"55ae391ea05405095724fe35a1604fd4FCC6D9EBBA53FAD79B0821B1D1D8D57293E42FDD"}');
//$login_response = json_decode($json);
//$sid = $login_response->eid;
//echo $login_response->eid;

//

//$vehicleResp = json_decode($json);
//foreach ($vehicleResp->items as $vehicle){
//    echo "INSERT INTO transmaster_transport_db.vehicles (transport_company_id, license_number, model, carrying_capacity, volume,  pallets_quantity, wialon_id) VALUES (200,'$vehicle->nm', '$vehicle->nm', 12, 12, 12, '$vehicle->id')";
//    echo "<br>";
//}


class WialonApi
{
    private static $_instance;
    private $token = '55ae391ea05405095724fe35a1604fd4CD458D58077D25D21A78836D9434BAE853CA9933';

    /**
     * WialonApi constructor.
     */
    private function __construct()
    {

    }

    public static function getInstance()
    {

        if (is_null(self::$_instance)) {
            self::$_instance = new WialonApi();
        }
        return self::$_instance;

    }

    public function getSid(){
        $json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=token/login&params={"token":"'.$this->token.'"}');
        $login_response = json_decode($json);
        return $login_response->eid;
    }
}

class AllVehiclesData
{
    private $json, $sid;

    /**
     * AllVehiclesData constructor.
     */
    public function __construct()
    {
//        $json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=token/login&params={"token":"55ae391ea05405095724fe35a1604fd4CD458D58077D25D21A78836D9434BAE853CA9933"}');
//        $login_response = json_decode($json);
        $this->sid = WialonApi::getInstance()->getSid();
        $this->json = json_decode(file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=core/search_items&params={"spec":{"itemsType":"avl_unit","propName":"sys_id","propValueMask":"*","sortType":"sys_id"},"force":1,"flags":1,"from":0,"to":0}&sid=' . $this->sid));
    }

    public function getData()
    {
        return $this->json;
    }

    public function getVehicles()
    {
        $vehicles = [];
        foreach ($this->json->items as $item) {
            array_push($vehicles, new WialonVehicle($item->id, $item->nm, $this->sid));
        }
        return $vehicles;
    }

}


class VehicleCoordinates implements JsonSerializable
{
    public $x, $y;

    /**
     * VehicleCoordinates constructor.
     * @param $x
     * @param $y
     */
    public function __construct($x, $y)
    {
        $this->x = $x;
        $this->y = $y;
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }


}

class WialonVehicle implements JsonSerializable
{
    private $wialonId;
    private $sid;
    private $coordinates;
    private $data;


    /**
     * Vehicle constructor.
     */
    public function __construct($wialonId, $vehicleName = '', $sid = '')
    {

        $this->wialonId = $wialonId;
        if ($sid == '') {
            $json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=token/login&params={"token":"55ae391ea05405095724fe35a1604fd4CD458D58077D25D21A78836D9434BAE853CA9933"}');
            $login_response = json_decode($json);
            $this->sid = $login_response->eid;
        } else {
            $this->sid = $sid;
        }
        $json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=core/search_item&params={"id":' . $this->wialonId . ',"flags":1024}&sid=' . $this->sid);
        $data = json_decode($json);
        $this->coordinates = new VehicleCoordinates($data->item->pos->x, $data->item->pos->y);
        $this->data = $data->item;
        $this->data->vehicleName = $vehicleName;
        $this->data->wialonId = $wialonId;
    }

    /**
     * @return mixed
     */
    public function getWialonId()
    {
        return $this->wialonId;
    }

    public function getName()
    {
        return $this->data->vehicleName;
    }

    public function getCoordinates()
    {
        return $this->coordinates;
    }

    /**
     * @return mixed
     */
    public function getFullData()
    {
        return $this->data;
    }

    public function jsonSerialize()
    {
        return $this->data;
    }


}