package ru.logistica.tms.dao2.docPeriodDao;

import ru.logistica.tms.dao2.docDao.Doc;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "doc_periods", schema = "public", catalog = "postgres")
@Inheritance(strategy=InheritanceType.JOINED)
public class DocPeriod {
    private Long docPeriodId;
    private Timestamp periodBegin;
    private Timestamp periodEnd;
    private String periodState;
    private Doc doc;

    @Id
    @Column(name = "docperiodid", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long getDocPeriodId() {
        return docPeriodId;
    }

    public void setDocPeriodId(Long docPeriodId) {
        this.docPeriodId = docPeriodId;
    }

    @Basic
    @Column(name = "periodbegin", nullable = false)
    public Timestamp getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Timestamp periodBegin) {
        this.periodBegin = periodBegin;
    }

    @Basic
    @Column(name = "periodend", nullable = false)
    public Timestamp getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Timestamp periodEnd) {
        this.periodEnd = periodEnd;
    }

    @Basic
    @Column(name = "periodstate", nullable = false, length = 32)
    public String getPeriodState() {
        return periodState;
    }

    public void setPeriodState(String periodState) {
        this.periodState = periodState;
    }

    @ManyToOne
    @JoinColumn(name="docid")
    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocPeriod docPeriod = (DocPeriod) o;

        if (docPeriodId != null ? !docPeriodId.equals(docPeriod.docPeriodId) : docPeriod.docPeriodId != null)
            return false;
        if (periodBegin != null ? !periodBegin.equals(docPeriod.periodBegin) : docPeriod.periodBegin != null)
            return false;
        if (periodEnd != null ? !periodEnd.equals(docPeriod.periodEnd) : docPeriod.periodEnd != null) return false;
        if (periodState != null ? !periodState.equals(docPeriod.periodState) : docPeriod.periodState != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = docPeriodId != null ? docPeriodId.hashCode() : 0;
        result = 31 * result + (periodBegin != null ? periodBegin.hashCode() : 0);
        result = 31 * result + (periodEnd != null ? periodEnd.hashCode() : 0);
        result = 31 * result + (periodState != null ? periodState.hashCode() : 0);
        return result;
    }


}
