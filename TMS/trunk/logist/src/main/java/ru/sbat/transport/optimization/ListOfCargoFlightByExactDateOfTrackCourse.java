//package ru.sbat.transport.optimization;
//
//
//import ru.sbat.transport.optimization.location.Route;
//import ru.sbat.transport.optimization.schedule.PlannedSchedule;
//import ru.sbat.transport.optimization.utils.LoadCost;
//
//import java.util.*;
//
//public class ListOfCargoFlightByExactDateOfTrackCourse extends HashMap<DateAndTrackCourse, LoadCost>{
//
//    /** splits planned schedule's routes into track courses with possible departure dates for the year
//     *
//     * @param plannedSchedule
//     * @return list of track courses with data: start route point, end route point, route, occupancy cost and possible departure date from track course
//     */
//    public ListOfCargoFlightByExactDateOfTrackCourse assignTrackCoursesDepartureDates(PlannedSchedule plannedSchedule, Invoice invoice) {
//        ListOfCargoFlightByExactDateOfTrackCourse result = new ListOfCargoFlightByExactDateOfTrackCourse();
//        List<TrackCourse> trackCourses = splitRoutesIntoTrackCourses(plannedSchedule);
//        for(TrackCourse trackCourse: trackCourses) {
//            List<Date> dates = calculateDepartureDatesForTrackCourseAfterCurrentDateToDeliveryDate(trackCourse, invoice);
//            for(Date date: dates){
//                TrackCourse tmp = new TrackCourse();
//                tmp.setStartTrackCourse(trackCourse.getStartTrackCourse());
//                tmp.setEndTrackCourse(trackCourse.getEndTrackCourse());
//                tmp.setRoute(trackCourse.getRoute());
//                double cost = tmp.getRoute().getCharacteristicsOfCar().getOccupancyCost();
//                LoadCost loadCost = new LoadCost(cost);
//                tmp.setLoadCostOfTrackCourse(trackCourse.getLoadCostOfTrackCourse());
//                DateAndTrackCourse dateAndTrackCourse = new DateAndTrackCourse(tmp, date);
////                System.out.println(date);
////                System.out.println("From " + trackCourse.getStartTrackCourse().getDeparturePoint().getPointId() + " to " + trackCourse.getEndTrackCourse().getDeparturePoint().getPointId() + " date = " + date);
//                result.put(dateAndTrackCourse, loadCost);
//            }
//        }
//        return result;
//    }
//
//    /** splits planned schedule's routes into track courses
//     *
//     * @param plannedSchedule
//     * @return list of track courses with data: start route point, end route point, route and occupancy cost
//     */
//    public List<TrackCourse> splitRoutesIntoTrackCourses (PlannedSchedule plannedSchedule) {
//        List<TrackCourse> result = new ArrayList<>();
//        for(Route route: plannedSchedule) {
//            for(int i = 0; i < (route.size() - 1); i++) {
//                TrackCourse trackCourse = new TrackCourse();
//                trackCourse.setStartTrackCourse(route.get(i));
//                trackCourse.setEndTrackCourse(route.get(i + 1));
//                trackCourse.setRoute(route);
//                trackCourse.setLoadCostOfTrackCourse(new LoadCost(route.getCharacteristicsOfCar().getOccupancyCost()));
//                trackCourse.getLoadCostOfTrackCourse().setLoadCost(route.getCharacteristicsOfCar().getOccupancyCost());
//                result.add(trackCourse);
//            }
//        }
//        return result;
//    }
//
//    /** counts from current date for the year possible departure dates for exact track course
//     *
//     * @param trackCourse
//     * @return list of possible departure dates from track course
//     */
//    public List<Date> calculateDepartureDatesForTrackCourseAfterCurrentDate(TrackCourse trackCourse) {
//        List<Date> result = new ArrayList<>();
//        Date currentDate = new Date();
//        int timeOfCurrentDate = currentDate.getHours() * 60 + currentDate.getMinutes() + + trackCourse.getStartTrackCourse().getLoadingOperationsTime();
//        int[] timeDeparture = trackCourse.getRoute().splitToComponentTime(trackCourse.getStartTrackCourse().getDepartureTime());
//        Date date = new Date();
//        date.setHours(timeDeparture[0]);
//        date.setMinutes(timeDeparture[1]);
//        int differenceWeekDays = trackCourse.getStartTrackCourse().getDayOfWeek() - (currentDate.getDay() + 1);
//        if(differenceWeekDays > 0){
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
//            date.setTime(calendar.getTimeInMillis());
//        }else if(differenceWeekDays < 0 || (differenceWeekDays == 0 && (timeOfCurrentDate > trackCourse.getStartTrackCourse().getDepartureTime()))){
//            int tmp = 7 - ((currentDate.getDay()+1) - trackCourse.getStartTrackCourse().getDayOfWeek());
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, tmp);
//            date.setTime(calendar.getTimeInMillis());
//        }
//        Date nextYearDate = currentDate;
//        nextYearDate.setYear(currentDate.getYear() + 1);
//        while (date.before(nextYearDate)){
//            result.add(date);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, 7);
//            date = new Date(calendar.getTimeInMillis());
//        }
//        return result;
//    }
//
//    public List<Date> calculateDepartureDatesForTrackCourseAfterCurrentDateToDeliveryDate(TrackCourse trackCourse, Invoice invoice) {
//        List<Date> result = new ArrayList<>();
//        Date currentDate = new Date();
//        int timeOfCurrentDate = currentDate.getHours() * 60 + currentDate.getMinutes() + trackCourse.getStartTrackCourse().getLoadingOperationsTime();
//        int[] timeDeparture = trackCourse.getRoute().splitToComponentTime(trackCourse.getStartTrackCourse().getDepartureTime());
//        Date date = new Date();
//        date.setHours(timeDeparture[0]);
//        date.setMinutes(timeDeparture[1]);
//        int differenceWeekDays = trackCourse.getStartTrackCourse().getDayOfWeek() - (currentDate.getDay() + 1);
//        if(differenceWeekDays > 0){
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
//            date.setTime(calendar.getTimeInMillis());
//        }else if(differenceWeekDays < 0 || (differenceWeekDays == 0 && (timeOfCurrentDate > trackCourse.getStartTrackCourse().getDepartureTime()))){
//            int tmp = 7 - ((currentDate.getDay()+1) - trackCourse.getStartTrackCourse().getDayOfWeek());
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, tmp);
//            date.setTime(calendar.getTimeInMillis());
//        }
//        while (date.before(invoice.getRequest().getPlannedDeliveryDate())){
//            result.add(date);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, 7);
//            date = new Date(calendar.getTimeInMillis());
//        }
//        return result;
//    }


//    public void loadCostForTrackCourse(InvoiceContainer invoiceContainer, ListOfCargoFlightByExactDateOfTrackCourse listOfCargoFlightByExactDateOfTrackCourse) {
//        invoiceContainer.stream().filter(invoice -> invoice.getDeliveryRoute() != null).forEach(invoice -> {
//            for (TrackCourse trackCourseFromInvoice : invoice.getDeliveryRoute()) {
//                listOfCargoFlightByExactDateOfTrackCourse.stream().filter(trackCourse -> trackCourseFromInvoice.getStartTrackCourse().equals(trackCourse.getStartTrackCourse()) &&
//                        trackCourseFromInvoice.getEndTrackCourse().equals(trackCourse.getEndTrackCourse()) &&
//                        trackCourseFromInvoice.getRoute().equals(trackCourse.getRoute()) &&
//                        trackCourseFromInvoice.getDepartureDate().equals(trackCourse.getDepartureDate())).forEach(trackCourse -> {
//                    trackCourse.setLoadCostOfTrackCourse(trackCourse.getLoadCostOfTrackCourse().getCurrentLoadCost(invoice, trackCourse.getLoadCostOfTrackCourse()));
//                });
//            }
//        });
//    }
//}