package ru.logistica.tms.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Time;

@Entity
public class Points {
    private Serializable pointid;
    private String pointname;
    private String region;
    private Short timezone;
    private Short docs;
    private String comments;
    private Time opentime;
    private Time closetime;
    private String district;
    private String locality;
    private String mailindex;
    private String address;
    private String email;
    private String phonenumber;
    private String pointtypeid;

    @Id
    @Column(name = "pointid")
    public Serializable getPointid() {
        return pointid;
    }

    public void setPointid(Serializable pointid) {
        this.pointid = pointid;
    }

    @Basic
    @Column(name = "pointname")
    public String getPointname() {
        return pointname;
    }

    public void setPointname(String pointname) {
        this.pointname = pointname;
    }

    @Basic
    @Column(name = "region")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Basic
    @Column(name = "timezone")
    public Short getTimezone() {
        return timezone;
    }

    public void setTimezone(Short timezone) {
        this.timezone = timezone;
    }

    @Basic
    @Column(name = "docs")
    public Short getDocs() {
        return docs;
    }

    public void setDocs(Short docs) {
        this.docs = docs;
    }

    @Basic
    @Column(name = "comments")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "opentime")
    public Time getOpentime() {
        return opentime;
    }

    public void setOpentime(Time opentime) {
        this.opentime = opentime;
    }

    @Basic
    @Column(name = "closetime")
    public Time getClosetime() {
        return closetime;
    }

    public void setClosetime(Time closetime) {
        this.closetime = closetime;
    }

    @Basic
    @Column(name = "district")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Basic
    @Column(name = "locality")
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Basic
    @Column(name = "mailindex")
    public String getMailindex() {
        return mailindex;
    }

    public void setMailindex(String mailindex) {
        this.mailindex = mailindex;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phonenumber")
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Basic
    @Column(name = "pointtypeid")
    public String getPointtypeid() {
        return pointtypeid;
    }

    public void setPointtypeid(String pointtypeid) {
        this.pointtypeid = pointtypeid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Points points = (Points) o;

        if (pointid != null ? !pointid.equals(points.pointid) : points.pointid != null) return false;
        if (pointname != null ? !pointname.equals(points.pointname) : points.pointname != null) return false;
        if (region != null ? !region.equals(points.region) : points.region != null) return false;
        if (timezone != null ? !timezone.equals(points.timezone) : points.timezone != null) return false;
        if (docs != null ? !docs.equals(points.docs) : points.docs != null) return false;
        if (comments != null ? !comments.equals(points.comments) : points.comments != null) return false;
        if (opentime != null ? !opentime.equals(points.opentime) : points.opentime != null) return false;
        if (closetime != null ? !closetime.equals(points.closetime) : points.closetime != null) return false;
        if (district != null ? !district.equals(points.district) : points.district != null) return false;
        if (locality != null ? !locality.equals(points.locality) : points.locality != null) return false;
        if (mailindex != null ? !mailindex.equals(points.mailindex) : points.mailindex != null) return false;
        if (address != null ? !address.equals(points.address) : points.address != null) return false;
        if (email != null ? !email.equals(points.email) : points.email != null) return false;
        if (phonenumber != null ? !phonenumber.equals(points.phonenumber) : points.phonenumber != null) return false;
        if (pointtypeid != null ? !pointtypeid.equals(points.pointtypeid) : points.pointtypeid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pointid != null ? pointid.hashCode() : 0;
        result = 31 * result + (pointname != null ? pointname.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (timezone != null ? timezone.hashCode() : 0);
        result = 31 * result + (docs != null ? docs.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (opentime != null ? opentime.hashCode() : 0);
        result = 31 * result + (closetime != null ? closetime.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (locality != null ? locality.hashCode() : 0);
        result = 31 * result + (mailindex != null ? mailindex.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phonenumber != null ? phonenumber.hashCode() : 0);
        result = 31 * result + (pointtypeid != null ? pointtypeid.hashCode() : 0);
        return result;
    }
}
