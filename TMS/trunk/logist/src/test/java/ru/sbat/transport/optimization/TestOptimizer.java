package ru.sbat.transport.optimization;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.location.*;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.text.ParseException;
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

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){
        Util.initRoute(
                route1,
                3,
                Util.getCharacteristicsOfCar(10_000_000),
                420,
                Util.createRoutePoint(x,   0),
                Util.createRoutePoint(c,  60),
                Util.createRoutePoint(a, 120)
        );
        Util.initRoute(
                route2,
                3,
                Util.getCharacteristicsOfCar(10_000_000),
                1080,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(p,  60),
                Util.createRoutePoint(z, 120),
                Util.createRoutePoint(q,  30)
        );
        Util.initRoute(
                route3,
                6,
                Util.getCharacteristicsOfCar(10_000_000),
                720,
                Util.createRoutePoint(p,   0),
                Util.createRoutePoint(a,  60),
                Util.createRoutePoint(b, 120),
                Util.createRoutePoint(c,  90)
        );
        Util.initRoute(
                route4,
                3,
                Util.getCharacteristicsOfCar(10_000_000),
                720,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(x,  30),
                Util.createRoutePoint(y,  30)
        );
        Util.initRoute(
                route5,
                4,
                Util.getCharacteristicsOfCar(10_000_000),
                900,
                Util.createRoutePoint(c,   0),
                Util.createRoutePoint(f,  60),
                Util.createRoutePoint(z,  30)
        );
        Util.initRoute(
                route6,
                5,
                Util.getCharacteristicsOfCar(10_000_000),
                1020,
                Util.createRoutePoint(y,   0),
                Util.createRoutePoint(w,  60),
                Util.createRoutePoint(z,  30)
        );
        Util.initRoute(
                route7,
                6,
                Util.getCharacteristicsOfCar(10_000_000),
                600,
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
        route2.get(0).setTravelTime(240);
        route2.get(1).setTravelTime(300);
        route2.get(0).setRoute(route2);
        route2.get(1).setRoute(route2);
        route2.get(2).setDistanceBetweenRoutePoints(390);
        route2.get(2).setTravelTime(180);
        route2.get(2).setRoute(route2);

        route3.get(0).setDistanceBetweenRoutePoints(120);
        route3.get(1).setDistanceBetweenRoutePoints(50);
        route3.get(0).setTravelTime(600);
        route3.get(1).setTravelTime(480);
        route3.get(0).setRoute(route3);
        route3.get(1).setRoute(route3);
        route3.get(2).setDistanceBetweenRoutePoints(390);
        route3.get(2).setTravelTime(720);
        route3.get(2).setRoute(route3);

        route4.get(0).setDistanceBetweenRoutePoints(120);
        route4.get(1).setDistanceBetweenRoutePoints(50);
        route4.get(0).setTravelTime(420);
        route4.get(1).setTravelTime(180);
        route4.get(0).setRoute(route4);
        route4.get(1).setRoute(route4);

        route5.get(0).setDistanceBetweenRoutePoints(120);
        route5.get(1).setDistanceBetweenRoutePoints(50);
        route5.get(0).setTravelTime(480);
        route5.get(1).setTravelTime(600);
        route5.get(0).setRoute(route5);
        route5.get(1).setRoute(route5);

        route6.get(0).setDistanceBetweenRoutePoints(120);
        route6.get(1).setDistanceBetweenRoutePoints(50);
        route6.get(0).setTravelTime(240);
        route6.get(1).setTravelTime(420);
        route6.get(0).setRoute(route6);
        route6.get(1).setRoute(route6);

        route7.get(0).setDistanceBetweenRoutePoints(120);
        route7.get(0).setTravelTime(360);
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
        Request request = Util.createRequest(z,  Util.createDate(2016, Calendar.FEBRUARY, 16, 20,  45));
        Invoice invoice = Util.createInvoice(c,  Util.createDate(2016, Calendar.JANUARY, 30, 12, 0),     request, 5_000_000);
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

    @Test
    public void testIsTheSameWeek() throws IncorrectRequirement {
        Optimizer optimizer = new Optimizer();
        DeliveryRoute deliveryRoute = new DeliveryRoute();
        deliveryRoute.add(route2.get(0));
        deliveryRoute.add(route2.get(1));
        Assert.assertTrue(optimizer.isTheSameWeek(deliveryRoute.get(0), deliveryRoute.get(1)));
    }

    @Test
    public void testIsFittingTrackCourseByLoadCostAndNumberOfWeek(){
        Optimizer optimizer = new Optimizer();
        TrackCourse trackCourse = route3.get(1);
        List<LoadUnit> loadUnits = new ArrayList<>();
        LoadUnit loadUnit = new LoadUnit();
        loadUnit.setNumberOfWeek(11);
        loadUnit.setLoadCost(5_000_010);
        loadUnits.add(loadUnit);
        trackCourse.setLoadUnits(loadUnits);
        Assert.assertTrue(optimizer.isFittingTrackCourseByLoadCostAndNumberOfWeek(invoiceContainer.get(0), trackCourse, 10));
    }

    @Test
    public void testAssignTrackCoursesPossibleWeeksByCost() throws IncorrectRequirement {
        Optimizer optimizer = new Optimizer();
        DeliveryRoute deliveryRoute = new DeliveryRoute();
        deliveryRoute.add(route3.get(1));
        deliveryRoute.add(route2.get(0));
        deliveryRoute.add(route2.get(1));
        System.out.println(invoiceContainer.get(0).calculateNumbersOfWeeksBetweenDates(invoiceContainer.get(0).getCreationDate(),
                invoiceContainer.get(0).getRequest().getPlannedDeliveryDate(), invoiceContainer.get(0), deliveryRoute.get(0), deliveryRoute.get(2)));
        List<PartOfDeliveryRoute> partOfDeliveryRoutes = optimizer.assignTrackCoursesPossibleWeeksByCost(invoiceContainer.get(0), deliveryRoute, invoiceContainer.get(0).calculateNumbersOfWeeksBetweenDates(invoiceContainer.get(0).getCreationDate(),
                invoiceContainer.get(0).getRequest().getPlannedDeliveryDate(), invoiceContainer.get(0), deliveryRoute.get(0), deliveryRoute.get(2)));
        for(PartOfDeliveryRoute partOfDeliveryRoute: partOfDeliveryRoutes){
            System.out.println(partOfDeliveryRoute.getTrackCourse().getStartTrackCourse().getPoint().getPointId() + " " + partOfDeliveryRoute.getTrackCourse().getEndTrackCourse().getPoint().getPointId() + ": " + partOfDeliveryRoute.getNumberOfWeek());
        }
    }

    @Test
    public void testOptimize() throws ParseException, RouteNotFoundException, IncorrectRequirement {
        Optimizer optimizer = new Optimizer();
        DeliveryRoute deliveryRoute = new DeliveryRoute();
//        deliveryRoute.add(route3.get(1));
//        deliveryRoute.add(route3.get(2));
//        deliveryRoute.add(route5.get(0));
//        deliveryRoute.add(route5.get(1));

        deliveryRoute.add(route3.get(1));
        deliveryRoute.add(route4.get(0));
        deliveryRoute.add(route1.get(0));
        deliveryRoute.add(route5.get(0));
        deliveryRoute.add(route5.get(1));

//        deliveryRoute.add(route3.get(1));
//        deliveryRoute.add(route2.get(0));
//        deliveryRoute.add(route2.get(1));
        Map<Invoice, List<DeliveryRoute>> invoiceListMap = new HashMap<>();
        List<DeliveryRoute> deliveryRoutes = new ArrayList<>();
        deliveryRoutes.add(deliveryRoute);
        invoiceListMap.put(invoiceContainer.get(0), deliveryRoutes);
        optimizer.optimize(plannedSchedule, invoiceListMap);
        List<PartOfDeliveryRoute> partOfDeliveryRoutes = invoiceContainer.get(0).getPartsOfDeliveryRoute();
        System.out.println(invoiceContainer.get(0).calculateNumbersOfWeeksBetweenDates(invoiceContainer.get(0).getCreationDate(),
                invoiceContainer.get(0).getRequest().getPlannedDeliveryDate(), invoiceContainer.get(0), deliveryRoute.get(0), deliveryRoute.get(4)));
        System.out.println(partOfDeliveryRoutes.size());
        for(PartOfDeliveryRoute partOfDeliveryRoute: partOfDeliveryRoutes) {
            System.out.println(partOfDeliveryRoute.getTrackCourse().getStartTrackCourse().getPoint().getPointId() + " " + partOfDeliveryRoute.getTrackCourse().getEndTrackCourse().getPoint().getPointId() + ": " + partOfDeliveryRoute.getNumberOfWeek());
        }
    }
}
