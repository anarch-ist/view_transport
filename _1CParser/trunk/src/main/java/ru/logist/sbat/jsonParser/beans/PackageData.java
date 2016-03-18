package ru.logist.sbat.jsonParser.beans;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

import java.util.ArrayList;
import java.util.List;

public class PackageData {
    private static final String FN_UPDATE_POINTS = "updatePoints";
    private static final String FN_UPDATE_DIRECTIONS = "updateDirections";
    private static final String FN_UPDATE_TRADER = "updateTrader";
    private static final String FN_UPDATE_CLIENTS = "updateClients";
    private static final String FN_UPDATE_ADDRESS = "updateAddress";
    private static final String FN_UPDATE_REQUESTS = "updateRequests";
    private static final String FN_UPDATE_STATUS = "updateStatus";
    private static final String FN_UPDATE_ROUTE_LISTS = "updateRouteLists";

    private List<PointData> updatePoints = new ArrayList<>();
    private List<DirectionsData> updateDirections = new ArrayList<>();
    private List<TraderData> updateTraders = new ArrayList<>();
    private List<ClientData> updateClients = new ArrayList<>();
    private List<AddressData> updateAddresses = new ArrayList<>();
    private List<RequestsData> updateRequests = new ArrayList<>();
    private List<StatusData> updateStatuses = new ArrayList<>();
    private List<RouteListsData> updateRouteLists = new ArrayList<>();

    public PackageData(JSONObject packageDataAsJsonObject) {
        // check fields
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_POINTS, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_POINTS), JSONArray.class, packageDataAsJsonObject);
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_DIRECTIONS, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_DIRECTIONS), JSONArray.class, packageDataAsJsonObject);
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_TRADER, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_TRADER), JSONArray.class, packageDataAsJsonObject);
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_CLIENTS, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_CLIENTS), JSONArray.class, packageDataAsJsonObject);
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_ADDRESS, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_ADDRESS), JSONArray.class, packageDataAsJsonObject);
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_REQUESTS, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_REQUESTS), JSONArray.class, packageDataAsJsonObject);
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_STATUS, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_STATUS), JSONArray.class, packageDataAsJsonObject);
        Util.checkFieldAvailableAndNotNull(FN_UPDATE_ROUTE_LISTS, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(FN_UPDATE_ROUTE_LISTS), JSONArray.class, packageDataAsJsonObject);

        // set values
        setUpdatePoints((JSONArray)packageDataAsJsonObject.get("updatePoints"));
        setUpdateDirections((JSONArray) packageDataAsJsonObject.get("updateDirections"));
        setUpdateTraders((JSONArray) packageDataAsJsonObject.get("updateTrader"));
        setUpdateClients((JSONArray) packageDataAsJsonObject.get("updateClients"));
        setUpdateAddresses((JSONArray)packageDataAsJsonObject.get("updateAddress"));
        setUpdateRequests((JSONArray) packageDataAsJsonObject.get("updateRequests"));
        setUpdateStatuses((JSONArray) packageDataAsJsonObject.get("updateStatus"));
        setUpdateRouteLists((JSONArray) packageDataAsJsonObject.get("updateRouteLists"));
    }

    public List<TraderData> getUpdateTraders() {
        return updateTraders;
    }

    private void setUpdateTraders(List<JSONObject> updateTradersAsJson) {
        updateTraders.clear();
        updateTradersAsJson.forEach(jsonObject -> updateTraders.add(new TraderData(jsonObject)));
    }

    public List<ClientData> getUpdateClients() {
        return updateClients;
    }

    private void setUpdateClients(List<JSONObject> updateClientsAsJson) {
        updateClients.clear();
        updateClientsAsJson.forEach(jsonObject -> updateClients.add(new ClientData(jsonObject)));
    }

    public List<RequestsData> getUpdateRequests() {
        return updateRequests;
    }

    private void setUpdateRequests(List<JSONObject> updateRequestsAsJson) {
        updateRequests.clear();
        updateRequestsAsJson.forEach(jsonObject -> updateRequests.add(new RequestsData(jsonObject)));
    }

    public List<StatusData> getUpdateStatuses() {
        return updateStatuses;
    }

    private void setUpdateStatuses(List<JSONObject> updateStatusesAsJson) {
        updateStatuses.clear();
        updateStatusesAsJson.forEach(jsonObject -> updateStatuses.add(new StatusData(jsonObject)));
    }

    public List<RouteListsData> getUpdateRouteLists() {
        return updateRouteLists;
    }

    private void setUpdateRouteLists(List<JSONObject> updateRouteListsAsJson) {
        updateRouteLists.clear();
        updateRouteListsAsJson.forEach(jsonObject -> updateRouteLists.add(new RouteListsData(jsonObject)));
    }

    public List<PointData> getUpdatePoints() {
        return updatePoints;
    }

    private void setUpdatePoints(List<JSONObject> updatePointsAsJson) {
        updatePoints.clear();
        updatePointsAsJson.forEach(jsonObject -> updatePoints.add(new PointData(jsonObject)));
    }

    public List<AddressData> getUpdateAddresses() {
        return updateAddresses;
    }

    private void setUpdateAddresses(List<JSONObject> updateAddressesAsJson) {
        updateAddresses.clear();
        updateAddressesAsJson.forEach(jsonObject -> updateAddresses.add(new AddressData(jsonObject)));
    }

    public List<DirectionsData> getUpdateDirections() {
        return updateDirections;
    }

    private void setUpdateDirections(List<JSONObject> updateDirectionsAsJson) {
        updateDirections.clear();
        updateDirectionsAsJson.forEach(jsonObject -> updateDirections.add(new DirectionsData(jsonObject)));
    }


    private String getStringRepr(List<?> data) {
        if (data.size() > 1)
            return "firstElement = " + data.get(0) + "\n" + "size = " + data.size();
        else
            return data.toString();
    }

    @Override
    public String toString() {
        return "PackageData{" +
                "updatePoints=" + getStringRepr(updatePoints) + "\n" +
                ", updateDirections=" + getStringRepr(updateDirections) + "\n" +
                ", updateTraders=" + getStringRepr(updateTraders) + "\n" +
                ", updateClients=" + getStringRepr(updateClients) + "\n" +
                ", updateAddresses=" + getStringRepr(updateAddresses) + "\n" +
                ", updateRequests=" + getStringRepr(updateRequests) + "\n" +
                ", updateStatuses=" + getStringRepr(updateStatuses) + "\n" +
                ", updateRouteLists=" + getStringRepr(updateRouteLists) +
                '}';
    }
}
