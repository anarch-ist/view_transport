package ru.sbat.transport.optimization.location;


// переименовать класс во время дальнейшей работы с ним
// characteristicsOfVehicle or characteristicsOfCar
public class CharacteristicsOfCar {
    private double capacityCar; //грузоподъемность в кг
    private double volumeCar; //объем машины
    private double cost;

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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
