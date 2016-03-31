package ru.sbat.transport.optimization.location;



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
    //-------------END СЛУЖЕБНЫЕ МЕТОДЫ-------------
}
