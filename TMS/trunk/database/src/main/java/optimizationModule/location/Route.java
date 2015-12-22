package optimizationModule.location;


import optimizationModule.utils.DayOfWeek;

public class Route{
    private Integer departureTime;
    private DayOfWeek dayOfWeek;
    private Integer timeToNextArrival;
    private Point point;

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

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
