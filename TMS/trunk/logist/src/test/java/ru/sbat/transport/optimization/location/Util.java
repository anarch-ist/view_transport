package ru.sbat.transport.optimization.location;


import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.Request;

import java.util.Calendar;
import java.util.Date;

public class Util {
    //----------СЛУЖЕБНЫЕ МЕТОДЫ--------------
    public static void initRoute(RouteNew route, int dayOfWeek, CharacteristicsOfCar characteristicsOfCar, Integer departureTime, RoutePoint... routePoints) {
        for (RoutePoint routePoint : routePoints) {
            route.addRoutePoint(routePoint);
        }
        route.setDayOfWeek(dayOfWeek);
        route.setCharacteristicsOfCar(characteristicsOfCar);
        route.setDepartureTimeFromFirstPoint(departureTime);
    }

    public static RoutePoint createRoutePoint(Point departurePoint, int loadingOperationsTime) {
        RoutePoint result = new RoutePoint();
        result.setPoint(departurePoint);
        result.setLoadingOperationsTime(loadingOperationsTime);
        return result;
    }

    public static CharacteristicsOfCar createCharacteristicsOfCar(double occupancyCost) {
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setOccupancyCost(occupancyCost);
        return result;
    }

    public static Invoice createInvoice(Point departurePoint, Date creationDate, Request request, double cost){
        Invoice result = new Invoice();
        result.setAddressOfWarehouse(departurePoint);
        result.setCreationDate(creationDate);
        result.setRequest(request);
        result.setCost(cost);
        return result;
    }

    public static Request createRequest(Point deliveryPoint, Date plannedDeliveryDateTime) {
        Request result = new Request();
        result.setDeliveryPoint(deliveryPoint);
        result.setPlannedDeliveryDate(plannedDeliveryDateTime);
        return result;
    }

    public static Date createDate(int year, int month, int day, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hours, minutes);
        return new Date(calendar.getTimeInMillis());
    }

    public static CharacteristicsOfCar getCharacteristicsOfCar(double occupancyCost){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setOccupancyCost(occupancyCost);
        return result;
    }
    //-------------END СЛУЖЕБНЫЕ МЕТОДЫ-------------
}
