package ru.sbat.transport.optimization;


public class LoadCostOfTrackCourse {

    public LoadCostOfTrackCourse(double loadCost) {
        this.loadCost = loadCost;
    }

    private double loadCost;

    public double getLoadCost() {
        return loadCost;
    }

    public void setLoadCost(double loadCost) {
        this.loadCost = loadCost;
    }

    public LoadCostOfTrackCourse getAvailableRestLoadCost(Invoice invoice, LoadCostOfTrackCourse loadCostOfTrackCourse) {
        if(isFittingForTrackCourseByCost(invoice, loadCostOfTrackCourse)) {
            loadCostOfTrackCourse.setLoadCost(loadCostOfTrackCourse.getLoadCost() - invoice.getCost());
        }
        return loadCostOfTrackCourse;
    }

    public boolean isFittingForTrackCourseByCost(Invoice invoice, LoadCostOfTrackCourse loadCostOfTrackCourse){
        boolean result = false;
        double costDifference = loadCostOfTrackCourse.getLoadCost() - invoice.getCost();
        if(costDifference >= 0){
            result = true;
        }
        return result;
    }
}
