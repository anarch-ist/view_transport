package ru.sbat.transport.optimization;


public class RouteCost {
    private double occupancyCost;

    public RouteCost(double occupancyCost) {
        this.occupancyCost = occupancyCost;
    }

    public double getOccupancyCost() {
        return occupancyCost;
    }

    public void setOccupancyCost(double occupancyCost) {
        this.occupancyCost = occupancyCost;
    }

    public RouteCost getAvailableOccupancyCost(Invoice invoice, RouteCost routeCost){
        if (isFittingForRouteByCost(invoice, routeCost)){
            routeCost.setOccupancyCost(routeCost.getOccupancyCost() - invoice.getCost());
        }
        return routeCost;
    }

    public boolean isFittingForRouteByCost(Invoice invoice, RouteCost routeCost){
        boolean result = false;
        double costDifference = routeCost.getOccupancyCost() - invoice.getCost();
        if(costDifference >= 0){
            result = true;
        }
        return result;
    }
}
