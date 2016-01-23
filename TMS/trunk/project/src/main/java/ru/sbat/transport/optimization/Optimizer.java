package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RouteList;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Optimizer implements IOptimizer {



    @Override
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException, RouteNotFoundException {

        for(Invoice invoice: unassignedInvoices) {
            if (invoice.getRoute() != null)
               throw new IllegalArgumentException("invoice.getRoute() should be null");

            Date plannedDeliveryTime = invoice.getRequest().getPlannedDeliveryTime(); //плановое время доставки Dd
            Date creationDate = invoice.getCreationDate(); //время создание заявки
            Point deliveryPoint = invoice.getRequest().getDeliveryPoint();
            Point departurePoint = invoice.getAddressOfWarehouse();
            double invoiceWeight = invoice.getWeight();
            int dayOfWeek = getWeekDay(plannedDeliveryTime);
            for(Route route: plannedSchedule){
                if(route == null){
                    throw new RouteNotFoundException("route should not be null");
                }
                if(route.getDeparturePoint().equals(departurePoint) && route.getArrivalPoint().equals(deliveryPoint) && dayOfWeek <= route.get(route.size()-1).getWeekDay() && (creationDate.getTime() + route.getFullTime() <= invoice.getRequest().getPlannedDeliveryTime().getTime())){
                    System.out.println();
                }
            }
        }
    }


    @Override
    public InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices) {
        InvoiceTypes invoiceTypes = null;

        return invoiceTypes;
    }

    @Override
    public int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int result = calendar.get(Calendar.DAY_OF_WEEK);
        return result;
    }
}
