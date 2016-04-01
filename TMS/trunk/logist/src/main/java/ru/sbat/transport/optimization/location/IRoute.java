package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;

import java.util.Collection;
import java.util.List;

public interface IRoute {
    CharacteristicsOfCar getCharacteristicsOfCar();
    int getDayOfWeek();
    Double getFullDistance();
    Integer getFullTime();
    Integer getDepartureTime();
    Integer getArrivalTime() throws IncorrectRequirement;
    Integer getArrivalDayOfWeek() throws IncorrectRequirement;
    int getActualDepartureTimeFromRoutePoint(RoutePoint routePoint) throws IncorrectRequirement;
    int getWeekDayOfActualDepartureDateInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement;
    Point getArrivalPoint();
    Point getDeparturePoint();
    int getWeekDayOfActualArrivalDateInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement;
    int getActualArrivalTimeInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement;
    int getWeekDayOfDepartureTime();
    int getDaysCountOfRoute() throws IncorrectRequirement;
    double getStartingOccupancyCost();
    boolean containsPoint(Point point);
    boolean isLastPoint(Point point);
    String getPointsAsString();
    int[] splitToComponentTime(Integer time);
    boolean isCorrectRoutePoint(RoutePoint routePoint);
    boolean addRoutePoint(RoutePoint routePoint);
    RoutePoint getRoutePoint(Integer routePointNumber);
    Integer getRouteSize();
    Integer getIndexOfRoutePoint(RoutePoint routePoint);
    Collection<RoutePoint> asRoutePointCollection();
    List<TrackCourse> splitRouteIntoTrackCourse(IRoute route);
}
