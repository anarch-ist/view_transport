package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.TrackCourse;

import java.util.LinkedList;

public class DeliveryRoute extends LinkedList<TrackCourse> implements IDeliveryRoute{

    @Override
    public int getCountOfRoutePoint() {
        return (this.size() + 1);
    }
}
