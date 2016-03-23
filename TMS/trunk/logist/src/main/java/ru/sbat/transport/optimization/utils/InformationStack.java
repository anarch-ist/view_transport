package ru.sbat.transport.optimization.utils;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;

import java.util.ArrayList;
import java.util.List;

public class InformationStack {

    private List<String> pointsHistory = new ArrayList<>();
    private List<String> routesHistory = new ArrayList<>();
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
}
