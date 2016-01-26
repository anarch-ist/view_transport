package ru.sbat.transport.optimization.location;


import java.util.Date;
import java.util.LinkedList;

public class Route extends LinkedList<RoutePoint> implements IRoute {

    @Override
    public Double getFullDistance() {
        double result = 0;
        for(RoutePoint routePoint : this) {
            result += routePoint.getDistanceToNextPoint();
        }
        return result;
    }

    @Override
    public Integer getFullTime() {
        int result = 0;
        for(RoutePoint routePoint : this){
            result += routePoint.getTimeToNextPoint() + routePoint.getLoadingOperationsTime();
        }
        return result;
    }

    @Override
    public Integer getDepartureTime() {
        return this.get(0).getDepartureTime();
    }

    @Override
    public Integer getArrivalTime() {
        int result = 0;
        result = this.get(this.size()-2).getDepartureTime() + this.get(this.size()-2).getTimeToNextPoint();
        if(result > 24){
            result = result - 24;
        }
        return result;
    }

    @Override
    public Point getArrivalPoint() {
        return this.get(this.size()-1).getDeparturePoint();
    }

    @Override
    public Point getDeparturePoint() {
        return this.get(0).getDeparturePoint();
    }

    @Override
    public int getWeekDayOfActualDeliveryTime() {
        return this.get(this.size()-1).getDayOfWeek();
    }

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

    @Override
    public int[] splitToComponentTime(Integer time) {
        int hours = time / 60;
        int minutes = (int) time - hours * 60;
        int[] result = new int[]{hours, minutes};
        return result;
    }

    @Override
    public int getWeekDayOfDepartureTime() {
        return this.get(0).getDayOfWeek();
    }

    @Override
    public int getDaysCountOfRoute() {
        int result = this.get(this.size()-1).getDayOfWeek() - this.get(0).getDayOfWeek();
        if(result < 0){
            result = 7 - (this.get(0).getDayOfWeek() - this.get(this.size()-1).getDayOfWeek());
        }
        return result;
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
