package ru.logistica.tms.dao2.docPeriodDao;

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

    @Override
    public String toString() {
        return "Period{" +
                "periodBegin=" + periodBegin +
                ", periodEnd=" + periodEnd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (periodBegin != null ? !periodBegin.equals(period.periodBegin) : period.periodBegin != null) return false;
        return periodEnd != null ? periodEnd.equals(period.periodEnd) : period.periodEnd == null;

    }

    @Override
    public int hashCode() {
        int result = periodBegin != null ? periodBegin.hashCode() : 0;
        result = 31 * result + (periodEnd != null ? periodEnd.hashCode() : 0);
        return result;
    }
}
