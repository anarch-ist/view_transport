package ru.sbat.transport.optimization.location;



public class RoutePoint {

    private Point departurePoint;
    private Integer loadingOperationsTime;
    private Integer timeToNextPoint;
    private Integer departureTime;
    private int dayOfWeek;
    private Double distanceToNextPoint ;// km


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

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
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

    public Integer getLoadingOperationsTime() {
        return loadingOperationsTime;
    }

    public void setLoadingOperationsTime(Integer loadingOperationsTime) {
        this.loadingOperationsTime = loadingOperationsTime;
    }


    @Override
    public String toString() {
        return "RoutePoint{" +
                ", departureTime=" + departureTime +
                ", dayOfWeek=" + dayOfWeek +
                ", timeToNextPoint=" + timeToNextPoint +
                ", distanceToNextPoint=" + distanceToNextPoint +
                ", departurePoint=" + departurePoint +
                ", loadingOperationsTime=" + loadingOperationsTime +
                '}';
    }
}
