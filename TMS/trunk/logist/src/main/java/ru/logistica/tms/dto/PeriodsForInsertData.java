package ru.logistica.tms.dto;

import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.Date;
import java.util.HashSet;

public class PeriodsForInsertData extends HashSet<PeriodsForInsertData.PeriodData> {
    public PeriodsForInsertData(String jsonString) throws ValidateDataException {
        try {
            JsonArray periodsForInsert = JsonUtils.parseStringAsArray(jsonString);
            for (JsonValue periodForInsert : periodsForInsert) {
                long periodBegin = ((JsonObject) periodForInsert).getJsonNumber("periodBegin").longValueExact();
                long periodEnd = ((JsonObject) periodForInsert).getJsonNumber("periodEnd").longValueExact();
                this.add(new PeriodData(new Date(periodBegin), new Date(periodEnd)));
            }
        } catch (Exception e) {
            throw new ValidateDataException(e);
        }

    }

    public static class PeriodData {
        public final Date periodBegin;
        public final Date periodEnd;

        public PeriodData(Date periodBegin, Date periodEnd) {
            this.periodBegin = periodBegin;
            this.periodEnd = periodEnd;
        }
    }
}
