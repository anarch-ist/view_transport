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

    @Test
    public void getTime() throws Exception {
        Route route = new Route();
        TrackCourse trackCourse1 = new TrackCourse();
        TrackCourse trackCourse2 = new TrackCourse();
        TrackCourse trackCourse3 = new TrackCourse();
        TrackCourse trackCourse4 = new TrackCourse();
        trackCourse1.setTimeToNextPoint(15);
        trackCourse2.setTimeToNextPoint(130);
        trackCourse3.setTimeToNextPoint(55);
        trackCourse4.setTimeToNextPoint(0);
        route.add(trackCourse1);
        route.add(trackCourse2);
        route.add(trackCourse3);
        route.add(trackCourse4);
        System.out.println(route.getFullTime());
        if (route.getFullTime() != 200) throw new AssertionError();
    }

    @Test
    public void getDepTime() throws Exception {
        Route route = new Route();
        TrackCourse trackCourse1 = new TrackCourse();
        TrackCourse trackCourse2 = new TrackCourse();
        TrackCourse trackCourse3 = new TrackCourse();
        trackCourse1.setDepartureTime(15);
        trackCourse2.setDepartureTime(13);
        trackCourse3.setDepartureTime(5);
        route.add(trackCourse1);
        route.add(trackCourse2);
        route.add(trackCourse3);
        if (route.getDepartureTime() != 15) throw new AssertionError();
    }

    @Test
    public void getArrTime() throws Exception {
        Route route = new Route();
        TrackCourse trackCourse1 = new TrackCourse();
        TrackCourse trackCourse2 = new TrackCourse();
        TrackCourse trackCourse3 = new TrackCourse();
        TrackCourse trackCourse4 = new TrackCourse();
        trackCourse1.setDepartureTime(15);
        trackCourse2.setDepartureTime(13);
        trackCourse3.setDepartureTime(5);
        trackCourse4.setDepartureTime(0);
        trackCourse1.setTimeToNextPoint(22);
        trackCourse2.setTimeToNextPoint(16);
        trackCourse3.setTimeToNextPoint(25);
        trackCourse4.setTimeToNextPoint(0);
        route.add(trackCourse1);
        route.add(trackCourse2);
        route.add(trackCourse3);
        route.add(trackCourse4);
        if (route.getArrivalTime() != 6) throw new AssertionError();
    }

    @Test
    public void getArrPoint() throws Exception {
        Route route = new Route();
        TrackCourse trackCourse1 = new TrackCourse();
        TrackCourse trackCourse2 = new TrackCourse();
        TrackCourse trackCourse3 = new TrackCourse();
        TrackCourse trackCourse4 = new TrackCourse();
        TradeRepresentativePoint tr1 = new TradeRepresentativePoint();
        TradeRepresentativePoint rightPoint = new TradeRepresentativePoint();
        trackCourse1.setArrivalPoint(tr1);
        trackCourse4.setArrivalPoint(rightPoint);
        route.add(trackCourse1);
        route.add(trackCourse2);
        route.add(trackCourse3);
        route.add(trackCourse4);
        if (!route.getArrivalPoint().equals(rightPoint)) throw new AssertionError();
    }
}