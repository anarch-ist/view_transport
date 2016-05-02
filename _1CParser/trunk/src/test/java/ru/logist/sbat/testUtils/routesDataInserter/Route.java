package ru.logist.sbat.testUtils.routesDataInserter;


import java.sql.Time;

public class Route {
    private Integer routeID;
    private String directionIDExternal;
    private String dataSourceID;
    private String routeName;
    private Time firstPointArrivalTime;
    private String dayOfWeek;
    private Integer tariffID;

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public String getDirectionIDExternal() {
        return directionIDExternal;
    }

    public void setDirectionIDExternal(String directionIDExternal) {
        this.directionIDExternal = directionIDExternal;
    }

    public String getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(String dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Time getFirstPointArrivalTime() {
        return firstPointArrivalTime;
    }

    public void setFirstPointArrivalTime(Time firstPointArrivalTime) {
        this.firstPointArrivalTime = firstPointArrivalTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getTariffID() {
        return tariffID;
    }

    public void setTariffID(Integer tariffID) {
        this.tariffID = tariffID;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeID=" + routeID +
                ", directionIDExternal='" + directionIDExternal + '\'' +
                ", dataSourceID='" + dataSourceID + '\'' +
                ", routeName='" + routeName + '\'' +
                ", firstPointArrivalTime=" + firstPointArrivalTime +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", tariffID=" + tariffID +
                '}';
    }
}
