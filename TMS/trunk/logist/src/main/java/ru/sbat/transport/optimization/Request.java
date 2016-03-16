package ru.sbat.transport.optimization;


import javafx.beans.property.*;
import ru.sbat.transport.optimization.location.Point;

import java.util.Date;

public class Request {
    private final IntegerProperty requestId = new SimpleIntegerProperty();
    private final IntegerProperty clientId = new SimpleIntegerProperty();
    private final ObjectProperty<Point> deliveryPoint = new SimpleObjectProperty<>();
    private final StringProperty deliveryAddress = new SimpleStringProperty();
    private final ObjectProperty<Date> plannedDeliveryDate = new SimpleObjectProperty<>();
    private final IntegerProperty dayOfWeek = new SimpleIntegerProperty();

    public int getDayOfWeek() {
        return dayOfWeek.get();
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek.set(dayOfWeek);
    }

    public IntegerProperty getDayOfWeekProperty(){
        return dayOfWeek;
    }

    public int getRequestId() {
        return requestId.get();
    }

    public void setRequestId(int requestId) {
        this.requestId.set(requestId);
    }

    public IntegerProperty getRequestIdProperty(){
        return requestId;
    }

    public int getClientId() {
        return clientId.get();
    }

    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    public IntegerProperty getCLientIdProperty(){
        return clientId;
    }

    public Point getDeliveryPoint() {
        return deliveryPoint.get();
    }

    public void setDeliveryPoint(Point deliveryPoint) {
        this.deliveryPoint.set(deliveryPoint);
    }

    public ObjectProperty<Point> getDeliveryPointProperty(){
        return deliveryPoint;
    }

    public Date getPlannedDeliveryDate() {
        return plannedDeliveryDate.get();
    }

    public void setPlannedDeliveryDate(Date plannedDeliveryDate) {
        this.plannedDeliveryDate.set(plannedDeliveryDate);

    }

    public ObjectProperty<Date> getPlannedDeliveryDateProperty(){
        return plannedDeliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress.get();
    }

    public StringProperty deliveryAddressProperty() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress.set(deliveryAddress);
    }
}
