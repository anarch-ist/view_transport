package ru.sbat.transport.optimization.location;


import java.util.List;

public interface IRoute {
    boolean containsPoint(Point point);
    boolean isLastPoint(Point point);
    String getPointsAsString();
    Double getFullDistance();
    Integer getFullTime();
    Integer getArrivalTime();
    Point getArrivalPoint();
    Point getDeparturePoint();
    int[] splitToComponentTime(Integer time);
    List<Point> makePointsInRoute();

}
