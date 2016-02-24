package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.HashMap;

public class RouteWeightAndVolume extends HashMap<Route, RoutePair> {


    public RouteWeightAndVolume getWeightAndVolumeForRoute(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) {
        RouteWeightAndVolume result = new RouteWeightAndVolume();
        for (Route route : plannedSchedule) {
            RoutePair routePair = new RoutePair(route.getStartingWeight(), route.getStartingVolume());
            for (Invoice invoice : invoiceContainer) {
                if (invoice.getRoute() != null) {
                    if (invoice.getRoute().equals(route))
                        routePair = routePair.getRestAvailableWeightAndVolume(invoice, routePair);
                }
            }
            result.put(route, routePair);
        }
        return result;
    }
}
