<?php
class YandexApiFeature Implements JsonSerializable
{
    private $type = "Feature";
    private $geometry;
    private $properties;
    private $options;

    /**
     * Feature constructor.
     * @param $geometry
     * @param $properties
     * @param $options
     */
    public function __construct($geometry, $properties, $options)
    {
        $this->geometry = $geometry;
        $this->properties = $properties;
        $this->options = $options;
    }

    public function toJson(){
        return json_encode($this);
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }

}

class YandexApiGeometryPoint Implements JsonSerializable
{
    private $type="Point";
    private $coordinates;

    /**
     * GeometryPoint constructor.
     * @param $x
     * @param $y
     */
    public function __construct($x,$y)
    {
        $this->coordinates = [$x,$y];
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }

}

class YandexApiProperties
{
    public function createProperty($name, $value){
        $this->{$name} = $value;
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }
}

class YandexApiOptions
{
    public function createProperty($name, $value){
        $this->{$name} = $value;
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }
}

class YandexApiFeatureCollection Implements JsonSerializable
{
    private $type = "FeatureCollection";
    private $features = array();

    public function addFeature($feature){
        array_push($this->features,$feature);
    }

    public function toJson(){
        return json_encode($this);
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }


}

class YandexApiRoute Implements JsonSerializable
{
    private $routePoints = array();


    public function addRoutePoint($routePoint)
    {
        array_push($this->routePoints,$routePoint);
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }


}

class YandexApiRoutePoint Implements JsonSerializable
{
    private $type;
    private $point;

    /**
     * YandexApiRoutePoint constructor.
     * @param $type
     * @param $x
     * @param $y
     */
    public function __construct($x,$y,$type,$address)
    {
        //coordinates in our database are messed up. Too late to blame anyone;
        if($y==0 || $x ==0){
            $this->point=$address;
        } else {
            $this->point = [$y,$x];
        }
        $this->type = $type;
    }


    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }

}

class YandexApiRoutePoints Implements JsonSerializable
{
    private $points = array();

    function addRoutePoint($routePoint)
    {
        array_push($this->points,$routePoint);
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }
}

class YandexApiPlacemark Implements JsonSerializable
{
    private $geometry;
    private $properties;
    private $options;

    /**
     * YandexApiPlacemark constructor.
     * @param $geometry
     * @param $properties
     * @param $options
     */
    public function __construct($x,$y, $properties, $options)
    {
        $this->geometry = [floatval($y),floatval($x)];
        $this->properties = $properties;
        $this->options = $options;
    }

    /**
     * @return mixed
     */
    function jsonSerialize()
    {
        return get_object_vars($this);
    }


}