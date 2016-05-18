package ru.logistica.tms.dao2.userDao;

import ru.logistica.tms.dao2.Binding;

@Binding(sqlObject = "user_roles")
public enum UserRoles {
    WH_BOSS, WH_DISPATCHER, SUPPLIER_MANAGER
}
