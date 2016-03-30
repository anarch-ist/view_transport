package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.IRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.utils.LoadCost;

import java.util.*;


public class TrackCourse {
    private LoadCost loadCost;
    private RoutePoint startTrackCourse;
    private RoutePoint endTrackCourse;
    private IRoute route;
    private int travelTime;
    private double distanceBetweenRoutePoints;
    private List<LoadUnit> loadUnits = new ArrayList<>();

    public LoadCost getLoadCost() {
        return loadCost;
    }

    public void setLoadCost(LoadCost loadCost) {
        this.loadCost = loadCost;
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

    public IRoute getRoute() {
        return route;
    }

    public void setRoute(IRoute route) {
        this.route = route;
    }

    public int getTravelTime() {
        return this.getStartTrackCourse().getTimeToNextPoint();
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public List<LoadUnit> getLoadUnits() {
        return loadUnits;
    }

    public void setLoadUnits(List<LoadUnit> loadUnits) {
        this.loadUnits = loadUnits;
    }

    public double getDistanceBetweenRoutePoints() {
        return this.getStartTrackCourse().getDistanceToNextPoint();
    }

    public void setDistanceBetweenRoutePoints(double distanceBetweenRoutePoints) {
        this.distanceBetweenRoutePoints = distanceBetweenRoutePoints;
    }

    public List<TrackCourse> sharePointsBetweenRoutes(List<Point> points, List<IRoute> routes) {
        List<TrackCourse> result = new ArrayList<>();
        for(int i = 0; i < points.size() - 1; i++){
        for(IRoute route: routes){
            List<TrackCourse> trackCoursesForRoute = route.splitRouteIntoTrackCourse(route);
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
                "loadCost=" + loadCost +
                ", startTrackCourse=" + startTrackCourse +
                ", endTrackCourse=" + endTrackCourse +
                ", route=" + route +
                ", travelTime=" + travelTime +
                ", loadUnits=" + loadUnits +
                ", distanceBetweenRoutePoints" + distanceBetweenRoutePoints +
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
        result = 31 * result + 1;
        return result;
    }
}
