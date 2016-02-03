package ru.sbat.transport.optimization.location;


import java.util.Date;

public interface IRoute {
    Double getFullDistance();
    Integer getFullTime();
    Integer getDepartureTime();
    Integer getArrivalTime();
    Point getArrivalPoint();
    Point getDeparturePoint();
    int getWeekDayOfActualDeliveryTime();
    Date getActualDeliveryTime();
    int[] splitToComponentTime(Integer time);
    int getWeekDayOfDepartureTime();
    int getDaysCountOfRoute();
    double getStartingCost();
}
