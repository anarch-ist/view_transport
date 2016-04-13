package ru.sbat.transport.optimization.utils;


import ru.sbat.transport.optimization.location.IRoute;
import ru.sbat.transport.optimization.location.Point;

import java.util.ArrayList;
import java.util.List;

public class InformationStack {

    private List<String> pointsHistory = new ArrayList<>();
    private List<String> routesHistory = new ArrayList<>();
    private List<String> markedStringPoints = new ArrayList<>();
    private List<Point> pointsForInvoice = new ArrayList<>();
    private List<IRoute> routesForInvoice = new ArrayList<>();
    private List<Point> markedPoints = new ArrayList<>();
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
        this.markedPoints.clear();
        this.markedPoints.addAll(informationStack.getMarkedPoints());
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

    public List<IRoute> getRoutesForInvoice() {
        return routesForInvoice;
    }

    public List<Point> getMarkedPoints() {
        return markedPoints;
    }

    public List<String> getMarkedStringPoints() {
        return markedStringPoints;
    }

    @Override
    public String toString() {
        return "InformationStack{" +
                "pointsHistory=" + pointsHistory +
                ", routesHistory=" + routesHistory +
                ", markedPoints=" + markedPoints +
                ", deep=" + deep +
                '}';
    }

    public void appendPointsHistory(Point point) {
        pointsHistory.add(point.getPointId());
    }

    public void appendRoutesHistory(IRoute route) {
        routesHistory.add(route.getPointsAsString());
    }

    public void appendMarkedPointsHistory(Point point){
        markedStringPoints.add(point.getPointId());
    }

    public void addPoint(Point point){
        pointsForInvoice.add(point);
    }

    public void addRoute(IRoute route){
        routesForInvoice.add(route);
    }

    public void addMarkedPoint(Point point) {
        markedPoints.add(point);
    }
}
