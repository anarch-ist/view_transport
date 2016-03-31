package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

public class TestRoute {

    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static RouteNew route = new RouteNew();
    static WarehousePoint warehousePoint1 = new WarehousePoint("g");
    static TradeRepresentativePoint tradeRepresentativePoint1 = new TradeRepresentativePoint("gt");
    static TradeRepresentativePoint tradeRepresentativePoint2 = new TradeRepresentativePoint("d");

    @BeforeClass
    public static void createPlannedSchedule() {
        Util.initRoute(
                route,
                3,
                Util.createCharacteristicsOfCar(10.0),
                930,
                // данные о машине(грузоподъемность в т., объем, стоимость), пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта, время ПРР, расстояние до следующего пункта
                Util.createRoutePoint(warehousePoint1,             0),
                Util.createRoutePoint(tradeRepresentativePoint1,  90),
                Util.createRoutePoint(tradeRepresentativePoint2, 120)
        );
        route.get(0).setDistanceBetweenRoutePoints(120);
        route.get(1).setDistanceBetweenRoutePoints(50);
        route.get(0).setTravelTime(600);
        route.get(1).setTravelTime(960);
        route.get(0).setRoute(route);
        route.get(1).setRoute(route);
        plannedSchedule.add(route);
    }

    @Test
    public void testGetFullDistance() throws Exception {
        Assert.assertEquals(170, plannedSchedule.get(0).getFullDistance(), 0);
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
        Assert.assertEquals(1140, plannedSchedule.get(0).getArrivalTime(), 0);
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
    public void testGetWeekDayOfActualDeliveryTime() throws IncorrectRequirement {
        Assert.assertEquals(3, plannedSchedule.get(0).getWeekDayOfActualArrivalDateInRoutePoint(route.get(0).getEndTrackCourse()));
        Assert.assertEquals(4, plannedSchedule.get(0).getWeekDayOfActualArrivalDateInRoutePoint(route.get(1).getEndTrackCourse()));
    }

    @Test
    public void testGetActualDeliveryTime() throws IncorrectRequirement {
        Assert.assertEquals(90, plannedSchedule.get(0).getActualArrivalTimeInRoutePoint(route.get(0).getEndTrackCourse()));
        Assert.assertEquals(1140, plannedSchedule.get(0).getActualArrivalTimeInRoutePoint(route.get(1).getEndTrackCourse()));
    }

    @Test
    public void testSplitTimeComponents() {
        Assert.assertEquals(21, plannedSchedule.get(0).splitToComponentTime(1270)[0]);
        Assert.assertEquals(10, plannedSchedule.get(0).splitToComponentTime(1270)[1]);
    }

    @Test
    public void testGetWeekDayOfDepartureTime() {
        Assert.assertEquals(3, plannedSchedule.get(0).getWeekDayOfDepartureTime());
    }

    @Test
    public void testGetDaysCountOfRoute() throws IncorrectRequirement {
        Assert.assertEquals(1, plannedSchedule.get(0).getDaysCountOfRoute());
    }

    @Test
    public void testGetStartingCost() {
        Assert.assertEquals(10.0, plannedSchedule.get(0).getStartingOccupancyCost(), 0);
    }
}