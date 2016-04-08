package ru.sbat.transport.optimization;



public class LoadUnit {
    private double loadCost;
    private int numberOfWeek;

    public double getLoadCost() {
        return loadCost;
    }

    public void setLoadCost(double loadCost) {
        this.loadCost = loadCost;
    }

    public int getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(int numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }

    @Override
    public String toString() {
        return "LoadUnit{" +
                "loadCost=" + loadCost +
                ", numberOfWeek=" + numberOfWeek +
                '}';
    }
}
