package ru.sbat.transport.optimization.location;


import java.util.LinkedList;

public class DeliveryRoute extends LinkedList<TrackCourse> implements IDeliveryRoute{

    /** calculates count of route points in route
     *
     * @return count of route points in route
     */
    @Override
    public int getCountOfRoutePoint() {
        return (this.size() + 1);
    }
}
