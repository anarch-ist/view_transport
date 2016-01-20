package ru.sbat.transport.optimization.location;

import org.junit.*;

public class RouteTest {

    @Test
    public void getDist() throws Exception {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        routePoint1.setDistanceToNextPoint(150.0);
        routePoint2.setDistanceToNextPoint(130.0);
        routePoint3.setDistanceToNextPoint(550.0);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        if (route.getFullDistance() != 830) throw new AssertionError();
    }

    @Test
    public void getTime() throws Exception {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        RoutePoint routePoint4 = new RoutePoint();
        routePoint1.setTimeToNextPoint(15);
        routePoint2.setTimeToNextPoint(130);
        routePoint3.setTimeToNextPoint(55);
        routePoint4.setTimeToNextPoint(0);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        route.add(routePoint4);
        System.out.println(route.getFullTime());
        if (route.getFullTime() != 200) throw new AssertionError();
    }

    @Test
    public void getDepTime() throws Exception {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        routePoint1.setDepartureTime(15);
        routePoint2.setDepartureTime(13);
        routePoint3.setDepartureTime(5);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        if (route.getDepartureTime() != 15) throw new AssertionError();
    }

    @Test
    public void getArrTime() throws Exception {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        RoutePoint routePoint4 = new RoutePoint();
        routePoint1.setDepartureTime(15);
        routePoint2.setDepartureTime(13);
        routePoint3.setDepartureTime(5);
        routePoint4.setDepartureTime(0);
        routePoint1.setTimeToNextPoint(22);
        routePoint2.setTimeToNextPoint(16);
        routePoint3.setTimeToNextPoint(25);
        routePoint4.setTimeToNextPoint(0);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        route.add(routePoint4);
        if (route.getArrivalTime() != 6) throw new AssertionError();
    }

    @Test
    public void getArrPoint() throws Exception {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        RoutePoint routePoint4 = new RoutePoint();
        TradeRepresentativePoint tr1 = new TradeRepresentativePoint();
        TradeRepresentativePoint tr2 = new TradeRepresentativePoint();
        TradeRepresentativePoint tr3 = new TradeRepresentativePoint();
        TradeRepresentativePoint rightPoint = new TradeRepresentativePoint();
        routePoint1.setDeparturePoint(tr1);
        routePoint2.setDeparturePoint(tr2);
        routePoint3.setDeparturePoint(tr3);
        routePoint4.setDeparturePoint(rightPoint);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        route.add(routePoint4);
        if (!route.getArrivalPoint().equals(rightPoint)) throw new AssertionError();
    }

    @Test
    public void getDepPoint() throws Exception {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        RoutePoint routePoint4 = new RoutePoint();
        TradeRepresentativePoint rightPoint = new TradeRepresentativePoint();
        TradeRepresentativePoint tr2 = new TradeRepresentativePoint();
        TradeRepresentativePoint tr3 = new TradeRepresentativePoint();
        TradeRepresentativePoint tr4 = new TradeRepresentativePoint();
        routePoint1.setDeparturePoint(rightPoint);
        routePoint2.setDeparturePoint(tr2);
        routePoint3.setDeparturePoint(tr3);
        routePoint4.setDeparturePoint(tr4);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        route.add(routePoint4);
        if (!route.getDeparturePoint().equals(rightPoint)) throw new AssertionError();
    }
}