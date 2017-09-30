<?php
//include_once __DIR__ . '../privilegedUser/PrivilegedUser.php';
//$privUser = PrivilegedUser::getInstance();
//$json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=token/login&params={"token":"55ae391ea05405095724fe35a1604fd4FCC6D9EBBA53FAD79B0821B1D1D8D57293E42FDD"}');
//$login_response = json_decode($json);
//$sid = $login_response->eid;
//echo $login_response->eid;

//$json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=core/search_items&params={"spec":{"itemsType":"avl_unit","propName":"sys_id","propValueMask":"*","sortType":"sys_id"},"force":1,"flags":1,"from":0,"to":0}&sid='.$sid);

//$vehicleResp = json_decode($json);
//foreach ($vehicleResp->items as $vehicle){
//    echo "INSERT INTO transmaster_transport_db.vehicles (transport_company_id, license_number, model, carrying_capacity, volume,  pallets_quantity, wialon_id) VALUES (200,'$vehicle->nm', '$vehicle->nm', 12, 12, 12, '$vehicle->id')";
//    echo "<br>";
//}


class VehicleCoordinates implements JsonSerializable {
    public $x,$y;

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

class WialonVehicle {
    private $wialonId;
    private $sid;
    private $coordinates;


    /**
     * Vehicle constructor.
     */
    public function __construct($wialonId)
    {
        $this->wialonId=$wialonId;
        $json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=token/login&params={"token":"55ae391ea05405095724fe35a1604fd4CD458D58077D25D21A78836D9434BAE853CA9933"}');
        $login_response = json_decode($json);
        $this->sid = $login_response->eid;
        $json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=core/search_item&params={"id":'.$this->wialonId.',"flags":1024}&sid='.$this->sid);
        $data = json_decode($json);
        $this->coordinates = new VehicleCoordinates($data->item->pos->x,$data->item->pos->y);
    }

    public function getCoordinates(){
        return $this->coordinates;
    }


}

//$vehicle =new WialonVehicle(15465212);
//echo json_encode($vehicle->getCoordinates());




//$privUser->getVehicleEntity()->insertVehicle();

//$json = file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=core/search_items&
//params={
//"spec":{
//				"itemsType":"user",
//				"propName":"sys_name",
//				"propValueMask":"*",
//				"sortType":"sys_name"
//			     },
//			     "force":1,
//			     "flags":1,
//		         "from":0,
//			     "to":0
//			     }&sid="'.$sid.'"');
//echo '<br>'.$json;
//$svc = 'svc=core/login&params={"token":"55ae391ea05405095724fe35a1604fd4FCC6D9EBBA53FAD79B0821B1D1D8D57293E42FDD"}';
//file_get_contents('http://hst-api.wialon.com/wialon/ajax.html?svc=core/logout&params={}&sid="'.$sid.'""');
    ?>