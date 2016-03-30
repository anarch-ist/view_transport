package ru.sbat.transport.optimization;


import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.*;

public class ListOfCargoFlightByExactDateOfTrackCourse extends ArrayList<TrackCourse>{

    /** splits planned schedule's routes into track courses with possible departure dates for the year
     *
     * @param trackCourses
     * @return list of track courses with data: start route point, end route point, route, occupancy cost and possible departure date from track course
     */
    public ListOfCargoFlightByExactDateOfTrackCourse assignTrackCoursesDepartureDates(List<TrackCourse> trackCourses) {
        ListOfCargoFlightByExactDateOfTrackCourse result = new ListOfCargoFlightByExactDateOfTrackCourse();
        for(TrackCourse trackCourse: trackCourses) {
            List<Date> dates = calculateDepartureDatesForTrackCourseAfterCurrentDate(trackCourse);
            for(Date date: dates){
                TrackCourse tmp = new TrackCourse();
                tmp.setLoadCostOfTrackCourse(trackCourse.getLoadCostOfTrackCourse());
                tmp.setStartTrackCourse(trackCourse.getStartTrackCourse());
                tmp.setEndTrackCourse(trackCourse.getEndTrackCourse());
                tmp.setRoute(trackCourse.getRoute());
                tmp.setDepartureDate(date);
                result.add(tmp);
            }
        }
        return result;
    }

    /** splits planned schedule's routes into track courses
     *
     * @param plannedSchedule
     * @return list of track courses with data: start route point, end route point, route and occupancy cost
     */
    public List<TrackCourse> splitRoutesIntoTrackCourses (PlannedSchedule plannedSchedule) {
        List<TrackCourse> result = new ArrayList<>();
        for(Route route: plannedSchedule) {
            for(int i = 0; i < (route.size() - 1); i++) {
                TrackCourse trackCourse = new TrackCourse();
                trackCourse.setStartTrackCourse(route.get(i));
                trackCourse.setEndTrackCourse(route.get(i + 1));
                trackCourse.setRoute(route);
                trackCourse.setLoadCostOfTrackCourse(new LoadCostOfTrackCourse(route.getStartingOccupancyCost()));
                trackCourse.getLoadCostOfTrackCourse().setLoadCost(route.getStartingOccupancyCost());
                result.add(trackCourse);
            }
        }
        return result;
    }

    /** counts from current date for the year possible departure dates for exact track course
     *
     * @param trackCourse
     * @return list of possible departure dates from track course
     */
    public List<Date> calculateDepartureDatesForTrackCourseAfterCurrentDate(TrackCourse trackCourse) {
        List<Date> result = new ArrayList<>();
        Date currentDate = new Date();
        int timeOfCurrentDate = currentDate.getHours() * 60 + currentDate.getMinutes();
        int[] timeDeparture = trackCourse.getRoute().splitToComponentTime(trackCourse.getStartTrackCourse().getDepartureTime());
        Date date = new Date();
        date.setHours(timeDeparture[0]);
        date.setMinutes(timeDeparture[1]);
        int differenceWeekDays = trackCourse.getStartTrackCourse().getDayOfWeek() - (currentDate.getDay() + 1);
        if(differenceWeekDays > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
            date.setTime(calendar.getTimeInMillis());
        }else if(differenceWeekDays < 0 || (differenceWeekDays == 0 && (timeOfCurrentDate > trackCourse.getStartTrackCourse().getDepartureTime()))){
            int tmp = 7 - ((currentDate.getDay()+1) - trackCourse.getStartTrackCourse().getDayOfWeek());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, tmp);
            date.setTime(calendar.getTimeInMillis());
        }
        Date nextYearDate = currentDate;
        nextYearDate.setYear(currentDate.getYear() + 1);
        while (date.before(nextYearDate)){
            result.add(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            date = new Date(calendar.getTimeInMillis());
        }
        return result;
    }

    /** load track courses invoices that have delivery routes
     *
     * @param invoiceContainer
     * @param listOfCargoFlightByExactDateOfTrackCourse
     */
    public void loadCostForTrackCourse(InvoiceContainer invoiceContainer, ListOfCargoFlightByExactDateOfTrackCourse listOfCargoFlightByExactDateOfTrackCourse) {
        invoiceContainer.stream().filter(invoice -> invoice.getDeliveryRoute() != null).forEach(invoice -> {
            for (TrackCourse trackCourseFromInvoice : invoice.getDeliveryRoute()) {
                listOfCargoFlightByExactDateOfTrackCourse.stream().filter(trackCourse -> trackCourseFromInvoice.getStartTrackCourse().equals(trackCourse.getStartTrackCourse()) &&
                        trackCourseFromInvoice.getEndTrackCourse().equals(trackCourse.getEndTrackCourse()) &&
                        trackCourseFromInvoice.getRoute().equals(trackCourse.getRoute()) &&
                        trackCourseFromInvoice.getDepartureDate().equals(trackCourse.getDepartureDate())).forEach(trackCourse -> {
                    trackCourse.setLoadCostOfTrackCourse(trackCourse.getLoadCostOfTrackCourse().getAvailableRestLoadCost(invoice, trackCourse.getLoadCostOfTrackCourse()));
                });
            }
        });
    }
}