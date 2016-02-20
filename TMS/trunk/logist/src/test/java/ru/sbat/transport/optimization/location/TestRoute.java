package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.Collections;

public class TestRoute {

    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static Route route = new Route();
    static WarehousePoint warehousePoint1 = new WarehousePoint();
    static TradeRepresentativePoint tradeRepresentativePoint1 = new TradeRepresentativePoint();
    static TradeRepresentativePoint tradeRepresentativePoint2 = new TradeRepresentativePoint();

    @BeforeClass
    public static void createPlannedSchedule() {
        initRoute(
                route,
                // данные о машине(грузоподъемность в т., объем, стоимость), пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта, время ПРР, расстояние до следующего пункта
                createRoutePoint(createCharacteristicsOfCar(10, 15, 13054.5), warehousePoint1,           2, 930, 600,   0, 120),
                createRoutePoint(createCharacteristicsOfCar(15, 25,   17894), tradeRepresentativePoint1, 3, 180, 960,  90,  50),
                createRoutePoint(createCharacteristicsOfCar(20, 30,   25040), tradeRepresentativePoint2, 3,   0,   0, 120, 300)
        );
        plannedSchedule.add(route);
    }

    //----------СЛУЖЕБНЫЕ МЕТОДЫ--------------
    public static void initRoute(Route route, RoutePoint... routePoints) {
        Collections.addAll(route, routePoints);
    }

    public static RoutePoint createRoutePoint(CharacteristicsOfCar characteristicsOfCar, Point departurePoint, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, double distanceToNextPoint) {
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

    public static CharacteristicsOfCar createCharacteristicsOfCar(double capacityCar, double volumeCar, double cost) {
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setCapacityCar(capacityCar);
        result.setVolumeCar(volumeCar);
        result.setCost(cost);
        return result;
    }
    //-------------END СЛУЖЕБНЫЕ МЕТОДЫ-------------

    @Test
    public void testGetFullDistance() throws Exception {
        Assert.assertEquals(470, plannedSchedule.get(0).getFullDistance(), 0);
    }

    @Test
    public void testGetFullTime() throws Exception {
        Assert.assertEquals(1770, plannedSchedule.get(0).getFullTime(), 0);
    }

    @Test
    public void testGetDepartureTime() throws Exception {
        Assert.assertEquals(930, plannedSchedule.get(0).getDepartureTime(), 0);
    }

    @Test
    public void testGetArrivalTime() throws Exception {
        Assert.assertEquals(1260, plannedSchedule.get(0).getArrivalTime(), 0);
    }

    @Test
    public void testGetArrivalPoint() throws Exception {
        Assert.assertEquals(tradeRepresentativePoint2, plannedSchedule.get(0).getArrivalPoint());
    }

    @Test
    public void testGetDeparturePoint() throws Exception {
        Assert.assertEquals(warehousePoint1, plannedSchedule.get(0).getDeparturePoint());
    }

    @Test
    public void testGetWeekDayOfActualDeliveryTime() {
        Assert.assertEquals(3, plannedSchedule.get(0).getWeekDayOfActualDeliveryTimeInRoutePoint(tradeRepresentativePoint1));
    }

    @Test
    public void testGetActualDeliveryTime() {
        Assert.assertEquals(3, plannedSchedule.get(0).getActualDeliveryTimeInRoutePoint(tradeRepresentativePoint1).getHours());
        Assert.assertEquals(0, plannedSchedule.get(0).getActualDeliveryTimeInRoutePoint(tradeRepresentativePoint1).getMinutes());
        Assert.assertEquals(21, plannedSchedule.get(0).getActualDeliveryTimeInRoutePoint(tradeRepresentativePoint2).getHours());
        Assert.assertEquals(0, plannedSchedule.get(0).getActualDeliveryTimeInRoutePoint(tradeRepresentativePoint2).getMinutes());
        Assert.assertEquals(15, plannedSchedule.get(0).getActualDeliveryTimeInRoutePoint(warehousePoint1).getHours());
        Assert.assertEquals(30, plannedSchedule.get(0).getActualDeliveryTimeInRoutePoint(warehousePoint1).getMinutes());
    }

    @Test
    public void testSplitTimeComponents() {
        Assert.assertEquals(21, plannedSchedule.get(0).splitToComponentTime(1270)[0]);
        Assert.assertEquals(10, plannedSchedule.get(0).splitToComponentTime(1270)[1]);
    }

    @Test
    public void testGetWeekDayOfDepartureTime() {
        Assert.assertEquals(2, plannedSchedule.get(0).getWeekDayOfDepartureTime());
    }

    @Test
    public void testGetDaysCountOfRoute() {
        Assert.assertEquals(1, plannedSchedule.get(0).getDaysCountOfRoute());
    }

    @Test
    public void testGetStartingCost() {
        Assert.assertEquals(13054.5, plannedSchedule.get(0).getStartingCost(), 0);
    }
}