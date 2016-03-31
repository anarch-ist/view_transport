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
    static RouteNew route1 = new RouteNew();
    static RouteNew route2 = new RouteNew();
    static RouteNew route3 = new RouteNew();
    static RouteNew route4 = new RouteNew();
    static RouteNew route5 = new RouteNew();
    static RouteNew route6 = new RouteNew();
    static RouteNew route7 = new RouteNew();
//    static IRoute route1 = new Route();
//    static IRoute route2 = new Route();
//    static IRoute route3 = new Route();
//    static IRoute route4 = new Route();
//    static IRoute route5 = new Route();
//    static IRoute route6 = new Route();
//    static IRoute route7 = new Route();

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){
        Util.initRoute(
                route1,
                2,
                getCharacteristicsOfCar(10000000),
                920,
                Util.createRoutePoint(x,   0),
                Util.createRoutePoint(c,  80),
                Util.createRoutePoint(a, 120)
        );
        Util.initRoute(
                route2,
                4,
                getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(p,  80),
                Util.createRoutePoint(z, 120),
                Util.createRoutePoint(q, 120)
        );
        Util.initRoute(
                route3,
                4,
                getCharacteristicsOfCar(10000000),
                940,
                Util.createRoutePoint(p,   0),
                Util.createRoutePoint(a,  80),
                Util.createRoutePoint(b, 120),
                Util.createRoutePoint(c,  90)
        );
        Util.initRoute(
                route4,
                4,
                getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(x,  80),
                Util.createRoutePoint(y, 120)
        );
        Util.initRoute(
                route5,
                4,
                getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(c,   0),
                Util.createRoutePoint(f,  80),
                Util.createRoutePoint(z, 120)
        );
        Util.initRoute(
                route6,
                4,
                getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(y,   0),
                Util.createRoutePoint(w,  80),
                Util.createRoutePoint(z, 120)
        );
        Util.initRoute(
                route7,
                4,
                getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(z, 120)
        );

        route1.get(0).setDistanceBetweenRoutePoints(120);
        route1.get(1).setDistanceBetweenRoutePoints(50);
        route1.get(0).setTravelTime(600);
        route1.get(1).setTravelTime(960);
        route1.get(0).setRoute(route1);
        route1.get(1).setRoute(route1);

        route2.get(0).setDistanceBetweenRoutePoints(120);
        route2.get(1).setDistanceBetweenRoutePoints(50);
        route2.get(0).setTravelTime(600);
        route2.get(1).setTravelTime(960);
        route2.get(0).setRoute(route2);
        route2.get(1).setRoute(route2);
        route2.get(2).setDistanceBetweenRoutePoints(390);
        route2.get(2).setTravelTime(4665);
        route2.get(2).setRoute(route2);

        route3.get(0).setDistanceBetweenRoutePoints(120);
        route3.get(1).setDistanceBetweenRoutePoints(50);
        route3.get(0).setTravelTime(600);
        route3.get(1).setTravelTime(960);
        route3.get(0).setRoute(route3);
        route3.get(1).setRoute(route3);
        route3.get(2).setDistanceBetweenRoutePoints(390);
        route3.get(2).setTravelTime(4665);
        route3.get(2).setRoute(route3);

        route4.get(0).setDistanceBetweenRoutePoints(120);
        route4.get(1).setDistanceBetweenRoutePoints(50);
        route4.get(0).setTravelTime(600);
        route4.get(1).setTravelTime(960);
        route4.get(0).setRoute(route4);
        route4.get(1).setRoute(route4);

        route5.get(0).setDistanceBetweenRoutePoints(120);
        route5.get(1).setDistanceBetweenRoutePoints(50);
        route5.get(0).setTravelTime(600);
        route5.get(1).setTravelTime(960);
        route5.get(0).setRoute(route5);
        route5.get(1).setRoute(route5);

        route6.get(0).setDistanceBetweenRoutePoints(120);
        route6.get(1).setDistanceBetweenRoutePoints(50);
        route6.get(0).setTravelTime(600);
        route6.get(1).setTravelTime(960);
        route6.get(0).setRoute(route6);
        route6.get(1).setRoute(route6);

        route7.get(0).setDistanceBetweenRoutePoints(120);
        route7.get(0).setTravelTime(600);
        route7.get(0).setRoute(route7);

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
        Map<IRoute, List<Integer>> result = optimizer.filterRoutesByPoint(plannedSchedule, a, true);
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
                System.out.println("Track Course: start point = " + trackCourse.getStartTrackCourse().getPoint().getPointId() + ", end point = " + trackCourse.getEndTrackCourse().getPoint().getPointId() + ", route = " + trackCourse.getRoute().getPointsAsString());
            }
            System.out.println("_______________________");
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

    public static CharacteristicsOfCar getCharacteristicsOfCar(double occupancyCost){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setOccupancyCost(occupancyCost);
        return result;
    }
    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------
}
