package ru.logistica.tms.dao.suppliersDao;

import java.io.Serializable;
import java.util.Date;

public class Supplier {
    private Serializable supplierID;
    private String inn;
    private String clientName;
    private String kpp;
    private String corAccount;
    private String curAccount;
    private String bik;
    private String bankName;
    private String contractNumber;
    private Date dateOfSigning;
    private Date startContractDate;
    private Date endContractDate;

    public Serializable getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Serializable supplierID) {
        this.supplierID = supplierID;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getCorAccount() {
        return corAccount;
    }

    public void setCorAccount(String corAccount) {
        this.corAccount = corAccount;
    }

    public String getCurAccount() {
        return curAccount;
    }

    public void setCurAccount(String curAccount) {
        this.curAccount = curAccount;
    }

    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Date getDateOfSigning() {
        return dateOfSigning;
    }

    public void setDateOfSigning(Date dateOfSigning) {
        this.dateOfSigning = dateOfSigning;
    }

    public Date getStartContractDate() {
        return startContractDate;
    }

    public void setStartContractDate(Date startContractDate) {
        this.startContractDate = startContractDate;
    }

    public Date getEndContractDate() {
        return endContractDate;
    }

    public void setEndContractDate(Date endContractDate) {
        this.endContractDate = endContractDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supplier supplier = (Supplier) o;

        if (supplierID != null ? !supplierID.equals(supplier.supplierID) : supplier.supplierID != null) return false;
        if (inn != null ? !inn.equals(supplier.inn) : supplier.inn != null) return false;
        if (clientName != null ? !clientName.equals(supplier.clientName) : supplier.clientName != null) return false;
        if (kpp != null ? !kpp.equals(supplier.kpp) : supplier.kpp != null) return false;
        if (corAccount != null ? !corAccount.equals(supplier.corAccount) : supplier.corAccount != null) return false;
        if (curAccount != null ? !curAccount.equals(supplier.curAccount) : supplier.curAccount != null) return false;
        if (bik != null ? !bik.equals(supplier.bik) : supplier.bik != null) return false;
        if (bankName != null ? !bankName.equals(supplier.bankName) : supplier.bankName != null) return false;
        if (contractNumber != null ? !contractNumber.equals(supplier.contractNumber) : supplier.contractNumber != null) return false;
        if (dateOfSigning != null ? !dateOfSigning.equals(supplier.dateOfSigning) : supplier.dateOfSigning != null) return false;
        if (startContractDate != null ? !startContractDate.equals(supplier.startContractDate) : supplier.startContractDate != null) return false;
        if (endContractDate != null ? endContractDate.equals(supplier.endContractDate) : supplier.endContractDate == null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = supplierID != null ? supplierID.hashCode() : 0;
        result = 31 * result + (inn != null ? inn.hashCode() : 0);
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        result = 31 * result + (kpp != null ? kpp.hashCode() : 0);
        result = 31 * result + (corAccount != null ? corAccount.hashCode() : 0);
        result = 31 * result + (curAccount != null ? curAccount.hashCode() : 0);
        result = 31 * result + (bik != null ? bik.hashCode() : 0);
        result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
        result = 31 * result + (contractNumber != null ? contractNumber.hashCode() : 0);
        result = 31 * result + (dateOfSigning != null ? dateOfSigning.hashCode() : 0);
        result = 31 * result + (startContractDate != null ? startContractDate.hashCode() : 0);
        result = 31 * result + (endContractDate != null ? endContractDate.hashCode() : 0);
        return result;
    }
}
