package ru.sbat.transport.optimization.location;


import org.junit.Test;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.Optimizer;
import ru.sbat.transport.optimization.Request;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.user.WarehouseDispatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OptimizerTest {

    @Test
    public void getWDay(){
        int result = 0;
        Optimizer optimizer = new Optimizer();
        Date date = new Date();
        result = optimizer.getWeekDay(date);
        System.out.println(result);
    }

    @Test
    public void opt() {
        PlannedSchedule plannedSchedule = new PlannedSchedule();
        RoutePoint routePoint11 = new RoutePoint();
        RoutePoint routePoint12 = new RoutePoint();
        RoutePoint routePoint13 = new RoutePoint();
        RoutePoint routePoint21 = new RoutePoint();
        RoutePoint routePoint22 = new RoutePoint();
        RoutePoint routePoint31 = new RoutePoint();
        RoutePoint routePoint32 = new RoutePoint();
        RoutePoint routePoint33 = new RoutePoint();
        RoutePoint routePoint34 = new RoutePoint();
        List<Invoice> invoiceList = new ArrayList<>();
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();
        WarehousePoint warehouse1 = new WarehousePoint();
        routePoint11.setDeparturePoint(warehouse1);
        routePoint21.setDeparturePoint(warehouse1);
        routePoint22.setDeparturePoint(warehouse1);
        invoice1.setAddressOfWarehouse(warehouse1);
        Request request1 = new Request();
        TradeRepresentativePoint tradeRepresentativePoint = new TradeRepresentativePoint();
        routePoint13.setDeparturePoint(tradeRepresentativePoint);
        routePoint31.setDeparturePoint(tradeRepresentativePoint);
        routePoint34.setDeparturePoint(tradeRepresentativePoint);
        request1.setDeliveryPoint(tradeRepresentativePoint);
        invoiceList.add(invoice1);
        invoiceList.add(invoice2);
        Route route1 = new Route();
        route1.add(routePoint11);
        route1.add(routePoint12);
        route1.add(routePoint13);
        Route route2 = new Route();
        route2.add(routePoint21);
        route2.add(routePoint22);
        Route route3 = new Route();
        route3.add(routePoint31);
        route3.add(routePoint32);
        route3.add(routePoint33);
        route3.add(routePoint34);
        plannedSchedule.add(route1);
        plannedSchedule.add(route2);
        plannedSchedule.add(route3);

    }
}
