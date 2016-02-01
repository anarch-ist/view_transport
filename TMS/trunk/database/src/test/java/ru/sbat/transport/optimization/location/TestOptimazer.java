package ru.sbat.transport.optimization.location;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.Optimizer;
import ru.sbat.transport.optimization.Request;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.*;

public class TestOptimazer {
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static List<Invoice> unassignedInvoices = new ArrayList<>();
    static WarehousePoint warehousePoint1 = new WarehousePoint();
    static TradeRepresentativePoint tradeRepresentativePoint1 = new TradeRepresentativePoint();
    static TradeRepresentativePoint tradeRepresentativePoint2 = new TradeRepresentativePoint();
    static WarehousePoint warehousePoint2 = new WarehousePoint();
    static Route route1 = new Route();
    static Route route2 = new Route();
    static Route route3 = new Route();
    static Map<Invoice, ArrayList<Route>> routesForInvoice = new HashMap<>();

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){

        initRoute(
                route1,
                //пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта в минутах, время ПРР в минутах
                createRoutePoint(warehousePoint1,           2, 930, 600,   0),
                createRoutePoint(tradeRepresentativePoint1, 3, 180, 960,  90),
                createRoutePoint(tradeRepresentativePoint2, 3,   0,   0, 120)
        );

        initRoute(
                route2,
                createRoutePoint(warehousePoint2,           2, 930, 600,   0),
                createRoutePoint(tradeRepresentativePoint1, 3, 180, 960,  90),
                createRoutePoint(tradeRepresentativePoint2, 3,   0,   0, 120)
        );

        initRoute(
                route3,
                createRoutePoint(warehousePoint2,           4, 975, 1440,   0),
                createRoutePoint(tradeRepresentativePoint2, 5,   0,    0, 104)
        );

        plannedSchedule.add(route1);
        plannedSchedule.add(route2);
        plannedSchedule.add(route3);
    }

    // создание накладных без маршрутов
    @BeforeClass
    public static void createUnassignedInvoices(){
        // пункт доставки, дата плановой доставки
        Request request = createRequest(tradeRepresentativePoint2, createDate(2016, Calendar.APRIL, 21, 18, 0));
        // адрес склада, дата создания накладной
        Invoice invoice = createInvoice(warehousePoint2, createDate(2016, Calendar.JANUARY, 25, 12, 0), request);
//        System.out.println(invoice.getCreationDate() + " дата создания накладной");
//        System.out.println(invoice.getRequest().getPlannedDeliveryTime() + " дата плановой доставки");
//        System.out.println("");
        unassignedInvoices.add(invoice);
    }

    // -------- СЛУЖЕБНЫЕ МЕТОДЫ -----------

    private static Invoice createInvoice(Point departurePoint, Date creationDate, Request request){
        Invoice result = new Invoice();
        result.setAddressOfWarehouse(departurePoint);
        result.setCreationDate(creationDate);
        result.setRequest(request);
        return result;
    }

    private static Request createRequest(Point deliveryPoint, Date plannedDeliveryDateTime) {
        Request result = new Request();
        result.setDeliveryPoint(deliveryPoint);
        result.setPlannedDeliveryTime(plannedDeliveryDateTime);
        return result;
    }

    private static Date createDate(int year, int month, int day, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hours, minutes);
        return new Date(calendar.getTimeInMillis());
    }

    private static void initRoute(Route route, RoutePoint... routePoints) {
        Collections.addAll(route, routePoints);
    }

    private static RoutePoint createRoutePoint(Point point, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime) {
        RoutePoint result = new RoutePoint();
        result.setDeparturePoint(point);
        result.setDayOfWeek(dayOfWeek);
        result.setDepartureTime(departureTime); // в минутах от начала суток
        result.setTimeToNextPoint(timeToNextPoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        return result;
    }
    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------


    @Test
    public void TestGetWeekDay(){
        Optimizer optimizer = new Optimizer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.JANUARY, 26, 17, 10);
        Date date = calendar.getTime();
        Assert.assertEquals(3, optimizer.getWeekDay(date));
    }

    @Test
    public void testFiltrate() throws RouteNotFoundException {
        Optimizer optimizer = new Optimizer();
        routesForInvoice = optimizer.filtrate(plannedSchedule, unassignedInvoices);
        ArrayList<Route> routes = routesForInvoice.get(unassignedInvoices.get(0));
        Assert.assertEquals(26, routes.size());
        Assert.assertFalse(routes.contains(route1));
        Assert.assertTrue(routes.contains(route2));
        Assert.assertTrue(routes.contains(route3));
    }

    @Test
    public void testGetPossibleDepartureDate(){
        Optimizer optimizer = new Optimizer();
        Route route = new Route();
        initRoute(
                route,
                //пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта в минутах, время ПРР в минутах
                createRoutePoint(warehousePoint1,           3, 930, 600,   0),
                createRoutePoint(tradeRepresentativePoint1, 4, 180, 960,  90),
                createRoutePoint(tradeRepresentativePoint2, 4,   0,   0, 120)
        );
        Request request = createRequest(tradeRepresentativePoint2, createDate(2016, 1, 4, 18, 0));
        Invoice invoice = createInvoice(warehousePoint2, createDate(2016, 0, 29, 12, 0), request);
        ArrayList<Date> result = optimizer.getPossibleDepartureDate(route, invoice);
        System.out.println(invoice.getCreationDate() + " дата создания накладной");
        System.out.println(result.get(0) + " дата возможного отъезда");
        System.out.println(invoice.getRequest().getPlannedDeliveryTime() + " планируемая дата доставки в заявке");
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(invoice.getCreationDate().before(result.get(0)));
        Assert.assertTrue(invoice.getRequest().getPlannedDeliveryTime().after(result.get(0)));
    }

    @Test
    public void testIsFittingForDeliveryTime(){
        Optimizer optimizer = new Optimizer();
        Route route = new Route();
        initRoute(
                route,
                //пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта в минутах, время ПРР в минутах
                createRoutePoint(warehousePoint1,           2, 900, 600,   0),
                createRoutePoint(tradeRepresentativePoint1, 3, 180, 1500, 120),
                createRoutePoint(tradeRepresentativePoint2, 4,   0,   0,  120)
        );
        Request request = createRequest(tradeRepresentativePoint2, createDate(2016, 1, 4, 18, 0));
        Invoice invoice = createInvoice(warehousePoint2, createDate(2016, 0, 26, 12, 0), request);
        ArrayList<Date>dateArray = optimizer.getPossibleDepartureDate(route, invoice);
        Assert.assertTrue(optimizer.isFittingForDeliveryTime(route, invoice, dateArray.get(0)));
    }

    @Test
    public  void  testGetPossibleArrivalDate(){
        Optimizer optimizer = new Optimizer();
        Route route = new Route();
        initRoute(
                route,
                //пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта в минутах, время ПРР в минутах
                createRoutePoint(warehousePoint1,           3, 930, 600,    0),
                createRoutePoint(tradeRepresentativePoint1, 4, 180, 960,   90),
                createRoutePoint(tradeRepresentativePoint2, 4,   0,   0,  120)
        );
        Request request = createRequest(tradeRepresentativePoint2, createDate(2016, 5, 4, 18, 0));
        Invoice invoice = createInvoice(warehousePoint2, createDate(2016, 0, 27, 12, 0), request);
        ArrayList<Date>dateArray = optimizer.getPossibleDepartureDate(route, invoice);
        Assert.assertTrue(invoice.getRequest().getPlannedDeliveryTime().after(optimizer.getPossibleArrivalDate(route, invoice, dateArray.get(0))));
    }

}
