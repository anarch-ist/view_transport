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
        int result = 0;
        for(TrackCourse trackCourse: this){
            result += trackCourse.getTimeToNextPoint();
        }
        return result;
    }

    @Override
    public Integer getDepartureTime() {
        return this.get(0).getDepartureTime();
    }

    @Override
    public Integer getArrivalTime() {
        int result = 0;
        result = this.get(this.size()-2).getDepartureTime() + this.get(this.size()-2).getTimeToNextPoint();
        if(result > 24){
            result = result - 24;
        }
        return result;
    }

    @Override
    public Point getArrivalPoint() {
        Point result = this.get(this.size()-1).getArrivalPoint();
        return result;
    }


}
