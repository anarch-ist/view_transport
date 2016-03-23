package ru.sbat.transport.optimization.location;


public abstract class Point {
    private Integer stationTime;
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

    public Integer getStationTime() {
        return stationTime;
    }

    public void setStationTime(Integer stationTime) {
        this.stationTime = stationTime;
    }
}
