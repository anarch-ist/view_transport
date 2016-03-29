package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.UpdatedRoute;
import ru.sbat.transport.optimization.location.RoutePoint;

import java.util.*;


public class TrackCourse {
    private RoutePoint startTrackCourse;
    private RoutePoint endTrackCourse;
    private int travelTime;
    private UpdatedRoute updatedRoute;
    private double distanceToNextPoint;
    private List<LoadUnit> loadUnits = new ArrayList<>();

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

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public UpdatedRoute getUpdatedRoute() {
        return updatedRoute;
    }

    public void setUpdatedRoute(UpdatedRoute updatedRoute) {
        this.updatedRoute = updatedRoute;
    }

    public double getDistanceToNextPoint() {
        return distanceToNextPoint;
    }

    public void setDistanceToNextPoint(double distanceToNextPoint) {
        this.distanceToNextPoint = distanceToNextPoint;
    }

    public List<LoadUnit> getLoadUnits() {
        return loadUnits;
    }

    public void setLoadUnits(List<LoadUnit> loadUnits) {
        this.loadUnits = loadUnits;
    }

    public List<TrackCourse> sharePointsBetweenTC(List<Point> points, List<UpdatedRoute> updatedRoutes){
        List<TrackCourse> result = new ArrayList<>();
//        for(int i = 0; i < points.size() - 1; i++){
//            for(UpdatedRoute route: updatedRoutes){
//                List<TrackCourse> trackCoursesForRoute = route.splitRouteIntoTrackCourse();
//                for(TrackCourse trackCourse: trackCoursesForRoute){
//                    if(points.get(i).getPointId().equals(trackCourse.getStartTrackCourse().getDeparturePoint().getPointId()) && points.get(i + 1).getPointId().equals(trackCourse.getEndTrackCourse().getDeparturePoint().getPointId())){
//                        result.add(trackCourse);
//                    }
//                }
//            }
//        }
//        for(TrackCourse trackCourse: result){
//            for(TrackCourse tmp: result){
//                if (trackCourse.getStartTrackCourse().getDeparturePoint().getPointId().equals(tmp.getStartTrackCourse().getDeparturePoint().getPointId())
//                        && trackCourse.getEndTrackCourse().getDeparturePoint().getPointId().equals(tmp.getEndTrackCourse().getDeparturePoint().getPointId())) {
//
//                }
//            }
//        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackCourse that = (TrackCourse) o;

        if (!startTrackCourse.equals(that.startTrackCourse)) return false;
        if (!endTrackCourse.equals(that.endTrackCourse)) return false;
        return updatedRoute.equals(that.updatedRoute);

    }

    @Override
    public int hashCode() {
        int result = startTrackCourse.hashCode();
        result = 31 * result + endTrackCourse.hashCode();
        result = 31 * result + updatedRoute.hashCode();
        return result;
    }
}
