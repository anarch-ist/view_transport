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

    public RoutePair getRestAvailableWeight(Invoice invoice, RoutePair routePair){
        if(isFittingForRouteByWeight(invoice, routePair)){
            routePair.setWeight(routePair.getWeight() - invoice.getWeight());
        }
        return routePair;
    }

    public RoutePair getRestAvailableVolume(Invoice invoice, RoutePair routePair){
        if(isFittingForRouteByVolume(invoice, routePair)){
            routePair.setVolume(routePair.getVolume() - invoice.getVolume());
        }
        return routePair;
    }

    public boolean isFittingForRouteByWeight(Invoice invoice, RoutePair routePair){
        boolean result = false;
        double weightDifference = routePair.getWeight() - invoice.getWeight();
        if(weightDifference >= 1){
            result = true;
        }
        return result;
    }

    public boolean isFittingForRouteByVolume(Invoice invoice, RoutePair routePair){
        boolean result = false;
        double volumeDifference = routePair.getVolume() - invoice.getVolume();
        if(volumeDifference >= 1){
            result = true;
        }
        return result;
    }
}