package ru.logist.sbat.jsonToBean.beans;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.logist.sbat.GlobalUtils;
import ru.logist.sbat.jsonToBean.jsonReader.Util;
import ru.logist.sbat.jsonToBean.jsonReader.ValidatorException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackageData {
    private static final Logger logger = LogManager.getLogger();

    public static final String FN_UPDATE_POINTS = "updatePoints";
    public static final DeleteCoordinate FN_DELETE_POINTS = new DeleteCoordinate("deletePoints", "pointId");
    public static final String FN_UPDATE_DIRECTIONS = "updateDirections";
    public static final DeleteCoordinate FN_DELETE_DIRECTIONS = new DeleteCoordinate("deleteDirections", "directId");
    public static final String FN_UPDATE_TRADER = "updateTrader";
    public static final DeleteCoordinate FN_DELETE_TRADER = new DeleteCoordinate("deleteTrader", "traderId");
    public static final String FN_UPDATE_CLIENTS = "updateClients";
    public static final DeleteCoordinate FN_DELETE_CLIENTS = new DeleteCoordinate("deleteClients", "clientId");
    public static final String FN_UPDATE_ADDRESS = "updateAddress";
    public static final DeleteCoordinate FN_DELETE_ADDRESS = new DeleteCoordinate("deleteAddress", "addressId");
    public static final String FN_UPDATE_REQUESTS = "updateRequests";
    public static final DeleteCoordinate FN_DELETE_REQUESTS = new DeleteCoordinate("deleteRequests", "requestId");
    public static final String FN_UPDATE_STATUS = "updateStatus";
    public static final String FN_UPDATE_ROUTE_LISTS = "updateRouteLists";
    public static final DeleteCoordinate FN_DELETE_ROUTE_LISTS = new DeleteCoordinate("deleteRouteLists", "routerSheetId");

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
        checkField(packageDataAsJsonObject, FN_DELETE_POINTS.arrayName);
        checkField(packageDataAsJsonObject, FN_UPDATE_DIRECTIONS);
        checkField(packageDataAsJsonObject, FN_DELETE_DIRECTIONS.arrayName);
        checkField(packageDataAsJsonObject, FN_UPDATE_TRADER);
        checkField(packageDataAsJsonObject, FN_DELETE_TRADER.arrayName);
        checkField(packageDataAsJsonObject, FN_UPDATE_CLIENTS);
        checkField(packageDataAsJsonObject, FN_DELETE_CLIENTS.arrayName);
        checkField(packageDataAsJsonObject, FN_UPDATE_ADDRESS);
        checkField(packageDataAsJsonObject, FN_DELETE_ADDRESS.arrayName);
        checkField(packageDataAsJsonObject, FN_UPDATE_REQUESTS);
        checkField(packageDataAsJsonObject, FN_DELETE_REQUESTS.arrayName);
        checkField(packageDataAsJsonObject, FN_UPDATE_STATUS);
        checkField(packageDataAsJsonObject, FN_UPDATE_ROUTE_LISTS);
        checkField(packageDataAsJsonObject, FN_DELETE_ROUTE_LISTS.arrayName);

        // set values
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_POINTS.arrayName), deletePoints, FN_DELETE_POINTS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_DIRECTIONS.arrayName), deleteDirections, FN_DELETE_DIRECTIONS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_TRADER.arrayName), deleteTraders, FN_DELETE_TRADER);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_CLIENTS.arrayName), deleteClients, FN_DELETE_CLIENTS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_ADDRESS.arrayName), deleteAddresses, FN_DELETE_ADDRESS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_REQUESTS.arrayName), deleteRequests, FN_DELETE_REQUESTS);
        setDeleteValue((JSONArray) packageDataAsJsonObject.get(FN_DELETE_ROUTE_LISTS.arrayName), deleteRouteLists, FN_DELETE_ROUTE_LISTS);

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
        Util.checkCorrectType(packageDataAsJsonObject.get(fieldName), JSONArray.class);
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

    private void setDeleteValue(JSONArray jsonArray, List<String> targetList, DeleteCoordinate deleteCoordinate) throws ValidatorException {

        if (jsonArray.isEmpty())
            return;

        for (Object o : jsonArray) {
            if (!o.getClass().equals(JSONObject.class))
                throw new ValidatorException(GlobalUtils.getParameterizedString("{}: id {} for delete must be type of {}", deleteCoordinate.arrayName, o, JSONObject.class.getSimpleName()));
            JSONObject jsonObject = (JSONObject) o;
            Object valueForDelete = jsonObject.get(deleteCoordinate.fieldName);
            if (valueForDelete == null) {
                throw new ValidatorException(GlobalUtils.getParameterizedString("{}: value for delete must not be empty", deleteCoordinate.arrayName));
            }
            if (!valueForDelete.getClass().equals(String.class)) {
                throw new ValidatorException(GlobalUtils.getParameterizedString("{} id {} value for delete must be type of {}", deleteCoordinate.arrayName, o, String.class.getSimpleName()));
            }
            targetList.add((String) valueForDelete);
        }
    }

    private <T> void fillList(JSONArray jsonArray, List<T> updateData, Class<T> dataClazz) throws ValidatorException {
        for (Object o : jsonArray) {
            Util.checkCorrectType(o, JSONObject.class);
            JSONObject jsonObject = (JSONObject) o;
            try {
                Object newInstance = dataClazz.getConstructor(JSONObject.class).newInstance(jsonObject);
                updateData.add(dataClazz.cast(newInstance));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                /*NOPE*/
            } catch (InvocationTargetException e) {
                logger.warn(e.getTargetException().getMessage());
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
                            throw new ValidatorException(GlobalUtils.getParameterizedString("not unique value {} for field {} in {}", valueOfUniqueField, declaredField.getName(), pointData));
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

    private static class DeleteCoordinate {
        public final String arrayName;
        public final String fieldName;

        public DeleteCoordinate(String arrayName, String fieldName) {
            this.arrayName = arrayName;
            this.fieldName = fieldName;
        }
    }
}
