package ru.sbat.transport.optimization.location;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.*;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.Map;

public class TestRouteWeightAndVolume {
    static RouteWeightAndVolume routeWeightAndVolume = new RouteWeightAndVolume();
    static InvoiceContainer invoiceContainer = new InvoiceContainer();
    static PlannedSchedule plannedSchedule = new PlannedSchedule();
    static Route route = new Route();
    static RoutePoint routePoint = createRoutePoint(getCharacteristicsOfCar(17, 25));
    static Route route2 = new Route();
    static RoutePoint routePoint2 = createRoutePoint(getCharacteristicsOfCar(10, 15));
    static Route route3 = new Route();
    static RoutePoint routePoint3 = createRoutePoint(getCharacteristicsOfCar(5, 10));
    static Route route4 = new Route();
    static RoutePoint routePoint4 = createRoutePoint(getCharacteristicsOfCar(7, 13));
    static Route route5 = new Route();
    static RoutePoint routePoint5 = createRoutePoint(getCharacteristicsOfCar(20, 30));
    static Invoice invoice1 = createInvoice(9, 15);
    static Invoice invoice2 = createInvoice(5, 6);
    static Invoice invoice3 = createInvoice(3, 4);
    static Invoice invoice4 = createInvoice(1, 5);

    public static RoutePoint createRoutePoint(CharacteristicsOfCar characteristicsOfCar){
        RoutePoint result = new RoutePoint();
        result.setCharacteristicsOfCar(characteristicsOfCar);
        return result;
    }

    public static CharacteristicsOfCar getCharacteristicsOfCar(double weight, double volume){
        CharacteristicsOfCar result = new CharacteristicsOfCar();
        result.setCapacityCar(weight);
        result.setVolumeCar(volume);
        return result;
    }

    public static Invoice createInvoice(double weight, double volume){
        Invoice result = new Invoice();
        result.setWeight(weight);
        result.setVolume(volume);
        return result;
    }

    @BeforeClass
    public static void createRoute(){
        route.add(routePoint);
        route2.add(routePoint2);
        route3.add(routePoint3);
        route4.add(routePoint4);
        route5.add(routePoint5);
        plannedSchedule.add(route);
        plannedSchedule.add(route2);
        plannedSchedule.add(route3);
        plannedSchedule.add(route4);
        plannedSchedule.add(route5);
        invoiceContainer.add(invoice1);
        invoiceContainer.add(invoice2);
        invoiceContainer.add(invoice3);
        invoiceContainer.add(invoice4);
//        invoice1.setDeliveryRoute(route);
//        invoice2.setDeliveryRoute(route4);
//        invoice4.setDeliveryRoute(route3);
//        invoice3.setDeliveryRoute(route4);
    }

    @Test
    public void testGetWeightAndVolumeForRoute(){
        Map<Route, RoutePair> result = routeWeightAndVolume.getWeightAndVolumeForRoute(plannedSchedule, invoiceContainer);
        Assert.assertEquals(5, result.size());
        RoutePair routePair = new RoutePair(8, 10);
        RoutePair routePair2 = new RoutePair(10, 15);
        RoutePair routePair3 = new RoutePair(4, 5);
        RoutePair routePair4 = new RoutePair(2, 7);
        System.out.println(route.getStartingWeight() + " " + route.getStartingVolume() + " были показ-ли машины");
        System.out.println(result.get(route).getWeight() + " " + result.get(route).getVolume() + " после привеса накладной стали");
        System.out.println(route.getStartingWeight() + " " + route.getStartingVolume() + " показ-ли машины не должны поменяться");
        Assert.assertEquals(routePair.getWeight(), result.get(route).getWeight(), 0);
        Assert.assertEquals(routePair.getVolume(), result.get(route).getVolume(), 0);
        System.out.println("");
        System.out.println(route2.getStartingWeight() + " " + route2.getStartingVolume() + " были показ-ли машины");
        System.out.println(result.get(route2).getWeight() + " " + result.get(route2).getVolume() + " после привеса накладной стали");
        System.out.println(route2.getStartingWeight() + " " + route2.getStartingVolume() + " показ-ли машины не должны поменяться");
        Assert.assertEquals(routePair2.getWeight(), result.get(route2).getWeight(), 0);
        Assert.assertEquals(routePair2.getVolume(), result.get(route2).getVolume(), 0);
        System.out.println("");
        System.out.println(route3.getStartingWeight() + " " + route3.getStartingVolume() + " были показ-ли машины");
        System.out.println(result.get(route3).getWeight() + " " + result.get(route3).getVolume() + " после привеса накладной стали");
        System.out.println(route3.getStartingWeight() + " " + route3.getStartingVolume() + " показ-ли машины не должны поменяться");
        Assert.assertEquals(routePair3.getWeight(), result.get(route3).getWeight(), 0);
        Assert.assertEquals(routePair3.getVolume(), result.get(route3).getVolume(), 0);
        System.out.println("");
        System.out.println(route4.getStartingWeight() + " " + route4.getStartingVolume() + " были показ-ли машины");
        System.out.println(result.get(route4).getWeight() + " " + result.get(route4).getVolume() + " после привеса трех накладных стали (последняя накладная не подходит по массе и объему, не учитывается)");
        System.out.println(route4.getStartingWeight() + " " + route4.getStartingVolume() + " показ-ли машины не должны поменяться");
        Assert.assertEquals(routePair4.getWeight(), result.get(route4).getWeight(), 0);
        Assert.assertEquals(routePair4.getVolume(), result.get(route4).getVolume(), 0);
    }
}
