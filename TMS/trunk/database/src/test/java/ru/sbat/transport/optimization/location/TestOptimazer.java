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


    @Test
    public void TestGetWeekDay(){
        Optimizer optimizer = new Optimizer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 10);
        Date date = calendar.getTime();
        Assert.assertEquals(3, optimizer.getWeekDay(date));
    }

    @BeforeClass
    public static void createPlannedSchedule(){

        RoutePoint routePoint1 = new RoutePoint();
        routePoint1.setDeparturePoint(warehousePoint1);
        routePoint1.setDayOfWeek(1);
        routePoint1.setDepartureTime(510); // в минутах от начала суток
        routePoint1.setTimeToNextPoint(600);
        routePoint1.setLoadingOperationsTime(0);
        RoutePoint routePoint2 = new RoutePoint();
        routePoint2.setDeparturePoint(tradeRepresentativePoint1);
        routePoint2.setDayOfWeek(1);
        routePoint2.setDepartureTime(1200);
        routePoint2.setTimeToNextPoint(900);
        routePoint2.setLoadingOperationsTime(90);
        RoutePoint routePoint3 = new RoutePoint();
        routePoint3.setDeparturePoint(tradeRepresentativePoint2);
        routePoint3.setDayOfWeek(2);
        routePoint3.setDepartureTime(0);
        routePoint3.setTimeToNextPoint(0);
        routePoint3.setLoadingOperationsTime(120);
        route1.add(routePoint1);
        route1.add(routePoint2);
        route1.add(routePoint3);
        RoutePoint routePoint4 = new RoutePoint();
        routePoint4.setDeparturePoint(warehousePoint2);
        routePoint4.setDayOfWeek(1);
        routePoint4.setDepartureTime(510); // в минутах от начала суток
        routePoint4.setTimeToNextPoint(600);
        routePoint4.setLoadingOperationsTime(0);
        RoutePoint routePoint5 = new RoutePoint();
        routePoint5.setDeparturePoint(tradeRepresentativePoint1);
        routePoint5.setDayOfWeek(1);
        routePoint5.setDepartureTime(1200);
        routePoint5.setTimeToNextPoint(900);
        routePoint5.setLoadingOperationsTime(90);
        RoutePoint routePoint6 = new RoutePoint();
        routePoint6.setDeparturePoint(tradeRepresentativePoint2);
        routePoint6.setDayOfWeek(2);
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
        Date date = new Date(2016, 0, 27, 18, 0);
        request.setPlannedDeliveryTime(date);
        invoice.setRequest(request);
        Date creationDate = new Date(2016, 0, 19, 17, 0);
        invoice.setCreationDate(creationDate);
        unassignedInvoices.add(invoice);

    }

    @Test
    public void testGetPossibleDepartureDate(){
        Optimizer optimizer = new Optimizer();
        Route route = new Route();
        RoutePoint routePoint = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        routePoint.setDayOfWeek(2);
        routePoint.setDepartureTime(900);
        routePoint2.setDayOfWeek(3);
        routePoint2.setDepartureTime(180);
        routePoint3.setDayOfWeek(4);
        routePoint3.setDepartureTime(0);
        route.add(routePoint);
        route.add(routePoint2);
        route.add(routePoint3);
        Invoice invoice = new Invoice();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        Date currentDate = calendar.getTime();
        invoice.setCreationDate(currentDate);
        route.getWeekDayOfDepartureTime();
        int tmp = 7 - (invoice.getWeekDay() - route.getWeekDayOfDepartureTime());
        Date[] result = optimizer.getPossibleDepartureDate(route, invoice);
        calendar.add(Calendar.DAY_OF_YEAR, tmp);
        Date date1 = new Date(calendar.getTimeInMillis());
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);
        Assert.assertEquals(date1, result[0]);
    }

    @Test
    public void testFiltrate() throws RouteNotFoundException {
        Optimizer optimizer = new Optimizer();
        optimizer.filtrate(plannedSchedule, unassignedInvoices);
        System.out.println("size " + unassignedInvoices.get(0).getRoute());
        Assert.assertEquals(route2, unassignedInvoices.get(0).getRoute());
    }

    @Test
    public void testIsFittingForDeliveryTime(){
        Optimizer optimizer = new Optimizer();
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        routePoint1.setDayOfWeek(2);
        routePoint1.setDepartureTime(900); // в минутах от начала суток
        routePoint1.setTimeToNextPoint(600);
        routePoint1.setLoadingOperationsTime(0);
        RoutePoint routePoint2 = new RoutePoint();
        routePoint2.setDayOfWeek(3);
        routePoint2.setDepartureTime(180);
        routePoint2.setTimeToNextPoint(1500);
        routePoint2.setLoadingOperationsTime(120);
        RoutePoint routePoint3 = new RoutePoint();
        routePoint3.setDayOfWeek(4);
        routePoint3.setDepartureTime(0);
        routePoint3.setTimeToNextPoint(0);
        routePoint3.setLoadingOperationsTime(120);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        Invoice invoice = new Invoice();
        Request request = new Request();
        invoice.setRequest(request);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 4);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        Date date = calendar.getTime();
        request.setPlannedDeliveryTime(date);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 26);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        Date creationDate = cal.getTime();
        invoice.setCreationDate(creationDate);
        Date[]dateArray = optimizer.getPossibleDepartureDate(route, invoice);
        Assert.assertTrue(optimizer.isFittingForDeliveryTime(route, invoice, dateArray[0]));
    }

}
