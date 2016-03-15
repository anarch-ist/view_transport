package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.HashMap;

public class RouteWeightAndVolume extends HashMap<Route, RoutePair> {

    /** Save available weight or volume for route from planned schedule
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @return pair of route and the rest of weight and volume
     */
    public RouteWeightAndVolume getWeightAndVolumeForRoute(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) {
        RouteWeightAndVolume result = new RouteWeightAndVolume();
        for (Route route : plannedSchedule) {
            RoutePair routePair = new RoutePair(route.getStartingWeight(), route.getStartingVolume());
            for (Invoice invoice : invoiceContainer) {
                if (invoice.getDeliveryRoute() != null) {
                    if (invoice.getDeliveryRoute().equals(route))
                        routePair = routePair.getRestAvailableWeightAndVolume(invoice, routePair);
                }
            }
            result.put(route, routePair);
        }
        return result;
    }
}
