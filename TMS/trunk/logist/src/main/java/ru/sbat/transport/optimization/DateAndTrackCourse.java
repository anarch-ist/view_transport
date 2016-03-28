package ru.sbat.transport.optimization;


import java.util.Date;

public class DateAndTrackCourse {
    private TrackCourse trackCourse;
    private Date departureDate;

    public DateAndTrackCourse(TrackCourse trackCourse, Date departureDate) {
        this.trackCourse = trackCourse;
        this.departureDate = departureDate;
    }

    public TrackCourse getTrackCourse() {
        return trackCourse;
    }

    public void setTrackCourse(TrackCourse trackCourse) {
        this.trackCourse = trackCourse;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
}
