package ru.sbat.transport.optimization.utils;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;

import java.util.ArrayList;
import java.util.List;

public class InformationStack {

    private List<String> pointsHistory = new ArrayList<>();
    private List<String> routesHistory = new ArrayList<>();
    private List<Point> pointsForInvoice = new ArrayList<>();
    private List<Route> routesForInvoice = new ArrayList<>();
    private int deep = 0;

    public InformationStack() {

    }

    /**
     * full copy constructor
     * @param informationStack
     */
    public InformationStack(InformationStack informationStack) {
        this.setDeep(informationStack.getDeep());
        this.pointsHistory.clear();
        this.pointsHistory.addAll(informationStack.getPointsHistory());
        this.routesHistory.clear();
        this.routesHistory.addAll(informationStack.getRoutesHistory());
        this.pointsForInvoice.clear();
        this.pointsForInvoice.addAll(informationStack.getPointsForInvoice());
        this.routesForInvoice.clear();
        this.routesForInvoice.addAll(informationStack.getRoutesForInvoice());
    }

    public List<String> getPointsHistory() {
        return pointsHistory;
    }

    public List<String> getRoutesHistory() {
        return routesHistory;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public List<Point> getPointsForInvoice() {
        return pointsForInvoice;
    }

    public List<Route> getRoutesForInvoice() {
        return routesForInvoice;
    }

    @Override
    public String toString() {
        return "InformationStack{" +
                "pointsHistory=" + pointsHistory +
                ", routesHistory=" + routesHistory +
                ", deep=" + deep +
                '}';
    }

    public void appendPointsHistory(Point point) {
        pointsHistory.add(point.getPointId());
    }

    public void appendRoutesHistory(Route route) {
        routesHistory.add(route.getPointsAsString());
    }

    public void addPoint(Point point){
        pointsForInvoice.add(point);
    }

    public void addRoute(Route route){
        routesForInvoice.add(route);
    }
}
