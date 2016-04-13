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

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserRoleRusName() {
        return userRoleRusName;
    }

    public void setUserRoleRusName(String userRoleRusName) {
        this.userRoleRusName = userRoleRusName;
    }
}
