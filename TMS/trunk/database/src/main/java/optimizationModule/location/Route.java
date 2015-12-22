package optimizationModule.location;


import optimizationModule.utils.DayOfWeek;

import java.util.ArrayList;

public class Route{
    private Integer departureTime;
    private DayOfWeek dayOfWeek;
    private Integer timeToNextArrival;
    private ArrayList<Point> points;

    public Integer getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Integer departureTime) {
        this.departureTime = departureTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getTimeToNextArrival() {
        return timeToNextArrival;
    }

    public void setTimeToNextArrival(Integer timeToNextArrival) {
        this.timeToNextArrival = timeToNextArrival;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }
}
