package ru.logistica.tms.dao.reportDao;

public class Report {
    private String city;
    private String supplier;
    private String ordernumber;
    private String docname;
    private String arrival_date;
    private int palletsqty;
    private int pallets;
    private int sum_boxes;

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDocname() {
        return this.docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getArrival_date() {
        return this.arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public int getPalletsqty() {
        return this.palletsqty;
    }

    public void setPalletsqty(int palletsqty) {
        this.palletsqty = palletsqty;
    }

    public String getOrdernumber() {
        return this.ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getPallets() {
        return this.pallets;
    }

    public void setPallets(int pallets) {
        this.pallets = pallets;
    }

    public int getSum_boxes() {
        return this.sum_boxes;
    }

    public void setSum_boxes(int sum_boxes) {
        this.sum_boxes = sum_boxes;
    }

    public String getSupplier() {
        return this.supplier;
    }

    protected void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String toString() {
        return "Report{city='" + this.city + '\'' + ", supplier='" + this.supplier + '\'' + ", ordernumber='" + this.ordernumber + '\'' + ", docname='" + this.docname + '\'' + ", arrival_date='" + this.arrival_date + '\'' + ", palletsqty=" + this.palletsqty + ", pallets=" + this.pallets + ", sum_boxes=" + this.sum_boxes + '}';
    }
}