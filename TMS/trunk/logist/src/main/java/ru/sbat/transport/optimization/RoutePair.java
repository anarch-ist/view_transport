package ru.sbat.transport.optimization;


public class RoutePair {
    private double weight;
    private double volume;


    public RoutePair(double weight, double volume) {
        this.weight = weight;
        this.volume = volume;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public RoutePair getRestAvailableWeightAndVolume(Invoice invoice, RoutePair routePair){
        if(isFittingForRouteByWeightAndVolume(invoice, routePair)){
            routePair.setVolume(routePair.getVolume() - invoice.getVolume());
            routePair.setWeight(routePair.getWeight() - invoice.getWeight());
        }
        return routePair;
    }

    public boolean isFittingForRouteByWeightAndVolume(Invoice invoice, RoutePair routePair){
        boolean result = false;
        double volumeDifference = routePair.getVolume() - invoice.getVolume();
        double weightDifference = routePair.getWeight() - invoice.getWeight();
        if(volumeDifference >= 0 && weightDifference >= 0){
            result = true;
        }
        return result;
    }
}