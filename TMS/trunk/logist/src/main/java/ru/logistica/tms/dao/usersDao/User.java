package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.constantsDao.UserRole;

public class User {
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
        return "User " +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", salt='" + salt + '\'' +
                ", passAndSalt='" + passAndSalt + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (salt != null ? !salt.equals(that.salt) : that.salt != null) return false;
        if (passAndSalt != null ? !passAndSalt.equals(that.passAndSalt) : that.passAndSalt != null) return false;
        if (userRole != null ? !userRole.equals(that.userRole) : that.userRole != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return position != null ? position.equals(that.position) : that.position == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (passAndSalt != null ? passAndSalt.hashCode() : 0);
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }
}
