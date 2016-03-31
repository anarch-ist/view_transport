package ru.sbat.transport.optimization.location;



public class RoutePoint {
    private Point point;
    private Integer loadingOperationsTime;


    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Integer getLoadingOperationsTime() {
        return loadingOperationsTime;
    }

    public void setLoadingOperationsTime(Integer loadingOperationsTime) {
        this.loadingOperationsTime = loadingOperationsTime;
    }

    @Override
    public String toString() {
        return "RoutePoint{" +
                "point=" + point +
                ", loadingOperationsTime=" + loadingOperationsTime +
                '}';
    }
}
