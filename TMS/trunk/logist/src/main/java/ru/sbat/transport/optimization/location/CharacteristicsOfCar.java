package ru.sbat.transport.optimization.location;



public class CharacteristicsOfCar {
    private double capacityCar; //грузоподъемность в кг
    private double volumeCar; //объем машины
    private double occupancyCost;
    private double costOfCar;

    public CharacteristicsOfCar(double capacityCar, double volumeCar) {
        this.capacityCar = capacityCar;
        this.volumeCar = volumeCar;
    }

    public CharacteristicsOfCar(){
    }

    public CharacteristicsOfCar(double capacityCar, double volumeCar, double occupancyCost) {
        this.capacityCar = capacityCar;
        this.volumeCar = volumeCar;
        this.occupancyCost = occupancyCost;
    }

    public CharacteristicsOfCar(double costOfCar) {
        this.costOfCar = costOfCar;
    }

    public double getCapacityCar() {
        return capacityCar;
    }

    public void setCapacityCar(double capacityCar) {
        this.capacityCar = capacityCar;
    }

    public double getVolumeCar() {
        return volumeCar;
    }

    public void setVolumeCar(double volumeCar) {
        this.volumeCar = volumeCar;
    }

    public double getOccupancyCost() {
        return occupancyCost;
    }

    public void setOccupancyCost(double occupancyCost) {
        this.occupancyCost = occupancyCost;
    }

    public double getCostOfCar() {
        return costOfCar;
    }

    public void setCostOfCar(double costOfCar) {
        this.costOfCar = costOfCar;
    }
}
