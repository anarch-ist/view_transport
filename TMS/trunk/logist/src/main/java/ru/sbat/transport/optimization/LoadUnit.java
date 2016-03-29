package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.utils.LoadCost;
import ru.sbat.transport.optimization.utils.NumberOfWeek;

public class LoadUnit {
    private LoadCost loadCost;
    private NumberOfWeek numberOfWeek;

    public LoadUnit(LoadCost loadCost, NumberOfWeek numberOfWeek) {
        this.loadCost = loadCost;
        this.numberOfWeek = numberOfWeek;
    }

    public LoadUnit() {
    }

    public LoadCost getLoadCost() {
        return loadCost;
    }

    public void setLoadCost(LoadCost loadCost) {
        this.loadCost = loadCost;
    }

    public NumberOfWeek getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(NumberOfWeek numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }
}
