package ru.sbat.transport.optimization.utils;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.UpdatedRoute;

import java.util.ArrayList;
import java.util.List;

public class InformationStack {

    private List<String> pointsHistory = new ArrayList<>();
    private List<String> updatedRoutesHistory = new ArrayList<>();
    private List<Point> pointsForInvoice = new ArrayList<>();
    private List<UpdatedRoute> updatedRoutesForInvoice = new ArrayList<>();
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
        this.updatedRoutesHistory.clear();
        this.updatedRoutesHistory.addAll(informationStack.getUpdatedRoutesHistory());
        this.pointsForInvoice.clear();
        this.pointsForInvoice.addAll(informationStack.getPointsForInvoice());
        this.updatedRoutesForInvoice.clear();
        this.updatedRoutesForInvoice.addAll(informationStack.getUpdatedRoutesForInvoice());
    }

    public List<String> getPointsHistory() {
        return pointsHistory;
    }

    public List<String> getUpdatedRoutesHistory() {
        return updatedRoutesHistory;
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

    public List<UpdatedRoute> getUpdatedRoutesForInvoice() {
        return updatedRoutesForInvoice;
    }

    @Override
    public String toString() {
        return "InformationStack{" +
                "pointsHistory=" + pointsHistory +
                ", updatedRoutesHistory=" + updatedRoutesHistory +
                ", deep=" + deep +
                '}';
    }

    public void appendPointsHistory(Point point) {
        pointsHistory.add(point.getPointId());
    }

    public void appendUpdatedRoutesHistory(UpdatedRoute updatedRoute) {
        updatedRoutesHistory.add(updatedRoute.getPointsAsString());
    }

    public void addPoint(Point point){
        pointsForInvoice.add(point);
    }

    public void addUpdatedRoute(UpdatedRoute updatedRoute){
        updatedRoutesForInvoice.add(updatedRoute);
    }
}
