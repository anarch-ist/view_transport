package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RouteListsData {
    private static final String NULL = "NULL";
    private static final DateTimeFormatter isoFormatter = DateTimeFormatter.BASIC_ISO_DATE;
    private static final Set<String> possibleStatuses = new HashSet<>(Arrays.asList(
            "CREATED", "APPROVED"
    ));
    public static final String DELIMITER_FOR_GENERATED_ROUTE_ID = "MAG";

    // внутриузловой или магистральный маршрут
    public enum RouteScopeType{INTRASITE_ROUTE, TRUNK_ROUTE, ERROR}

    private String routeListId;
    private String routeListNumber;
    private Date routeListDate;
    private Date departureDate;
    private String forwarderId;
    private String driverId;
    private String status;
    private String pointDepartureId;
    private String pointArrivalId;
    private String directId;
    private Set<String> invoices = new HashSet<>();

    public RouteListsData(JSONObject updateRouteLists) {
        setRouteListId((String) updateRouteLists.get("routerSheetId"));
        setRouteListNumber((String) updateRouteLists.get("routerSheetNumber"));
        setRouteListDate((String) updateRouteLists.get("routerSheetDate"));
        setDepartureDate((String) updateRouteLists.get("departureDate"));
        setForwarderId((String) updateRouteLists.get("forwarderId"));
        setDriverId((String) updateRouteLists.get("driverId"));
        setStatus((String) updateRouteLists.get("status"));
        setPointDepartureId((String) updateRouteLists.get("pointDepartureId"));
        setPointArrivalId((String) updateRouteLists.get("pointArrivalId"));
        setDirectId((String) updateRouteLists.get("directId"));
        setInvoices((JSONArray) updateRouteLists.get("invoices"));

        if (getRouteState().equals(RouteScopeType.ERROR))
            throw new ValidatorException("Illegal route scope for routerSheetId = [" + getRouteListId() + "]");
    }

    public String getRouteListId() {
        return routeListId;
    }

    private void setRouteListId(String routeListId) {
        Util.requireNonNullOrEmpty(routeListId, "routerSheetId");
        this.routeListId = routeListId;
    }

    public String getRouteListNumber() {
        return routeListNumber;
    }

    private void setRouteListNumber(String routeListNumber) {
        Util.requireNonNull(routeListNumber, "routerSheetNumber");
        this.routeListNumber = routeListNumber;
    }

    public Date getRouteListDate() {
        return routeListDate;
    }

    private void setRouteListDate(String routeListDateAsString) {
        routeListDate = Util.getDateWithCheck(routeListDateAsString, "routerSheetDate", isoFormatter);
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    private void setDepartureDate(String departureDateAsString) {
        departureDate = Util.getDateWithCheck(departureDateAsString, "departureDate", isoFormatter);
    }

    public String getForwarderId() {
        return forwarderId;
    }

    private void setForwarderId(String forwarderId) {
        Util.requireNonNull(forwarderId, "forwarderId");
        this.forwarderId = forwarderId;
    }

    public String getDriverId() {
        return driverId;
    }

    private void setDriverId(String driverId) {
        Util.requireNonNull(driverId, "driverId");
        this.driverId = driverId;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        Util.withValidatorExceptionRedirect(() -> {
            if (!possibleStatuses.contains(status))
                throw new IllegalArgumentException("status [" + status + "] is not contained in set of possible statuses [" + possibleStatuses + "]");
            this.status = status;
        });
    }

    public String getPointDepartureId() {
        return pointDepartureId;
    }

    private void setPointDepartureId(String pointDepartureId) {
        Util.requireNonNullOrEmpty(pointDepartureId, "pointDepartureId");
        this.pointDepartureId = pointDepartureId;
    }

    public String getPointArrivalId() {
        if (isIntrasiteRoute())
            throw new UnsupportedOperationException("this is intrasite route");
        return pointArrivalId;
    }

    private void setPointArrivalId(String pointArrivalId) {
        Util.requireNonNullOrEmpty(pointArrivalId, "pointArrivalId");
        this.pointArrivalId = pointArrivalId;
    }

    public String getDirectId() {
        if (isTrunkRoute())
            throw new UnsupportedOperationException("this is a trunk route");
        return directId;
    }

    private void setDirectId(String directId) {
        Util.requireNonNullOrEmpty(directId, "directId");
        this.directId = directId;
    }

    public Set<String> getInvoices() {
        return invoices;
    }

    private void setInvoices(JSONArray invoices) {
        Util.withValidatorExceptionRedirect(() -> {
            this.invoices.clear();
            for(Object invoice: invoices) {
                RouteListsData.this.invoices.add((String) invoice);
            }
        });
    }

    /**
     *
     * @return state of this object in terms of route scope. it can be intrasite route, trunk route or error.
     */
    private RouteScopeType getRouteState() {
        if (getDirectId().equals(NULL) && !getPointArrivalId().isEmpty())
            return RouteScopeType.TRUNK_ROUTE;
        else if(!getDirectId().isEmpty() && getPointArrivalId().equals(NULL))
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