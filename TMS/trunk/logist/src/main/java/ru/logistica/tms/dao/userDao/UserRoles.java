package ru.logistica.tms.dao.userDao;

import ru.logistica.tms.dao.Binding;

@Binding(sqlObject = "user_roles")
public enum UserRoles {
    WH_BOSS, WH_DISPATCHER, SUPPLIER_MANAGER
}
