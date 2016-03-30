package ru.sbat.transport.optimization.location;



public class CharacteristicsOfCar {
    private double occupancyCost; // вместимость ТС по стоимости

    public CharacteristicsOfCar(){
    }

    public CharacteristicsOfCar(double occupancyCost) {
        this.occupancyCost = occupancyCost;
    }

    public double getOccupancyCost() {
        return occupancyCost;
    }

    public void setOccupancyCost(double occupancyCost) {
        this.occupancyCost = occupancyCost;
    }
}
