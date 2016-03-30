package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.utils.LoadCost;
import ru.sbat.transport.optimization.TrackCourse;

import java.util.*;

public class Route extends LinkedList<RoutePoint> implements IRoute {

    @Override
    public boolean containsPoint(Point point) {
        for(RoutePoint routePoint: this){
            if(routePoint.getDeparturePoint().equals(point)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLastPoint(Point point) {
        return (this.get(this.size()-1).getDeparturePoint().equals(point));
    }

    /** calculates the total distance on the route
     *
     * @return km route
     */
    @Override
    public Double getFullDistance() {
        double result = 0;
        for(RoutePoint routePoint : this) {
            result += routePoint.getDistanceToNextPoint();
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
        for(RoutePoint routePoint : this){
            result += routePoint.getTimeToNextPoint() + routePoint.getLoadingOperationsTime();
        }
        return result;
    }

    /** determines the time of departure
     *
     * @return minutes from the start of the day
     */
    @Override
    public Integer getDepartureTime() {
        return this.get(0).getDepartureTime();
    }

    /** determines the arrival time to the final point of the route
     *
     * @return minutes from the start of the day
     */
    @Override
    public Integer getArrivalTime() {
        int result = 0;
        result = this.get(this.size()-2).getDepartureTime() + this.get(this.size()-2).getTimeToNextPoint() + this.get(this.size()-1).getLoadingOperationsTime();
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
        return this.get(this.size()-1).getDeparturePoint();
    }

    /** determines the first point of the route
     *
     * @return the first point of the route
     */
    @Override
    public Point getDeparturePoint() {
        return this.get(0).getDeparturePoint();
    }


    /** determines day of week of arrival in the point of route
     *
     * @return day of week of point
     */
    @Override
    public int getWeekDayOfActualDeliveryDateInRoutePoint(RoutePoint routePoint) {
        int result = 0;
        for(RoutePoint routePointFromRoute: this){
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
        for(RoutePoint routePointFromRoute: this) {
            if (routePointFromRoute.equals(routePoint)) {
                if (indexOf(routePointFromRoute) != 0) {
                    int index = this.indexOf(routePointFromRoute);
                    int time = this.get(index - 1).getDepartureTime() + this.get(index - 1).getTimeToNextPoint() + this.get(index).getLoadingOperationsTime();
                    if (time >= 1440) {
                        time = (time - 1440);
                    }
                    int[] hoursAndMinutes = splitToComponentTime(time);
                    date.setHours(hoursAndMinutes[0]);
                    date.setMinutes(hoursAndMinutes[1]);
                }else if(indexOf(routePointFromRoute) == 0){
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
        return this.get(0).getDayOfWeek();
    }

    /** calculates count of days in route
     *
     * @return count of days
     */
    @Override
    public int getDaysCountOfRoute() {
        int result = this.get(this.size()-1).getDayOfWeek() - this.get(0).getDayOfWeek();
        if(result < 0){
            result = 7 - (this.get(0).getDayOfWeek() - this.get(this.size()-1).getDayOfWeek());
        }
        return result;
    }

    @Override
    public double getStartingOccupancyCost() {
        return this.get(0).getCharacteristicsOfCar().getOccupancyCost();
    }


    @Override
    public String getPointsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RoutePoint routePoint: this) {
            stringBuilder.append(routePoint.getDeparturePoint().getPointId()).append(" ");
        }
        return stringBuilder.toString();
    }

    public List<TrackCourse> splitRouteIntoTrackCourse(IRoute route) {
        List<TrackCourse> result = new ArrayList<>();
        for(int i = 0; i < (this.size() - 1); i++) {
            TrackCourse trackCourse = new TrackCourse();
            trackCourse.setStartTrackCourse(this.get(i));
            trackCourse.setEndTrackCourse(this.get(i + 1));
            trackCourse.setRoute(this);
            trackCourse.setLoadCost(new LoadCost(this.getStartingOccupancyCost()));
            result.add(trackCourse);
        }
        return result;
    }

    @Override
    public boolean isCorrectRoutePoint(RoutePoint routePoint) {
        boolean result = true;
        if(this.size() >= 1) {
            if (this.get(this.size() - 1).getDeparturePoint().equals(routePoint.getDeparturePoint())) {
                return false;
            }
        }
        return result;
    }

    @Override
    public boolean addRoutePoint(RoutePoint routePoint) {
        return isCorrectRoutePoint(routePoint) && super.add(routePoint);
    }

    @Override
    public RoutePoint getRoutePoint(Integer routePointNumber) {
        return get(routePointNumber);
    }

    @Override
    public Integer getRouteSize() {
        return this.size();
    }

    @Override
    public Integer getIndexOfRoutePoint(RoutePoint routePoint) {
        return this.indexOf(routePoint);
    }

    @Override
    public Collection<RoutePoint> asRoutePointCollection() {
        return this;
    }

    @Override
    public String toString(){
        return "Route{" +
                "fullDistance=" + getFullDistance() + " км." +
                ", fullTime=" + getFullTime() + " ч." +
                ", departureTime=" + getDepartureTime() + " ч." +
                ", arrivalTime=" + getArrivalTime() + " ч." +
                ", arrivalPoint=" + getArrivalPoint() +
                ", departurePoint=" + getDeparturePoint() +
                '}';
    }
}
