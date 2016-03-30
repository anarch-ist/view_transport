package ru.sbat.transport.optimization.utils;


import ru.sbat.transport.optimization.location.CharacteristicsOfCar;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.location.TradeRepresentativePoint;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RoutesFactory {
    private final Set<TradeRepresentativePoint> tradeRepresentativePoints;
    private final Set<Route> routes;


    public RoutesFactory(int pointsCount, int routesCount) {
        tradeRepresentativePoints = generatePoints(pointsCount);
        routes = generateRandomRoutes(routesCount);
        System.out.println(tradeRepresentativePoints);
        System.out.println(routes);
    }

    public Set<TradeRepresentativePoint> getTradeRepresentativePoints() {
        return tradeRepresentativePoints;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    private Set<TradeRepresentativePoint> generatePoints(Integer quantity) {
        Set<TradeRepresentativePoint> result = new HashSet<>();
        for (int i = 0; i < quantity; i++) {
            TradeRepresentativePoint tradeRepresentativePoint = new TradeRepresentativePoint("Point" + i);
            result.add(tradeRepresentativePoint);
        }
        return result;
    }

    private Route generateRandomRoute() {
        Route result = new Route();
        List<RoutePoint> routePoints = new ArrayList<>();
        int routeLength = randInt(2, 8);
        for (int i = 0; i < routeLength; i++) {
            RoutePoint routePoint = new RoutePoint();
            TradeRepresentativePoint randomPoint = getRandomPoint();
            routePoint.setDeparturePoint(randomPoint);
            routePoint.setLoadingOperationsTime(90);
            routePoint.setCharacteristicsOfCar(new CharacteristicsOfCar(1000));
            routePoint.setDayOfWeek(4);
            routePoint.setDepartureTime(900);
            routePoint.setDistanceToNextPoint(1000.0);
            routePoint.setTimeToNextPoint(600);
            boolean hasEqualPoint = false;
            for(RoutePoint routePointFromRoute: routePoints){
                if(routePointFromRoute.getDeparturePoint().getPointId().equals(routePoint.getDeparturePoint().getPointId())){
                    hasEqualPoint = true;
                    break;
                }
            }

            boolean wasAdded = true;
            if (!hasEqualPoint) {
                wasAdded = routePoints.add(routePoint);
            }

            if (!wasAdded || hasEqualPoint){
                i -= 1;
            }
        }
        result.addAll(routePoints);

//        for(int i = 0; i < routePoints.size() - 1; i++){
//            TrackCourse trackCourse = new TrackCourse();
//            trackCourse.setStartTrackCourse(routePoints.get(i));
//            trackCourse.setEndTrackCourse(routePoints.get(i + 1));
//            result.add(trackCourse);
//        }
        return result;
    }

    private Set<Route> generateRandomRoutes(int routesCount) {
        Set<Route> result = new HashSet<>();
        for (int i = 0; i < routesCount; i++) {
            result.add(generateRandomRoute());
        }
        return result;
    }

    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private TradeRepresentativePoint getRandomPoint() {
        int size = tradeRepresentativePoints.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(TradeRepresentativePoint obj : tradeRepresentativePoints) {
            if (i == item)
                return obj;
            i = i + 1;
        }
        return null;
    }

    public List<TradeRepresentativePoint> getDepartureDeliveryPoints(){
        List<TradeRepresentativePoint> result = new ArrayList<>();
        TradeRepresentativePoint departurePoint = getRandomPoint();
        TradeRepresentativePoint deliveryPoint = getRandomPoint();
        while (!departurePoint.equals(deliveryPoint)) {
            deliveryPoint = getRandomPoint();
        }
        result.add(departurePoint);
        result.add(deliveryPoint);
        return result;
    }
}
