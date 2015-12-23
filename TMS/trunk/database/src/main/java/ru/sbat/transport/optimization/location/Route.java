package ru.sbat.transport.optimization.location;


import java.util.LinkedList;

public class Route extends LinkedList<TrackCourse> implements IRoute{

    @Override
    public Double getFullDistance() {
        double result = 0;
        for(TrackCourse trackCourse: this) {
            result += trackCourse.getDistanceToNextPoint();
        }
        return result;
    }

    @Override
    public Integer getFullTime() {
        return null;
    }

    @Override
    public Integer getDepartureTime() {
        return null;
    }

    @Override
    public Integer getArrivalTime() {
        return null;
    }
}
