package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.RoutePoint;

public class TrackCourse {
    private double congestionCost;
    private RoutePoint startTrackCourse;
    private RoutePoint endTrackCourse;

    public double getCongestionCost() {
        return congestionCost;
    }

    public void setCongestionCost(double congestionCost) {
        this.congestionCost = congestionCost;
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
}
