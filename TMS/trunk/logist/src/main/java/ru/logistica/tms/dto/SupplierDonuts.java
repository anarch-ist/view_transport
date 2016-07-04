package ru.logistica.tms.dto;

import java.text.SimpleDateFormat;
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

    public static class SupplierDonut {
        // BINDING supplierHistory.jsp
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
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
            return getFormattedDate(periodBegin);
        }

        public void setPeriodBegin(Date dateBegin) {
            this.periodBegin = dateBegin;
        }

        public Date getPeriodEnd() {
            return getFormattedDate(periodEnd);
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
            return getFormattedDate(lastModified);
        }

        public void setLastModified(Date lastModified) {
            this.lastModified = lastModified;
        }

        private Date getFormattedDate(Date date) {
            return new Date(date.getTime()) {
                @Override
                public String toString() {
                    return dateFormat.format(this);
                }
            };
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
