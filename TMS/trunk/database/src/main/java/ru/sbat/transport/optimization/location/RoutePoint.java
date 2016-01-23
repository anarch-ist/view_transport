package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.utils.DayOfWeek;


public class RoutePoint {
    private CharacteristicsOfCar characteristicsOfCar;
    private Integer departureTime;
    private DayOfWeek dayOfWeek;
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

    public int getWeekDay(){
        int result = 0;
        DayOfWeek dayOfWeek = this.getDayOfWeek();
        switch (dayOfWeek){
            case SUNDAY: result = 1;
                break;
            case MONDAY: result = 2;
                break;
            case TUESDAY: result = 3;
                break;
            case WEDNESDAY: result = 4;
                break;
            case THURSDAY: result = 5;
                break;
            case FRIDAY: result = 6;
                break;
            case SATURDAY: result = 7;
                break;
        }
        return result;
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
