package ru.sbat.transport.optimization.location;


public abstract class Point {
    private Integer stationTime;

    public Integer getStationTime() {
        return stationTime;
    }

    public void setStationTime(Integer stationTime) {
        this.stationTime = stationTime;
    }
}
