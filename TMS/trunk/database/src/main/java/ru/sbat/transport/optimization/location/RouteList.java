package ru.sbat.transport.optimization.location;

import ru.sbat.transport.optimization.Invoice;

import java.util.ArrayList;
import java.util.Set;


public class RouteList extends ArrayList{
    private Route route;
    private String nameDriver;
    private String carNumber;


    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getNameDriver() {
        return nameDriver;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }


}
