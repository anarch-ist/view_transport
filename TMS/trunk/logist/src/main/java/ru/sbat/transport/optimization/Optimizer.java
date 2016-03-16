package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceType;

import java.text.ParseException;
import java.util.*;

public class Optimizer implements IOptimizer {
    /** Selects for invoice appropriate delivery routes without weight/volume/cost
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @return map with invoice(key) and delivery routes(values)
     * @throws RouteNotFoundException
     */
    @Override
    public Map<Invoice, ArrayList<DeliveryRoute>> filtrate(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) throws RouteNotFoundException {
        Map<Invoice, ArrayList<DeliveryRoute>> result = new HashMap<>();
        ArrayList<DeliveryRoute> possibleDeliveryRouteForInvoice = new ArrayList<>();
        for (Invoice invoice : invoiceContainer) {
            if (invoice.getDeliveryRoute() == null) {
//                throw new IllegalArgumentException("invoice.getDeliveryRoute() should be null");
                Point deliveryPoint = invoice.getRequest().getDeliveryPoint();
                Point departurePoint = invoice.getAddressOfWarehouse();
                Map<Route, Integer> routesWithDeliveryPoint = new HashMap<>();
                for (Route route : plannedSchedule) {
                    if (route == null) {
                        throw new RouteNotFoundException("route should not be null");
                    }
                    for(RoutePoint routePointDelivery: route){
                        DeliveryRoute deliveryRoute = new DeliveryRoute();
                        if(routePointDelivery.getDeparturePoint().equals(deliveryPoint) && getArrivalDateInEachRoutePointInRoute(route, invoice).get(routePointDelivery).before(invoice.getRequest().getPlannedDeliveryDate())){
                            getDeliveryRoute(routePointDelivery, route.indexOf(routePointDelivery), route, invoice, plannedSchedule);
//                            for(RoutePoint routePointDeparture: route){
//                                long invoiceCreationDatePlusLoadingOperationTime = invoice.getCreationDate().getTime();
//                                invoiceCreationDatePlusLoadingOperationTime = invoiceCreationDatePlusLoadingOperationTime + routePointDeparture.getLoadingOperationsTime();
//                                Date departureDate = new Date(invoiceCreationDatePlusLoadingOperationTime);
//                                Date departureDateFromRoutePoint = new Date(routePointDeparture.getDepartureTime());
//                                if(routePointDeparture.getDeparturePoint().equals(departurePoint) && departureDateFromRoutePoint.after(departureDate)){
//                                    deliveryRoute.add(route);
//                                    possibleDeliveryRouteForInvoice.add(deliveryRoute);
//                                }
//                            }
                        }
                    }
                    }
                }
                result.put(invoice, possibleDeliveryRouteForInvoice);
            }
        return result;
    }

    public ArrayList<DeliveryRoute> getDeliveryRoute(RoutePoint routePoint, int index, Route route, Invoice invoice, PlannedSchedule plannedSchedule){
        ArrayList<DeliveryRoute> result = new ArrayList<>();
        if(index > 0){
            RoutePoint routePointPrevious = route.get(index - 1);
            long invoiceCreationDatePlusLoadingOperationTime = invoice.getCreationDate().getTime();
            invoiceCreationDatePlusLoadingOperationTime = invoiceCreationDatePlusLoadingOperationTime + routePointPrevious.getLoadingOperationsTime();
            Date departureDate = new Date(invoiceCreationDatePlusLoadingOperationTime);
            if(getDepartureDateFromEachRoutePointInRoute(route, invoice).get(routePointPrevious).after(departureDate)){
                if(routePointPrevious.getDeparturePoint().equals(invoice.getAddressOfWarehouse())){

                }else if(!routePointPrevious.getDeparturePoint().equals(invoice.getAddressOfWarehouse())){
                }
            }
        }else if(index == 0){

        }
        return result;
    }

    /** determines date when car arrives in route point
     *
     * @param route
     * @param invoice
     * @return map of route point and arrival date
     */
    public Map<RoutePoint, Date> getArrivalDateInEachRoutePointInRoute(Route route, Invoice invoice){
        Map<RoutePoint, Date> result = new HashMap<>();
        ArrayList<Date> possibleDepartureDate = getPossibleDepartureDate(route, invoice);
        if(possibleDepartureDate.size() > 0){
            for(Date date: possibleDepartureDate){
                result.put(route.get(0), date);
                for(int i = 1; i < route.size(); i++){
                    long time = date.getTime();
                    time = time + route.get(i-1).getTimeToNextPoint() + route.get(i-1).getLoadingOperationsTime();
                    date = new Date(time);
                    result.put(route.get(i), date);
                }
            }
        }
        return result;
    }

    /** determines date when car leaves route point
     *
     * @param route
     * @param invoice
     * @return map of route point and departure date
     */
    // SUNDAY IS ALWAYS THE 1ST DAY OF WEEK!
    public Map<RoutePoint, Date> getDepartureDateFromEachRoutePointInRoute(Route route, Invoice invoice){
        Map<RoutePoint, Date> result = new HashMap<>();
        Map<RoutePoint, Date> arrivalDateInEachRoutePoint = getArrivalDateInEachRoutePointInRoute(route, invoice);
        Iterator<Map.Entry<RoutePoint, Date>> iterator = arrivalDateInEachRoutePoint.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<RoutePoint, Date> entry = iterator.next();
            RoutePoint routePoint = entry.getKey();
            Date date = entry.getValue();
            for(RoutePoint rP: route){
                if(rP.getDeparturePoint().equals(routePoint.getDeparturePoint())){
                    if(rP.getDayOfWeek() == (date.getDay()+1)){
                        int[] time = route.splitToComponentTime(rP.getDepartureTime());
                        date.setHours(time[0]);
                        date.setMinutes(time[1]);
                        result.put(rP, date);
                    }else if(rP.getDayOfWeek() > (date.getDay()+1)){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.DAY_OF_MONTH, rP.getDayOfWeek()-(date.getDay()+1));
                        date.setTime(calendar.getTimeInMillis());
                        int[] time = route.splitToComponentTime(rP.getDepartureTime());
                        date.setHours(time[0]);
                        date.setMinutes(time[1]);
                        result.put(rP, date);
                    }else if(rP.getDayOfWeek() < (date.getDay() + 1)){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.DAY_OF_MONTH, (7 - ((date.getDay()+1) - rP.getDayOfWeek())));
                        date.setTime(calendar.getTimeInMillis());
                        int[] time = route.splitToComponentTime(rP.getDepartureTime());
                        date.setHours(time[0]);
                        date.setMinutes(time[1]);
                        result.put(rP, date);
                    }
                }
            }
        }
        return  result;
    }

    /** selects possible options of routes by departure time
     *
     * @param route
     * @param invoice
     * @return array of possible departure date
     */
    @Override
    public ArrayList<Date> getPossibleDepartureDate(Route route, Invoice invoice){
        ArrayList<Date> result = new ArrayList<>();
        Date invoiceCreationDate = new Date(invoice.getCreationDate().getTime());
        Date plannedDeliveryDate = invoice.getRequest().getPlannedDeliveryDate();
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
//        if(date.before(plannedDeliveryDate) && isFittingForDeliveryTime(route, invoice, date)){
//            result.add(date);
//        }
        while (date.before(plannedDeliveryDate) && isFittingForDeliveryTime(route, invoice, date)) {
            result.add(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            date = new Date(calendar.getTimeInMillis());
//            if(date.before(plannedDeliveryDate) && isFittingForDeliveryTime(route, invoice, date)) {
//                result.add(date);
//            }else{
//                break;
//            }
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
        Date plannedDeliveryTime = new Date(invoice.getRequest().getPlannedDeliveryDate().getTime());
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
        route.stream().filter(routePoint -> invoice.getRequest().getDeliveryPoint().equals(routePoint.getDeparturePoint())).forEach(routePoint -> {
            tmp.setHours(route.getActualDeliveryDateInRoutePoint(invoice.getDeliveryRoute().get(invoice.getDeliveryRoute().size()-1).get(invoice.getDeliveryRoute().get(invoice.getDeliveryRoute().size()-1).size()-1)).getHours());
            tmp.setMinutes(route.getActualDeliveryDateInRoutePoint(invoice.getDeliveryRoute().get(invoice.getDeliveryRoute().size()-1).get(invoice.getDeliveryRoute().get(invoice.getDeliveryRoute().size()-1).size()-1)).getMinutes());
            calendar.setTime(tmp);
            int countDays = route.getDaysCountOfRoute();
            calendar.add(Calendar.DAY_OF_MONTH, countDays);
        });
        Date actualDeliveryDate = new Date(calendar.getTimeInMillis());
        return actualDeliveryDate;
    }

    /**
     *
     * @param plannedSchedule
    //     * @param additionalSchedule
     * @param invoiceContainer
     * @param routesForInvoice
     * @throws ParseException
     * @throws RouteNotFoundException
     */
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
                if(routeData.get(route).isFittingForRouteByWeightAndVolume(invoice, routeData.get(route))) {
//                    invoice.setDeliveryRoute(route);
                    // maybe not first date!!!
                    invoice.setRealDepartureDate(getPossibleDepartureDate(route, invoice).get(0));
                    invoice.setInvoiceType(InvoiceType.C);
                    System.out.println("Date: " + invoice.getRealDepartureDate() + " for invoice: " + invoice.toString());
                    break;
                }
            }if (invoice.getDeliveryRoute() == null){

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
