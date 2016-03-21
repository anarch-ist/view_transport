package ru.sbat.transport.optimization.location;


import java.util.LinkedList;

public class DeliveryRoute extends LinkedList<Route> implements IDeliveryRoute{

    @Override
    public int getCountOfRoutePoint() {
        int count = 0;
        for(Route route: this){
            count += route.size();
        }
        return count;
    }
}
