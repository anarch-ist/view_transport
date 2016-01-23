package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RouteList;
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
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException, RouteNotFoundException {

        for(Invoice invoice: unassignedInvoices) {
            if (invoice.getRoute() != null)
               throw new IllegalArgumentException("invoice.getRoute() should be null");

            // сначала нужно для каждого инвойса выбрать подходящие маршруты, такие что начальный и конечный пункт совпадали с соответсвующими пунктами накладных,
            // также маршруты должны проходить по времени, то есть время доставки по указанному маршруту должно быть меньше чем время доставки указанное в заявке
            // день недели в который осуществляется доставка должен совпадать с днями недели доставки маршрута

            Date plannedDeliveryTime = invoice.getRequest().getPlannedDeliveryTime(); //плановое время доставки Dd
            Date creationDate = invoice.getCreationDate(); //время создания заявки
            Point deliveryPoint = invoice.getRequest().getDeliveryPoint(); // пункт доставки
            Point departurePoint = invoice.getAddressOfWarehouse(); // пункт в котором была сформирована накладная(первый пункт маршрута)
            //double invoiceWeight = invoice.getWeight(); // масса товаров в накладной(не должна быть больше чем грузоподъёмность автомобиля)
            int dayOfWeek = getWeekDay(plannedDeliveryTime); // день недели в который будет планово доставлен товар в накладной


            for(Route route: plannedSchedule){
                if(route == null){
                    throw new RouteNotFoundException("route should not be null");
                }
                if (route.getDeparturePoint().equals(departurePoint) &&
                        route.getArrivalPoint().equals(deliveryPoint) &&
                        dayOfWeek <= route.get(route.size() - 1).getWeekDay() &&
                        (creationDate.getTime() + route.getFullTime() <= invoice.getRequest().getPlannedDeliveryTime().getTime())) {
                    //invoice.setRoute(route);
                    //System.out.println("for invoice" + invoice + "found route = " + route);
                }
            }
        }
    }


    // TODO сделать toString для Invoice and Route
    // TODO сделать метод который отбирает все подходящие маршруты для накладной(бех оптимизации)
    // TODO написать тесты для проверки

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
    private int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
