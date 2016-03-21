package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    public Map<Invoice, List<DeliveryRoute>> filtrate(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) throws RouteNotFoundException {
        Map<Invoice, List<DeliveryRoute>> result = new HashMap<>();
        List<DeliveryRoute> possibleDeliveryRouteForInvoice = new ArrayList<>();
        Collections.sort(invoiceContainer);
        for (Invoice invoice : invoiceContainer) {
            if (invoice.getDeliveryRoute() != null)
                throw new IllegalArgumentException("invoice.getDeliveryRoutesForInvoice() should be null");
                Point deliveryPoint = invoice.getRequest().getDeliveryPoint(); // point - торговое пред-во доставка
                Point departurePoint = invoice.getAddressOfWarehouse(); // point - адрес склада отправления накладной
                possibleDeliveryRouteForInvoice = getDeliveryRoutesForInvoice(invoice, plannedSchedule);
                result.put(invoice, possibleDeliveryRouteForInvoice);
            }
        return result;
    }

    /** creates possible delivery routes of one or more routes without dates and occupancy cost
     *
     * @param invoice
     * @param plannedSchedule
     * @return array list of delivery routes
     */
    public List<DeliveryRoute> getDeliveryRoutesForInvoice(final Invoice invoice, final PlannedSchedule plannedSchedule) throws RouteNotFoundException {

        final Point departurePoint = invoice.getAddressOfWarehouse();
        final Point deliveryPoint = invoice.getRequest().getDeliveryPoint();

        List<DeliveryRoute> result = new ArrayList<>();

        // find all routes with departure point not last
        Map<Route, List<Integer>> filteredRoutesByPoint = filterRoutesByPoint(plannedSchedule, departurePoint, true);

// A B C D B K

        for (Map.Entry<Route, List<Integer>> entry: filteredRoutesByPoint.entrySet()) {
            DeliveryRoute deliveryRoute = new DeliveryRoute();
            Route route = entry.getKey();
            List<Integer> indexesOfPoint = entry.getValue();

            if (indexesOfPoint.size() == 1) {
                for (int i = indexesOfPoint.get(0) + 1; i < route.size(); i++) {
                    if(route.get(i).getDeparturePoint().equals(deliveryPoint)){
                        deliveryRoute.add(route);
                        result.add(deliveryRoute);
                        break;
                    }
                    filterRoutesByPoint(plannedSchedule, route.get(indexesOfPoint.get(0) + 1).getDeparturePoint(), true);
                }





            } else if (indexesOfPoint.size() > 1) {

            }

        }
        return result;
    }


    private void recursive(final PlannedSchedule plannedSchedule, final Point deliveryPoint, Point departurePoint, List<DeliveryRoute> result) {

        Map<Route, List<Integer>> filteredRoutesByPoint = filterRoutesByPoint(plannedSchedule, departurePoint, true);

        for (Map.Entry<Route, List<Integer>> entry: filteredRoutesByPoint.entrySet()) {
            DeliveryRoute deliveryRoute = new DeliveryRoute();
            Route route = entry.getKey();
            List<Integer> indexesOfPoint = entry.getValue();

            if (indexesOfPoint.size() == 1) {
                for (int i = indexesOfPoint.get(0) + 1; i < route.size(); i++) {
                    if(route.get(i).getDeparturePoint().equals(deliveryPoint)){
                        deliveryRoute.add(route);
                        result.add(deliveryRoute);
                        break;
                    }
                    recursive(plannedSchedule, deliveryPoint, route.get(i).getDeparturePoint(), result);
                }

            } else if (indexesOfPoint.size() > 1) {
                throw new NotImplementedException();
            }

        }
    }



    private Map<Route, List<Integer>> filterRoutesByPoint(PlannedSchedule plannedSchedule, Point point, boolean considerLastPoint) {
        Map<Route, List<Integer>> result = new HashMap<>();
        for(Route route: plannedSchedule){
            List<Integer> indexes = new ArrayList<>();
            boolean wasFound = false;
            for(RoutePoint routePoint: route){
                if(route.containsPoint(point, considerLastPoint)){
                    indexes.add(route.indexOf(routePoint));
                    wasFound = true;
                }
            }
            if(wasFound) {
                result.put(route, indexes);
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
