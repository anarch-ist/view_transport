package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.HashMap;

public class RouteAndOccupancyCost extends HashMap<Route, RouteCost>{
    /** Save available occupancy cost for route from planned schedule
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @return pair of route and the rest of occupancy cost
     */

    public RouteAndOccupancyCost getOccupancyCostForRoute(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer){
        RouteAndOccupancyCost result = new RouteAndOccupancyCost();
        for(Route route: plannedSchedule){
            RouteCost routeCost = new RouteCost(route.getStartingOccupancyCost());
            for(Invoice invoice: invoiceContainer){
                if(invoice.getDeliveryRoute() != null){
                    if(invoice.getDeliveryRoute().equals(route)){
                        routeCost = routeCost.getAvailableOccupancyCost(invoice, routeCost);
                    }
                }
            }
            result.put(route, routeCost);
        }
        return  result;
    }
}
