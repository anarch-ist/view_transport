package ru.sbat.transport.optimization.location;


import java.util.Date;
import java.util.LinkedList;

public class Route extends LinkedList<RoutePoint> implements IRoute {

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


    /** determines day of week of arrival in the last point of route
     *
     * @return day of week of last point
     */
    @Override
    public int getWeekDayOfActualDeliveryTime() {
        return this.get(this.size()-1).getDayOfWeek();
    }

    /** determines time of arrival in the last point of route
     *
     * @return date with real hours and minutes of arrival in the last point
     */
    @Override
    public Date getActualDeliveryTime() {
        Date date = new Date();
        int time = this.get(this.size()-2).getDepartureTime() + this.get(this.size()-2).getTimeToNextPoint() + this.get(this.size()-1).getLoadingOperationsTime();
        if(time >= 1440){
            time = (time - 1440);
        }
        int[]hoursAndMinutes = splitToComponentTime(time);
        date.setHours(hoursAndMinutes[0]);
        date.setMinutes(hoursAndMinutes[1]);
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
    public double getStartingCost() {
        return this.get(0).getCharacteristicsOfCar().getCost();
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