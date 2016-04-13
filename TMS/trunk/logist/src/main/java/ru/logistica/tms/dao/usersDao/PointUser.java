package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.Point;

public class PointUser extends PrivelegedUser {
    private Point point;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
