package ru.sbat.transport.optimization.location;


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

    /** determines if route contains point
     *
     * @param point
     * @return true if contains
     */
    @Override
    public boolean containsPoint(Point point) {
        for(TrackCourse trackCourse: this){
            if(trackCourse.getStartTrackCourse().getPoint().equals(point) || trackCourse.getEndTrackCourse().getPoint().equals(point)){
                return true;
            }
        }
        return false;
    }

    /** determines if point is last in route
     *
     * @param point
     * @return true if point is last
     */
    @Override
    public boolean isLastPoint(Point point) {
        return (this.get(this.size()-1).getEndTrackCourse().getPoint().equals(point));
    }

    /** returns points id in route
     *
     * @return string expression of points id
     */
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

    /** determines the arrival day of week to the final point of the route
     *
     * @return day of week, sunday = 1
     */
    @Override
    public Integer getArrivalDayOfWeek() throws IncorrectRequirement {
        return this.getWeekDayOfActualArrivalDateInRoutePoint(this.get(this.size() - 1).getEndTrackCourse());
    }

    /** determines the final point of the route
     *
     * @return the final point of the route
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


    /** determines day of week of departure from the point of route
     *
     * @return day of week when car leaves route point
     */
    @Override
    public int getWeekDayOfActualDepartureDateInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement {
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        int result = 0;
        if(routePoints.indexOf(routePoint) == 0){
            result = this.getDayOfWeek();
        }if(routePoints.indexOf(routePoint) == (routePoints.size() - 1)){
            throw new IncorrectRequirement("Cannot departure from the last route point");
        }else {
            result = calculateArrivalTimeInRoutePoint(routePoint);
            result = (result / (24 * 60)) + this.getDayOfWeek();
            if(result > 7){
                result = result - 7;
            }
        }
        return result;
    }

    /** determines departure time from the point of route
     *
     * @return minutes of arrival in the point with loading operation time in route point
     */
    public int getActualDepartureTimeFromRoutePoint(RoutePoint routePoint) throws IncorrectRequirement {
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        int result = 0;
        if(routePoints.indexOf(routePoint) == 0){
            result = this.getDepartureTimeFromFirstPoint();
        }if(routePoints.indexOf(routePoint) == (routePoints.size() - 1)){
            throw new IncorrectRequirement("Cannot departure from the last route point");
        } else {
            result = calculateArrivalTimeInRoutePoint(routePoint);
            result = result - ((result / (24 * 60)) * 24 * 60);
            result = result + routePoint.getLoadingOperationsTime();
        }
        return result;
    }


    /** determines day of week of arrival in the point of route
     *
     * @return day of week when car arrives in route point without loading operation time in route point
     */
    @Override
    public int getWeekDayOfActualArrivalDateInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement {
        int result = calculateArrivalTimeInRoutePoint(routePoint);
        result = (result / (24 * 60)) + this.getDayOfWeek();
        if(result > 7){
            result = result - 7;
        }
        return result;
    }

    /** determines time of arrival in the point of route
     *
     * @return minutes of arrival in the point without loading operation time in route point
     */
    @Override
    public int getActualArrivalTimeInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement {
        int result = calculateArrivalTimeInRoutePoint(routePoint);
        result = result - ((result / (24 * 60)) * 24 * 60);
        return result;
    }

    /** determines arrival minutes in route point from departure date
     *
     * @param routePoint
     * @return travel minutes to route point
     * @throws IncorrectRequirement
     */
    public int calculateArrivalTimeInRoutePoint(RoutePoint routePoint) throws IncorrectRequirement {
        int result = this.getDepartureTimeFromFirstPoint();
        if(this.get(0).getStartTrackCourse().getPoint().equals(routePoint.getPoint())){
//            throw new IncorrectRequirement("arrival time in planned schedule can't be in the first point");
                result = this.getDepartureTimeFromFirstPoint();
        }
        for(TrackCourse trackCourse: this) {
            if (trackCourse.getEndTrackCourse().getPoint().equals(routePoint.getPoint())) {
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
//                    result -= this.get(index).getEndTrackCourse().getLoadingOperationsTime();
                }
            }
        }
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
     * @return start day of week
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

    /** returns occupancy cost of the car on route
     *
     * @return occupancy cost of car in money
     */
    @Override
    public double getStartingOccupancyCost() {
        return this.getCharacteristicsOfCar().getOccupancyCost();
    }

    /** splits route new in route points
     *
     * @return list of route points
     */
    public List<RoutePoint> splitRouteNewIntoRoutePoints(){
        List<RoutePoint> result = new ArrayList<>();
        result.add(this.get(0).getStartTrackCourse());
        for(TrackCourse trackCourse: this){
            result.add(trackCourse.getEndTrackCourse());
        }
        return result;
    }

    /** check out if points in route not duplicated
     *
     * @param routePoint
     * @return true if there is no duplicates
     */
    public boolean isCorrectRoutePoint(RoutePoint routePoint) {
        boolean result = true;
        if(this.size() >= 1) {
            if (this.splitRouteNewIntoRoutePoints().get(this.size() - 1).getPoint().equals(routePoint.getPoint())) {
                return false;
            }
        }
        return result;
    }

    /** add route points in route new and creates track courses
     *
     * @param routePoint
     * @return true if track course was added in route new and route point is correct
     */
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

    /** get route point in route new by index
     *
     * @param routePointNumber
     * @return index of route point
     */
    @Override
    public RoutePoint getRoutePoint(Integer routePointNumber) {
        List<RoutePoint> routePoints = this.splitRouteNewIntoRoutePoints();
        RoutePoint result = routePoints.get(routePointNumber);
        return result;
    }

    /** returns count of route point in route new
     *
     * @return count of real size of route new by track courses plus 1
     */
    @Override
    public Integer getRouteSize() {
        return (this.size() + 1);
    }

    /** get index of route point in route new by route point
     *
     * @param routePoint
     * @return index of route point
     */
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

    /** splits iroute in route points
     *
     * @return list of route points
     */
    @Override
    public Collection<RoutePoint> asRoutePointCollection() {
        Collection<RoutePoint> result = new ArrayList<>();
        result.add(this.get(0).getStartTrackCourse());
        for(TrackCourse trackCourse: this){
            result.add(trackCourse.getEndTrackCourse());
        }
        return result;
    }

    /** split route new into track courses
     *
     * @param route
     * @return list of track courses
     */
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

    @Override
    public String toString() {
        return "RouteNew{" +
                "characteristicsOfCar=" + characteristicsOfCar +
                ", departureTimeFromFirstPoint=" + departureTimeFromFirstPoint +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }
}
