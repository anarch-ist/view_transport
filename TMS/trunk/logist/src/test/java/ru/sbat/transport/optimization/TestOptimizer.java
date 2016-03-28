package ru.sbat.transport.optimization;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.location.*;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.*;

public class TestOptimizer {
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static InvoiceContainer invoiceContainer = new InvoiceContainer();
    static WarehousePoint b = new WarehousePoint("B");
    static WarehousePoint c = new WarehousePoint("C");
    static TradeRepresentativePoint x = new TradeRepresentativePoint("X");
    static TradeRepresentativePoint p = new TradeRepresentativePoint("P");
    static TradeRepresentativePoint a = new TradeRepresentativePoint("A");
    static TradeRepresentativePoint z = new TradeRepresentativePoint("Z");
    static TradeRepresentativePoint y = new TradeRepresentativePoint("Y");
    static TradeRepresentativePoint q = new TradeRepresentativePoint("Q");
    static TradeRepresentativePoint f = new TradeRepresentativePoint("F");
    static TradeRepresentativePoint w = new TradeRepresentativePoint("W");
    static Route route1 = new Route();
    static Route route2 = new Route();
    static Route route3 = new Route();
    static Route route4 = new Route();
    static Route route5 = new Route();
    static Route route6 = new Route();
    static Route route7 = new Route();

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){
        initRoute(
                route1,
                getCharacteristicsOfCar(10),
                createRoutePoint(x, 2, 930, 600,   0, 690),
                createRoutePoint(c, 3, 170, 960,  80, 789),
                createRoutePoint(a, 3,   0,   0, 120,   0)
        );
        initRoute(
                route2,
                getCharacteristicsOfCar(15),
                createRoutePoint(b, 3, 1080, 240,   0, 459),
                createRoutePoint(p, 3, 1380, 300,  70, 123),
                createRoutePoint(z, 4,  360, 180, 120, 245),
                createRoutePoint(q, 4,    0,   0,  30, 245)
        );
        initRoute(
                route3,
                getCharacteristicsOfCar(20),
                createRoutePoint(p, 2,  720, 600,   0,  657),
                createRoutePoint(a, 2, 1380, 480,  60,   35),
                createRoutePoint(b, 3,  540, 720, 120, 4535),
                createRoutePoint(c, 3,    0,   0,  90,  345)
        );
        initRoute(
                route4,
                getCharacteristicsOfCar(10),
                createRoutePoint(b, 3,  720, 420,   0, 459),
                createRoutePoint(x, 3, 1170, 180,  30, 123),
                createRoutePoint(y, 3,    0,   0,  30, 245)
        );
        initRoute(
                route5,
                getCharacteristicsOfCar(7),
                createRoutePoint(c, 4, 900, 480,   0, 459),
                createRoutePoint(f, 5,   0, 600,  60, 123),
                createRoutePoint(z, 5,   0,   0,  30, 245)
        );
        initRoute(
                route6,
                getCharacteristicsOfCar(25),
                createRoutePoint(y, 5, 1020, 240,   0, 459),
                createRoutePoint(w, 5, 1320, 420,  60, 123),
                createRoutePoint(z, 6,    0,   0,  30, 245)
        );
        initRoute(
                route7,
                getCharacteristicsOfCar(16),
                createRoutePoint(b, 6, 600, 360,   0, 459),
                createRoutePoint(z, 6,   0,   0, 120, 245)
        );
        plannedSchedule.add(route1);
        plannedSchedule.add(route2);
        plannedSchedule.add(route3);
        plannedSchedule.add(route4);
        plannedSchedule.add(route5);
        plannedSchedule.add(route6);
        plannedSchedule.add(route7);
    }

    // создание накладных без маршрутов
    @BeforeClass
    public static void fullInvoiceContainer(){
        Request request = createRequest(z,  createDate(2016, Calendar.FEBRUARY, 16, 20,  45));
        Invoice invoice = createInvoice(c,  createDate(2016, Calendar.JANUARY, 30, 12, 0),     request, 5);
        invoiceContainer.add(invoice);

    }

    @Test
    public void testFiltrate() throws RouteNotFoundException {
        Optimizer optimizer = new Optimizer();
        System.out.println(optimizer.filtrate(plannedSchedule, invoiceContainer).size());
        System.out.println(optimizer.filtrate(plannedSchedule, invoiceContainer).get(invoiceContainer.get(0)).size());
    }

    @Test
    public void testFilterRoutesByPoint() {
        Optimizer optimizer = new Optimizer();
        Map<Route, List<Integer>> result = optimizer.filterRoutesByPoint(plannedSchedule, a, true);
        System.out.println(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(1, result.get(route3).size());
    }

    @Test
    public void testRecursive() {
        Optimizer optimizer = new Optimizer();
        List<DeliveryRoute> result = new ArrayList<>();
        List<Point> markedPoints = new ArrayList<>();
        List<DeliveryRoute> possibleDeliveryRoutes = optimizer.startRecursive(plannedSchedule, a, z, result, markedPoints);
        for(DeliveryRoute deliveryRoute: possibleDeliveryRoutes) {
            for (TrackCourse trackCourse : deliveryRoute) {
                System.out.println("Track Course: start point = " + trackCourse.getStartTrackCourse().getDeparturePoint().getPointId() + ", end point = " + trackCourse.getEndTrackCourse().getDeparturePoint().getPointId() + ", route = " + trackCourse.getRoute().getPointsAsString());
            }
            System.out.println("_______________________");
        }

//        System.out.println("MARKED POINTS");
//        for (Point markedPoint : markedPoints) {
//            System.out.println(markedPoint.getPointId());
//        }
    }

    @Test
    public void testGetArrivalDateFromEachRoutePointInRoute(){
        Optimizer optimizer = new Optimizer();
        Map<RoutePoint, ArrayList<Date>> result = optimizer.getArrivalDateInEachRoutePointInRoute(route1, invoiceContainer.get(0));
        for(RoutePoint routePoint: route1) {
            System.out.println(result.get(routePoint) + " дата возможного прибытия в пункт " + routePoint);
        }
        System.out.println(invoiceContainer.get(0).getCreationDate() + " дата создания накладной");
        System.out.println(invoiceContainer.get(0).getRequest().getPlannedDeliveryDate() + " планируемая дата доставки в заявке");
    }

    @Test
    public void testGetDepartureDateFromEachRoutePointInRoute(){
        Optimizer optimizer = new Optimizer();
        ArrayList<Date> result1 = optimizer.getPossibleDepartureDateFromRoutePoint(route1, route1.get(0), invoiceContainer.get(0));
        for(Date date: result1) {
            System.out.println(date + " дата возможного отъезда из 1");
        }
        ArrayList<Date> result2 = optimizer.getPossibleDepartureDateFromRoutePoint(route1, route1.get(1), invoiceContainer.get(0));
        for(Date date: result2) {
            System.out.println(date + " дата возможного отъезда из 2");
        }
        System.out.println("");
        System.out.println(invoiceContainer.get(0).getCreationDate() + " дата создания накладной");
        System.out.println(invoiceContainer.get(0).getRequest().getPlannedDeliveryDate() + " планируемая дата доставки в заявке");
    }

    @Test
    public void testGetDepartureDateFromEachRoutePointInRoute2(){
        Optimizer optimizer = new Optimizer();
        ArrayList<Date> result1 = optimizer.getPossibleDepartureDateFromRoutePoint(route2, route2.get(0), invoiceContainer.get(0));
        for(Date date: result1) {
            System.out.println(date + " дата возможного отъезда из 1");
        }
        ArrayList<Date> result2 = optimizer.getPossibleDepartureDateFromRoutePoint(route2, route2.get(1), invoiceContainer.get(0));
        for(Date date: result2) {
            System.out.println(date + " дата возможного отъезда из 2");
        }
        ArrayList<Date> result3 = optimizer.getArrivalDateInEachRoutePointInRoute(route2, invoiceContainer.get(0)).get(route2.get(route2.size() - 1));
        for(Date date: result3) {
            System.out.println(date + " дата возможного отъезда из 3");
        }
        System.out.println("");
        System.out.println(invoiceContainer.get(0).getCreationDate() + " дата создания накладной");
        System.out.println(invoiceContainer.get(0).getRequest().getPlannedDeliveryDate() + " планируемая дата доставки в заявке");
    }

    @Test
    public void testGetDepartureArrivalDatesBetweenTwoRoutePoints(){
        Optimizer optimizer = new Optimizer();
        ArrayList<PairDate> pairDates = optimizer.getDepartureArrivalDatesBetweenTwoRoutePoints(route1, route1, invoiceContainer.get(0), route1.get(route1.size() - 2), route1.get(route1.size() - 1));
        Assert.assertEquals(3, pairDates.size());
        for(PairDate pairDate: pairDates){
            System.out.println("Отправление из пункта: " + pairDate.getDepartureDate() + " и прибытие в нужный: " + pairDate.getArrivalDate());
        }
    }

    // -------- СЛУЖЕБНЫЕ МЕТОДЫ -----------

    private static Invoice createInvoice(Point departurePoint, Date creationDate, Request request, double cost){
        Invoice result = new Invoice();
        result.setAddressOfWarehouse(departurePoint);
        result.setCreationDate(creationDate);
        result.setRequest(request);
        result.setCost(cost);
        return result;
    }

    private static Request createRequest(Point deliveryPoint, Date plannedDeliveryDateTime) {
        Request result = new Request();
        result.setDeliveryPoint(deliveryPoint);
        result.setPlannedDeliveryDate(plannedDeliveryDateTime);
        return result;
    }

    private static Date createDate(int year, int month, int day, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hours, minutes);
        return new Date(calendar.getTimeInMillis());
    }

    private static void initRoute(Route route, CharacteristicsOfCar characteristicsOfCar, RoutePoint... routePoints) {
        route.setCharacteristicsOfCar(characteristicsOfCar);
        Collections.addAll(route, routePoints);
    }

    private static RoutePoint createRoutePoint(Point point, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, double distanceToNextPoint) {
        RoutePoint result = new RoutePoint();
        result.setDeparturePoint(point);
        result.setDayOfWeek(dayOfWeek);
        result.setDepartureTime(departureTime); // в минутах от начала суток
        result.setTimeToNextPoint(timeToNextPoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        result.setDistanceToNextPoint(distanceToNextPoint);
        return result;
    }

    public static CharacteristicsOfCar getCharacteristicsOfCar(double occupancyCost){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setOccupancyCost(occupancyCost);
        return result;
    }
    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------
}
