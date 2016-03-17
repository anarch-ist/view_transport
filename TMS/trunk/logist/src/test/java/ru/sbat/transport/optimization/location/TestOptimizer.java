package ru.sbat.transport.optimization.location;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.*;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.*;

public class TestOptimizer {
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static InvoiceContainer invoiceContainer = new InvoiceContainer();
    static WarehousePoint warehousePoint2 = new WarehousePoint();
    static TradeRepresentativePoint tradeRepresentativePoint1 = new TradeRepresentativePoint();
    static TradeRepresentativePoint tradeRepresentativePoint2 = new TradeRepresentativePoint();
    static Route route = new Route();

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){
        initRoute(
                route,
                createRoutePoint(warehousePoint2,           2, 930, 600,   0, getCharacteristicsOfCar(10)),
                createRoutePoint(tradeRepresentativePoint1, 3, 170, 960,  80, getCharacteristicsOfCar(10)),
                createRoutePoint(tradeRepresentativePoint2, 3,   0,   0, 120, getCharacteristicsOfCar(10))
        );
        plannedSchedule.add(route);
    }

    // создание накладных без маршрутов
    @BeforeClass
    public static void fullInvoiceContainer(){
        Request request = createRequest(tradeRepresentativePoint1,  createDate(2016, Calendar.FEBRUARY, 9, 2,  45));
        Invoice invoice = createInvoice(warehousePoint2,  createDate(2016, Calendar.JANUARY, 30, 12, 0),     request, 5);
        invoiceContainer.add(invoice);

    }

    @Test
    public void testGetArrivalDateFromEachRoutePointInRoute(){
        Optimizer optimizer = new Optimizer();
        Map<RoutePoint, ArrayList<Date>> result = optimizer.getArrivalDateInEachRoutePointInRoute(route, invoiceContainer.get(0));
        for(RoutePoint routePoint: route) {
            System.out.println(result.get(routePoint) + " дата возможного прибытия в пункт " + routePoint);
        }
        System.out.println(invoiceContainer.get(0).getCreationDate() + " дата создания накладной");
        System.out.println(invoiceContainer.get(0).getRequest().getPlannedDeliveryDate() + " планируемая дата доставки в заявке");
    }

    @Test
    public void testGetDepartureDateFromEachRoutePointInRoute(){
        Optimizer optimizer = new Optimizer();
        ArrayList<Date> result1 = optimizer.getPossibleDepartureDateFromRoutePoint(route, route.get(0), invoiceContainer.get(0));
        for(Date date: result1) {
            System.out.println(date + " дата возможного отъезда из 1");
        }
        ArrayList<Date> result2 = optimizer.getPossibleDepartureDateFromRoutePoint(route, route.get(1), invoiceContainer.get(0));
        for(Date date: result2) {
            System.out.println(date + " дата возможного отъезда из 2");
        }
        ArrayList<Date> result3 = optimizer.getPossibleDepartureDateFromRoutePoint(route, route.get(2), invoiceContainer.get(0));
        for(Date date: result3) {
            System.out.println(date + " дата возможного отъезда мз 3");
        }
        System.out.println("");
        System.out.println(invoiceContainer.get(0).getCreationDate() + " дата создания накладной");
        System.out.println(invoiceContainer.get(0).getRequest().getPlannedDeliveryDate() + " планируемая дата доставки в заявке");
    }

    @Test
    public void testGetDepartureArrivalDatesBetweenTwoRoutePoints(){
        Optimizer optimizer = new Optimizer();
        ArrayList<PairDate> pairDates = optimizer.getDepartureArrivalDatesBetweenTwoRoutePoints(route, invoiceContainer.get(0), route.get(0), route.get(route.size() - 1));
        Assert.assertEquals(1, pairDates.size());
        Assert.assertEquals(2, optimizer.getPossibleDepartureDateFromRoutePoint(route, route.get(0), invoiceContainer.get(0)).size());
        Assert.assertEquals(1, optimizer.getArrivalDateInEachRoutePointInRoute(route, invoiceContainer.get(0)).get(route.get(route.size() - 1)).size());
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

    private static void initRoute(Route route, RoutePoint... routePoints) {
        Collections.addAll(route, routePoints);
    }

    private static RoutePoint createRoutePoint(Point point, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, CharacteristicsOfCar characteristicsOfCar) {
        RoutePoint result = new RoutePoint();
        result.setDeparturePoint(point);
        result.setDayOfWeek(dayOfWeek);
        result.setDepartureTime(departureTime); // в минутах от начала суток
        result.setTimeToNextPoint(timeToNextPoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        result.setCharacteristicsOfCar(characteristicsOfCar);
        return result;
    }

    public static CharacteristicsOfCar getCharacteristicsOfCar(double occupancyCost){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setOccupancyCost(occupancyCost);
        return result;
    }
    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------
}
