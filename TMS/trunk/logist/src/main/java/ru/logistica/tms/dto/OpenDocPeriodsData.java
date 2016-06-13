package ru.logistica.tms.dto;

import ru.logistica.tms.util.JsonUtils;

import javax.json.*;
import java.util.HashSet;
import java.util.Set;

public class OpenDocPeriodsData extends HashSet<OpenDocPeriodsData.DocAction> {
    // BINDING main.jsp
    private enum Action {INSERT, DELETE, UPDATE}
    public OpenDocPeriodsData(String jsonString) throws ValidateDataException {
        try {
            JsonArray openPeriodsData = JsonUtils.parseStringAsArray(jsonString);
            for (JsonValue jsonValue : openPeriodsData) {
                JsonArray jsonArray = (JsonArray) jsonValue;

                JsonObject firstJsonObject = (JsonObject) jsonArray.get(0);
                String firstAction = firstJsonObject.getString("action");
                Action action = Action.valueOf(firstAction);

                DocAction.IdOperation idOperation;
                if (action == Action.DELETE) {
                    idOperation = new DocAction.DeleteOperation(firstJsonObject.getJsonNumber("docPeriodId").longValueExact());
                } else if (action == Action.UPDATE) {
                    idOperation = new DocAction.UpdateOperation(
                            firstJsonObject.getJsonNumber("docPeriodId").longValueExact(),
                            firstJsonObject.getJsonNumber("periodBegin").longValueExact(),
                            firstJsonObject.getJsonNumber("periodEnd").longValueExact()
                    );
                } else {
                    throw new ValidateDataException("bad action");
                }

                Set<DocAction.InsertOperation> insertOperations = new HashSet<>();
                if (jsonArray.size() > 1) {
                    for (int i = 1; i < jsonArray.size(); i++) {
                        insertOperations.add(
                                new DocAction.InsertOperation(
                                        ((JsonObject)jsonArray.get(i)).getInt("docId"),
                                        ((JsonObject)jsonArray.get(i)).getJsonNumber("periodBegin").longValue(),
                                        ((JsonObject)jsonArray.get(i)).getJsonNumber("periodEnd").longValue()
                                ));
                    }
                }
                DocAction docAction = new DocAction(idOperation, insertOperations);
                this.add(docAction);
            }
        } catch (Exception e) {
            throw new ValidateDataException(e);
        }

    }

    public static class DocAction {
        public final Set<InsertOperation> insertOperations;
        public final IdOperation idOperation;

        public DocAction(IdOperation idOperation, Set<InsertOperation> operationSet) {
            this.idOperation = idOperation;
            this.insertOperations = operationSet;
        }

        @Override
        public String toString() {
            return "DocAction{" +
                    "insertOperations=" + insertOperations +
                    ", idOperation=" + idOperation +
                    '}';
        }

        public abstract static class Operation {}
        public abstract static class IdOperation{
            public final long docPeriodId;
            protected IdOperation(long docPeriodId) {
                this.docPeriodId = docPeriodId;
            }

            @Override
            public String toString() {
                return "IdOperation{" +
                        "docPeriodId=" + docPeriodId +
                        '}';
            }
        }
        public static class InsertOperation extends Operation {
            public final long periodBegin;
            public final long periodEnd;
            public final int docId;
            public InsertOperation(int docId, long periodBegin, long periodEnd) {
                this.docId = docId;
                this.periodBegin = periodBegin;
                this.periodEnd = periodEnd;
            }

            @Override
            public String toString() {
                return "InsertOperation{" +
                        "periodBegin=" + periodBegin +
                        ", periodEnd=" + periodEnd +
                        ", docId=" + docId +
                        '}';
            }
        }
        public static class UpdateOperation extends IdOperation {
            public final Long periodBegin;
            public final Long periodEnd;
            public UpdateOperation(long docPeriodId, Long periodBegin, Long periodEnd) {
                super(docPeriodId);
                this.periodBegin = periodBegin;
                this.periodEnd = periodEnd;
            }

            @Override
            public String toString() {
                return "UpdateOperation{" +
                        "periodBegin=" + periodBegin +
                        ", periodEnd=" + periodEnd +
                        "} " + super.toString();
            }
        }
        public static class DeleteOperation extends IdOperation {
            public DeleteOperation(long docPeriodId) {
                super(docPeriodId);
            }

            @Override
            public String toString() {
                return "DeleteOperation{} " + super.toString();
            }
        }
    }


}
