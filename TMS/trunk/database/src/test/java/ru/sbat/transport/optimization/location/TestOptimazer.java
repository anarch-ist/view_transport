package ru.sbat.transport.optimization.location;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.Optimizer;
import ru.sbat.transport.optimization.Request;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.DayOfWeek;

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


    @Test
    public void TestGetWeekDay(){
        Optimizer optimizer = new Optimizer();
        Date date = new Date();
        int result = optimizer.getWeekDay(date);
        System.out.println(result);
    }

    @BeforeClass
    public static void createPlannedSchedule(){

        RoutePoint routePoint1 = new RoutePoint();
        routePoint1.setDeparturePoint(warehousePoint1);
        routePoint1.setDayOfWeek(DayOfWeek.MONDAY);
        routePoint1.setDepartureTime(510); // в минутах от начала суток
        routePoint1.setTimeToNextPoint(600);
        routePoint1.setLoadingOperationsTime(0);
        RoutePoint routePoint2 = new RoutePoint();
        routePoint2.setDeparturePoint(tradeRepresentativePoint1);
        routePoint2.setDayOfWeek(DayOfWeek.MONDAY);
        routePoint2.setDepartureTime(1200);
        routePoint2.setTimeToNextPoint(900);
        routePoint2.setLoadingOperationsTime(90);
        RoutePoint routePoint3 = new RoutePoint();
        routePoint3.setDeparturePoint(tradeRepresentativePoint2);
        routePoint3.setDayOfWeek(DayOfWeek.TUESDAY);
        routePoint3.setDepartureTime(0);
        routePoint3.setTimeToNextPoint(0);
        routePoint3.setLoadingOperationsTime(120);
        route1.add(routePoint1);
        route1.add(routePoint2);
        route1.add(routePoint3);
        RoutePoint routePoint4 = new RoutePoint();
        routePoint4.setDeparturePoint(warehousePoint2);
        routePoint4.setDayOfWeek(DayOfWeek.MONDAY);
        routePoint4.setDepartureTime(510); // в минутах от начала суток
        routePoint4.setTimeToNextPoint(600);
        routePoint4.setLoadingOperationsTime(0);
        RoutePoint routePoint5 = new RoutePoint();
        routePoint5.setDeparturePoint(tradeRepresentativePoint1);
        routePoint5.setDayOfWeek(DayOfWeek.MONDAY);
        routePoint5.setDepartureTime(1200);
        routePoint5.setTimeToNextPoint(900);
        routePoint5.setLoadingOperationsTime(90);
        RoutePoint routePoint6 = new RoutePoint();
        routePoint6.setDeparturePoint(tradeRepresentativePoint2);
        routePoint6.setDayOfWeek(DayOfWeek.TUESDAY);
        routePoint6.setDepartureTime(0);
        routePoint6.setTimeToNextPoint(0);
        routePoint6.setLoadingOperationsTime(120);
        route2.add(routePoint4);
        route2.add(routePoint5);
        route2.add(routePoint6);
        plannedSchedule.add(route1);
        plannedSchedule.add(route2);
    }

    @BeforeClass
    public static void createUnassignedInvoices(){

        Invoice invoice = new Invoice();
        invoice.setAddressOfWarehouse(warehousePoint2);
        Request request = new Request();
        request.setDeliveryPoint(tradeRepresentativePoint2);
        Date date = new Date(2016, 1, 27, 18, 0);
        request.setPlannedDeliveryTime(date);
        invoice.setRequest(request);
        Date creationDate = new Date(2016, 1, 22, 17, 0);
        invoice.setCreationDate(creationDate);
        unassignedInvoices.add(invoice);

    }

//    @BeforeClass
//    public void createAdditionalSchedule(){
//
//    }

    @Test
    public void testFiltrate() throws RouteNotFoundException {
        Optimizer optimizer = new Optimizer();
        optimizer.filtrate(plannedSchedule, unassignedInvoices);
        Assert.assertEquals(route2, unassignedInvoices.get(0).getRoute());
    }

//    @Test
//    public void testOptimize() {
//
//    }
}
