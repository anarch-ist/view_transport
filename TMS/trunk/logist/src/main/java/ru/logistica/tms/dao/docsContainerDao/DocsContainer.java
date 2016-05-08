package ru.logistica.tms.dao.docsContainerDao;


import java.util.Date;

public class DocsContainer {
    private Integer containerId;
    private Integer docId;
    private Integer timeDiffId;
    private Date date;
    private DocState docState;

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getTimeDiffId() {
        return timeDiffId;
    }

    public void setTimeDiffId(Integer timeDiffId) {
        this.timeDiffId = timeDiffId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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
