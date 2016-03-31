package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.TrackCourse;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;

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
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean containsPoint(Point point) {
        for(TrackCourse trackCourse: this){
            if(trackCourse.getStartTrackCourse().getPoint().equals(point) || trackCourse.getEndTrackCourse().getPoint().equals(point)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLastPoint(Point point) {
        return (this.get(this.size()-1).getEndTrackCourse().getPoint().equals(point));
    }

    @Override
    public String getPointsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        for (RoutePoint routePoint: routePoints) {
            stringBuilder.append(routePoint.getPoint().getPointId()).append(" ");
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
    public Integer getArrivalTime() throws IncorrectRequirement {
        return this.getActualArrivalTimeInRoutePoint(this.get(this.size() - 1).getEndTrackCourse());
    }

    /** determines the final point of the route
     *
     * @return the final point
     */
    @Override
    public Point getArrivalPoint() {
        return this.get(this.size()-1).getEndTrackCourse().getPoint();
    }

    /** determines the first point of the route
     *
     * @return the first point of the route
     */
    @Override
    public Point getDeparturePoint() {
        return this.get(0).getStartTrackCourse().getPoint();
    }

    /** determines day of week of arrival in the point of route
     *
     * @return day of week of point
     */
    @Override
    public int getWeekDayOfActualArrivalDateInRoutePoint(RoutePoint endTC) throws IncorrectRequirement {
        int result = this.getDepartureTimeFromFirstPoint();
        if(this.get(0).getStartTrackCourse().getPoint().equals(endTC.getPoint())){
            throw new IncorrectRequirement("arrival day of week in planned schedule can't be in the first point");
        }
        for(TrackCourse trackCourse: this) {
            if (trackCourse.getEndTrackCourse().getPoint().equals(endTC.getPoint())) {
                int index = this.indexOf(trackCourse);
                if (index == 0) {
                    result += trackCourse.getTravelTime();
                    if (result >= 1440) {
                        result = (result - 1440);
                    }
                }else {
                    for(int i = 0; i <= index; i++){
                        result += this.get(i).getTravelTime() + this.get(i).getEndTrackCourse().getLoadingOperationsTime();
                    }
                    result -= this.get(index).getEndTrackCourse().getLoadingOperationsTime();
                }
            }
        }
        result = (result / (24 * 60)) + this.getDayOfWeek();
        return result;
    }

    /** determines time of arrival in the point of route
     *
     * @return minutes of arrival in the point
     */
    // ARRIVAL TIME
    @Override
    public int getActualArrivalTimeInRoutePoint(RoutePoint endTC) throws IncorrectRequirement {
        int result = this.getDepartureTimeFromFirstPoint();
        if(this.get(0).getStartTrackCourse().getPoint().equals(endTC.getPoint())){
            throw new IncorrectRequirement("arrival time in planned schedule can't be in the first point");
        }
        for(TrackCourse trackCourse: this) {
            if (trackCourse.getEndTrackCourse().getPoint().equals(endTC.getPoint())) {
                int index = this.indexOf(trackCourse);
                if (index == 0) {
                    result += trackCourse.getTravelTime();
                    if (result >= 1440) {
                        result = (result - 1440);
                    }
                }else {
                    for(int i = 0; i <= index; i++){
                        result += this.get(i).getTravelTime() + this.get(i).getEndTrackCourse().getLoadingOperationsTime();
                    }
                    result -= this.get(index).getEndTrackCourse().getLoadingOperationsTime();
                }
            }
        }
        result = result - ((result / (24 * 60)) * 24 * 60);
        return result;
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
    public int getDaysCountOfRoute() throws IncorrectRequirement {
        return (this.getFullTime() / (60 * 24));
    }

    @Override
    public double getStartingOccupancyCost() {
        return this.getCharacteristicsOfCar().getOccupancyCost();
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
            if (this.splitRouteNewIntoRoutePoints().get(this.size() - 1).getPoint().equals(routePoint.getPoint())) {
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

    public List<TrackCourse> splitRouteIntoTrackCourse(IRoute route) {
        List<TrackCourse> result = new ArrayList<>();
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        for(int i = 0; i < (routePoints.size() - 1); i++) {
            TrackCourse trackCourse = new TrackCourse();
            trackCourse.setStartTrackCourse(routePoints.get(i));
            trackCourse.setEndTrackCourse(routePoints.get(i + 1));
            trackCourse.setRoute(route);
            result.add(trackCourse);
        }
        return result;
    }
}
