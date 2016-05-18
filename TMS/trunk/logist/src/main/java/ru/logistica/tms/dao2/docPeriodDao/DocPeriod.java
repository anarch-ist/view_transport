package ru.logistica.tms.dao2.docPeriodDao;

import ru.logistica.tms.dao2.docDao.Doc;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "doc_periods", schema = "public", catalog = "postgres")
@Inheritance(strategy=InheritanceType.JOINED)
public class DocPeriod {
    private Long docPeriodId;
    private Period period;
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

    @Embedded
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
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

        return docPeriodId != null ? docPeriodId.equals(docPeriod.docPeriodId) : docPeriod.docPeriodId == null;

    }

    @Override
    public int hashCode() {
        return docPeriodId != null ? docPeriodId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DocPeriod{" +
                "docPeriodId=" + docPeriodId +
                ", period=" + period +
                ", doc=" + doc +
                '}';
    }
}
