package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;

import java.text.ParseException;
import java.util.*;

public class Optimizer implements IOptimizer {
    /** Selects for invoice appropriate routes
     *
     * @param plannedSchedule
     * @param unassignedInvoices
     * @return map with invoice(key) and routes(values)
     * @throws RouteNotFoundException
     */
    @Override
    public Map<Invoice, ArrayList<Route>> filtrate(PlannedSchedule plannedSchedule, List<Invoice> unassignedInvoices) throws RouteNotFoundException {
        Map<Invoice, ArrayList<Route>> map = new HashMap<>();
        for(Invoice invoice: unassignedInvoices) {
            if (invoice.getRoute() != null)
                throw new IllegalArgumentException("invoice.getRoute() should be null");

            Point deliveryPoint = invoice.getRequest().getDeliveryPoint();
            Point departurePoint = invoice.getAddressOfWarehouse();
            ArrayList<Route> possibleRouteForInvoice = new ArrayList<>();


            for(Route route: plannedSchedule){
                if(route == null){
                    throw new RouteNotFoundException("route should not be null");
                }
                if (route.getDeparturePoint().equals(departurePoint) &&
                        route.getArrivalPoint().equals(deliveryPoint)) {
                    Date[] possibleDepartureDate = getPossibleDepartureDate(route, invoice);
                    for(Date date: possibleDepartureDate){
                        if(isFittingForDeliveryTime(route, invoice, date)){
                            possibleRouteForInvoice.add(route);
                            System.out.println(date + " день отправления");
                            System.out.println(getPossibleArrivalDate(route, invoice, date) + " день прибытия");
                           }
                        }
                    }
                }
            map.put(invoice, possibleRouteForInvoice);
            }
        return map;
        }

    /** selects possible options of routes by departure time
     *
     * @param route
     * @param invoice
     * @return array of three possible departure date
     */
    @Override
    public Date[] getPossibleDepartureDate(Route route, Invoice invoice){
        Date creationDate = invoice.getCreationDate();
        int[] timeDeparture = route.splitToComponentTime(route.getDepartureTime());
        creationDate.setHours(timeDeparture[0]);
        creationDate.setMinutes(timeDeparture[1]);
        Date date1 = creationDate;
        int differenceWeekDays = route.getWeekDayOfDepartureTime() - invoice.getWeekDay();
        if(differenceWeekDays > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
            date1.setTime(calendar.getTimeInMillis());
        }else if(differenceWeekDays == 0 &&((invoice.getCreationDate().getHours()*60 + invoice.getCreationDate().getMinutes()) < route.getDepartureTime())){
            date1 = creationDate;
        }else {
            int tmp = 7 - (invoice.getWeekDay() - route.getWeekDayOfDepartureTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, tmp);
            date1.setTime(calendar.getTimeInMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date date2 = new Date(calendar.getTimeInMillis());
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.add(Calendar.DAY_OF_MONTH, 7);
        Date date3 = new Date(calendar2.getTimeInMillis());
        Date[] result = new Date[]{date1, date2, date3};
        return result;
    }

    /** Determines the suitability of the route for the day planned delivery
     *
     * @param route
     * @param invoice
     * @param date
     * @return suitable or not
     */
    @Override
    public boolean isFittingForDeliveryTime(Route route, Invoice invoice, Date date) {
        boolean result = false;
        Date plannedDeliveryTime = invoice.getRequest().getPlannedDeliveryTime();
        if(getPossibleArrivalDate(route, invoice, date).before(plannedDeliveryTime)){
            result = true;
        }
        return result;
    }

    @Override
    public Date getPossibleArrivalDate(Route route, Invoice invoice, Date date){
        Calendar calendar = Calendar.getInstance();
        date.setHours(route.getActualDeliveryTime().getHours());
        date.setMinutes(route.getActualDeliveryTime().getMinutes());
        calendar.setTime(date);
        int countDays = route.getDaysCountOfRoute();
        calendar.add(Calendar.DAY_OF_MONTH, countDays);
        Date actualDeliveryDate = new Date(calendar.getTimeInMillis());
        return actualDeliveryDate;
    }


    @Override
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException, RouteNotFoundException {

    }

    /** sets the invoice's types
     *
     * @param unassignedInvoices
     * @return type of invoice (A, B, C)
     */
    @Override
    public InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices) {
        InvoiceTypes invoiceTypes = null;

        return invoiceTypes;
    }

    /**
     *
     * @param date
     * @return day of week from date
     */
    public int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
