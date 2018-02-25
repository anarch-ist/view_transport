/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  ru.logistica.tms.dto.DocReports$DocReport
 */
package ru.logistica.tms.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.logistica.tms.dto.DocReports;



public class DocReports
        extends ArrayList<DocReports.DocReport> {
    private String warehouseName;
    private String periodBegin;
    private String periodEnd;

    public String getWarehouseName() {
        return this.warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public static class DocReport {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        private String inn;
        private String date;
        private String docname;
        private String licenseplate;
        private String period;
        private String finaldestinations;
        private String orderpalletsqty;
        private String boxqty;

        public String getInn() {
            return this.inn;
        }

        public void setInn(String inn) {
            this.inn = inn;
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDocname() {
            return this.docname;
        }

        public void setDocname(String docname) {
            this.docname = docname;
        }

        public String getLicenseplate() {
            return this.licenseplate;
        }

        public void setLicenseplate(String licenseplate) {
            this.licenseplate = licenseplate;
        }

        public String getPeriod() {
            return this.period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getFinaldestinations() {
            return this.finaldestinations;
        }

        public void setFinaldestinations(String finaldestinations) {
            this.finaldestinations = finaldestinations;
        }

        public String getOrderpalletsqty() {
            return this.orderpalletsqty;
        }

        public void setOrderpalletsqty(String orderpalletsqty) {
            this.orderpalletsqty = orderpalletsqty;
        }

        public String getBoxqty() {
            return this.boxqty;
        }

        public void setBoxqty(String boxqty) {
            this.boxqty = boxqty;
        }


        public String toString() {
            return "DocReport{inn='" + this.inn + '\'' + ", date=" + this.date + ", docname='" + this.docname + '\'' + ", licenseplate='" + this.licenseplate + '\'' + ", period='" + this.period + '\'' + ", finaldestinations='" + this.finaldestinations + '\'' + ", orderpalletsqty='" + this.orderpalletsqty + '\'' + ", boxqty='" + this.boxqty + '\'' + '}';
        }


    }
}

