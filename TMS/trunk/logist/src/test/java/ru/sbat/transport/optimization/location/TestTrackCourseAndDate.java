//package ru.sbat.transport.optimization.location;
//
//
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//import ru.sbat.transport.optimization.*;
//import ru.sbat.transport.optimization.schedule.PlannedSchedule;
//import ru.sbat.transport.optimization.utils.LoadCost;
//
//import java.util.*;
//
//public class TestTrackCourseAndDate {
//    static PlannedSchedule plannedSchedule = new PlannedSchedule();
//    static InvoiceContainer invoiceContainer = new InvoiceContainer();
//    static Route route = new Route();
//    static WarehousePoint c = new WarehousePoint("C");
//    static TradeRepresentativePoint x = new TradeRepresentativePoint("X");
//    static TradeRepresentativePoint a = new TradeRepresentativePoint("A");
//    static TradeRepresentativePoint z = new TradeRepresentativePoint("Z");
//
//    @BeforeClass
//    public static void createPlannedSchedule() {
//        initRoute(
//                route,
//                getCharacteristicsOfCar(10),
//                createRoutePoint(c, 5, 930, 600,   120, 120),
//                createRoutePoint(x, 6, 150, 960,    60,  50),
//                createRoutePoint(a, 6,   0,   0,    90, 300)
//        );
//
//        plannedSchedule.add(route);
//    }
//
//    @BeforeClass
//    public static void fullInvoiceContainer(){
//        Request request = createRequest(a,  createDate(2016, Calendar.APRIL, 6, 20,  45));
//        Invoice invoice = createInvoice(c,  createDate(2016, Calendar.MARCH, 28, 16, 0),     request, 5);
//        invoiceContainer.add(invoice);
//    }
//
//    @Test
//    public void testGetTrackCoursesAndDates() {
//        ListOfCargoFlightByExactDateOfTrackCourse listOfCargoFlightByExactDateOfTrackCourse = new ListOfCargoFlightByExactDateOfTrackCourse();
//        listOfCargoFlightByExactDateOfTrackCourse = listOfCargoFlightByExactDateOfTrackCourse.assignTrackCoursesDepartureDates(plannedSchedule);
//        System.out.println(listOfCargoFlightByExactDateOfTrackCourse.size());
//        Iterator<Map.Entry<DateAndTrackCourse, LoadCost>> iterator = listOfCargoFlightByExactDateOfTrackCourse.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<DateAndTrackCourse, LoadCost> entry = iterator.next();
//            System.out.println("departure date = " + entry.getKey().getDepartureDate());
//        }
//    }
//
//    @Test
//    public void testCalculate(){
//        ListOfCargoFlightByExactDateOfTrackCourse listOfCargoFlightByExactDateOfTrackCourse = new ListOfCargoFlightByExactDateOfTrackCourse();
//        List<Date> dates = listOfCargoFlightByExactDateOfTrackCourse.calculateDepartureDatesForTrackCourseAfterCurrentDateToDeliveryDate(listOfCargoFlightByExactDateOfTrackCourse.splitRoutesIntoTrackCourses(plannedSchedule).get(0), invoiceContainer.get(0));
//        for (Date date: dates){
//            System.out.println("departure date = " + date);
//        }
//    }
//
//
//    // -------- СЛУЖЕБНЫЕ МЕТОДЫ -----------
//
//    private static void initRoute(Route route, CharacteristicsOfCar characteristicsOfCar, RoutePoint... routePoints) {
//        route.setCharacteristicsOfCar(characteristicsOfCar);
//        Collections.addAll(route, routePoints);
//    }
//
//    private static RoutePoint createRoutePoint(Point point, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, double distanceToNextPoint) {
//        RoutePoint result = new RoutePoint();
//        result.setDeparturePoint(point);
//        result.setDayOfWeek(dayOfWeek);
//        result.setDepartureTime(departureTime); // в минутах от начала суток
//        result.setTimeToNextPoint(timeToNextPoint);
//        result.setLoadingOperationsTime(loadingOperationsTime);
//        result.setDistanceToNextPoint(distanceToNextPoint);
//        return result;
//    }
//
//    public static CharacteristicsOfCar getCharacteristicsOfCar(double occupancyCost){
//        CharacteristicsOfCar result = new CharacteristicsOfCar();
//        result.setOccupancyCost(occupancyCost);
//        return result;
//    }
//
//    private static Invoice createInvoice(Point departurePoint, Date creationDate, Request request, double cost){
//        Invoice result = new Invoice();
//        result.setAddressOfWarehouse(departurePoint);
//        result.setCreationDate(creationDate);
//        result.setRequest(request);
//        result.setCost(cost);
//        return result;
//    }
//
//    private static Request createRequest(Point deliveryPoint, Date plannedDeliveryDateTime) {
//        Request result = new Request();
//        result.setDeliveryPoint(deliveryPoint);
//        result.setPlannedDeliveryDate(plannedDeliveryDateTime);
//        return result;
//    }
//
//    private static Date createDate(int year, int month, int day, int hours, int minutes) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, day, hours, minutes);
//        return new Date(calendar.getTimeInMillis());
//    }
//    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------
//}
