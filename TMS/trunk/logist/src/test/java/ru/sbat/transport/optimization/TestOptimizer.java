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
    static UpdatedRoute route1 = new UpdatedRoute();
    static UpdatedRoute route2 = new UpdatedRoute();
    static UpdatedRoute route3 = new UpdatedRoute();
    static UpdatedRoute route4 = new UpdatedRoute();
    static UpdatedRoute route5 = new UpdatedRoute();
    static UpdatedRoute route6 = new UpdatedRoute();
    static UpdatedRoute route7 = new UpdatedRoute();

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){
        createUpdatedRoute(
                route1,
                createRoutePoint(x, 0),
                createRoutePoint(c, 80),
                createRoutePoint(a, 120)
        );
        createUpdatedRoute(
                route2,
                createRoutePoint(b, 0),
                createRoutePoint(p, 70),
                createRoutePoint(z, 120),
                createRoutePoint(q, 30)
        );
        createUpdatedRoute(
                route3,
                createRoutePoint(p, 0),
                createRoutePoint(a, 60),
                createRoutePoint(b, 120),
                createRoutePoint(c, 90)
        );
        createUpdatedRoute(
                route4,
                createRoutePoint(b, 0),
                createRoutePoint(x, 30),
                createRoutePoint(y, 30)
        );
        createUpdatedRoute(
                route5,
                createRoutePoint(c, 0),
                createRoutePoint(f, 60),
                createRoutePoint(z, 30)
        );
        createUpdatedRoute(
                route6,
                createRoutePoint(y, 0),
                createRoutePoint(w, 60),
                createRoutePoint(z, 30)
        );
        createUpdatedRoute(
                route7,
                createRoutePoint(b, 0),
                createRoutePoint(z, 120)
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
        Map<UpdatedRoute, List<Integer>> result = optimizer.filterRoutesByPoint(plannedSchedule, a, true);
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
                System.out.println("Track Course: start point = " + trackCourse.getStartTrackCourse().getPoint().getPointId() + ", end point = " + trackCourse.getEndTrackCourse().getPoint().getPointId() + ", route = " + trackCourse.getUpdatedRoute().getPointsAsString());
            }
            System.out.println("_______________________");
        }

//        System.out.println("MARKED POINTS");
//        for (Point markedPoint : markedPoints) {
//            System.out.println(markedPoint.getPointId());
//        }
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

    private static void initRoute(UpdatedRoute route, CharacteristicsOfCar characteristicsOfCar, TrackCourse... trackCourses) {
        route.setCharacteristicsOfCar(characteristicsOfCar);
        Collections.addAll(route, trackCourses);
    }

    private static RoutePoint createRoutePoint(Point point, int loadingOperationsTime) {
        RoutePoint result = new RoutePoint();
        result.setPoint(point);
        result.setLoadingOperationsTime(loadingOperationsTime);
        return result;
    }

    private static void createUpdatedRoute(UpdatedRoute updatedRoute, RoutePoint... routePoints){
        for(int i = 0; i < routePoints.length - 1; i++){
            TrackCourse trackCourse = new TrackCourse();
            trackCourse.setStartTrackCourse(routePoints[i]);
            trackCourse.setEndTrackCourse(routePoints[i + 1]);
            trackCourse.setUpdatedRoute(updatedRoute);
            System.out.println(trackCourse.getStartTrackCourse().getPoint().getPointId() + " " + trackCourse.getEndTrackCourse().getPoint().getPointId());
            System.out.println();

            updatedRoute.add(trackCourse);
        }
    }

    public static CharacteristicsOfCar getCharacteristicsOfCar(double occupancyCost){
        CharacteristicsOfCar result = new CharacteristicsOfCar(10000);
        result.setOccupancyCost(occupancyCost);
        return result;
    }
    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------
}
