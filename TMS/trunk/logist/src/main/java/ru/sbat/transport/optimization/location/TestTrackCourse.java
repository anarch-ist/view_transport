package ru.sbat.transport.optimization.location;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.TrackCourse;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestTrackCourse {
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static Route route = new Route();
    static WarehousePoint c = new WarehousePoint("C");
    static TradeRepresentativePoint x = new TradeRepresentativePoint("X");
    static TradeRepresentativePoint a = new TradeRepresentativePoint("A");

    @BeforeClass
    public static void createPlannedSchedule() {
        initRoute(
                route,
                createRoutePoint(c, 5, 930, 600,   120, 120, getCharacteristicsOfCar(10)),
                createRoutePoint(x, 6, 150, 960,    60,  50, getCharacteristicsOfCar(10)),
                createRoutePoint(a, 6,   0,   0,    90, 300, getCharacteristicsOfCar(10))
        );

        plannedSchedule.add(route);
    }

    @Test
    public void testShareTrackCourses() {
        List<Point> points = new ArrayList<>();
        points.add(x);
        points.add(a);
        TrackCourse trackCourse = new TrackCourse();
        List<TrackCourse> trackCourses = trackCourse.sharePointsBetweenRoutes(points, plannedSchedule);
        System.out.println(trackCourses.size());
        for(TrackCourse trackCourse1: trackCourses){
            System.out.println(trackCourse.getStartTrackCourse().getDeparturePoint().getPointId());
        }
    }


    // -------- СЛУЖЕБНЫЕ МЕТОДЫ -----------

    private static void initRoute(Route route, RoutePoint... routePoints) {
        Collections.addAll(route, routePoints);
    }

    private static RoutePoint createRoutePoint(Point point, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, double distanceToNextPoint, CharacteristicsOfCar characteristicsOfCar) {
        RoutePoint result = new RoutePoint();
        result.setDeparturePoint(point);
        result.setDayOfWeek(dayOfWeek);
        result.setDepartureTime(departureTime); // в минутах от начала суток
        result.setTimeToNextPoint(timeToNextPoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        result.setDistanceToNextPoint(distanceToNextPoint);
        result.setCharacteristicsOfCar(characteristicsOfCar);
        return result;
    }

    public static CharacteristicsOfCar getCharacteristicsOfCar(double occupancyCost){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setOccupancyCost(occupancyCost);
        return result;
    }
    // -------------- END СЛУЖЕБНЫЕ МЕТОДЫ ---------------
}
