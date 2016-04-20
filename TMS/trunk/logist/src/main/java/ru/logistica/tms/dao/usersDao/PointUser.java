package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.pointsDao.Point;

public class PointUser extends PrivelegedUser {
    private Point point;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return super.toString() + ", point='" + point + "\'";
    }
}
