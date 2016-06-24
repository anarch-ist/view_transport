package ru.logistica.tms.dao.docPeriodDao;

import ru.logistica.tms.dao.docDao.Doc;

import javax.persistence.*;

@Entity
@Table(name = "doc_periods", schema = "public")
@Inheritance(strategy=InheritanceType.JOINED)
public class DocPeriod {
    private Long docPeriodId;
    private Period period;
    private Doc doc;

    @Id
    @SequenceGenerator(name="doc_periods_docperiodid_seq", sequenceName="doc_periods_docperiodid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "doc_periods_docperiodid_seq")
    @Column(name = "docperiodid", updatable = false)
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
    @JoinColumn(name="docid", nullable = false)
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
                '}';
    }
}
