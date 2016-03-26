package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RouteListsData {
    private static final String FN_ROUTE_LIST_ID = "routerSheetId";
    private static final String FN_ROUTE_LIST_NUMBER = "routerSheetNumber";
    private static final String FN_ROUTE_LIST_DATE = "routerSheetDate";
    private static final String FN_DEPARTURE_DATE = "departureDate";
    private static final String FN_FORWARDER_ID = "forwarderId";
    private static final String FN_DRIVER_ID = "driverId";
    private static final String FN_POINT_DEPARTURE_ID = "pointDepartureId";
    private static final String FN_POINT_ARRIVAL_ID = "pointArrivalId";
    private static final String FN_DIRECT_ID = "directId";
    private static final String FN_STATUS = "status";
    private static final String FN_INVOICES = "invoices";
    private static final DateTimeFormatter isoFormatter = DateTimeFormatter.BASIC_ISO_DATE;
    private static final Set<String> possibleStatuses = new HashSet<>(Arrays.asList("CREATED", "APPROVED"));
    private static final String NULL = "NULL";
    private static final String DELIMITER_FOR_GENERATED_ROUTE_ID = "MAG";
    // внутриузловой или магистральный маршрут
    private enum RouteScopeType{INTRASITE_ROUTE, TRUNK_ROUTE, ERROR}

    @Unique
    private String routeListId;
    private String routeListNumber;
    private Date routeListDate;
    private Date departureDate;
    private String forwarderId;
    private String driverId;
    private String pointDepartureId;
    private String pointArrivalId;
    private String directId;
    private String status;
    private Set<String> invoices;

    public RouteListsData(JSONObject updateRouteLists) {

        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_ROUTE_LIST_ID, updateRouteLists);
        Util.checkFieldAvailableAndNotNull           (FN_ROUTE_LIST_NUMBER, updateRouteLists);
        Util.checkFieldAvailableAndNotNull           (FN_ROUTE_LIST_DATE, updateRouteLists);
        Util.checkFieldAvailableAndNotNull           (FN_DEPARTURE_DATE, updateRouteLists);
        Util.checkFieldAvailableAndNotNull           (FN_FORWARDER_ID, updateRouteLists);
        Util.checkFieldAvailableAndNotNull           (FN_DRIVER_ID, updateRouteLists);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_POINT_DEPARTURE_ID, updateRouteLists);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_POINT_ARRIVAL_ID, updateRouteLists);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_DIRECT_ID, updateRouteLists);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_STATUS, updateRouteLists);
        Util.checkFieldAvailableAndNotNull           (FN_INVOICES, updateRouteLists);

        //set values
        Util.setStringValue              (FN_ROUTE_LIST_ID, updateRouteLists, this, "routeListId");
        Util.setStringValue              (FN_ROUTE_LIST_NUMBER, updateRouteLists, this, "routeListNumber");
        Util.setNullIfEmptyOrSetValueDate(FN_ROUTE_LIST_DATE, updateRouteLists, this, "routeListDate", isoFormatter);
        Util.setNullIfEmptyOrSetValueDate(FN_DEPARTURE_DATE, updateRouteLists, this, "departureDate", isoFormatter);
        Util.setStringValue              (FN_FORWARDER_ID, updateRouteLists, this, "forwarderId");
        Util.setStringValue              (FN_DRIVER_ID, updateRouteLists, this, "driverId");
        Util.setStringValue              (FN_POINT_DEPARTURE_ID, updateRouteLists, this, "pointDepartureId");
        Util.setStringValue              (FN_POINT_ARRIVAL_ID, updateRouteLists, this, "pointArrivalId");
        Util.setStringValue              (FN_DIRECT_ID, updateRouteLists, this, "directId");
        Util.setStringValue              (FN_STATUS, updateRouteLists, this, "status", possibleStatuses);
        Util.setStringSetValue           (FN_INVOICES, updateRouteLists, this, "invoices");

        if (getRouteState().equals(RouteScopeType.ERROR))
            throw new ValidatorException(Util.getParameterizedString("Illegal route scope in {}", updateRouteLists));
    }

    public String getRouteListId() {
        return routeListId;
    }

    public String getRouteListNumber() {
        return routeListNumber;
    }

    public Date getRouteListDate() {
        return routeListDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public String getForwarderId() {
        return forwarderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getStatus() {
        return status;
    }

    public String getPointDepartureId() {
        return pointDepartureId;
    }

    public String getPointArrivalId() {
        if (isIntrasiteRoute())
            throw new UnsupportedOperationException("this is intrasite route");
        return pointArrivalId;
    }

    public String getDirectId() {
        if (isTrunkRoute())
            throw new UnsupportedOperationException("this is a trunk route");
        return directId;
    }

    public Set<String> getRequests() {
        return invoices;
    }

    /**
     *
     * @return state of this object in terms of route scope. it can be intrasite route, trunk route or error.
     */
    private RouteScopeType getRouteState() {
        if (directId.equals(NULL) && !pointArrivalId.isEmpty())
            return RouteScopeType.TRUNK_ROUTE;
        else if(!directId.isEmpty() && pointArrivalId.equals(NULL))
            return RouteScopeType.INTRASITE_ROUTE;
        else
            return RouteScopeType.ERROR;
    }

    /**
     * directId is NULL, pointArrivalId is not NULL
     * If a route is the trunk route then it has no any direction, so method get directId has no sens.
     * We must generate route for such case, with routeName like 52MAG607 where 52 is point departure id
     * and 607 is point arrival id.
     * @return true if route is trunk route, false in other case.
     */
    public boolean isTrunkRoute() {
        return getRouteState().equals(RouteScopeType.TRUNK_ROUTE);
    }

    public String getGeneratedRouteId() {
        if (!isTrunkRoute())
            throw new UnsupportedOperationException("Generated route id is valuable only for trunk routes");
        return getPointDepartureId() + DELIMITER_FOR_GENERATED_ROUTE_ID + getPointArrivalId();
    }

    /**
     * directId is not NULL, pointArrivalId is NULL
     * @return true if route is intrasite, false in other case.
     */
    public boolean isIntrasiteRoute() {
        return getRouteState().equals(RouteScopeType.INTRASITE_ROUTE);
    }

    @Override
    public String toString() {
        return "RouteListsData{" +
                "routeListId='" + routeListId + '\'' +
                ", routeListNumber='" + routeListNumber + '\'' +
                ", routeListDate=" + routeListDate +
                ", departureDate=" + departureDate +
                ", forwarderId='" + forwarderId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", status='" + status + '\'' +
                ", pointDepartureId='" + pointDepartureId + '\'' +
                ", pointArrivalId='" + pointArrivalId + '\'' +
                ", directId='" + directId + '\'' +
                ", invoices='" + invoices + '\'' +
                ", routeState='" + getRouteState() + '\'' +
                '}';
    }
}
