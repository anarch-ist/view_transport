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
    public static final String FN_UPDATE_POINTS = "updatePoints";
    public static final String FN_DELETE_POINTS = "deletePoints";
    public static final String FN_UPDATE_DIRECTIONS = "updateDirections";
    public static final String FN_DELETE_DIRECTIONS = "deleteDirections";
    public static final String FN_UPDATE_TRADER = "updateTrader";
    public static final String FN_DELETE_TRADER = "deleteTrader";
    public static final String FN_UPDATE_CLIENTS = "updateClients";
    public static final String FN_DELETE_CLIENTS = "deleteClients";
    public static final String FN_UPDATE_ADDRESS = "updateAddress";
    public static final String FN_DELETE_ADDRESS = "deleteAddress";
    public static final String FN_UPDATE_REQUESTS = "updateRequests";
    public static final String FN_DELETE_REQUESTS = "deleteRequests";
    public static final String FN_UPDATE_STATUS = "updateStatus";
    public static final String FN_UPDATE_ROUTE_LISTS = "updateRouteLists";
    public static final String FN_DELETE_ROUTE_LISTS = "deleteRouteLists";

    private List<PointData> updatePoints = new ArrayList<>();
    private List<String> deletePoints = new ArrayList<>();
    private List<DirectionsData> updateDirections = new ArrayList<>();
    private List<String> deleteDirections = new ArrayList<>();
    private List<TraderData> updateTraders = new ArrayList<>();
    private List<String> deleteTraders = new ArrayList<>();
    private List<ClientData> updateClients = new ArrayList<>();
    private List<String> deleteClients = new ArrayList<>();
    private List<AddressData> updateAddresses = new ArrayList<>();
    private List<String> deleteAddresses = new ArrayList<>();
    private List<RequestsData> updateRequests = new ArrayList<>();
    private List<String> deleteRequests = new ArrayList<>();
    private List<StatusData> updateStatuses = new ArrayList<>();
    private List<RouteListsData> updateRouteLists = new ArrayList<>();
    private List<String> deleteRouteLists = new ArrayList<>();

    public PackageData(JSONObject packageDataAsJsonObject) throws ValidatorException {

        // check fields
        checkField(packageDataAsJsonObject, FN_UPDATE_POINTS);
        checkField(packageDataAsJsonObject, FN_DELETE_POINTS);
        checkField(packageDataAsJsonObject, FN_UPDATE_DIRECTIONS);
        checkField(packageDataAsJsonObject, FN_DELETE_DIRECTIONS);
        checkField(packageDataAsJsonObject, FN_UPDATE_TRADER);
        checkField(packageDataAsJsonObject, FN_DELETE_TRADER);
        checkField(packageDataAsJsonObject, FN_UPDATE_CLIENTS);
        checkField(packageDataAsJsonObject, FN_DELETE_CLIENTS);
        checkField(packageDataAsJsonObject, FN_UPDATE_ADDRESS);
        checkField(packageDataAsJsonObject, FN_DELETE_ADDRESS);
        checkField(packageDataAsJsonObject, FN_UPDATE_REQUESTS);
        checkField(packageDataAsJsonObject, FN_DELETE_REQUESTS);
        checkField(packageDataAsJsonObject, FN_UPDATE_STATUS);
        checkField(packageDataAsJsonObject, FN_UPDATE_ROUTE_LISTS);
        checkField(packageDataAsJsonObject, FN_DELETE_ROUTE_LISTS);

        // set values
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_POINTS), deletePoints, FN_DELETE_POINTS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_DIRECTIONS), deleteDirections, FN_DELETE_DIRECTIONS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_TRADER), deleteTraders, FN_DELETE_TRADER);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_CLIENTS), deleteClients, FN_DELETE_CLIENTS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_ADDRESS), deleteAddresses, FN_DELETE_ADDRESS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_REQUESTS), deleteRequests, FN_DELETE_REQUESTS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_ROUTE_LISTS), deleteRouteLists, FN_DELETE_ROUTE_LISTS);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_POINTS), updatePoints, PointData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_DIRECTIONS), updateDirections, DirectionsData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_TRADER), updateTraders, TraderData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_CLIENTS), updateClients, ClientData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_ADDRESS), updateAddresses, AddressData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_REQUESTS), updateRequests, RequestsData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_STATUS), updateStatuses, StatusData.class);
        setValue((JSONArray) packageDataAsJsonObject.get(FN_UPDATE_ROUTE_LISTS), updateRouteLists, RouteListsData.class);
    }

    private void checkField(JSONObject packageDataAsJsonObject, String fieldName) throws ValidatorException {
        Util.checkFieldAvailableAndNotNull(fieldName, packageDataAsJsonObject);
        Util.checkCorrectType(packageDataAsJsonObject.get(fieldName), JSONArray.class, packageDataAsJsonObject);
    }

    public List<String> getDeleteDirections() {
        return deleteDirections;
    }
    public List<String> getDeleteTraders() {
        return deleteTraders;
    }
    public List<String> getDeleteClients() {
        return deleteClients;
    }
    public List<String> getDeleteAddresses() {
        return deleteAddresses;
    }
    public List<String> getDeleteRequests() {
        return deleteRequests;
    }
    public List<String> getDeleteRouteLists() {
        return deleteRouteLists;
    }
    public List<PointData> getUpdatePoints() {
        return updatePoints;
    }
    public List<String> getDeletePoints() {
        return deletePoints;
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
    public List<AddressData> getUpdateAddresses() {
        return updateAddresses;
    }
    public List<DirectionsData> getUpdateDirections() {
        return updateDirections;
    }

    private <T> void setValue(JSONArray jsonArray, List<T> updateData, Class<T> dataClazz) throws ValidatorException {

        if (jsonArray.isEmpty())
            return;

        fillList(jsonArray, updateData, dataClazz);
        uniqueCheck(updateData, dataClazz);
    }

    private void setDeleteValue(JSONArray jsonArray, List<String> targetList, String fieldName) throws ValidatorException {

        if (jsonArray.isEmpty())
            return;

        for (Object o : jsonArray) {
            if (!o.getClass().equals(String.class))
                throw new ValidatorException(Util.getParameterizedString("{}: id {} for delete must be type of {}", fieldName, o, String.class.getSimpleName()));
            String externalIdForDelete = (String) o;
            if (externalIdForDelete.equals(""))
                throw new ValidatorException(Util.getParameterizedString("{}: id {} for delete must not be empty", fieldName, externalIdForDelete));
            targetList.add(externalIdForDelete);
        }
    }

    private <T> void fillList(JSONArray jsonArray, List<T> updateData, Class<T> dataClazz) throws ValidatorException {
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

    private void uniqueCheck(List<?> updateData, Class clazz) throws ValidatorException {
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

    private String getStringRepr(List<?> data) {
        if (data.size() > 1)
            return "firstElement = " + data.get(0) + "\n" + "size = " + data.size();
        else
            return data.toString();
    }

    @Override
    public String toString() {
        return "PackageData{" +
                "updatePoints=" + getStringRepr(updatePoints) +
                ", deletePoints=" + getStringRepr(deletePoints) +
                ", updateDirections=" + getStringRepr(updateDirections) +
                ", deleteDirections=" + getStringRepr(deleteDirections) +
                ", updateTraders=" + getStringRepr(updateTraders) +
                ", deleteTraders=" + getStringRepr(deleteTraders) +
                ", updateClients=" + getStringRepr(updateClients) +
                ", deleteClients=" + getStringRepr(deleteClients) +
                ", updateAddresses=" + getStringRepr(updateAddresses) +
                ", deleteAddresses=" + getStringRepr(deleteAddresses) +
                ", updateRequests=" + getStringRepr(updateRequests) +
                ", deleteRequests=" + getStringRepr(deleteRequests) +
                ", updateStatuses=" + getStringRepr(updateStatuses) +
                ", updateRouteLists=" + getStringRepr(updateRouteLists) +
                ", deleteRouteLists=" + getStringRepr(deleteRouteLists) +
                '}';
    }
}
