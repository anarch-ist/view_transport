package ru.sbat.transport.optimization.location;

public class Util {
    //----------СЛУЖЕБНЫЕ МЕТОДЫ--------------
    public static void initRoute(IRoute route, RoutePoint... routePoints) {
        for (RoutePoint routePoint : routePoints) {
            route.addRoutePoint(routePoint);
        }
    }

    public static RoutePoint createRoutePoint(CharacteristicsOfCar characteristicsOfCar, Point departurePoint, int dayOfWeek, int departureTime, int timeToNextPoint, int loadingOperationsTime, double distanceToNextPoint) {
        RoutePoint result = new RoutePoint();
        result.setCharacteristicsOfCar(characteristicsOfCar);
        result.setDepartureTime(departureTime);
        result.setDayOfWeek(dayOfWeek);
        result.setTimeToNextPoint(timeToNextPoint);
        result.setDistanceToNextPoint(distanceToNextPoint);
        result.setDeparturePoint(departurePoint);
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
