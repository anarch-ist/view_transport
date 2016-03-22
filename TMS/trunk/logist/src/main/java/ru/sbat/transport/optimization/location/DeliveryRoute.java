package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.TrackCourse;

import java.util.LinkedList;

public class DeliveryRoute extends LinkedList<TrackCourse> implements IDeliveryRoute{

    @Override
    public int getCountOfRoutePoint() {
        int count = 0;
        if(this.size() != 0 ) {
            count = 2;
            if(this.size() > 1){
                for (int i = 1; i < this.size(); i++) {
                    count++;
                }
            }
        }
        return count;
    }
}
