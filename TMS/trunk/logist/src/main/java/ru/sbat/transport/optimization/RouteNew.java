package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.CharacteristicsOfCar;
import ru.sbat.transport.optimization.location.IRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.RoutePoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RouteNew extends LinkedList<TrackCourse> implements IRoute{
    private CharacteristicsOfCar characteristicsOfCar;
    private int departureTimeFromFirstPoint;
    private int dayOfWeek;

    public CharacteristicsOfCar getCharacteristicsOfCar() {
        return characteristicsOfCar;
    }

    public void setCharacteristicsOfCar(CharacteristicsOfCar characteristicsOfCar) {
        this.characteristicsOfCar = characteristicsOfCar;
    }

    public int getDepartureTimeFromFirstPoint() {
        return departureTimeFromFirstPoint;
    }

    public void setDepartureTimeFromFirstPoint(int departureTimeFromFirstPoint) {
        this.departureTimeFromFirstPoint = departureTimeFromFirstPoint;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean containsPoint(Point point) {
        for(TrackCourse trackCourse: this){
            if(trackCourse.getStartTrackCourse().getDeparturePoint().equals(point) || trackCourse.getEndTrackCourse().getDeparturePoint().equals(point)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLastPoint(Point point) {
        return (this.get(this.size()-1).getEndTrackCourse().getDeparturePoint().equals(point));
    }

    @Override
    public String getPointsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.get(0).getStartTrackCourse().getDeparturePoint().getPointId()).append(" ");
        for (TrackCourse trackCourse: this) {
            stringBuilder.append(trackCourse.getEndTrackCourse().getDeparturePoint().getPointId()).append(" ");
        }
        return stringBuilder.toString();
    }

    /** calculates the total distance on the route
     *
     * @return km route
     */
    @Override
    public Double getFullDistance() {
        double result = 0;
        for(TrackCourse trackCourse : this) {
            result += trackCourse.getDistanceBetweenRoutePoints();
        }
        return result;
    }

    /** calculates the total time on the route
     *
     * @return minutes of all route
     */
    @Override
    public Integer getFullTime() {
        int result = 0;
        for(TrackCourse trackCourse : this){
            result += trackCourse.getTravelTime() + trackCourse.getEndTrackCourse().getLoadingOperationsTime();
        }
        return result;
    }

    /** determines the time of departure
     *
     * @return minutes from the start of the day
     */
    public Integer getDepartureTime() {
        return this.getDepartureTimeFromFirstPoint();
    }

    /** determines the arrival time to the final point of the route
     *
     * @return minutes from the start of the day
     */
    @Override
    public Integer getArrivalTime() {
        return -1;
    }

    /** determines the final point of the route
     *
     * @return the final point
     */
    @Override
    public Point getArrivalPoint() {
        return this.get(this.size()-1).getEndTrackCourse().getDeparturePoint();
    }

    /** determines the first point of the route
     *
     * @return the first point of the route
     */
    @Override
    public Point getDeparturePoint() {
        return this.get(0).getStartTrackCourse().getDeparturePoint();
    }

    /** determines day of week of arrival in the point of route
     *
     * @return day of week of point
     */
    @Override
    public int getWeekDayOfActualDeliveryDateInRoutePoint(RoutePoint routePoint) {
        return -1;
    }

    /** determines time of arrival in the point of route
     *
     * @return current day, month, year with REAL hours and minutes of arrival in the point
     */
    @Override
    public Date getActualDeliveryTimeInRoutePoint(RoutePoint routePoint) {
        return new Date();
    }

    /** parts minutes from the start of the day to hours and minutes
     *
     * @param time
     * @return int array of hours(0) and minutes(1)
     */
    @Override
    public int[] splitToComponentTime(Integer time) {
        int hours = time / 60;
        int minutes = (int) time - hours * 60;
        int[] result = new int[]{hours, minutes};
        return result;
    }

    /** determines day of week of departure from the first point of route
     *
     * @return day of week from the first element of route
     */
    @Override
    public int getWeekDayOfDepartureTime() {
        return this.getDayOfWeek();
    }

    /** calculates count of days in route
     *
     * @return count of days
     */
    @Override
    public int getDaysCountOfRoute() {
        return -1;
    }

    @Override
    public double getStartingOccupancyCost() {
        return this.getCharacteristicsOfCar().getOccupancyCost();
    }

    @Override
    public List<TrackCourse> splitRouteIntoTrackCourse() {
        return this;
    }

    public List<RoutePoint> splitRouteNewIntoRoutePoints(){
        List<RoutePoint> result = new ArrayList<>();
        result.add(this.get(0).getStartTrackCourse());
        for(TrackCourse trackCourse: this){
            result.add(trackCourse.getEndTrackCourse());
        }
        return result;
    }

    public boolean isCorrectRoutePoint(RoutePoint routePoint) {
        boolean result = true;
        if(this.size() >= 1) {
            if (this.splitRouteNewIntoRoutePoints().get(this.size() - 1).getDeparturePoint().equals(routePoint.getDeparturePoint())) {
                return false;
            }
        }
        return result;
    }
}
