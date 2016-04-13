package ru.logistica.tms.dao.usersDao;


public class TimeDiff {
    private short timeDiffId;

    public short getTimeDiffId() {
        return timeDiffId;
    }

    public void setTimeDiffId(short timeDiffId) {
        this.timeDiffId = timeDiffId;
    }

    @Override
    public String toString() {
        return "TimeDiff{" +
                "timeDiffId=" + timeDiffId +
                '}';
    }
}
