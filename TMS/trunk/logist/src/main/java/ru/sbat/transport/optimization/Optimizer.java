package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.*;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InformationStack;
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
            List<Point> points = new ArrayList<>();
            Point deliveryPoint = invoice.getRequest().getDeliveryPoint(); // point - торговое пред-во доставка
            Point departurePoint = invoice.getAddressOfWarehouse(); // point - адрес склада отправления накладной
            possibleDeliveryRouteForInvoice = startRecursive(plannedSchedule, departurePoint, deliveryPoint, possibleDeliveryRouteForInvoice, points);
            result.put(invoice, possibleDeliveryRouteForInvoice);
            }
        return result;
    }

    /** creates possible delivery routes of one or more routes without dates and occupancy cost
     *
     * @param plannedSchedule
     * @param departurePoint
     * @param deliveryPoint
     * @param result
     * @param markedPoints
     * @return array list of delivery routes
     */
    protected List<DeliveryRoute> startRecursive(final PlannedSchedule plannedSchedule, final Point departurePoint, final Point deliveryPoint, List<DeliveryRoute> result, List<Point> markedPoints) {
        List<DeliveryRoute> possibleDeliveryRoutes = new ArrayList<>();
        Map<IRoute, List<Integer>> filteredRoutesByPoint = filterRoutesByPoint(plannedSchedule, departurePoint, false);
        InformationStack informationStack = new InformationStack();
        informationStack.setDeep(1);
        informationStack.appendPointsHistory(departurePoint);
        informationStack.addPoint(departurePoint);
        informationStack.addMarkedPoint(departurePoint);
        informationStack.appendMarkedPointsHistory(departurePoint);
        rec(plannedSchedule, deliveryPoint, result, markedPoints, filteredRoutesByPoint, informationStack);
        for(DeliveryRoute deliveryRoute: result){
            for (TrackCourse trackCourse: deliveryRoute){
                if(trackCourse.getEndTrackCourse().getPoint().getPointId().equals(deliveryPoint.getPointId())){
                    DeliveryRoute tmp = removeDuplicates(deliveryRoute);
                    if(isCorrectDeliveryRoute(tmp, departurePoint, deliveryPoint)){
                        possibleDeliveryRoutes.add(tmp);
                        break;
                    }
                }
            }
        }
        return possibleDeliveryRoutes;
    }

    private void rec(final PlannedSchedule plannedSchedule, final Point deliveryPoint, List<DeliveryRoute> result, List<Point> markedPoints, Map<IRoute, List<Integer>> filteredRoutesByPoint, InformationStack informationStack) {
        for (Map.Entry<IRoute, List<Integer>> entry: filteredRoutesByPoint.entrySet()) {
            IRoute route = entry.getKey();
            List<Integer> indexesOfPoint = entry.getValue();
            // if departure point occurs once
            if (indexesOfPoint.size() == 1) {

                Integer indexOfPointInRoute = indexesOfPoint.get(0);
                Point pointInRoute = route.getRoutePoint(indexOfPointInRoute).getPoint();
                if(!pointInRoute.equals(deliveryPoint)) {
                    markedPoints.add(pointInRoute);
                }
                // last point of route
                if ((indexOfPointInRoute + 1) == route.getRouteSize()) {

//                    Point newDeparturePoint = route.get(indexOfPointInRoute).getPoint();
//                    Map<Route, List<Integer>> filteredRoutesByPointNew = filterRoutesByPoint(plannedSchedule, newDeparturePoint, false);
//                    rec(plannedSchedule, deliveryPoint, result, markedPoints, filteredRoutesByPointNew, informationStack);
                    // if middle point of route
                } else {
                    for (int i = indexOfPointInRoute + 1; i < route.getRouteSize(); i++) {

                        Point point = route.getRoutePoint(i).getPoint();

//                        System.out.println("infStack = " + informationStack);
//                        System.out.println("currentRoute = "+route.getPointsAsString());
//                        System.out.println("currentPoint = " + point.getPointId());
//                        //System.out.println("indexOfPointInRoute = " + i);
//                        System.out.print("markedPoints = ");
//                        for (Point point1: markedPoints) {
//                            System.out.print(point1.getPointId() + " ");
//                        }
//                        System.out.print("NewmarkedPoints = ");
//                        for (Point point1: informationStack.getMarkedPoints()) {
//                            System.out.print(point1.getPointId() + " ");
//                        }
//                        System.out.println("");
//                        System.out.println("_______________________");

                        if(!informationStack.getMarkedPoints().contains(point)) {

                            if(point.equals(deliveryPoint)){
                                DeliveryRoute deliveryRoute = new DeliveryRoute();
                                List<Point> pointsId = new ArrayList<>();
                                pointsId.addAll(informationStack.getPointsForInvoice());
                                pointsId.add(deliveryPoint);
//                                for(Point point1: pointsId){
//                                    System.out.print(point1.getPointId() + " ");
//                                }
//                                System.out.println(pointsId.size());
                                List<IRoute> routesId = new LinkedList<>();
                                routesId.addAll(informationStack.getRoutesForInvoice());
                                routesId.add(route);
                                TrackCourse trackCourse = new TrackCourse();
                                List<TrackCourse> trackCourses = trackCourse.sharePointsBetweenRoutes(pointsId, routesId);
                                for(TrackCourse correctTrackCourse: trackCourses) {
                                    deliveryRoute.add(correctTrackCourse);
//                                    System.out.println("Track Courses, start point id = " + correctTrackCourse.getStartTrackCourse().getPoint().getPointId() + ", end point id = " + correctTrackCourse.getEndTrackCourse().getPoint().getPointId());
                                }
                                result.add(deliveryRoute);
//                                System.out.println("FIND delivery point!");
                                break;
                            }
                            InformationStack newInformationStack = new InformationStack(informationStack);
                            newInformationStack.appendPointsHistory(point);
                            newInformationStack.appendRoutesHistory(route);
                            newInformationStack.addPoint(point);
                            newInformationStack.addRoute(route);
                            newInformationStack.setDeep(informationStack.getDeep() + 1);
                            newInformationStack.addMarkedPoint(point);
                            newInformationStack.appendMarkedPointsHistory(point);
                            Map<IRoute, List<Integer>> filteredRoutesByPointNew = filterRoutesByPoint(plannedSchedule, point, true);
                            rec(plannedSchedule, deliveryPoint, result, markedPoints, filteredRoutesByPointNew, newInformationStack);
                        }
                    }
                }
                // if departure point occurs more then once
            } else if (indexesOfPoint.size() > 1) {
                throw new NotImplementedException();
            }
        }
    }

    /** remove duplicates of track courses in delivery routes after recursive
     *
     * @param deliveryRoute
     * @return correct delivery route from track courses
     */
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

    /** determines if delivery route contains of departure and delivery points
     *
     * @param deliveryRoute
     * @param departurePoint
     * @param deliveryPoint
     * @return true if contains
     */
    private boolean isCorrectDeliveryRoute(DeliveryRoute deliveryRoute, Point departurePoint, Point deliveryPoint){
        if(!deliveryRoute.get(0).getStartTrackCourse().getPoint().getPointId().equals(departurePoint.getPointId())
                || !deliveryRoute.get(deliveryRoute.size() - 1).getEndTrackCourse().getPoint().getPointId().equals(deliveryPoint.getPointId())){
            return false;
        }
        for(int i = 0; i < deliveryRoute.size() - 1; i++){
            if(!deliveryRoute.get(i).getEndTrackCourse().getPoint().getPointId().equals(deliveryRoute.get(i + 1).getStartTrackCourse().getPoint().getPointId())){
                return false;
            }
        }
        return true;
    }

    /**
     * find all routes that contains point. if true -> all routes, if false -> without last
     * @param plannedSchedule
     * @param point
     * @param considerLastPoint
     * @return
     */
    protected Map<IRoute, List<Integer>> filterRoutesByPoint(PlannedSchedule plannedSchedule, Point point, boolean considerLastPoint) {
        Map<IRoute, List<Integer>> result = new HashMap<>();
        for(IRoute route: plannedSchedule){
            List<Integer> indexes = new ArrayList<>();
            boolean wasFound = false;
            if (considerLastPoint) {

                for(RoutePoint routePoint: route.asRoutePointCollection()) {
                    if(route.containsPoint(point) && routePoint.getPoint().equals(point)){
                        indexes.add(route.getIndexOfRoutePoint(routePoint));
                        wasFound = true;
                    }
                }
            } else {
                for(RoutePoint routePoint: route.asRoutePointCollection()) {
                    if(!route.isLastPoint(point) && route.containsPoint(point) && routePoint.getPoint().equals(point)){
                        indexes.add(route.getIndexOfRoutePoint(routePoint));
                        wasFound = true;
                    }
                }
            }

            if(wasFound) {
                result.put(route, indexes);
            }
        }
        String routes = "";
        for (IRoute route : result.keySet()) {
            routes+=route.getPointsAsString()+" ";
        }
//        System.out.println("filtrated routes for point " + point.getPointId() + " :" + routes);
        return result;
    }

    /** selects specific delivery route for invoices' type C by occupancy cost, sets delivery route: track course and its number of week
     *
     * @param plannedSchedule
     * @param routesForInvoice
     * @throws ParseException
     * @throws RouteNotFoundException
     */
    @Override
    public void optimize(PlannedSchedule plannedSchedule, Map<Invoice, List<DeliveryRoute>> routesForInvoice) throws ParseException, RouteNotFoundException, IncorrectRequirement {
        Iterator<Map.Entry<Invoice, List<DeliveryRoute>>> iterator = routesForInvoice.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Invoice, List<DeliveryRoute>> entry = iterator.next();
            Invoice invoice = entry.getKey();

            if(routesForInvoice.get(invoice).size() == 0){
                throw new RouteNotFoundException("invoice should have options of delivery routes");
            }else {
                List<DeliveryRoute> deliveryRoutes = routesForInvoice.get(invoice);
                for(DeliveryRoute deliveryRoute: deliveryRoutes){
                    List<PartOfDeliveryRoute> partOfDeliveryRouteList = new ArrayList<>();
                    TrackCourse beginOfDeliveryRoute = deliveryRoute.get(0);
                    List<Integer> countOfWeek = invoice.calculateNumbersOfWeeksBetweenDates(invoice.getCreationDate(), invoice.getRequest().getPlannedDeliveryDate(),
                            invoice, beginOfDeliveryRoute, deliveryRoute.get(deliveryRoute.size() - 1));
                    System.out.println(countOfWeek);
                    if(countOfWeek.size() == 0 || !isMadeChainOfTrackCourses(deliveryRoute, countOfWeek)){
                        continue;
                    }

                    double invoiceCost = invoice.getCost();
                    TrackCourse trackCourseForCompare = beginOfDeliveryRoute;
                    System.out.println(trackCourseForCompare);
                    int index = 0;
                    for(int y = 0; y < countOfWeek.size(); y++) {
                        int numberOfWeek = countOfWeek.get(y);
                        if (isFittingTrackCourseByLoadCostAndNumberOfWeek(invoice, trackCourseForCompare, numberOfWeek)) {
                            loadTrackCourse(trackCourseForCompare, numberOfWeek, invoiceCost);
                            addPartsOfDeliveryRoute(trackCourseForCompare, numberOfWeek, partOfDeliveryRouteList);
                            index = y;
                            break;
                        }
                    }
                    if(partOfDeliveryRouteList.size() == 0) {
                        continue;
                    }
                    for (int i = 1; i < deliveryRoute.size(); i++) {
                        TrackCourse currentTrackCourse = deliveryRoute.get(i);
                        System.out.println(currentTrackCourse);
                        for(LoadUnit loadUnit: currentTrackCourse.getLoadUnits()) {
                            System.out.println("loadCost" + loadUnit.getLoadCost());
                        }
                        for (int j = index; j < countOfWeek.size(); j++) {
                            int numberOfWeek = countOfWeek.get(j);
                            if (isTheSameWeek(trackCourseForCompare, currentTrackCourse)) {
                                if (isFittingTrackCourseByLoadCostAndNumberOfWeek(invoice, currentTrackCourse, numberOfWeek)) {
                                    loadTrackCourse(currentTrackCourse, numberOfWeek, invoiceCost);
                                    addPartsOfDeliveryRoute(currentTrackCourse, numberOfWeek, partOfDeliveryRouteList);
                                    trackCourseForCompare = currentTrackCourse;
                                    for(LoadUnit loadUnit: currentTrackCourse.getLoadUnits()) {
                                        System.out.println("loadCost = " + loadUnit.getLoadCost());
                                    }
                                    index = j;
                                    break;
                                }
                            }else if (!isTheSameWeek(trackCourseForCompare, currentTrackCourse)) {
                                j++;
                                if (isFittingTrackCourseByLoadCostAndNumberOfWeek(invoice, currentTrackCourse, numberOfWeek)) {
                                    loadTrackCourse(currentTrackCourse, numberOfWeek, invoiceCost);
                                    addPartsOfDeliveryRoute(currentTrackCourse, numberOfWeek, partOfDeliveryRouteList);
                                    trackCourseForCompare = currentTrackCourse;
                                    index = j;
                                    break;
                                }
                            }
                        }
                    }
                    if(partOfDeliveryRouteList.size() != 0 && partOfDeliveryRouteList.get(0).getTrackCourse().getStartTrackCourse().getPoint().getPointId().equals(invoice.getAddressOfWarehouse().getPointId()) &&
                            partOfDeliveryRouteList.get(partOfDeliveryRouteList.size() - 1).getTrackCourse().getEndTrackCourse().getPoint().getPointId().equals(invoice.getRequest().getDeliveryPoint().getPointId())){
                        invoice.setPartsOfDeliveryRoute(partOfDeliveryRouteList);
                        break;
                    }
                }
            }
        }
    }

    /** add in invoice's parts of delivery routes track course and number of week
     *
     * @param trackCourse
     * @param numberOfWeek
     * @param partOfDeliveryRoutes
     */
    public void addPartsOfDeliveryRoute(TrackCourse trackCourse, int numberOfWeek, List<PartOfDeliveryRoute> partOfDeliveryRoutes){
        PartOfDeliveryRoute partOfDeliveryRoute = new PartOfDeliveryRoute();
        partOfDeliveryRoute.setTrackCourse(trackCourse);
        partOfDeliveryRoute.setNumberOfWeek(numberOfWeek);
        partOfDeliveryRoutes.add(partOfDeliveryRoute);
    }

    /** add cost to load units of track course
     *
     * @param trackCourse
     * @param numberOfWeek
     * @param invoiceCost
     */
    public void loadTrackCourse(TrackCourse trackCourse, int numberOfWeek, double invoiceCost){
        List<LoadUnit> loadUnits = trackCourse.getLoadUnits();
        int count = 0;
        for (LoadUnit loadUnit : loadUnits) {
            if (loadUnit.getNumberOfWeek() == numberOfWeek) {
                count++;
                loadUnit.setLoadCost(loadUnit.getLoadCost() + invoiceCost);
            }
        }
        if (count == 0 || loadUnits.size() == 0) {
            LoadUnit loadUnit = new LoadUnit();
            loadUnit.setNumberOfWeek(numberOfWeek);
            loadUnit.setLoadCost(invoiceCost);
            trackCourse.getLoadUnits().add(loadUnit);
        }
    }


    /** determines if it's enough counts of week between creation date and planned delivery date by exact delivery route
     *
     * @param deliveryRoute
     * @param numberOfWeeks
     * @return true if delivery route is fitting for delivery by dates
     * @throws IncorrectRequirement
     */
    public boolean isMadeChainOfTrackCourses(DeliveryRoute deliveryRoute, List<Integer> numberOfWeeks) throws IncorrectRequirement {
        int countOfWeeks = 1;
        for(int i = 0; i < deliveryRoute.size() - 1; i++){
            TrackCourse arrivalToEndRoutePoint = deliveryRoute.get(i);
            TrackCourse departureRoutePoint = deliveryRoute.get(i + 1);
            if(!isTheSameWeek(arrivalToEndRoutePoint, departureRoutePoint)){
                countOfWeeks++;
            }
        }
        if(countOfWeeks > numberOfWeeks.size()){
            return false;
        }
        return true;
    }

    /** determines if invoice is fitting for loading on exact track course on exact week
     *
     * @param invoice
     * @param trackCourse
     * @param numberOfWeek
     * @return true if invoice can be loading on this week in track course
     */
    public boolean isFittingTrackCourseByLoadCostAndNumberOfWeek(Invoice invoice, TrackCourse trackCourse, int numberOfWeek){
        double invoiceCost = invoice.getCost();
        if(trackCourse.getLoadUnits().size() == 0){
            return true;
        }else {
            List<LoadUnit> loadUnits = trackCourse.getLoadUnits();
            for(LoadUnit loadUnit: loadUnits){
                if(loadUnit.getNumberOfWeek() == numberOfWeek){
                    if((loadUnit.getLoadCost() + invoiceCost) > trackCourse.getRoute().getCharacteristicsOfCar().getOccupancyCost()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** determines if two nearby track courses can be arrival on the same week on not
     *
     * @param arrivalToEndRoutePoint
     * @param departureFromStartRoutePoint
     * @return true if they are on the one week
     * @throws IncorrectRequirement
     */
    public boolean isTheSameWeek(TrackCourse arrivalToEndRoutePoint, TrackCourse departureFromStartRoutePoint) throws IncorrectRequirement {
        int arrivalTimeToEndRoutePointInTrackCourse = arrivalToEndRoutePoint.getRoute().getActualArrivalTimeInRoutePoint(arrivalToEndRoutePoint.getEndTrackCourse());
        int arrivalDayOfWeekToEndRoutePointInTrackCourse = arrivalToEndRoutePoint.getRoute().getWeekDayOfActualArrivalDateInRoutePoint(arrivalToEndRoutePoint.getEndTrackCourse());
        int departureTimeFromStartRoutePointInTrackCourse = departureFromStartRoutePoint.getRoute().getActualDepartureTimeFromRoutePoint(departureFromStartRoutePoint.getStartTrackCourse());
        int departureDayOfWeekFromStartRoutePointInTrackCourse = departureFromStartRoutePoint.getRoute().getWeekDayOfActualDepartureDateFromRoutePoint(departureFromStartRoutePoint.getStartTrackCourse());
        int loadingOperationTimeInDepartureRoutePoint = departureFromStartRoutePoint.getStartTrackCourse().getLoadingOperationsTime();
        int arrivalTimeWithNextLoading = loadingOperationTimeInDepartureRoutePoint + arrivalTimeToEndRoutePointInTrackCourse;

        if(arrivalTimeWithNextLoading >= 1440){
            arrivalTimeWithNextLoading = arrivalTimeWithNextLoading - 1440;
            arrivalDayOfWeekToEndRoutePointInTrackCourse++;
            if(arrivalDayOfWeekToEndRoutePointInTrackCourse > 7){
                arrivalDayOfWeekToEndRoutePointInTrackCourse = arrivalDayOfWeekToEndRoutePointInTrackCourse - 7;
            }
        }

        if(departureTimeFromStartRoutePointInTrackCourse >= 1440){
            departureTimeFromStartRoutePointInTrackCourse = departureTimeFromStartRoutePointInTrackCourse - 1440;
            departureDayOfWeekFromStartRoutePointInTrackCourse++;
            if(departureDayOfWeekFromStartRoutePointInTrackCourse > 7){
                departureDayOfWeekFromStartRoutePointInTrackCourse = departureDayOfWeekFromStartRoutePointInTrackCourse - 7;
            }
        }

        if (arrivalDayOfWeekToEndRoutePointInTrackCourse == 1) {
            if ((departureDayOfWeekFromStartRoutePointInTrackCourse == 1 && departureTimeFromStartRoutePointInTrackCourse < arrivalTimeWithNextLoading) || arrivalDayOfWeekToEndRoutePointInTrackCourse < departureDayOfWeekFromStartRoutePointInTrackCourse) {
                return false;
            }
        } else if (arrivalDayOfWeekToEndRoutePointInTrackCourse != 1) {
            if (((departureDayOfWeekFromStartRoutePointInTrackCourse != 1) && (departureDayOfWeekFromStartRoutePointInTrackCourse < arrivalDayOfWeekToEndRoutePointInTrackCourse)) || (departureDayOfWeekFromStartRoutePointInTrackCourse == arrivalDayOfWeekToEndRoutePointInTrackCourse && departureTimeFromStartRoutePointInTrackCourse < arrivalTimeWithNextLoading)) {
                return false;
            }
        }
        return true;
    }

    /** shifts invoices' type B to send the maximum number of invoices on time
     *
     * @param plannedSchedule
     * @param invoiceContainer
     * @param routesForInvoice
     */
    @Override
    public List<Invoice> shift (PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, List<DeliveryRoute>> routesForInvoice){
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
    public Map<Invoice, List<DeliveryRoute>> useAdditionalSchedule(AdditionalSchedule additionalSchedule, PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, List<DeliveryRoute>> routesForInvoice) throws RouteNotFoundException{
        Map<Invoice, List<DeliveryRoute>> result = new HashMap<>();
        List<Invoice> unsentInvoicesByPlannedSchedule = shift(plannedSchedule, invoiceContainer, routesForInvoice);
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
