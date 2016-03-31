package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.TrackCourse;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public interface IRoute {
    boolean containsPoint(Point point);
    boolean isLastPoint(Point point);
    String getPointsAsString();
    Double getFullDistance();
    Integer getFullTime();
    Integer getDepartureTime();
    Integer getArrivalTime() throws IncorrectRequirement;
    Point getArrivalPoint();
    Point getDeparturePoint();
    int getWeekDayOfActualArrivalDateInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement;
    int getActualArrivalTimeInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement;
    int[] splitToComponentTime(Integer time);
    int getWeekDayOfDepartureTime();
    int getDaysCountOfRoute() throws IncorrectRequirement;
    double getStartingOccupancyCost();

    boolean isCorrectRoutePoint(RoutePoint routePoint);
    boolean addRoutePoint(RoutePoint routePoint);
    RoutePoint getRoutePoint(Integer routePointNumber);
    Integer getRouteSize();
    Integer getIndexOfRoutePoint(RoutePoint routePoint);
    Collection<RoutePoint> asRoutePointCollection();

    List<TrackCourse> splitRouteIntoTrackCourse(IRoute route);

}
