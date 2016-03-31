package ru.sbat.transport.optimization.location;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.TrackCourse;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.ArrayList;
import java.util.List;

public class TestTrackCourse {
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static RouteNew route = new RouteNew();
    static WarehousePoint c = new WarehousePoint("C");
    static TradeRepresentativePoint x = new TradeRepresentativePoint("X");
    static TradeRepresentativePoint a = new TradeRepresentativePoint("A");

    @BeforeClass
    public static void createPlannedSchedule() {
        Util.initRoute(
                route,
                3,
                Util.createCharacteristicsOfCar(10.0),
                930,
                Util.createRoutePoint(c,   0),
                Util.createRoutePoint(x,  90),
                Util.createRoutePoint(a, 120)
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
    public void testShareTrackCourses() {
        List<Point> points = new ArrayList<>();
        points.add(x);
        points.add(a);
        TrackCourse trackCourse = new TrackCourse();
        List<TrackCourse> trackCourses = trackCourse.sharePointsBetweenRoutes(points, plannedSchedule);
        System.out.println("Size = " + trackCourses.size());
        for(TrackCourse trackCourse1: trackCourses){
            System.out.println(trackCourse1.getStartTrackCourse().getPoint().getPointId());
            System.out.println(trackCourse1.getEndTrackCourse().getPoint().getPointId());
        }
    }
}
