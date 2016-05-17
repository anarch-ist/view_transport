package ru.logistica.tms.dao2.docPeriodDao;

import ru.logistica.tms.dao2.docDao.Doc;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "doc_periods", schema = "public", catalog = "postgres")
@Inheritance(strategy=InheritanceType.JOINED)
public class DocPeriod {
    private Long docPeriodId;
    private Date periodBegin;
    private Date periodEnd;
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
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }

    @Basic
    @Column(name = "periodend", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
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
        return doc != null ? doc.equals(docPeriod.doc) : docPeriod.doc == null;

    }

    @Override
    public int hashCode() {
        int result = docPeriodId != null ? docPeriodId.hashCode() : 0;
        result = 31 * result + (periodBegin != null ? periodBegin.hashCode() : 0);
        result = 31 * result + (periodEnd != null ? periodEnd.hashCode() : 0);
        result = 31 * result + (doc != null ? doc.hashCode() : 0);
        return result;
    }
}
