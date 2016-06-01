package ru.logistica.tms.dao.docDao;

import ru.logistica.tms.dao.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "docs", schema = "public", catalog = "postgres")
public class Doc {
    private Integer docId;
    private String docName;
    private Warehouse warehouse;
    private Set<DocPeriod> docPeriods;

    @Id
    @SequenceGenerator(name="docs_docid_seq", sequenceName="docs_docid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "docs_docid_seq")
    @Column(name = "docid", updatable = false)
    public Integer getDocId() {
        return docId;
    }
    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    @OneToMany(mappedBy = "doc")
    public Set<DocPeriod> getDocPeriods() {
        return docPeriods;
    }
    public void setDocPeriods(Set<DocPeriod> docPeriods) {
        this.docPeriods = docPeriods;
    }

    @ManyToOne
    @JoinColumn(name="warehouseid", nullable = false)
    public Warehouse getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Basic
    @Column(name = "docname", nullable = false, length = 255)
    public String getDocName() {
        return docName;
    }
    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doc doc = (Doc) o;

        return docId != null ? docId.equals(doc.docId) : doc.docId == null;

    }

    @Override
    public int hashCode() {
        return docId != null ? docId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "docId=" + docId +
                ", docName='" + docName + '\'' +
                '}';
    }
}
