package ru.logist.sbat.jsonParser.beans;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_POINTS), updatePoints, PointData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_DIRECTIONS), updateDirections, DirectionsData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_TRADER), updateTraders, TraderData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_CLIENTS), updateClients, ClientData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_ADDRESS), updateAddresses, AddressData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_REQUESTS), updateRequests, RequestsData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_STATUS), updateStatuses, StatusData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_ROUTE_LISTS), updateRouteLists, RouteListsData.class);
    }

    private <T> void setValue(JSONArray jsonArray, List<T> updateData, Class<T> dataClazz) {
        fillList(jsonArray, updateData, dataClazz);
        uniqueCheck(updateData, dataClazz);
    }

    private <T> void fillList(JSONArray jsonArray, List<T> updateData, Class<T> dataClazz) {
        for (Object o : jsonArray) {
            Util.checkCorrectType(o, JSONObject.class);
            JSONObject jsonObject = (JSONObject) o;
            try {
                Object newInstance = dataClazz.getConstructor(JSONObject.class).newInstance(jsonObject);
                updateData.add(dataClazz.cast(newInstance));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                /*NOPE*/
                e.printStackTrace();
            }
        }
    }

    private void uniqueCheck(List<?> updateData, Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getDeclaredAnnotation(Unique.class) != null) {
                declaredField.setAccessible(true);
                Set<Object> objectSet = new HashSet<>();
                for (Object pointData : updateData) {
                    try {
                        Object valueOfUniqueField = declaredField.get(pointData);
                        if (objectSet.contains(valueOfUniqueField))
                            throw new ValidatorException(Util.getParameterizedString("not unique value {} for field {} in {}", valueOfUniqueField, declaredField.getName(), pointData));
                        if (!valueOfUniqueField.equals(""))
                            objectSet.add(valueOfUniqueField);
                    } catch (IllegalAccessException e) {/*NOPE*/}
                }
            }
        }
    }

    public List<TraderData> getUpdateTraders() {
        return updateTraders;
    }

    public List<ClientData> getUpdateClients() {
        return updateClients;
    }

    public List<RequestsData> getUpdateRequests() {
        return updateRequests;
    }

    public List<StatusData> getUpdateStatuses() {
        return updateStatuses;
    }

    public List<RouteListsData> getUpdateRouteLists() {
        return updateRouteLists;
    }

    public List<PointData> getUpdatePoints() {
        return updatePoints;
    }

    public List<AddressData> getUpdateAddresses() {
        return updateAddresses;
    }

    public List<DirectionsData> getUpdateDirections() {
        return updateDirections;
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
