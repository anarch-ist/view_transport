package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.UpdatedRoute;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InformationStack;
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
        Collections.sort(invoiceContainer);
        for (Invoice invoice : invoiceContainer) {
            if (invoice.getDeliveryRoute() != null)
                throw new IllegalArgumentException("invoice.getDeliveryRoutesForInvoice() should be null");
            List<DeliveryRoute> possibleDeliveryRouteForInvoice = new ArrayList<>();
            Point deliveryPoint = invoice.getRequest().getDeliveryPoint(); // point - торговое пред-во, доставка
            Point departurePoint = invoice.getAddressOfWarehouse(); // point - адрес склада, отправления накладной
            List<Point> markedPoints = new ArrayList<>();
            List<DeliveryRoute> deliveryRoutes = new ArrayList<>();
            possibleDeliveryRouteForInvoice = startRecursive(plannedSchedule, departurePoint, deliveryPoint, deliveryRoutes, markedPoints);
            result.put(invoice, possibleDeliveryRouteForInvoice);
        }
        return result;
    }

    /** creates possible delivery routes of one or more routes by departure and delivery points without dates and occupancy cost
     *
     * @param plannedSchedule
     * @param departurePoint
     * @param deliveryPoint
     * @param result
     * @param markedPoints
     * @return list of possible delivery routes
     */
    public List<DeliveryRoute> startRecursive(final PlannedSchedule plannedSchedule, final Point departurePoint, final Point deliveryPoint, List<DeliveryRoute> result, List<Point> markedPoints) {
        List<DeliveryRoute> possibleDeliveryRoutes = new ArrayList<>();
        Map<UpdatedRoute, List<Integer>> filteredRoutesByPoint = filterRoutesByPoint(plannedSchedule, departurePoint, false);
        InformationStack informationStack = new InformationStack();
        informationStack.setDeep(1);
        informationStack.appendPointsHistory(departurePoint);
        informationStack.addPoint(departurePoint);
        rec(plannedSchedule, deliveryPoint, result, markedPoints, filteredRoutesByPoint, informationStack);
        for(DeliveryRoute deliveryRoute: result){
            for (TrackCourse trackCourse: deliveryRoute){
                if(trackCourse.getEndTrackCourse().getPoint().getPointId().equals(deliveryPoint.getPointId())){
                    DeliveryRoute tmp = removeDuplicates(deliveryRoute);
                    possibleDeliveryRoutes.add(tmp);
                    break;
                }
            }
        }
        return possibleDeliveryRoutes;
    }

    private DeliveryRoute removeDuplicates(DeliveryRoute deliveryRoute){
        DeliveryRoute result = new DeliveryRoute();
        for(int i = 0; i < deliveryRoute.size(); i++){
            int count = 1;
            for(int j = 1; j < deliveryRoute.size(); j++){
                if(deliveryRoute.get(i).equals(deliveryRoute.get(j))){
                    count++;
                }
            }
            if(count == 1 && (!result.contains(deliveryRoute.get(i)))){
                result.add(deliveryRoute.get(i));
            }else if(count > 1 && (!result.contains(deliveryRoute.get(i)))){
                result.add(deliveryRoute.get(i));
            }
        }
        return result;
    }

    private void rec(final PlannedSchedule plannedSchedule, final Point deliveryPoint, List<DeliveryRoute> result, List<Point> markedPoints, Map<UpdatedRoute, List<Integer>> filteredRoutesByPoint, InformationStack informationStack) {
        for (Map.Entry<UpdatedRoute, List<Integer>> entry: filteredRoutesByPoint.entrySet()) {
            UpdatedRoute route = entry.getKey();
            List<Integer> indexesOfPoint = entry.getValue();
            // if departure point occurs once
            if (indexesOfPoint.size() == 1) {
                Integer indexOfPointInRoute = indexesOfPoint.get(0);
                Point pointInRoute = route.makePointsInRoute().get(indexOfPointInRoute);
                if(!pointInRoute.equals(deliveryPoint)) {
                    markedPoints.add(pointInRoute);
                }
                for (int i = indexOfPointInRoute - 1; i < route.size(); i++) {
                    Point point = route.makePointsInRoute().get(i);
                    System.out.println("infStack = " + informationStack);
                    System.out.println("currentRoute = "+route.getPointsAsString());
                    System.out.println("currentPoint = " + point.getPointId());
                    //System.out.println("indexOfPointInRoute = " + i);
                    System.out.print("markedPoints = ");
                    for (Point point1: markedPoints) {
                        System.out.print(point1.getPointId() + " ");
                    }
                    System.out.println("");
                    System.out.println("_______________________");
                    if(!markedPoints.contains(point)) {
                        if(point.equals(deliveryPoint)){
                            DeliveryRoute deliveryRoute = new DeliveryRoute();
//                            deliveryRoute.add(route.makePointsInRoute().get(i));
                            List<Point> pointsId = new ArrayList<>();
                            pointsId.addAll(informationStack.getPointsForInvoice());
                            pointsId.add(deliveryPoint);
                            for(Point point1: pointsId){
                                System.out.print(point1.getPointId() + " ");
                            }
                            System.out.println(pointsId.size());
                            List<UpdatedRoute> routesId = new LinkedList<>();
                            routesId.addAll(informationStack.getUpdatedRoutesForInvoice());
                            routesId.add(route);
                            TrackCourse trackCourse = new TrackCourse();
                            List<TrackCourse> trackCourses = new ArrayList<>(); /// FIX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            trackCourses.add(route.get(i));
                            for(TrackCourse correctTrackCourse: trackCourses) {
                                deliveryRoute.add(correctTrackCourse);
//                                System.out.println("Track Courses, start point id = " + correctTrackCourse.getStartTrackCourse().getDeparturePoint().getPointId() + ", end point id = " + correctTrackCourse.getEndTrackCourse().getDeparturePoint().getPointId());
                            }
                            result.add(deliveryRoute);
                            System.out.println("FIND delivery point!");
                            break;
                        }
                        InformationStack newInformationStack = new InformationStack(informationStack);
                        newInformationStack.appendPointsHistory(point);
                        newInformationStack.appendUpdatedRoutesHistory(route);
                        newInformationStack.addPoint(point);
                        newInformationStack.addUpdatedRoute(route);
                        newInformationStack.setDeep(informationStack.getDeep() + 1);
                        Map<UpdatedRoute, List<Integer>> filteredRoutesByPointNew = filterRoutesByPoint(plannedSchedule, point, true);
                        rec(plannedSchedule, deliveryPoint, result, markedPoints, filteredRoutesByPointNew, newInformationStack);
                    }
                }
                // if departure point occurs more then once
            } else if (indexesOfPoint.size() > 1) {
                throw new NotImplementedException();
            }
        }
    }

    /** find all routes that contains point. if true -> all routes, if false -> without routes where the point is last
     *
     * @param plannedSchedule
     * @param point
     * @param considerLastPoint
     * @return
     */
    protected Map<UpdatedRoute, List<Integer>> filterRoutesByPoint(PlannedSchedule plannedSchedule, Point point, boolean considerLastPoint) {
        Map<UpdatedRoute, List<Integer>> result = new HashMap<>();
        for(UpdatedRoute updatedRoute: plannedSchedule){
            List<Integer> indexes = new ArrayList<>();
            List<Point> points = updatedRoute.makePointsInRoute();
            boolean wasFound = false;
            if (considerLastPoint) {
                for(Point pointFromRoute: points) {
                    if(updatedRoute.containsPoint(point) && pointFromRoute.equals(point)){
                        indexes.add(points.indexOf(pointFromRoute));
                        wasFound = true;
                    }
//                     if((updatedRoute.size() - 1) == updatedRoute.indexOf(trackCourse)){
//                        if(updatedRoute.containsPoint(point) && trackCourse.getEndTrackCourse().getPoint().equals(point)){
//                            indexes.add(updatedRoute.indexOf(trackCourse));
//                            wasFound = true;
//                        }
//                    }
                }
            } else {
                for(Point pointFromRoute: points) {
                    if(!updatedRoute.isLastPoint(point) && updatedRoute.containsPoint(point) && pointFromRoute.equals(point)){
                        indexes.add(points.indexOf(pointFromRoute));
                        wasFound = true;
                    }
                }
            }
            if(wasFound) {
                result.put(updatedRoute, indexes);
            }
        }
        String routes = "";
        for (UpdatedRoute route : result.keySet()) {
            routes+=route.getPointsAsString()+" ";
        }
//        System.out.println("filtrated routes for point " + point.getPointId() + " :" + routes);
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
    public void optimize(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, List<DeliveryRoute>> routesForInvoice) throws ParseException, RouteNotFoundException {
        Iterator<Map.Entry<Invoice, List<DeliveryRoute>>> iterator = routesForInvoice.entrySet().iterator();
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
//    @Override
//    public ArrayList<Date> getPossibleDepartureDateFromRoutePoint(Route route, RoutePoint routePoint, Invoice invoice){
//        ArrayList<Date> result = new ArrayList<>();
//        Date invoiceCreationDateAndLoadingTime = new Date(invoice.getCreationDate().getTime() + routePoint.getLoadingOperationsTime());
//        Date plannedDeliveryDate = invoice.getRequest().getPlannedDeliveryDate();
//        int timeOfCreation = invoiceCreationDateAndLoadingTime.getHours()*60 + invoiceCreationDateAndLoadingTime.getMinutes();
//        int[] timeDeparture = route.splitToComponentTime(routePoint.getDepartureTime());
//        invoiceCreationDateAndLoadingTime.setHours(timeDeparture[0]);
//        invoiceCreationDateAndLoadingTime.setMinutes(timeDeparture[1]);
//        Date date = invoiceCreationDateAndLoadingTime;
//        int differenceWeekDays = routePoint.getDayOfWeek() - (invoiceCreationDateAndLoadingTime.getDay()+1);
//        if(differenceWeekDays > 0){
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
//            date.setTime(calendar.getTimeInMillis());
//        }else if(differenceWeekDays == 0 &&(timeOfCreation < routePoint.getDepartureTime())){
//            date = invoiceCreationDateAndLoadingTime;
//        }else {
//            int tmp = 7 - ((invoiceCreationDateAndLoadingTime.getDay()+1) - routePoint.getDayOfWeek());
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, tmp);
//            date.setTime(calendar.getTimeInMillis());
//        }
//        while (date.before(plannedDeliveryDate)){
//            if(route.indexOf(routePoint) == (route.size() - 1)){
//
//                result.add(date);
//            }else {
//                result.add(date);
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                calendar.add(Calendar.DAY_OF_MONTH, 7);
//                date = new Date(calendar.getTimeInMillis());
//            }
//        }
//        return result;
//    }

//    /** determines date when car arrives in each route point
//     *
//     * @param route
//     * @return map of route point and arrival date
//     */
//    public Map<RoutePoint, ArrayList<Date>> getArrivalDateInEachRoutePointInRoute(Route route, Invoice invoice){
//        Map<RoutePoint, ArrayList<Date>> result = new HashMap<>();
//        if(route != null){
//            result.put(route.get(0), getPossibleDepartureDateFromRoutePoint(route, route.get(0), invoice));
//            for(int i = 1; i < route.size(); i++){
//                ArrayList<Date> newDate = new ArrayList<>();
//                ArrayList<Date> dates = getPossibleDepartureDateFromRoutePoint(route, route.get(i - 1), invoice);
//                for(Date date: dates){
//                    long time = date.getTime() + route.get(i - 1).getTimeToNextPoint() * 60000; // + route.get(i).getLoadingOperationsTime() * 60000 ???
//                    Date tmp = new Date(time);
//                    newDate.add(tmp);
//                }
//                result.put(route.get(i), newDate);
//            }
//        }
//        return result;
//    }


    /** determines day of week by date where SUNDAY is the 1-st day
     *
     * @param date
     * @return day of week from date
     */
    public int getWeekDay(Date date) {
        return (date.getDay()+1);
    }
}
