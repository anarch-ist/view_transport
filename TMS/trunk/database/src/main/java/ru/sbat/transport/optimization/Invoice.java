package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.user.MarketAgent;

import java.util.Date;

public class Invoice {
    private Request request;
    private Point addressOfWarehouse;
    private double weight;
    private double amount;
    private int countOfBoxes;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
}
