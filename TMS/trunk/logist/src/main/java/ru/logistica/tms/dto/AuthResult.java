package ru.logistica.tms.dto;

import ru.logistica.tms.dao.usersDao.User;

public class AuthResult {
    private int state = -3;
    private User user;
    public AuthResult() {}
    public boolean isSystemError() {return state == -3;}
    public boolean isNoSuchLogin() {return state == -2;}
    public boolean isNoSuchPassword() {return state == -1;}
    public boolean isAuthSuccess() {return state == 0;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public void setNoSuchLogin() {state = -2;}
    public void setNoSuchPassword() {state = -1;}
    public void setAuthSuccess() {state = 0;}
}
