package ru.sbat.transport.optimization.location;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.InvoiceContainer;
import ru.sbat.transport.optimization.Optimizer;
import ru.sbat.transport.optimization.Request;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.text.ParseException;
import java.util.*;

public class TestOptimizerOptimize {
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static InvoiceContainer invoiceContainer = new InvoiceContainer();
    static TradeRepresentativePoint tradeRepresentativePoint1 = new TradeRepresentativePoint();
    static TradeRepresentativePoint tradeRepresentativePoint2 = new TradeRepresentativePoint();
    static TradeRepresentativePoint tradeRepresentativePoint3 = new TradeRepresentativePoint();
    static WarehousePoint warehousePoint2 = new WarehousePoint();
    static Route route = new Route();
    static Route route1 = new Route();
    static Route route2 = new Route();
    static Route route3 = new Route();
    static Route route4 = new Route();
    static Map<Invoice, ArrayList<Route>> routesForInvoice = new HashMap<>();

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){

        initRoute(
                route,
                //пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта в минутах, время ПРР в минутах
                createRoutePoint(warehousePoint2,           2, 900, 600,   0, getCharacteristicsOfCar(11, 16), 1000),
                createRoutePoint(tradeRepresentativePoint2, 3,   0,   0,  90, getCharacteristicsOfCar(11, 16),    0)
        );

        initRoute(
                route1,
                //пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта в минутах, время ПРР в минутах
                createRoutePoint(warehousePoint2,           2, 900, 600,   0, getCharacteristicsOfCar(11, 16), 1000),
                createRoutePoint(tradeRepresentativePoint3, 3,   0,   0,  90, getCharacteristicsOfCar(11, 16),    0)
        );

        initRoute(
                route2,
                createRoutePoint(warehousePoint2,           2, 930, 600,   0, getCharacteristicsOfCar(15, 20), 800),
                createRoutePoint(tradeRepresentativePoint1, 3, 170, 960,  80, getCharacteristicsOfCar(15, 20), 730),
                createRoutePoint(tradeRepresentativePoint2, 3,   0,   0, 120, getCharacteristicsOfCar(15, 20),   0)
        );

        initRoute(
                route3,
                createRoutePoint(warehousePoint2,           4, 975, 1440,   0, getCharacteristicsOfCar(7, 9), 990),
                createRoutePoint(tradeRepresentativePoint2, 5,   0,    0, 104, getCharacteristicsOfCar(7, 9),   0)
        );

        initRoute(
                route4,
                createRoutePoint(warehousePoint2,           5, 1080, 540,   0, getCharacteristicsOfCar(7, 9), 760),
                createRoutePoint(tradeRepresentativePoint1, 6,   0,    0, 120, getCharacteristicsOfCar(7, 9),   0)
        );

        plannedSchedule.add(route);
        plannedSchedule.add(route1);
        plannedSchedule.add(route2);
        plannedSchedule.add(route3);
        plannedSchedule.add(route4);
    }

    // создание накладных без маршрутов
    @BeforeClass
    public static void fullInvoiceContainer(){
        // пункт доставки, дата плановой доставки
        Request request = createRequest(tradeRepresentativePoint1,  createDate(2016, Calendar.FEBRUARY, 9, 18,  0));
        Request request2 = createRequest(tradeRepresentativePoint3, createDate(2016, Calendar.FEBRUARY, 9, 17, 30));
        Request request3 = createRequest(tradeRepresentativePoint2, createDate(2016, Calendar.FEBRUARY, 9, 18, 30));
        Request request4 = createRequest(tradeRepresentativePoint2, createDate(2016, Calendar.FEBRUARY, 9, 19, 30));
        Request request5 = createRequest(tradeRepresentativePoint2, createDate(2016, Calendar.FEBRUARY, 9, 21, 00));
        // адрес склада, дата создания накладной
        Invoice invoice = createInvoice(warehousePoint2,  createDate(2016, Calendar.JANUARY, 25, 12, 0),     request, 5, 7);
        Invoice invoice2 = createInvoice(warehousePoint2, createDate(2016, Calendar.JANUARY, 25, 12, 30), request2, 10, 15);
        Invoice invoice3 = createInvoice(warehousePoint2, createDate(2016, Calendar.JANUARY, 25, 13, 0),    request3, 2, 3);
        Invoice invoice4 = createInvoice(warehousePoint2, createDate(2016, Calendar.JANUARY, 25, 14, 0),    request4, 4, 6);
        Invoice invoice5 = createInvoice(warehousePoint2, createDate(2016, Calendar.JANUARY, 25, 15, 0),    request5, 1, 2);
        invoice2.setRoute(route1);// накладная (10кг., 15м3.), маршрут (11кг., 16м3.) - заполнен
        invoice3.setRoute(route2);// накладная (2кг., 3м3.), маршрут (10кг., 15м3.) - можно догрузить
//        System.out.println(invoice.getCreationDate() + " дата создания накладной");
//        System.out.println(invoice.getRequest().getPlannedDeliveryTime() + " дата плановой доставки");
//        System.out.println("");
        invoiceContainer.add(invoice);
        invoiceContainer.add(invoice2);
        invoiceContainer.add(invoice3);
        invoiceContainer.add(invoice4);
        invoiceContainer.add(invoice5);
    }

    // -------- СЛУЖЕБНЫЕ МЕТОДЫ -----------

    private static Invoice createInvoice(Point departurePoint, Date creationDate, Request request, double weight, double volume){
        Invoice result = new Invoice();
        result.setAddressOfWarehouse(departurePoint);
        result.setCreationDate(creationDate);
        result.setRequest(request);
        result.setWeight(weight);
        result.setVolume(volume);
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

    private static RoutePoint createRoutePoint(Point point, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, CharacteristicsOfCar characteristicsOfCar, double distance) {
        RoutePoint result = new RoutePoint();
        result.setDeparturePoint(point);
        result.setDayOfWeek(dayOfWeek);
        result.setDepartureTime(departureTime); // в минутах от начала суток
        result.setTimeToNextPoint(timeToNextPoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        result.setCharacteristicsOfCar(characteristicsOfCar);
        result.setDistanceToNextPoint(distance);
        return result;
    }

    public static CharacteristicsOfCar getCharacteristicsOfCar(double weight, double volume){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setCapacityCar(weight);
        result.setVolumeCar(volume);
        return result;
    }
    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------


    @Test
    public void testOptimize() throws ParseException, RouteNotFoundException {
        Optimizer optimizer = new Optimizer();
        routesForInvoice = optimizer.filtrate(plannedSchedule, invoiceContainer);
        optimizer.optimize(plannedSchedule, invoiceContainer, routesForInvoice);
        System.out.println(optimizer.getPossibleDepartureDate(invoiceContainer.get(0).getRoute(), invoiceContainer.get(0)));
        Assert.assertEquals(invoiceContainer.get(0).getRoute(), route2);
        Assert.assertEquals(invoiceContainer.get(3).getRoute(), route);
        Assert.assertNotEquals(invoiceContainer.get(3).getRoute(), route3);
        Assert.assertNotEquals(invoiceContainer.get(3).getRoute(), route4);
        Assert.assertEquals(invoiceContainer.get(invoiceContainer.size()-1).getRoute(), route);
    }
}
