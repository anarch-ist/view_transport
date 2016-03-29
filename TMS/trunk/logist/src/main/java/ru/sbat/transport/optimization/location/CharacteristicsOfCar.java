package ru.sbat.transport.optimization.location;



public class CharacteristicsOfCar {
    private double occupancyCost; // вместимость ТС на маршруте по стоимости

    public double getOccupancyCost() {
        return occupancyCost;
    }

    public void setOccupancyCost(double occupancyCost) {
        this.occupancyCost = occupancyCost;
    }

    public CharacteristicsOfCar(double occupancyCost) {
        this.occupancyCost = occupancyCost;
    }
}
