package ru.sbat.transport.optimization.location;


public abstract class Point {
    private String pointId;

    public Point(String pointId) {
        this.pointId = pointId;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }
}
