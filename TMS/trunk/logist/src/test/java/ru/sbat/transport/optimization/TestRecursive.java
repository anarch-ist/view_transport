package ru.sbat.transport.optimization;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.location.*;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
//import ru.sbat.transport.optimization.utilsForTests.RoutesFactory;

import java.util.*;

//public class TestRecursive {
//    static PlannedSchedule plannedSchedule = new PlannedSchedule();
//    static RoutesFactory routesFactory = new RoutesFactory(80, 1000);
//
//    // создание планового расписания
//    @BeforeClass
//    public static void createPlannedSchedule(){
//        plannedSchedule.addAll(routesFactory.getRoutes());
//    }
//
//    @Test
//    public void testRecursive() {
//        Optimizer optimizer = new Optimizer();
//        List<DeliveryRoute> result = new ArrayList<>();
//        List<Point> markedPoints = new ArrayList<>();
//        List<DeliveryRoute> possibleDeliveryRoutes = optimizer.startRecursive(plannedSchedule, routesFactory.getDepartureDeliveryPoints().get(0), routesFactory.getDepartureDeliveryPoints().get(1), result, markedPoints);
//        for (DeliveryRoute deliveryRoute : possibleDeliveryRoutes) {
//            for (TrackCourse trackCourse : deliveryRoute) {
//                System.out.println("Track Course: start point = " + trackCourse.getStartTrackCourse().getDeparturePoint().getPointId() + ", end point = " + trackCourse.getEndTrackCourse().getDeparturePoint().getPointId() + ", route = " + trackCourse.getRoute().getPointsAsString());
//            }
//            System.out.println("_______________________");
//        }
//        System.out.println(optimizer.getCount());
//    }
//}
