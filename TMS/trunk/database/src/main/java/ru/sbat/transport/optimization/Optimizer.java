package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Optimizer implements IOptimizer {

    @Override
    public void filtrate(PlannedSchedule plannedSchedule, List<Invoice> unassignedInvoices) throws RouteNotFoundException {

        for(Invoice invoice: unassignedInvoices) {
            if (invoice.getRoute() != null)
               throw new IllegalArgumentException("invoice.getRoute() should be null");

            // сначала нужно для каждого инвойса выбрать подходящие маршруты, такие что начальный и конечный пункт совпадали с соответсвующими пунктами накладных,
            // также маршруты должны проходить по времени, то есть время доставки по указанному маршруту должно быть меньше чем время доставки указанное в заявке
            // день недели в который осуществляется доставка должен совпадать с днями недели доставки маршрута

            Date plannedDeliveryTime = invoice.getRequest().getPlannedDeliveryTime(); //плановое время доставки Dd
            Date creationDate = invoice.getCreationDate(); //время создания заявки
            Point deliveryPoint = invoice.getRequest().getDeliveryPoint(); // пункт доставки
            Point departurePoint = invoice.getAddressOfWarehouse(); // пункт, в котором была сформирована накладная(первый пункт маршрута)
            int dayOfWeek = getWeekDay(plannedDeliveryTime); // день недели, в который будет планово доставлен товар в накладной

            for(Route route: plannedSchedule){
                if(route == null){
                    throw new RouteNotFoundException("route should not be null");
                }
                if (route.getDeparturePoint().equals(departurePoint) &&
                        route.getArrivalPoint().equals(deliveryPoint)) {

                }
                    invoice.setRoute(route);
//                    System.out.println("For invoice: " + invoice.toString() + " found route = " + route.toString());
                }
            }
        }

    /* dayOfWeek <= route.getWeekDayOfActualDeliveryTime() &&
    (creationDate.getTime() + route.getFullTime() < invoice.getRequest().getPlannedDeliveryTime().getTime()*/

    // TODO сделать toString для Invoice and Route +
    // TODO сделать метод который отбирает все подходящие маршруты для накладной(бех оптимизации) +
    // TODO написать тесты для проверки

    @Override
    public Date[] getPossibleDepartureDate(Route route, Invoice invoice){
        Date creationDate = invoice.getCreationDate();
        Date date1 = creationDate;
        int differenceWeekDays = route.getWeekDayOfDepartureTime() - invoice.getWeekDay();
        if (differenceWeekDays < 0) {
            int tmp = 7 - (invoice.getWeekDay() - route.getWeekDayOfDepartureTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(creationDate);
            calendar.add(Calendar.DAY_OF_MONTH, tmp);
            date1.setTime(calendar.getTimeInMillis());
        }else if(differenceWeekDays > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(creationDate);
            calendar.add(Calendar.DAY_OF_MONTH, differenceWeekDays);
            date1.setTime(calendar.getTimeInMillis());
        }else if(differenceWeekDays == 0){
            date1 = creationDate;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date date2 = new Date(calendar.getTimeInMillis());
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.add(Calendar.DAY_OF_MONTH, 7);
        Date date3 = new Date(calendar2.getTimeInMillis());
        Date[] result = new Date[]{date1, date2, date3};
        return result;
    }

    @Override
    public boolean isFittingForDeliveryTime(Route route, Invoice invoice, Date date) {
        boolean result = false;
        Date plannedDeliveryTime = invoice.getRequest().getPlannedDeliveryTime();
        Calendar calendar = Calendar.getInstance();
        date.setHours(route.getActualDeliveryTime().getHours());
        date.setMinutes(route.getActualDeliveryTime().getMinutes());
        calendar.setTime(date);
        int countDays = route.getDaysCountOfRoute();
        calendar.add(Calendar.DAY_OF_MONTH, countDays);
        Date actualDeliveryDate = new Date(calendar.getTimeInMillis());
        if(actualDeliveryDate.before(plannedDeliveryTime)){
            result = true;
        }
        return result;
    }


    @Override
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException, RouteNotFoundException {

    }

    @Override
    public InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices) {
        InvoiceTypes invoiceTypes = null;

        return invoiceTypes;
    }

    /**
     *
     * @param date
     * @return day of week from date
     */
    public int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
