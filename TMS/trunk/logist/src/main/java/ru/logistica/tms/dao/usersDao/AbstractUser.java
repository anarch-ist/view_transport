package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.constantsDao.UserRole;

public class AbstractUser {
    private Integer userId;
    private String login;
    private String salt;
    private String passAndSalt;
    private UserRole userRole;
    private String userName;
    private String phoneNumber;
    private String email;
    private String position;

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassAndSalt() {
        return passAndSalt;
    }

    public void setPassAndSalt(String passAndSalt) {
        this.passAndSalt = passAndSalt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "AbstractUser{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", salt='" + salt + '\'' +
                ", passAndSalt='" + passAndSalt + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
