package ru.logistica.tms.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Want {
    private Serializable wantsID;
    private String wantsNumber;
    private Date wantsDate;
    private Integer supplierId;
    private Integer destinationPointId;
    private String invoiceNumber;
    private Date invoiceDate;
    private Date deliveryDate;
    private Integer boxQty;
    private Integer weight;
    private Integer volume;
    private Double goodsCost;
    private Date lastStatusUpdated;
    private Integer lastModifiedBy;
    private DonutStatuses wantsStatusId;
    private String commentForStatus;
    private Integer donutId;

    @Id
    @Column(name = "wantsID")
    public Serializable getWantsID() {
        return wantsID;
    }

    public void setWantsID(Serializable wantsID) {
        this.wantsID = wantsID;
    }

    @Basic
    @Column(name = "wantsNumber")
    public String getWantsNumber() {
        return wantsNumber;
    }

    public void setWantsNumber(String wantsNumber) {
        this.wantsNumber = wantsNumber;
    }

    @Basic
    @Column(name = "wantsDate")
    public Date getWantsDate() {
        return wantsDate;
    }

    public void setWantsDate(Date wantsDate) {
        this.wantsDate = wantsDate;
    }

    @Basic
    @Column(name = "supplierId")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "destinationPointId")
    public Integer getDestinationPointId() {
        return destinationPointId;
    }

    public void setDestinationPointId(Integer destinationPointId) {
        this.destinationPointId = destinationPointId;
    }

    @Basic
    @Column(name = "invoiceNumber")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Basic
    @Column(name = "invoiceDate")
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Basic
    @Column(name = "deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Basic
    @Column(name = "boxQty")
    public Integer getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(Integer boxQty) {
        this.boxQty = boxQty;
    }

    @Basic
    @Column(name = "weight")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "volume")
    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @Basic
    @Column(name = "goodsCost")
    public Double getGoodsCost() {
        return goodsCost;
    }

    public void setGoodsCost(Double goodsCost) {
        this.goodsCost = goodsCost;
    }

    @Basic
    @Column(name = "lastStatusUpdated")
    public Date getLastStatusUpdated() {
        return lastStatusUpdated;
    }

    public void setLastStatusUpdated(Date lastStatusUpdated) {
        if(lastStatusUpdated == null){
            this.lastStatusUpdated = new Date();
        }else {
            this.lastStatusUpdated = lastStatusUpdated;
        }
    }

    @Basic
    @Column(name = "lastModifiedBy")
    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Basic
    @Column(name = "wantsStatusId")
    public DonutStatuses getWantsStatusId() {
        return wantsStatusId;
    }

    public void setWantsStatusId(DonutStatuses wantsStatusId) {
        this.wantsStatusId = wantsStatusId;
    }

    @Basic
    @Column(name = "commentForStatus")
    public String getCommentForStatus() {
        return commentForStatus;
    }

    public void setCommentForStatus(String commentForStatus) {
        this.commentForStatus = commentForStatus;
    }

    @Basic
    @Column(name = "donutId")
    public Integer getDonutId() {
        return donutId;
    }

    public void setDonutId(Integer donutId) {
        this.donutId = donutId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Want want = (Want) o;

        if (wantsID != null ? !wantsID.equals(want.wantsID) : want.wantsID != null) return false;
        if (wantsNumber != null ? !wantsNumber.equals(want.wantsNumber) : want.wantsNumber != null) return false;
        if (wantsDate != null ? !wantsDate.equals(want.wantsDate) : want.wantsDate != null) return false;
        if (supplierId != null ? !supplierId.equals(want.supplierId) : want.supplierId != null) return false;
        if (destinationPointId != null ? !destinationPointId.equals(want.destinationPointId) : want.destinationPointId != null)
            return false;
        if (invoiceNumber != null ? !invoiceNumber.equals(want.invoiceNumber) : want.invoiceNumber != null)
            return false;
        if (invoiceDate != null ? !invoiceDate.equals(want.invoiceDate) : want.invoiceDate != null) return false;
        if (deliveryDate != null ? !deliveryDate.equals(want.deliveryDate) : want.deliveryDate != null) return false;
        if (boxQty != null ? !boxQty.equals(want.boxQty) : want.boxQty != null) return false;
        if (weight != null ? !weight.equals(want.weight) : want.weight != null) return false;
        if (volume != null ? !volume.equals(want.volume) : want.volume != null) return false;
        if (goodsCost != null ? !goodsCost.equals(want.goodsCost) : want.goodsCost != null) return false;
        if (lastStatusUpdated != null ? !lastStatusUpdated.equals(want.lastStatusUpdated) : want.lastStatusUpdated != null)
            return false;
        if (lastModifiedBy != null ? !lastModifiedBy.equals(want.lastModifiedBy) : want.lastModifiedBy != null)
            return false;
        if (wantsStatusId != want.wantsStatusId) return false;
        if (commentForStatus != null ? !commentForStatus.equals(want.commentForStatus) : want.commentForStatus != null)
            return false;
        if (donutId != null ? donutId.equals(want.donutId) : want.donutId == null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = wantsID != null ? wantsID.hashCode() : 0;
        result = 31 * result + (wantsNumber != null ? wantsNumber.hashCode() : 0);
        result = 31 * result + (wantsDate != null ? wantsDate.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (destinationPointId != null ? destinationPointId.hashCode() : 0);
        result = 31 * result + (invoiceNumber != null ? invoiceNumber.hashCode() : 0);
        result = 31 * result + (invoiceDate != null ? invoiceDate.hashCode() : 0);
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        result = 31 * result + (boxQty != null ? boxQty.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (volume != null ? volume.hashCode() : 0);
        result = 31 * result + (goodsCost != null ? goodsCost.hashCode() : 0);
        result = 31 * result + (lastStatusUpdated != null ? lastStatusUpdated.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (wantsStatusId != null ? wantsStatusId.hashCode() : 0);
        result = 31 * result + (commentForStatus != null ? commentForStatus.hashCode() : 0);
        result = 31 * result + (donutId != null ? donutId.hashCode() : 0);
        return result;
    }
}
