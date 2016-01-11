package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.utils.DayOfWeek;

import java.util.Date;

public class Request {
    private int requestId;
    private int clientId;
    private Point deliveryPoint;
    private Date plannedDeliveryTime;
    private DayOfWeek dayOfWeek;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Point getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(Point deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public Date getPlannedDeliveryTime() {
        return plannedDeliveryTime;
    }

    public void setPlannedDeliveryTime(Date plannedDeliveryTime) {
        this.plannedDeliveryTime = plannedDeliveryTime;
    }
}
