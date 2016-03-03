package ru.logist.sbat.jsonParser;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PackageData {
    private List<PointData> updatePoints;
    private List<UpdateDirections> updateDirections;
    private List<UpdateTrader> updateTraders;
    private List<UpdateClients> updateClients;
    private List<AddressData> updateAddresses;
    private List<UpdateRequests> updateRequests;
    private List<UpdateStatuses> updateStatuses;
    private List<UpdateRouteLists> updateRouteLists;

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

    public List<PointData> getUpdatePoints() {
        return updatePoints;
    }

    private void setUpdatePoints(List<JSONObject> updatePoints) {
        List<PointData> pointsData = new ArrayList<>();
        updatePoints.forEach(jsonObject -> pointsData.add(new PointData(jsonObject)));
        this.updatePoints = pointsData;
    }

    public List<AddressData> getUpdateAddresses() {
        return updateAddresses;
    }

    public void setUpdateAddresses(List<JSONObject> updateAddresses) {
        List<AddressData> addressData = new ArrayList<>();
        updateAddresses.forEach(jsonObject -> addressData.add(new AddressData(jsonObject)));
        this.updateAddresses = addressData;
    }


    public List<UpdateDirections> getUpdateDirections() {
        return updateDirections;
    }

    public void setUpdateDirections(List<UpdateDirections> updateDirections) {
        this.updateDirections = updateDirections;
    }

    public List<UpdateTrader> getUpdateTraders() {
        return updateTraders;
    }

    public void setUpdateTraders(List<UpdateTrader> updateTraders) {
        this.updateTraders = updateTraders;
    }

    public List<UpdateClients> getUpdateClients() {
        return updateClients;
    }

    public void setUpdateClients(List<UpdateClients> updateClients) {
        this.updateClients = updateClients;
    }



    public List<UpdateRequests> getUpdateRequests() {
        return updateRequests;
    }

    public void setUpdateRequests(List<UpdateRequests> updateRequests) {
        this.updateRequests = updateRequests;
    }

    public List<UpdateStatuses> getUpdateStatuses() {
        return updateStatuses;
    }

    public void setUpdateStatuses(List<UpdateStatuses> updateStatuses) {
        this.updateStatuses = updateStatuses;
    }

    public List<UpdateRouteLists> getUpdateRouteLists() {
        return updateRouteLists;
    }

    public void setUpdateRouteLists(List<UpdateRouteLists> updateRouteLists) {
        this.updateRouteLists = updateRouteLists;
    }


}
