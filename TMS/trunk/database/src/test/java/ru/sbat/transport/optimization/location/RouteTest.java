package ru.sbat.transport.optimization.location;

import org.junit.*;
import org.postgresql.ssl.jdbc4.AbstractJdbc4MakeSSL;

public class RouteTest {

    @Test
    public void testGetFullDistance() throws Exception {
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
    public void testGetFullTime() throws Exception {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        routePoint1.setTimeToNextPoint(10);
        routePoint2.setTimeToNextPoint(15);
        routePoint3.setTimeToNextPoint(0);
        routePoint1.setLoadingOperationsTime(0);
        routePoint2.setLoadingOperationsTime(2);
        routePoint3.setLoadingOperationsTime(2);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        if (route.getFullTime() != 29) throw new AssertionError();
    }

    @Test
    public void testGetDepartureTime() throws Exception {
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
    public void testGetArrivalTime() throws Exception {
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
    public void testGetArrivalPoint() throws Exception {
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
    public void testGetDeparturePoint() throws Exception {
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

    @Test
    public void testGetWeekDayOfActualDeliveryTime() {
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        RoutePoint routePoint4 = new RoutePoint();
        routePoint1.setDayOfWeek(1);
        routePoint2.setDayOfWeek(1);
        routePoint3.setDayOfWeek(2);
        routePoint4.setDayOfWeek(2);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        route.add(routePoint4);
        Assert.assertEquals(2, route.getWeekDayOfActualDeliveryTime());
    }

    @Test
    public void testGetActualDeliveryTime(){
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        routePoint1.setDepartureTime(510);
        routePoint1.setLoadingOperationsTime(0);
        routePoint1.setTimeToNextPoint(600);
        routePoint2.setDepartureTime(1200);
        routePoint2.setLoadingOperationsTime(90);
        routePoint2.setTimeToNextPoint(900);
        routePoint3.setDepartureTime(0);
        routePoint3.setLoadingOperationsTime(173);
        routePoint3.setTimeToNextPoint(0);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        Assert.assertEquals(13, route.getActualDeliveryTime().getHours());
        Assert.assertEquals(53, route.getActualDeliveryTime().getMinutes());
    }

    @Test
    public void testSplitTimeComponents() {
        Route route = new Route();
        Assert.assertEquals(13, route.splitToComponentTime(833)[0]);
        Assert.assertEquals(53, route.splitToComponentTime(833)[1]);
    }

    @Test
    public void testGetWeekDayOfDepartureTime(){
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        routePoint1.setDayOfWeek(4);
        routePoint2.setDayOfWeek(1);
        routePoint3.setDayOfWeek(2);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        Assert.assertEquals(4, route.getWeekDayOfDepartureTime());
    }
}