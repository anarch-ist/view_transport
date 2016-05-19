package ru.logistica.tms.dao.docPeriodDao;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Period {
    @Column(name = "periodbegin", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodBegin;

    @Column(name = "periodend", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodEnd;

    protected Period() {}

    public Period(Date periodBegin, Date periodEnd) {
        Objects.requireNonNull(periodBegin);
        Objects.requireNonNull(periodEnd);

        if (periodBegin.after(periodEnd))
            throw new IllegalArgumentException("periodEnd must be greater then periodBegin");
        this.periodBegin = periodBegin;
        this.periodEnd = periodEnd;
    }

    public Date getPeriodBegin() {
        return periodBegin;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    @Override
    public String toString() {
        return "Period{" +
                "periodBegin=" + periodBegin +
                ", periodEnd=" + periodEnd +
                '}';
    }
}
