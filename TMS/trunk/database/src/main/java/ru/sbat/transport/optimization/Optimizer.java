package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RouteList;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Optimizer implements IOptimizer{



    @Override
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException {

        for(Invoice invoice: unassignedInvoices) {
            if (invoice.getRoute() != null)
               throw new IllegalArgumentException("invoice.getRoute() should be null");

            Date plannedDeliveryTime = invoice.getRequest().getPlannedDeliveryTime();
            Date creationDate = invoice.getCreationDate();
            Point deliveryPoint = invoice.getRequest().getDeliveryPoint();
            double invoiceWeight = invoice.getWeight();

            RouteList routeListForInvoice = new RouteList();
            for(Route route: plannedSchedule){
//                if(deliveryPoint.equals(route.getArrivalPoint()) && (creationDate) && (plannedDeliveryTime.before(route.getDepartureTime()))){
//
//                }
            }
        }

    }


    @Override
    public InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices) {
        InvoiceTypes invoiceTypes = null;

        return invoiceTypes;
    }
}
