package ru.sbat.transport.optimization.location;

import ru.sbat.transport.optimization.TrackCourse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class UpdatedRoute extends LinkedList<TrackCourse> implements IRoute{
    private CharacteristicsOfCar characteristicsOfCar;
    private int departureTime;
    private int dayOfWeek;

    public CharacteristicsOfCar getCharacteristicsOfCar() {
        return characteristicsOfCar;
    }

    public void setCharacteristicsOfCar(CharacteristicsOfCar characteristicsOfCar) {
        this.characteristicsOfCar = characteristicsOfCar;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDepartureTime() {
        return departureTime;
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
        for (TrackCourse trackCourse: this) {
            stringBuilder.append(trackCourse.getStartTrackCourse().getPoint().getPointId()).append(" ");
        }
        stringBuilder.append(this.get(this.size() - 1).getEndTrackCourse().getPoint().getPointId());
        return stringBuilder.toString();
    }

    /** calculates the total distance on the route
     *
     * @return km route
     */
    @Override
    public Double getFullDistance() {
        double result = 0;
        for(TrackCourse trackCourse: this) {
            result += trackCourse.getDistanceToNextPoint();
        }
        return result;
    }

    /** calculates the total time on the route
     *
     * @return minutes of all route
     */
    @Override
    public Integer getFullTime() {
        int result = this.get(0).getStartTrackCourse().getLoadingOperationsTime();
        for(TrackCourse trackCourse: this){
            result += trackCourse.getTravelTime() + trackCourse.getEndTrackCourse().getLoadingOperationsTime();
        }
        return result;
    }

    /** determines the arrival time to the final point of the route
     *
     * @return minutes from the start of the day
     */
    @Override
    public Integer getArrivalTime() {
        int result = 0;
        result = this.getDepartureTime() + this.getFullTime();
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

    @Override
    public List<Point> makePointsInRoute() {
        List<Point> result = new ArrayList<>();
        result.add(this.get(0).getStartTrackCourse().getPoint());
        for(int i = 0; i < this.size(); i++){
            result.add(this.get(i).getEndTrackCourse().getPoint());
        }
        return result;
    }

//    public boolean isCorrectRoutePoint(RoutePoint routePoint) {
//        boolean result = true;
//        if(this.size() >= 1) {
//            if (this.get(this.size() - 1).getDeparturePoint().equals(routePoint.getDeparturePoint())) {
//                return false;
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public boolean add(RoutePoint routePoint) {
//        if (isCorrectRoutePoint(routePoint))
//            return super.add(routePoint);
//        else
//            return false;
//    }
}
