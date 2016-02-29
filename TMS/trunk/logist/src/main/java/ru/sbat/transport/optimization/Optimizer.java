package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceType;

import java.text.ParseException;
import java.util.*;

public class Optimizer implements IOptimizer {
    /** Selects for invoice appropriate routes
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @return map with invoice(key) and routes(values)
     * @throws RouteNotFoundException
     */
    @Override
    public Map<Invoice, ArrayList<Route>> filtrate(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) throws RouteNotFoundException {
        Map<Invoice, ArrayList<Route>> map = new HashMap<>();
        RouteWeightAndVolume routeWeightAndVolume = new RouteWeightAndVolume();
        Map<Route, RoutePair> routeData = routeWeightAndVolume.getWeightAndVolumeForRoute(plannedSchedule, invoiceContainer);
        for (Invoice invoice : invoiceContainer) {
            if (invoice.getRoute() == null) {
//                throw new IllegalArgumentException("invoice.getRoute() should be null");

                Point deliveryPoint = invoice.getRequest().getDeliveryPoint();
                Point departurePoint = invoice.getAddressOfWarehouse();
                ArrayList<Route> possibleRouteForInvoice = new ArrayList<>();


                for (Route route : plannedSchedule) {
                    if (route == null) {
                        throw new RouteNotFoundException("route should not be null");
                    }
                    if (route.getDeparturePoint().equals(departurePoint)){
                        for(RoutePoint routePoint: route){
                            if(routePoint.getDeparturePoint().equals(deliveryPoint)){
                                ArrayList<Date> possibleDepartureDate = getPossibleDepartureDate(route, invoice);
                                for (Date date : possibleDepartureDate) {
                                    if (isFittingForDeliveryTime(route, invoice, date) && routeData.get(route).isFittingForRoute(invoice, routeData.get(route))) {
                                        possibleRouteForInvoice.add(route);
                                        System.out.println(date + " день отправления");
                                        System.out.println(getPossibleArrivalDate(route, invoice, date) + " день прибытия");
                                    }
                                }
                            }
                        }

                    }
                }
                System.out.println(" ");
                map.put(invoice, possibleRouteForInvoice);
            }
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
    public ArrayList<Date> getPossibleDepartureDate(Route route, Invoice invoice){
        ArrayList<Date> result = new ArrayList<>();
        Date invoiceCreationDate = new Date(invoice.getCreationDate().getTime());
        Date plannedDeliveryDate = invoice.getRequest().getPlannedDeliveryTime();
        int timeOfCreation = invoiceCreationDate.getHours()*60 + invoiceCreationDate.getMinutes();
        int[] timeDeparture = route.splitToComponentTime(route.getDepartureTime());
        invoiceCreationDate.setHours(timeDeparture[0]);
        invoiceCreationDate.setMinutes(timeDeparture[1]);
        Date date = invoiceCreationDate;
        int differenceWeekDays = route.getWeekDayOfDepartureTime() - (invoiceCreationDate.getDay()+1);
        if(differenceWeekDays > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
            date.setTime(calendar.getTimeInMillis());
        }else if(differenceWeekDays == 0 &&(timeOfCreation < route.getDepartureTime())){
            date = invoiceCreationDate;
        }else {
            int tmp = 7 - ((invoiceCreationDate.getDay()+1) - route.getWeekDayOfDepartureTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, tmp);
            date.setTime(calendar.getTimeInMillis());
        }
        if(date.before(plannedDeliveryDate) && isFittingForDeliveryTime(route, invoice, date)){
            result.add(date);
        }
        while (true) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            date = new Date(calendar.getTimeInMillis());
            if(date.before(plannedDeliveryDate) && isFittingForDeliveryTime(route, invoice, date)) {
                result.add(date);
            }else{
                break;
            }
        }
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
        Date plannedDeliveryTime = new Date(invoice.getRequest().getPlannedDeliveryTime().getTime());
        if(getPossibleArrivalDate(route, invoice, date).before(plannedDeliveryTime)){
            result = true;
        }
        return result;
    }

    /** Determines arrival date for certain route
     *
     * @param route
     * @param invoice
     * @param date
     * @return actual delivery date after loading operations
     */
    @Override
    public Date getPossibleArrivalDate(Route route, Invoice invoice, Date date){
        Calendar calendar = Calendar.getInstance();
        Date tmp = new Date(date.getTime());
        for(RoutePoint routePoint: route){
            if(invoice.getRequest().getDeliveryPoint().equals(routePoint.getDeparturePoint())){
                tmp.setHours(route.getActualDeliveryTimeInRoutePoint(invoice.getRequest().getDeliveryPoint()).getHours());
                tmp.setMinutes(route.getActualDeliveryTimeInRoutePoint(invoice.getRequest().getDeliveryPoint()).getMinutes());
                calendar.setTime(tmp);
                int countDays = route.getDaysCountOfRoute();
                calendar.add(Calendar.DAY_OF_MONTH, countDays);
            }
        }
        Date actualDeliveryDate = new Date(calendar.getTimeInMillis());
        return actualDeliveryDate;
    }


    @Override
    public void optimize(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<Route>> routesForInvoice) throws ParseException, RouteNotFoundException {
        Iterator<Map.Entry<Invoice, ArrayList<Route>>> iterator = routesForInvoice.entrySet().iterator();
        RouteWeightAndVolume routeWeightAndVolume = new RouteWeightAndVolume();
        while (iterator.hasNext()){
            Map<Route, RoutePair> routeData = routeWeightAndVolume.getWeightAndVolumeForRoute(plannedSchedule, invoiceContainer);
            Map.Entry<Invoice, ArrayList<Route>> entry = iterator.next();
            ArrayList<Route> routes = entry.getValue();
            Invoice invoice = entry.getKey();
            for(Route route: routes) {
                if(routeData.get(route).isFittingForRoute(invoice, routeData.get(route))) {
                    invoice.setRoute(route);
                    invoice.setRealDepartureDate(getPossibleDepartureDate(route, invoice).get(0));
                    invoice.setInvoiceType(InvoiceType.C);
                    System.out.println("Date: " + invoice.getRealDepartureDate() + " for invoice: " + invoice.toString());
                    break;
                }
            }if (invoice.getRoute() == null){

            }
        }
    }

    /** sets the invoice's types
     *
     * @param unassignedInvoices
     * @return type of invoice (A, B, C)
     */
    @Override
    public InvoiceType getInvoiceTypes(List<Invoice> unassignedInvoices) {
        InvoiceType invoiceType = null;

        return invoiceType;
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
