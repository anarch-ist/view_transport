package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.user.MarketAgent;

import java.util.Calendar;
import java.util.Date;

public class Invoice {
    private Request request;
    private Point addressOfWarehouse;
    private double weight; //масса
    private double volume;//объем
    private int countOfBoxes;//кол-во коробок
    private MarketAgent marketAgent;
    private int priority;
    private Route route;
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public MarketAgent getMarketAgent() {
        return marketAgent;
    }

    public void setMarketAgent(MarketAgent marketAgent) {
        this.marketAgent = marketAgent;
    }

    public int getCountOfBoxes() {
        return countOfBoxes;
    }

    public void setCountOfBoxes(int countOfBoxes) {
        this.countOfBoxes = countOfBoxes;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Point getAddressOfWarehouse() {
        return addressOfWarehouse;
    }

    public void setAddressOfWarehouse(Point addressOfWarehouse) {
        this.addressOfWarehouse = addressOfWarehouse;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getWeekDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getCreationDate());
        return calendar.get(Calendar.DAY_OF_WEEK);
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
                '}';
    }
}
