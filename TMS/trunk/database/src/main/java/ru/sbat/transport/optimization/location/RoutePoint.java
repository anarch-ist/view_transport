package ru.sbat.transport.optimization.location;



public class RoutePoint {
    private CharacteristicsOfCar characteristicsOfCar;
    private Integer departureTime;
    private int dayOfWeek;
    private Integer timeToNextPoint;
    private Double distanceToNextPoint ; // km
    private Point departurePoint;
    private Integer loadingOperationsTime;


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

    public CharacteristicsOfCar getCharacteristicsOfCar() {
        return characteristicsOfCar;
    }

    public void setCharacteristicsOfCar(CharacteristicsOfCar characteristicsOfCar) {
        this.characteristicsOfCar = characteristicsOfCar;
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
                "characteristicsOfCar=" + characteristicsOfCar +
                ", departureTime=" + departureTime +
                ", dayOfWeek=" + dayOfWeek +
                ", timeToNextPoint=" + timeToNextPoint +
                ", distanceToNextPoint=" + distanceToNextPoint +
                ", departurePoint=" + departurePoint +
                ", loadingOperationsTime=" + loadingOperationsTime +
                '}';
    }
}
