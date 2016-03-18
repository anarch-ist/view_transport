package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceType;

import java.text.ParseException;
import java.util.*;

public class Optimizer implements IOptimizer {
    /** Selects for invoice appropriate delivery routes without weight/volume/cost and determines type of invoice B or C
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
            if (invoice.getDeliveryRoute() != null)
                throw new IllegalArgumentException("invoice.getDeliveryRoute() should be null");
                Point deliveryPoint = invoice.getRequest().getDeliveryPoint(); // point - торговое пред-во доставка
                Point departurePoint = invoice.getAddressOfWarehouse(); // point - адрес склада отправления накладной
                for (Route route : plannedSchedule) {
                    if (route == null) {
                        throw new RouteNotFoundException("route should not be null");
                    }
                    for(RoutePoint routePointDelivery: route){
                        DeliveryRoute deliveryRoute = new DeliveryRoute();
                        if(routePointDelivery.getDeparturePoint().equals(deliveryPoint) && route.indexOf(routePointDelivery) != 0){
                            ArrayList<PairDate> pairDates = getDepartureArrivalDatesBetweenTwoRoutePoints(route, route, invoice, route.get(route.indexOf(routePointDelivery) - 1), routePointDelivery);
                            if(pairDates.size() > 0){
                                if((route.get(route.indexOf(routePointDelivery)).getDeparturePoint()).equals(departurePoint)){
                                    deliveryRoute.add(route);
                                }else {

                                }
                            }
                        }
                    }
                }
                result.put(invoice, possibleDeliveryRouteForInvoice);
            }
        return result;
    }

    /** creates possible delivery routes of one or more routes
     *
     * @param invoice
     * @param plannedSchedule
     * @return array list of delivery routes
     */
    public ArrayList<DeliveryRoute> getDeliveryRoute(Invoice invoice, PlannedSchedule plannedSchedule, Point point) throws RouteNotFoundException {
        // at the beginning point is invoice.getRequest.getPlannedDeliveryPoint
        ArrayList<DeliveryRoute> result = new ArrayList<>();
        for (Route route : plannedSchedule) {
            if (route == null) {
                throw new RouteNotFoundException("route should not be null");
            }
            for(RoutePoint routePoint: route){
                DeliveryRoute deliveryRoute = new DeliveryRoute();
                if(routePoint.getDeparturePoint().equals(point) && route.indexOf(routePoint) != 0){
                    for(int i = route.indexOf(routePoint) - 1; i >= 0; i--) {
                        ArrayList<PairDate> pairDates = getDepartureArrivalDatesBetweenTwoRoutePoints(route, route, invoice, route.get(i), routePoint);
                        if (pairDates.size() > 0) {
                            if ((route.get(route.indexOf(routePoint)).getDeparturePoint()).equals(invoice.getAddressOfWarehouse())) {
                                deliveryRoute.add(route);
                                result.add(0, deliveryRoute);
                                System.out.println(result.size());
                                break;
                            }
                        }
                    }
                    point = route.get(0).getDeparturePoint();
                    getDeliveryRoute(invoice, plannedSchedule, point);
                }
            }
        }
        return result;
    }

    /** determines specific delivery route for invoices' type C by occupancy cost, sets delivery route
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @param routesForInvoice
     * @throws ParseException
     * @throws RouteNotFoundException
     */
    @Override
    public void optimize(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<DeliveryRoute>> routesForInvoice) throws ParseException, RouteNotFoundException {
        Iterator<Map.Entry<Invoice, ArrayList<DeliveryRoute>>> iterator = routesForInvoice.entrySet().iterator();
        for(Invoice invoice: invoiceContainer){
            if(invoice.getInvoiceType().equals(InvoiceType.C)){

            }
        }
    }

    /** shifts invoices' type B to send the maximum number of invoices on time
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @param routesForInvoice
     */
    @Override
    public ArrayList<Invoice> shift (PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<DeliveryRoute>> routesForInvoice){
        ArrayList<Invoice> result = new ArrayList<>();
        return result;
    }

    /** defines options of delivery routes for invoices' type B using additional schedule, sets type A for unrealized invoices
     *
     * @param additionalSchedule
     * @return map with invoice(key) and delivery routes(values)
     * @throws RouteNotFoundException
     */
    @Override
    public Map<Invoice, ArrayList<DeliveryRoute>> useAdditionalSchedule(AdditionalSchedule additionalSchedule, PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<DeliveryRoute>> routesForInvoice) throws RouteNotFoundException{
        Map<Invoice, ArrayList<DeliveryRoute>> result = new HashMap<>();
        ArrayList<Invoice> unsentInvoicesByPlannedSchedule = shift(plannedSchedule, invoiceContainer, routesForInvoice);
        return result;
    }

    /** selects possible options of departure date from certain route point
     *
     * @param route
     * @param invoice
     * @return array of possible departure date from route point
     */
    @Override
    public ArrayList<Date> getPossibleDepartureDateFromRoutePoint(Route route, RoutePoint routePoint, Invoice invoice){
        ArrayList<Date> result = new ArrayList<>();
        Date invoiceCreationDateAndLoadingTime = new Date(invoice.getCreationDate().getTime() + routePoint.getLoadingOperationsTime());
        Date plannedDeliveryDate = invoice.getRequest().getPlannedDeliveryDate();
        int timeOfCreation = invoiceCreationDateAndLoadingTime.getHours()*60 + invoiceCreationDateAndLoadingTime.getMinutes();
        int[] timeDeparture = route.splitToComponentTime(routePoint.getDepartureTime());
        invoiceCreationDateAndLoadingTime.setHours(timeDeparture[0]);
        invoiceCreationDateAndLoadingTime.setMinutes(timeDeparture[1]);
        Date date = invoiceCreationDateAndLoadingTime;
        int differenceWeekDays = routePoint.getDayOfWeek() - (invoiceCreationDateAndLoadingTime.getDay()+1);
        if(differenceWeekDays > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
            date.setTime(calendar.getTimeInMillis());
        }else if(differenceWeekDays == 0 &&(timeOfCreation < routePoint.getDepartureTime())){
            date = invoiceCreationDateAndLoadingTime;
        }else {
            int tmp = 7 - ((invoiceCreationDateAndLoadingTime.getDay()+1) - routePoint.getDayOfWeek());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, tmp);
            date.setTime(calendar.getTimeInMillis());
        }
        while (date.before(plannedDeliveryDate)){
            if(route.indexOf(routePoint) == (route.size() - 1)){

                result.add(date);
            }else {
                result.add(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                date = new Date(calendar.getTimeInMillis());
            }
        }
        return result;
    }

    /** determines date when car arrives in each route point
     *
     * @param route
     * @return map of route point and arrival date
     */
    public Map<RoutePoint, ArrayList<Date>> getArrivalDateInEachRoutePointInRoute(Route route, Invoice invoice){
        Map<RoutePoint, ArrayList<Date>> result = new HashMap<>();
        if(route != null){
            result.put(route.get(0), getPossibleDepartureDateFromRoutePoint(route, route.get(0), invoice));
            for(int i = 1; i < route.size(); i++){
                ArrayList<Date> newDate = new ArrayList<>();
                ArrayList<Date> dates = getPossibleDepartureDateFromRoutePoint(route, route.get(i - 1), invoice);
                for(Date date: dates){
                    long time = date.getTime() + route.get(i - 1).getTimeToNextPoint() * 60000; // + route.get(i).getLoadingOperationsTime() * 60000 ???
                    Date tmp = new Date(time);
                    newDate.add(tmp);
                }
                result.put(route.get(i), newDate);
            }
        }
        return result;
    }

    @Override
    public ArrayList<PairDate> getDepartureArrivalDatesBetweenTwoRoutePoints(Route departureRoute, Route arrivalRoute, Invoice invoice, RoutePoint departureRoutePoint, RoutePoint arrivalRoutePoint){
        ArrayList<PairDate> result = new ArrayList<>();
        ArrayList<Date> departureDates = getPossibleDepartureDateFromRoutePoint(departureRoute, departureRoutePoint, invoice);
        ArrayList<Date> arrivalDates = getArrivalDateInEachRoutePointInRoute(arrivalRoute, invoice).get(arrivalRoutePoint);
        for(int i = 0; i < arrivalDates.size(); i++){
            if(arrivalDates.get(i).after(invoice.getRequest().getPlannedDeliveryDate())){
                break;
            }else {
                PairDate pairDate = new PairDate(departureDates.get(i), arrivalDates.get(i));
                result.add(pairDate);
            }
        }
        return result;
    }


    /** determines day of week by date where SUNDAY is the 1-st day
     *
     * @param date
     * @return day of week from date
     */
    public int getWeekDay(Date date) {
        return (date.getDay()+1);
    }
}
