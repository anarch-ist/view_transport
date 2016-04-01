package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.TrackCourse;

public class PartOfDeliveryRoute {
    private TrackCourse trackCourse;
    private int numberOfWeek;

    public TrackCourse getTrackCourse() {
        return trackCourse;
    }

    public void setTrackCourse(TrackCourse trackCourse) {
        this.trackCourse = trackCourse;
    }

    public int getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(int numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }
}
