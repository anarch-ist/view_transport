package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.TrackCourse;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class RouteNew extends LinkedList<TrackCourse> implements IRoute {

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
        return this.get(0).getStartTrackCourse().getDayOfWeek();
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
        return this.get(0).getStartTrackCourse().getDepartureTime();
    }

    /** determines the arrival time to the final point of the route
     *
     * @return minutes from the start of the day
     */
    @Override
    public Integer getArrivalTime() {
        int result = 0;
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        result = routePoints.get(routePoints.size()-2).getDepartureTime() + routePoints.get(routePoints.size()-2).getTimeToNextPoint() + routePoints.get(routePoints.size()-1).getLoadingOperationsTime();
        if(result >= 1440){
            result = result - 1440;
        }
        return result;
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
        int result = 0;
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        for(RoutePoint routePointFromRoute: routePoints){
            if(routePointFromRoute.equals(routePoint)){
                result = routePointFromRoute.getDayOfWeek();
            }
        }
        return result;
    }

    /** determines time of arrival in the point of route
     *
     * @return current day, month, year with REAL hours and minutes of arrival in the point
     */
    @Override
    public Date getActualDeliveryTimeInRoutePoint(RoutePoint routePoint) {
        Date date = new Date();
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        for(RoutePoint routePointFromRoute: routePoints) {
            if (routePointFromRoute.equals(routePoint)) {
                if (routePoints.indexOf(routePointFromRoute) != 0) {
                    int index = routePoints.indexOf(routePointFromRoute);
                    int time = routePoints.get(index - 1).getDepartureTime() + routePoints.get(index - 1).getTimeToNextPoint() + routePoints.get(index).getLoadingOperationsTime();
                    if (time >= 1440) {
                        time = (time - 1440);
                    }
                    int[] hoursAndMinutes = splitToComponentTime(time);
                    date.setHours(hoursAndMinutes[0]);
                    date.setMinutes(hoursAndMinutes[1]);
                }else if(routePoints.indexOf(routePointFromRoute) == 0){
                    int time = routePointFromRoute.getDepartureTime();
                    if (time >= 1440) {
                        time = (time - 1440);
                    }
                    int[] hoursAndMinutes = splitToComponentTime(time);
                    date.setHours(hoursAndMinutes[0]);
                    date.setMinutes(hoursAndMinutes[1]);
                }
            }
        }
        return date;
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
        return this.get(0).getStartTrackCourse().getDayOfWeek();
    }

    /** calculates count of days in route
     *
     * @return count of days
     */
    @Override
    public int getDaysCountOfRoute() {
        int result = this.get(this.size()-1).getEndTrackCourse().getDayOfWeek() - this.getDayOfWeek();
        if(result < 0){
            result = 7 - (this.getDayOfWeek() - this.get(this.size()-1).getEndTrackCourse().getDayOfWeek());
        }
        return result;
    }

    @Override
    public double getStartingOccupancyCost() {
        return this.get(0).getStartTrackCourse().getCharacteristicsOfCar().getOccupancyCost();
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

    @Override
    public boolean addRoutePoint(RoutePoint routePoint) {
        if (!isCorrectRoutePoint(routePoint))
            return false;

        if(this.isEmpty()){
            TrackCourse trackCourse = new TrackCourse();
            trackCourse.setStartTrackCourse(routePoint);
            this.add(trackCourse);
        }else if(!this.isEmpty() && this.size() == 1 && this.get(0).getEndTrackCourse() == null && this.get(0).getStartTrackCourse() != null){
            this.get(0).setEndTrackCourse(routePoint);
        } else {
            TrackCourse trackCourse = new TrackCourse();
            trackCourse.setStartTrackCourse(this.get(this.size() - 1).getEndTrackCourse());
            trackCourse.setEndTrackCourse(routePoint);
            this.add(trackCourse);
        }

        return true;
    }

    @Override
    public RoutePoint getRoutePoint(Integer routePointNumber) {
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        RoutePoint result = routePoints.get(routePointNumber);
        return result;
    }

    @Override
    public Integer getRouteSize() {
        return (this.size() + 1);
    }

    @Override
    public Integer getIndexOfRoutePoint(RoutePoint routePoint) {
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        int index = 0;
        for(RoutePoint routePoint1: routePoints){
            if(routePoint1.equals(routePoint)){
                index = routePoints.indexOf(routePoint1);
            }
        }
        return index;
    }

    @Override
    public Collection<RoutePoint> asRoutePointCollection() {
        Collection<RoutePoint> result = new ArrayList<>();
        result.add(this.get(0).getStartTrackCourse());
        for(TrackCourse trackCourse: this){
            result.add(trackCourse.getEndTrackCourse());
        }
        return result;
    }
}
