package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;

public class RouteTriple {
    private double weight;
    private double volume;
    private double cost;
    private Invoice invoice;

    public RouteTriple(Route route, Invoice invoice) {
        this.weight += route.getStartingWeight() - invoice.getWeight();
        this.volume = route.getStartingVolume();
        this.cost = route.getStartingCost();
    }
}
