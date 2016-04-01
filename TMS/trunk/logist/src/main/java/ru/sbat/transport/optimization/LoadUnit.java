package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.utils.LoadCost;

public class LoadUnit {
    private LoadCost loadCost;
    private int numberOfWeek;

    public LoadCost getLoadCost() {
        return loadCost;
    }

    public void setLoadCost(LoadCost loadCost) {
        this.loadCost = loadCost;
    }

    public int getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(int numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }
}
