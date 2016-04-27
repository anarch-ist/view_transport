package ru.logistica.tms.dao.usersDao;

import java.util.Set;

public class PrivelegedUser extends User {
    private Set<Privelege> privelege;

    public Set<Privelege> getPrivelege() {
        return privelege;
    }

    public void setPrivelege(Set<Privelege> privelege) {
        this.privelege = privelege;
    }
}
