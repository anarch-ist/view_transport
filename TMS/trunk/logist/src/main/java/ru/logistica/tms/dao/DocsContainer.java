package ru.logistica.tms.dao;


//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

//@Entity
public class DocsContainer {
    private Serializable containerId;
    private Integer docId;
    private Integer timeDiffId;
    private Date date;
    private DocState docState;

//    @Id
//    @Column(name = "containerId")
    public Serializable getContainerId() {
        return containerId;
    }

    public void setContainerId(Serializable containerId) {
        this.containerId = containerId;
    }

//    @Basic
//    @Column(name = "docId")
    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

//    @Basic
//    @Column(name = "timeDiffId")
    public Integer getTimeDiffId() {
        return timeDiffId;
    }

    public void setTimeDiffId(Integer timeDiffId) {
        this.timeDiffId = timeDiffId;
    }

//    @Basic
//    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

//    @Basic
//    @Column(name = "docState")
    public DocState getDocState() {
        return docState;
    }

    public void setDocState(DocState docState) {
        if(docState == null) {
            this.docState = DocState.FREE;
        }else {
            this.docState = docState;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocsContainer that = (DocsContainer) o;

        if (containerId != null ? !containerId.equals(that.containerId) : that.containerId != null) return false;
        if (docId != null ? !docId.equals(that.docId) : that.docId != null) return false;
        if (timeDiffId != null ? !timeDiffId.equals(that.timeDiffId) : that.timeDiffId != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (docState == that.docState) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = containerId != null ? containerId.hashCode() : 0;
        result = 31 * result + (docId != null ? docId.hashCode() : 0);
        result = 31 * result + (timeDiffId != null ? timeDiffId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (docState != null ? docState.hashCode() : 0);
        return result;
    }
}
