package ru.sbat.transport.optimization.utils;


import ru.sbat.transport.optimization.Invoice;

public class LoadCost {
    private double loadCost;
    public LoadCost(double loadCost) {
        this.loadCost = loadCost;
    }

    public double getLoadCost() {
        return loadCost;
    }

    public void setLoadCost(double loadCost) {
        this.loadCost = loadCost;
    }

    public LoadCost getCurrentLoadCost(Invoice invoice, LoadCost loadCost) {
        if(isFittingForTrackCourseByCost(invoice, loadCost)) {
            loadCost.setLoadCost(loadCost.getLoadCost() - invoice.getCost());
        }
        return loadCost;
    }

    public boolean isFittingForTrackCourseByCost(Invoice invoice, LoadCost loadCost){
        boolean result = false;
        double costDifference = loadCost.getLoadCost() - invoice.getCost();
        if(costDifference >= 0){
            result = true;
        }
        return result;
    }
}
