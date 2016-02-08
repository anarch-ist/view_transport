package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.Request;
import ru.sbat.transport.optimization.user.MarketAgent;

import java.util.Date;

public class TestInvoiceOverviewController {
    static TradeRepresentativePoint tradeRepresentativePoint0 = new TradeRepresentativePoint();
    static WarehousePoint warehousePoint0 = new WarehousePoint();

    public Invoice createInvoice(Request request, Point addressOfWarehouse, double weight, double volume, int countOfBoxes, MarketAgent marketAgent, int priority, Route route, Date creationDate, double cost){
        Invoice result = new Invoice();
        result.setRequest(request);
        result.setAddressOfWarehouse(addressOfWarehouse);
        result.setWeight(weight);
        result.setVolume(volume);
        result.setCountOfBoxes(countOfBoxes);
        result.setMarketAgent(marketAgent);
        result.setPriority(priority);
        result.setRoute(route);
        result.setCreationDate(creationDate);
        result.setCost(cost);
        return result;
    }

    public Request createRequest(int requestId, int clientId, Point deliveryPoint, Date plannedDeliveryTime, int dayOfWeek){
        Request result = new Request();
        result.setRequestId(requestId);
        result.setClientId(clientId);
        result.setDeliveryPoint(deliveryPoint);
        result.setPlannedDeliveryTime(plannedDeliveryTime);
        result.setDayOfWeek(dayOfWeek);
        return result;
    }

    /*


    private final ObjectProperty<Request> request = new SimpleObjectProperty<>();
    private final ObjectProperty<Point> addressOfWarehouse = new SimpleObjectProperty<>();
    private final DoubleProperty weight = new SimpleDoubleProperty(); //масса
    private final DoubleProperty volume = new SimpleDoubleProperty();//объем
    private final IntegerProperty countOfBoxes = new SimpleIntegerProperty();//кол-во коробок
    private final ObjectProperty<MarketAgent> marketAgent = new SimpleObjectProperty<>();
    private final IntegerProperty priority = new SimpleIntegerProperty();
    private final ObjectProperty<Route> route = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> creationDate = new SimpleObjectProperty<>();
    private final DoubleProperty cost = new SimpleDoubleProperty();
     */
}
