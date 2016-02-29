package ru.sbat.transport.optimization;


import javafx.beans.property.*;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.user.MarketAgent;
import ru.sbat.transport.optimization.utils.InvoiceType;

import java.util.*;

public class Invoice {
    private final ObjectProperty<Request> request = new SimpleObjectProperty<>();
    private final ObjectProperty<Point> addressOfWarehouse = new SimpleObjectProperty<>();
    private final DoubleProperty weight = new SimpleDoubleProperty(); //масса
    private final DoubleProperty volume = new SimpleDoubleProperty();//объем
    private final IntegerProperty countOfBoxes = new SimpleIntegerProperty();//кол-во коробок
    private final ObjectProperty<MarketAgent> marketAgent = new SimpleObjectProperty<>();
    private final IntegerProperty priority = new SimpleIntegerProperty();
    private final ObjectProperty<Route> route = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> creationDate = new SimpleObjectProperty<>();
    private final DoubleProperty cost = new SimpleDoubleProperty();
    private final ObjectProperty<InvoiceType> invoiceType = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> realDepartureDate = new SimpleObjectProperty<>();

    public Invoice() {
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public Date getCreationDate() {
        return creationDate.get();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate.set(creationDate);
    }

    public ObjectProperty<Date> creationDateProperty() {
        return creationDate;
    }

    public Route getRoute() {
        return route.get();
    }

    public void setRoute(Route route) {
        this.route.set(route);
    }

    public ObjectProperty<Route> routeProperty() {
        return route;
    }

    public int getPriority() {
        return priority.get();
    }

    public void setPriority(int priority) {
        this.priority.set(priority);
    }

    public IntegerProperty priorityProperty() {
        return priority;
    }

    public MarketAgent getMarketAgent() {
        return marketAgent.get();
    }

    public void setMarketAgent(MarketAgent marketAgent) {
        this.marketAgent.set(marketAgent);
    }

    public ObjectProperty<MarketAgent> marketAgentProperty() {
        return marketAgent;
    }

    public int getCountOfBoxes() {
        return countOfBoxes.get();
    }

    public void setCountOfBoxes(int countOfBoxes) {
        this.countOfBoxes.set(countOfBoxes);
    }

    public IntegerProperty countOfBoxesProperty() {
        return countOfBoxes;
    }

    public double getVolume() {
        return volume.get();
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public Point getAddressOfWarehouse() {
        return addressOfWarehouse.get();
    }

    public void setAddressOfWarehouse(Point addressOfWarehouse) {
        this.addressOfWarehouse.set(addressOfWarehouse);
    }

    public ObjectProperty<Point> addressOfWarehouseProperty() {
        return addressOfWarehouse;
    }

    public Request getRequest() {
        return request.get();
    }

    public void setRequest(Request request) {
        this.request.set(request);
    }

    public ObjectProperty<Request> requestProperty() {
        return request;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType.get();
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType.set(invoiceType);
    }

    public ObjectProperty<InvoiceType> invoiceTypeProperty() {
        return invoiceType;
    }

    public Date getRealDepartureDate() {
        return realDepartureDate.get();
    }

    public void setRealDepartureDate(Date realDepartureDate) {
        this.realDepartureDate.set(realDepartureDate);
    }

    public ObjectProperty<Date> realDepartureDateProperty() {
        return realDepartureDate;
    }

    /** determines week day of invoice's creation date
     *
     * @return day of week from date
     */
    public int getWeekDay(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(this.getCreationDate());
        return this.getCreationDate().getDay() + 1;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "request=" + request +
                ", addressOfWarehouse=" + addressOfWarehouse +
                ", weight=" + weight +
                ", volume=" + volume +
                ", countOfBoxes=" + countOfBoxes +
                ", marketAgent=" + marketAgent +
                ", priority=" + priority +
                ", route=" + route +
                ", creationDate=" + creationDate +
                ", cost=" + cost +
                ", invoiceType=" + invoiceType +
                ", realDepartureDate=" + realDepartureDate +
                '}';
    }
}
