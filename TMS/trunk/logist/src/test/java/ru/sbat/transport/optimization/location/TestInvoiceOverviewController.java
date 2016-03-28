package ru.sbat.transport.optimization.location;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.InvoiceContainer;
import ru.sbat.transport.optimization.InvoiceOverviewController;
import ru.sbat.transport.optimization.Request;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.user.MarketAgent;

import java.util.Collections;
import java.util.Date;

public class TestInvoiceOverviewController {
//    static InvoiceContainer invoiceContainer = new InvoiceContainer();
//    static PlannedSchedule plannedSchedule = new PlannedSchedule();
//    static TradeRepresentativePoint tradeRepresentativePoint0 = new TradeRepresentativePoint();
//    static WarehousePoint warehousePoint0 = new WarehousePoint();
//    static MarketAgent marketAgent = new MarketAgent();
//    static Route route = new Route();
//    static WarehousePoint warehousePoint1 = new WarehousePoint();
//    static TradeRepresentativePoint tradeRepresentativePoint1 = new TradeRepresentativePoint();
//    static TradeRepresentativePoint tradeRepresentativePoint2 = new TradeRepresentativePoint();

    //---------------СЛУЖЕБНЫЕ МЕТОДЫ----------------------
    public Invoice createInvoice(Request request, Point addressOfWarehouse, double weight, double volume, int countOfBoxes, MarketAgent marketAgent, int priority, DeliveryRoute deliveryRoute, Date creationDate, double cost){
        Invoice result = new Invoice();
        result.setRequest(request);
        result.setAddressOfWarehouse(addressOfWarehouse);
        result.setWeight(weight);
        result.setVolume(volume);
        result.setCountOfBoxes(countOfBoxes);
        result.setMarketAgent(marketAgent);
        result.setPriority(priority);
        result.setDeliveryRoute(deliveryRoute);
        result.setCreationDate(creationDate);
        result.setCost(cost);
        return result;
    }

    public Request createRequest(int requestId, int clientId, Point deliveryPoint, Date plannedDeliveryTime, int dayOfWeek){
        Request result = new Request();
        result.setRequestId(requestId);
        result.setClientId(clientId);
        result.setDeliveryPoint(deliveryPoint);
        result.setPlannedDeliveryDate(plannedDeliveryTime);
        result.setDayOfWeek(dayOfWeek);
        return result;
    }

    public static DeliveryRoute initDeliveryRoute(Route... route) {
        DeliveryRoute result = new DeliveryRoute();
//        Collections.addAll(result, route);
        return result;
    }

    public static void initRoute(Route route, CharacteristicsOfCar characteristicsOfCar, RoutePoint... routePoints) {
        route.setCharacteristicsOfCar(characteristicsOfCar);
        Collections.addAll(route, routePoints);
    }

    public static RoutePoint createRoutePoint(Point departurePoint, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, double distanceToNextPoint) {
        RoutePoint result = new RoutePoint();
        result.setDepartureTime(departureTime);
        result.setDayOfWeek(dayOfWeek);
        result.setTimeToNextPoint(timeToNextPoint);
        result.setDistanceToNextPoint(distanceToNextPoint);
        result.setDeparturePoint(departurePoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        return result;
    }

    public static CharacteristicsOfCar createCharacteristicsOfCar(double capacityCar, double volumeCar, double cost) {
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setCapacityCar(capacityCar);
        result.setVolumeCar(volumeCar);
        result.setCostOfCar(cost);
        return result;
    }
    //-------------END СЛУЖЕБНЫЕ МЕТОДЫ---------------------
//    @BeforeClass
//    public static void createPlannedSchedule() {
//        initRoute(
//                route,
//                // данные о машине(грузоподъемность в т., объем, стоимость), пункт отправления, день недели, время отправления в минутах от начала суток, время до следующего пункта, время ПРР, расстояние до следующего пункта
//                createRoutePoint(createCharacteristicsOfCar(10, 15, 13054.5), warehousePoint1,           2, 930, 600,   0, 120),
//                createRoutePoint(createCharacteristicsOfCar(15, 25,   17894), tradeRepresentativePoint1, 3, 180, 960,  90,  50),
//                createRoutePoint(createCharacteristicsOfCar(20, 30,   25040), tradeRepresentativePoint2, 3,   0,   0, 120, 300)
//        );
//        plannedSchedule.add(route);
//    }
//
//    @Test
//    public void testInvoiceOverViewController(){
//        Request request = createRequest(12, 4, tradeRepresentativePoint0, new Date(2016, 1, 27, 12, 30), 7);
//        Invoice invoice = createInvoice(request, warehousePoint0, 15.5, 17.7, 17, marketAgent, 1, initDeliveryRoute(route), new Date(2016, 1, 9, 15, 40), 14546);
//        InvoiceOverviewController invoiceOverviewController = new InvoiceOverviewController(invoiceContainer, plannedSchedule);
//        invoiceContainer.add(invoice);
//        invoice.setVolume(15);
//    }
}
