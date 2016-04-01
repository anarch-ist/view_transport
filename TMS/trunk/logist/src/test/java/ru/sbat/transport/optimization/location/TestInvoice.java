package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.InvoiceContainer;
import ru.sbat.transport.optimization.Request;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestInvoice {
    static InvoiceContainer invoiceContainer = new InvoiceContainer();
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static WarehousePoint b = new WarehousePoint("B");
    static TradeRepresentativePoint x = new TradeRepresentativePoint("X");
    static TradeRepresentativePoint p = new TradeRepresentativePoint("P");
    static TradeRepresentativePoint a = new TradeRepresentativePoint("A");
    static TradeRepresentativePoint y = new TradeRepresentativePoint("Y");
    static TradeRepresentativePoint q = new TradeRepresentativePoint("Q");
    static TradeRepresentativePoint f = new TradeRepresentativePoint("F");
    static TradeRepresentativePoint w = new TradeRepresentativePoint("W");
    static RouteNew route1 = new RouteNew();
    static RouteNew route2 = new RouteNew();
    static TradeRepresentativePoint z = new TradeRepresentativePoint("Z");
    static WarehousePoint c = new WarehousePoint("C");

    @BeforeClass
    public static void createPlannedSchedule() {
        Util.initRoute(
                route1,
                1,
                Util.getCharacteristicsOfCar(10000000),
                920,
                Util.createRoutePoint(x, 0),
                Util.createRoutePoint(c, 80),
                Util.createRoutePoint(a, 120)
        );
        Util.initRoute(
                route2,
                7,
                Util.getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(b, 0),
                Util.createRoutePoint(c, 80),
                Util.createRoutePoint(z, 120),
                Util.createRoutePoint(q, 120)
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

        plannedSchedule.add(route1);
        plannedSchedule.add(route2);
    }


    @BeforeClass
    public static void fullInvoiceContainer(){
        Request request = Util.createRequest(z,  Util.createDate(2016, Calendar.MARCH, 19, 20,  51));
        Invoice invoice = Util.createInvoice(x,  Util.createDate(2016, Calendar.MARCH, 4, 15, 21),     request, 5);
        invoiceContainer.add(invoice);
    }

    @Test
    public void testGetWeekDay(){
        Invoice invoice = new Invoice();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 1, 2, 17, 10);
        Date date = calendar.getTime();
        invoice.setCreationDate(date);
        Assert.assertEquals(3, invoice.getWeekDay());
    }

    @Test
    public void testCalculateWeekDays() throws IncorrectRequirement {
        List<Integer> numberOfWeek = invoiceContainer.get(0).calculateNumbersOfWeeksBetweenDates(invoiceContainer.get(0).getCreationDate(),
                invoiceContainer.get(0).getRequest().getPlannedDeliveryDate(), invoiceContainer.get(0), plannedSchedule.get(0).splitRouteIntoTrackCourse(plannedSchedule.get(0)).get(0),
                plannedSchedule.get(1).splitRouteIntoTrackCourse(plannedSchedule.get(1)).get(1));
        System.out.println(numberOfWeek);
        Assert.assertEquals(10, numberOfWeek.get(0), 0);
        Assert.assertEquals(11, numberOfWeek.get(1), 0);
    }
}
