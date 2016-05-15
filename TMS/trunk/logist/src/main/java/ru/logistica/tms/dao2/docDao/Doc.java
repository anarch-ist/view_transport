package ru.logistica.tms.dao2.docDao;

import ru.logistica.tms.dao2.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao2.warehouseDao.Warehouse;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "docs", schema = "public", catalog = "postgres")
public class Doc {
    private Integer docId;
    private String docName;
    private Warehouse warehouse;

    @OneToMany(mappedBy = "doc")
    public Set<DocPeriod> getDocPeriods() {
        return docPeriods;
    }

    public void setDocPeriods(Set<DocPeriod> docPeriods) {
        this.docPeriods = docPeriods;
    }

    private Set<DocPeriod> docPeriods;


    @Id
    @Column(name = "docid", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getDocId() {
        return docId;
    }

    @ManyToOne
    @JoinColumn(name="warehouseid")
    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
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

        Doc docs = (Doc) o;

        if (docId != null ? !docId.equals(docs.docId) : docs.docId != null) return false;
        if (docName != null ? !docName.equals(docs.docName) : docs.docName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = docId != null ? docId.hashCode() : 0;
        result = 31 * result + (docName != null ? docName.hashCode() : 0);
        return result;
    }
}
