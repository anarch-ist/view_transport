package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;
import java.text.ParseException;
import java.time.LocalDate;
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
        Date date1;
        int differenceWeekDays = route.getWeekDayOfDepartureTime() - invoice.getWeekDay();
        if (differenceWeekDays <= 0) {
            long tmp = 7 - (invoice.getWeekDay() - route.getWeekDayOfDepartureTime());
            LocalDate localDate = LocalDate.of(creationDate.getYear(), creationDate.getMonth(), creationDate.getDay()).plusDays(tmp);
            date1 = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        }else {
            LocalDate localDate = LocalDate.of(creationDate.getYear(), creationDate.getMonth(), creationDate.getDay()).plusDays(differenceWeekDays);
            date1 = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        }
        Date date2;
        Date date3;
        LocalDate localDate = LocalDate.of(date1.getYear(), date1.getMonth(), date1.getDay()).plusDays(7);
        date2 = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        LocalDate localDate2 = LocalDate.of(date2.getYear(), date2.getMonth(), date2.getDay()).plusDays(7);
        date3 = new Date(localDate2.getYear(), localDate2.getMonthValue(), localDate2.getDayOfMonth());
        Date[] result = new Date[]{date1, date2, date3};
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
