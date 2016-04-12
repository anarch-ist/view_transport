package ru.logistica.tms.dao.usersDao;

import ru.sbat.transport.optimization.location.Point;

public class PointsUser extends PrivelegedUser {
    private Point point;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
