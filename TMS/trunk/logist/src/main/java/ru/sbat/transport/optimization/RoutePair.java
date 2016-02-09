package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;


public class RoutePair {
    private double weight;
    private double volume;

    public RoutePair(){

    }

    public RoutePair(Route route) {
        this.weight = route.getStartingWeight();
        this.volume = route.getStartingVolume();
    }

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

    public RoutePair getAvailableWeightAndVolume(Invoice invoice, RoutePair routePair){
        if(isFittingForRoute(invoice, routePair)){
            routePair.setWeight(routePair.getWeight() - invoice.getWeight());
            routePair.setVolume(routePair.getVolume() - invoice.getVolume());
        }
        return routePair;
    }

    public boolean isFittingForRoute(Invoice invoice, RoutePair routePair){
        boolean result = false;
        double weightDifference = routePair.getWeight() - invoice.getWeight();
        double volumeDifference = routePair.getVolume() - invoice.getVolume();
        if(weightDifference >= 1 && volumeDifference >= 1){
            result = true;
        }
        return result;
    }
}