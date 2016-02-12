package ru.sbat.transport.optimization.location;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.RoutePair;


public class TestRoutePair {
    static Route route = new Route();
    static RoutePoint routePoint = createRoutePoint(getCharacteristicsOfCar(10, 15));
    static Route route2 = new Route();
    static RoutePoint routePoint2 = createRoutePoint(getCharacteristicsOfCar(10, 15));
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
    }

    @Test
    public void testGetAvailableWeightVolume(){
        RoutePair routePair = new RoutePair(2, 5);
        RoutePair result = new RoutePair(route.getStartingWeight(), route.getStartingVolume());
        System.out.println(routePair.getWeight() + " " + routePair.getVolume() + " должно быть");
        result = result.getAvailableWeightAndVolume(invoice2, result);
        System.out.println(result.getWeight() + " " + result.getVolume() + " что есть после 2 invoice");
        result = result.getAvailableWeightAndVolume(invoice3, result);
        System.out.println(result.getWeight() + " " + result.getVolume() + " что есть после 3 invoice");
        result = result.getAvailableWeightAndVolume(invoice4, result);
        System.out.println(result.getWeight() + " " + result.getVolume() + " что есть после 4 invoice");
        Assert.assertEquals(routePair.getWeight(), result.getWeight(), 0);
        Assert.assertEquals(routePair.getVolume(), result.getVolume(), 0);
    }

    @Test
    public void testIsFittingForRoute(){
        RoutePair routePair = new RoutePair(route2.getStartingWeight(), route2.getStartingVolume());
        Assert.assertFalse(routePair.isFittingForRoute(invoice1, routePair));
    }
}
