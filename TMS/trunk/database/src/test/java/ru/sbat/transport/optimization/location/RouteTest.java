package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.Collections;

public class RouteTest {

    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static Route route = new Route();
    static WarehousePoint warehousePoint1 = new WarehousePoint();
    static TradeRepresentativePoint tradeRepresentativePoint1 = new TradeRepresentativePoint();
    static TradeRepresentativePoint tradeRepresentativePoint2 = new TradeRepresentativePoint();

    //----------СЛУЖЕБНЫЕ МЕТОДЫ--------------
    public void createPlannedSchedule(){
        initRoute(
                route,
                createRoutePoint(createCharacteristicsOfCar(10, 15, 13054.5), warehousePoint1,         2, 930, 600,   0, 120),
                createRoutePoint(createCharacteristicsOfCar(15, 25, 17894), tradeRepresentativePoint1, 3, 180, 960,  90,  50),
                createRoutePoint(createCharacteristicsOfCar(20, 30, 25040), tradeRepresentativePoint2, 3,   0,   0, 120, 300)
        );
        plannedSchedule.add(route);
    }

    public void initRoute(Route route, RoutePoint... routePoints){
        Collections.addAll(route, routePoints);
    }

    public RoutePoint createRoutePoint(CharacteristicsOfCar characteristicsOfCar,  Point departurePoint, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, double distanceToNextPoint){
        RoutePoint result = new RoutePoint();
        result.setCharacteristicsOfCar(characteristicsOfCar);
        result.setDepartureTime(departureTime);
        result.setDayOfWeek(dayOfWeek);
        result.setTimeToNextPoint(timeToNextPoint);
        result.setDistanceToNextPoint(distanceToNextPoint);
        result.setDeparturePoint(departurePoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        return result;
    }

    public CharacteristicsOfCar createCharacteristicsOfCar(double capacityCar, double volumeCar, double cost){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setCapacityCar(capacityCar);
        result.setVolumeCar(volumeCar);
        result.setCost(cost);
        return result;
    }
    //-------------END СЛУЖЕБНЫЕ МЕТОДЫ-------------

    @Test
    public void testGetFullDistance() throws Exception {

        if (route.getFullDistance() != 470) throw new AssertionError();
    }

    @Test
    public void testGetFullTime() throws Exception {
        if (route.getFullTime() != 1770) throw new AssertionError();
    }

    @Test
    public void testGetDepartureTime() throws Exception {
        if (route.getDepartureTime() != 1110) throw new AssertionError();
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
        routePoint3.setLoadingOperationsTime(83);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        Assert.assertEquals(5, route.getActualDeliveryTime().getHours());
        Assert.assertEquals(23, route.getActualDeliveryTime().getMinutes());
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

    @Test
    public void testGetDaysCountOfRoute(){
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        routePoint1.setDayOfWeek(1);
        routePoint2.setDayOfWeek(1);
        routePoint3.setDayOfWeek(1);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        Assert.assertEquals(0, route.getDaysCountOfRoute());
    }

    @Test
    public void testGetStartingCost(){
        Route route = new Route();
        RoutePoint routePoint1 = new RoutePoint();
        RoutePoint routePoint2 = new RoutePoint();
        RoutePoint routePoint3 = new RoutePoint();
        CharacteristicsOfCar characteristicsOfCar1 = new CharacteristicsOfCar();
        CharacteristicsOfCar characteristicsOfCar2 = new CharacteristicsOfCar();
        CharacteristicsOfCar characteristicsOfCar3 = new CharacteristicsOfCar();
        characteristicsOfCar1.setCost(125.5);
        characteristicsOfCar2.setCost(56);
        characteristicsOfCar3.setCost(100);
        routePoint1.setCharacteristicsOfCar(characteristicsOfCar1);
        routePoint2.setCharacteristicsOfCar(characteristicsOfCar2);
        routePoint3.setCharacteristicsOfCar(characteristicsOfCar3);
        route.add(routePoint1);
        route.add(routePoint2);
        route.add(routePoint3);
        System.out.println(route.getStartingCost());
        Assert.assertEquals(125.5, route.getStartingCost(), 0);
    }
}