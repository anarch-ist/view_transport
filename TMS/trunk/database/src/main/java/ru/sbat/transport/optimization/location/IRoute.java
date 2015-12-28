package ru.sbat.transport.optimization.location;


public interface IRoute {
    Double getFullDistance();
    Integer getFullTime();
    Integer getDepartureTime();
    Integer getArrivalTime();
    Point getArrivalPoint();
}
