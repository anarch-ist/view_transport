package ru.sbat.transport.optimization;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.location.*;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.RoutesCreation;

import java.util.*;

public class TestRecursive {
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static RoutesCreation routesCreation = new RoutesCreation(80, 1000);

    // создание планового расписания
    @BeforeClass
    public static void createPlannedSchedule(){
        plannedSchedule.addAll(routesCreation.getUpdatedRoute());
    }

    @Test
    public void testRecursive() {
        Optimizer optimizer = new Optimizer();
        List<DeliveryRoute> result = new ArrayList<>();
        List<Point> markedPoints = new ArrayList<>();
        List<DeliveryRoute> possibleDeliveryRoutes = optimizer.startRecursive(plannedSchedule, routesCreation.getDepartureDeliveryPoints().get(0), routesCreation.getDepartureDeliveryPoints().get(1), result, markedPoints);
        for (DeliveryRoute deliveryRoute : possibleDeliveryRoutes) {
            for (TrackCourse trackCourse : deliveryRoute) {
                System.out.println("Track Course: start point = " + trackCourse.getStartTrackCourse().getPoint().getPointId() + ", end point = " + trackCourse.getEndTrackCourse().getPoint().getPointId() + ", route = " + trackCourse.getUpdatedRoute().getPointsAsString());
            }
            System.out.println("_______________________");
        }
    }
}
