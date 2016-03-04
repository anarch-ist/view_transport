package ru.logist.sbat.jsonParser.beans;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PackageData {
    private List<PointData> updatePoints = new ArrayList<>();
    private List<DirectionsData> updateDirections = new ArrayList<>();
    private List<TraderData> updateTraders = new ArrayList<>();
    private List<ClientData> updateClients = new ArrayList<>();
    private List<AddressData> updateAddresses = new ArrayList<>();
    private List<RequestsData> updateRequests = new ArrayList<>();
    private List<StatusData> updateStatuses = new ArrayList<>();
    private List<RouteListsData> updateRouteLists = new ArrayList<>();

    public PackageData(JSONObject packageDataAsJsonObject) {
        setUpdatePoints((JSONArray)packageDataAsJsonObject.get("updatePoints"));
        setUpdateAddresses((JSONArray)packageDataAsJsonObject.get("updateAddress"));
        setUpdateDirections((JSONArray) packageDataAsJsonObject.get("updateDirections"));
        setUpdateTraders((JSONArray) packageDataAsJsonObject.get("updateTrader"));
        setUpdateClients((JSONArray) packageDataAsJsonObject.get("updateClients"));
        setUpdateRequests((JSONArray) packageDataAsJsonObject.get("updateRequests"));
        setUpdateStatuses((JSONArray) packageDataAsJsonObject.get("updateStatus"));
        setUpdateRouteLists((JSONArray) packageDataAsJsonObject.get("updateRouteLists"));
        setUpdateClients((JSONArray) packageDataAsJsonObject.get("updateClients"));
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
