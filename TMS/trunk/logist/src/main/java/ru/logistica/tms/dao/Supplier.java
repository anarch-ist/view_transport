package ru.logistica.tms.dao;

//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//@Entity
public class Supplier {
    private Serializable supplierID;
//    private String supplierIdExternal;
//    private DataSources dataSourceId;
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


    public Supplier getSupplierById(Integer id) throws SQLException {
        Supplier supplier = new Supplier();
        String sql = "SELECT * from points WHERE supplierID = ?";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            supplier.setSupplierID(resultSet.getInt("supplierID"));
            supplier.setInn(resultSet.getString("INN"));
            supplier.setClientName(resultSet.getString("clientName"));
            supplier.setKpp(resultSet.getString("KPP"));
            supplier.setCorAccount(resultSet.getString("corAccount"));
            supplier.setCurAccount(resultSet.getString("curAccount"));
            supplier.setBik(resultSet.getString("BIK"));
            supplier.setBankName(resultSet.getString("bankName"));
            supplier.setContractNumber(resultSet.getString("contractNumber"));
            supplier.setDateOfSigning(resultSet.getDate("dateOfSigning"));
            supplier.setStartContractDate(resultSet.getDate("startContractDate"));
            supplier.setEndContractDate(resultSet.getDate("endContractDate"));
        }
        return supplier;
    }

//    @Id
//    @Column(name = "supplierID")
    public Serializable getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Serializable supplierID) {
        this.supplierID = supplierID;
    }

//    @Basic
//    @Column(name = "inn")
    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

//    @Basic
//    @Column(name = "clientName")
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

//    @Basic
//    @Column(name = "kpp")
    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

//    @Basic
//    @Column(name = "corAccount")
    public String getCorAccount() {
        return corAccount;
    }

    public void setCorAccount(String corAccount) {
        this.corAccount = corAccount;
    }

//    @Basic
//    @Column(name = "curAccount")
    public String getCurAccount() {
        return curAccount;
    }

    public void setCurAccount(String curAccount) {
        this.curAccount = curAccount;
    }

//    @Basic
//    @Column(name = "bik")
    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }

//    @Basic
//    @Column(name = "bankName")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

//    @Basic
//    @Column(name = "contractNumber")
    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

//    @Basic
//    @Column(name = "dateOfSigning")
    public Date getDateOfSigning() {
        return dateOfSigning;
    }

    public void setDateOfSigning(Date dateOfSigning) {
        this.dateOfSigning = dateOfSigning;
    }

//    @Basic
//    @Column(name = "startContractDate")
    public Date getStartContractDate() {
        return startContractDate;
    }

    public void setStartContractDate(Date startContractDate) {
        this.startContractDate = startContractDate;
    }

//    @Basic
//    @Column(name = "endContractDate")
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
