package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.utils.DayOfWeek;


public class TrackCourse {
    private Integer departureTime;
    private Integer timeToNextPoint;
    private DayOfWeek dayOfWeek;
    private Double distanceToNextPoint; // km
    private Point departurePoint;

    public Integer getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Integer departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getTimeToNextPoint() {
        return timeToNextPoint;
    }

    public void setTimeToNextPoint(Integer timeToNextPoint) {
        this.timeToNextPoint = timeToNextPoint;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Double getDistanceToNextPoint() {
        return distanceToNextPoint;
    }

    public void setDistanceToNextPoint(Double distanceToNextPoint) {
        this.distanceToNextPoint = distanceToNextPoint;
    }

    public Point getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(Point departurePoint) {
        this.departurePoint = departurePoint;
    }


    @Override
    public String toString() {
        return "TrackCourse{" +
                "departureTime=" + departureTime +
                ", timeToNextPoint=" + timeToNextPoint +
                ", dayOfWeek=" + dayOfWeek +
                ", distanceToNextPoint=" + distanceToNextPoint +
                ", departurePoint=" + departurePoint +
                '}';
    }
}
