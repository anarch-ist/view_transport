package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.utils.DayOfWeek;

import static org.junit.Assert.*;

public class RouteTest {

    @Test
    public void getDist() throws Exception {
        Route route = new Route();
        TrackCourse trackCourse1 = new TrackCourse();
        TrackCourse trackCourse2 = new TrackCourse();
        TrackCourse trackCourse3 = new TrackCourse();
        trackCourse1.setDistanceToNextPoint(150.0);
        trackCourse2.setDistanceToNextPoint(130.0);
        trackCourse3.setDistanceToNextPoint(550.0);
        route.add(trackCourse1);
        route.add(trackCourse2);
        route.add(trackCourse3);
        if (route.getFullDistance() != 830) throw new AssertionError();
    }
}