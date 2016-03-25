package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;

import java.util.*;


public class TrackCourse {
    private LoadCostOfTrackCourse loadCostOfTrackCourse;
    private RoutePoint startTrackCourse;
    private RoutePoint endTrackCourse;
    private Route route;
    private Date departureDate;

    public LoadCostOfTrackCourse getLoadCostOfTrackCourse() {
        return loadCostOfTrackCourse;
    }

    public void setLoadCostOfTrackCourse(LoadCostOfTrackCourse loadCostOfTrackCourse) {
        this.loadCostOfTrackCourse = loadCostOfTrackCourse;
    }

    public RoutePoint getStartTrackCourse() {
        return startTrackCourse;
    }

    public void setStartTrackCourse(RoutePoint startTrackCourse) {
        this.startTrackCourse = startTrackCourse;
    }

    public RoutePoint getEndTrackCourse() {
        return endTrackCourse;
    }

    public void setEndTrackCourse(RoutePoint endTrackCourse) {
        this.endTrackCourse = endTrackCourse;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public List<TrackCourse> sharePointsBetweenRoutes(List<Point> points, List<Route> routes) {
        List<TrackCourse> possibleTrackCourses = new ArrayList<>();
        List<TrackCourse> result = new ArrayList<>();
        for(int i = 0; i < points.size() - 1; i++){
        for(Route route: routes){
            List<TrackCourse> trackCoursesForRoute = route.splitRouteIntoTrackCourse();
            for(TrackCourse trackCourse: trackCoursesForRoute){
                    if(points.get(i).getPointId().equals(trackCourse.getStartTrackCourse().getDeparturePoint().getPointId()) && points.get(i + 1).getPointId().equals(trackCourse.getEndTrackCourse().getDeparturePoint().getPointId())){
                            result.add(trackCourse);
                    }
                }
            }
        }
        for(TrackCourse trackCourse: result){
            for(TrackCourse tmp: result){
                if (trackCourse.getStartTrackCourse().getDeparturePoint().getPointId().equals(tmp.getStartTrackCourse().getDeparturePoint().getPointId())
                        && trackCourse.getEndTrackCourse().getDeparturePoint().getPointId().equals(tmp.getEndTrackCourse().getDeparturePoint().getPointId())) {

                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "TrackCourse{" +
                "loadCost=" + loadCostOfTrackCourse +
                ", startTrackCourse=" + startTrackCourse +
                ", endTrackCourse=" + endTrackCourse +
                ", route=" + route +
                ", departureDate=" + departureDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackCourse that = (TrackCourse) o;

        if (!startTrackCourse.equals(that.startTrackCourse)) return false;
        if (!endTrackCourse.equals(that.endTrackCourse)) return false;
        return route.equals(that.route);

    }

    @Override
    public int hashCode() {
        int result = startTrackCourse.hashCode();
        result = 31 * result + endTrackCourse.hashCode();
        result = 31 * result + route.hashCode();
        return result;
    }
}
