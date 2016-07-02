package ru.logistica.tms.dto;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.*;

public class SupplierDonuts extends ArrayList<SupplierDonuts.Donut> {
    private String supplierName;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String toJsonString() {
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("supplierName", getSupplierName());
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(Donut donut : this) {
            JsonObjectBuilder donutBuilder = Json.createObjectBuilder();
            donutBuilder.add("periodBegin", donut.getPeriodBegin().toString());
            donutBuilder.add("periodEnd", donut.getPeriodEnd().toString());
            donutBuilder.add("warehouseName", donut.getWarehouseName());
            donutBuilder.add("docName", donut.getDocName());
            donutBuilder.add("comment", donut.getComment());
            donutBuilder.add("lastModified", donut.getLastModified().toString());
            donutBuilder.add("orderNumbers", donut.orderNumbersAsString);
            donutBuilder.add("orderStatuses", donut.orderStatusesAsString);
        }
        result.add("donutDocPeriods", arrayBuilder);
        return result.build().toString();
    }

    public static class Donut {
        private Date dateBegin;
        private Date dateEnd;
        private String warehouseName;
        private String docName;
        private String comment;
        private Date lastModified;
        private String orderNumbersAsString;
        private String orderStatusesAsString;

        public String getOrderNumbersAsString() {
            return orderNumbersAsString;
        }

        public void setOrderNumbersAsString(String orderNumbersAsString) {
            this.orderNumbersAsString = orderNumbersAsString;
        }

        public String getOrderStatusesAsString() {
            return orderStatusesAsString;
        }

        public void setOrderStatusesAsString(String orderStatusesAsString) {
            this.orderStatusesAsString = orderStatusesAsString;
        }

        public Date getPeriodBegin() {
            return dateBegin;
        }

        public void setPeriodBegin(Date dateBegin) {
            this.dateBegin = dateBegin;
        }

        public Date getPeriodEnd() {
            return dateEnd;
        }

        public void setPeriodEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
        }

        public String getWarehouseName() {
            return warehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
        }

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Date getLastModified() {
            return lastModified;
        }

        public void setLastModified(Date lastModified) {
            this.lastModified = lastModified;
        }

        @Override
        public String toString() {
            return "Donut{" +
                    "dateBegin=" + dateBegin +
                    ", dateEnd=" + dateEnd +
                    ", warehouseName='" + warehouseName + '\'' +
                    ", docName='" + docName + '\'' +
                    ", comment='" + comment + '\'' +
                    ", lastModified=" + lastModified +
                    ", orderNumbersAsString='" + orderNumbersAsString + '\'' +
                    ", orderStatusesAsString='" + orderStatusesAsString + '\'' +
                    '}';
        }
    }
}
