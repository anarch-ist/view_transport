package ru.logistica.tms.dto;

import java.util.ArrayList;
import java.util.Date;

public class SupplierDonuts extends ArrayList<SupplierDonuts.SupplierDonut> {
    private String supplierName;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

//    public String toJsonString() {
//        JsonObjectBuilder result = Json.createObjectBuilder();
//        result.add("supplierName", getSupplierName());
//        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//        for(SupplierDonut donut : this) {
//            JsonObjectBuilder donutBuilder = Json.createObjectBuilder();
//            donutBuilder.add("periodBegin", donut.getPeriodBegin().toString());
//            donutBuilder.add("periodEnd", donut.getPeriodEnd().toString());
//            donutBuilder.add("warehouseName", donut.getWarehouseName());
//            donutBuilder.add("docName", donut.getDocName());
//            donutBuilder.add("comment", donut.getComment());
//            donutBuilder.add("lastModified", donut.getLastModified().toString());
//            donutBuilder.add("orderNumbers", donut.orderNumbersAsString);
//            donutBuilder.add("orderStatuses", donut.orderStatusesAsString);
//        }
//        result.add("donutDocPeriods", arrayBuilder);
//        return result.build().toString();
//    }

    public static class SupplierDonut {
        private Date periodBegin;
        private Date periodEnd;
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
            return periodBegin;
        }

        public void setPeriodBegin(Date dateBegin) {
            this.periodBegin = dateBegin;
        }

        public Date getPeriodEnd() {
            return periodEnd;
        }

        public void setPeriodEnd(Date dateEnd) {
            this.periodEnd = dateEnd;
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
                    "periodBegin=" + periodBegin +
                    ", periodEnd=" + periodEnd +
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
