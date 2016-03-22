package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;

import java.util.Date;

public class TrackCourse {
    private double loadByCost;
    private RoutePoint startTrackCourse;
    private RoutePoint endTrackCourse;
    private Route route;
    private Date departureCargoFlight;

    public double getLoadByCost() {
        return loadByCost;
    }

    public void setLoadByCost(double loadByCost) {
        this.loadByCost = loadByCost;
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

    public Date getDepartureCargoFlight() {
        return departureCargoFlight;
    }

    public void setDepartureCargoFlight(Date departureCargoFlight) {
        this.departureCargoFlight = departureCargoFlight;
    }
}
