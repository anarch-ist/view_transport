package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.HashMap;

public class RouteWeightAndVolume extends HashMap<Route, RoutePair> {

    /** Save available weight or volume for route from planned schedule
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @param selectionOption
     * @return
     */
    public RouteWeightAndVolume getWeightAndVolumeForRoute(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, SelectionOption selectionOption) {
        RouteWeightAndVolume result = new RouteWeightAndVolume();
        for (Route route : plannedSchedule) {
            RoutePair routePair = new RoutePair(route.getStartingWeight(), route.getStartingVolume());
            for (Invoice invoice : invoiceContainer) {
                if (invoice.getRoute() != null) {
                    if (invoice.getRoute().equals(route))
                        if(selectionOption.equals(SelectionOption.WEIGHT)){
                            routePair = routePair.getRestAvailableWeight(invoice, routePair);
                        }else if(selectionOption.equals(SelectionOption.VOLUME)){
                            routePair = routePair.getRestAvailableVolume(invoice, routePair);
                        }
//                        routePair = routePair.getRestAvailableWeightAndVolume(invoice, routePair);
                }
            }
            result.put(route, routePair);
        }
        return result;
    }
}
