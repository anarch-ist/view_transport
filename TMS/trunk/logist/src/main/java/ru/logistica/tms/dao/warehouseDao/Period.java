package ru.logistica.tms.dao.warehouseDao;

import ru.logistica.tms.dao.Binding;
import ru.logistica.tms.dao.donutsDao.Donut;

import javax.xml.ws.BindingType;
import java.sql.Timestamp;

public class Period {
    private Long periodId;
    private Doc doc;
    private Timestamp periodBegin;
    private Timestamp periodEnd;
    private State state;
    private Donut donut;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    public Timestamp getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Timestamp periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Timestamp getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Timestamp periodEnd) {
        this.periodEnd = periodEnd;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Donut getDonut() {
        return donut;
    }

    public void setDonut(Donut donut) {
        this.donut = donut;
    }

    @Binding(sqlObject = "period_states")
    enum State {
        OPENED("OPENED"), CLOSED("CLOSED"), OCCUPIED("OCCUPIED");
        private String name;
        State(String name) {this.name = name;}
        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public String toString() {
        return "Period{" +
                "periodId=" + periodId +
                ", doc=" + doc +
                ", periodBegin=" + periodBegin +
                ", periodEnd=" + periodEnd +
                ", state=" + state +
                ", donut=" + donut +
                '}';
    }
}

