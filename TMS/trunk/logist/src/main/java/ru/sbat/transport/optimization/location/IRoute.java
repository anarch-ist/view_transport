package ru.sbat.transport.optimization.location;


import java.util.Date;

public interface IRoute {
    boolean containsPoint(Point point, boolean considerLastPoint);
    Double getFullDistance();
    Integer getFullTime();
    Integer getDepartureTime();
    Integer getArrivalTime();
    Point getArrivalPoint();
    Point getDeparturePoint();
    int getWeekDayOfActualDeliveryDateInRoutePoint(RoutePoint routePoint);
    Date getActualDeliveryTimeInRoutePoint(RoutePoint routePoint);
    int[] splitToComponentTime(Integer time);
    int getWeekDayOfDepartureTime();
    int getDaysCountOfRoute();
    double getStartingOccupancyCost();
    double getStartingWeight();
    double getStartingVolume();
}
