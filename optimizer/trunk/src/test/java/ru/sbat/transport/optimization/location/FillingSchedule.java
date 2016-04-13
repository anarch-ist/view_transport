package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.schedule.PlannedSchedule;

public class FillingSchedule {

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


    public PlannedSchedule fillFirstVersionOfSchedule(PlannedSchedule plannedSchedule){
        Util.initRoute(
                route1,
                2,
                Util.getCharacteristicsOfCar(10000000),
                920,
                Util.createRoutePoint(x,   0),
                Util.createRoutePoint(c,  80),
                Util.createRoutePoint(a, 120)
        );
        Util.initRoute(
                route2,
                4,
                Util.getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(p,  80),
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
        return plannedSchedule;
    }

    public PlannedSchedule fillSecondVersionOfSchedule(PlannedSchedule plannedSchedule){
        Util.initRoute(
                route1,
                2,
                Util.getCharacteristicsOfCar(10000000),
                920,
                Util.createRoutePoint(x,   0),
                Util.createRoutePoint(c,  80),
                Util.createRoutePoint(a, 120)
        );
        Util.initRoute(
                route2,
                4,
                Util.getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(p,  80),
                Util.createRoutePoint(z, 120),
                Util.createRoutePoint(q, 120)
        );
        Util.initRoute(
                route3,
                4,
                Util.getCharacteristicsOfCar(10000000),
                940,
                Util.createRoutePoint(p,   0),
                Util.createRoutePoint(a,  80),
                Util.createRoutePoint(b, 120),
                Util.createRoutePoint(c,  90)
        );
        Util.initRoute(
                route4,
                4,
                Util.getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(b,   0),
                Util.createRoutePoint(x,  80),
                Util.createRoutePoint(y, 120)
        );
        Util.initRoute(
                route5,
                4,
                Util.getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(c,   0),
                Util.createRoutePoint(f,  80),
                Util.createRoutePoint(z, 120)
        );
        Util.initRoute(
                route6,
                4,
                Util.getCharacteristicsOfCar(10000000),
                930,
                Util.createRoutePoint(y,   0),
                Util.createRoutePoint(w,  80),
                Util.createRoutePoint(z, 120)
        );
        Util.initRoute(
                route7,
                4,
                Util.getCharacteristicsOfCar(10000000),
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
        return plannedSchedule;
    }
}
