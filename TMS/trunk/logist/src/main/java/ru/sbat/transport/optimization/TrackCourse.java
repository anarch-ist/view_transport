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
        List<TrackCourse> result = new ArrayList<>();
        List<TrackCourse> correctTrackCourses = new ArrayList<>();
        for(Route route: routes){
            List<TrackCourse> trackCoursesForRoute = route.splitRouteIntoTrackCourse();
            for(TrackCourse trackCourse: trackCoursesForRoute){
                if(points.contains(trackCourse.getStartTrackCourse().getDeparturePoint()) && points.contains(trackCourse.getEndTrackCourse().getDeparturePoint())){
                    correctTrackCourses.add(trackCourse);
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
}
