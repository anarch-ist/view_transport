package ru.logistica.tms.dao.usersDao;

public class UserRole {
    private String userRoleId;
    private String userRoleRusName;

    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleId='" + userRoleId + '\'' +
                ", userRoleRusName='" + userRoleRusName + '\'' +
                '}';
    }
}
