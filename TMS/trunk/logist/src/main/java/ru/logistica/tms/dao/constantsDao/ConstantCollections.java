package ru.logistica.tms.dao.constantsDao;

import java.util.Objects;
import java.util.Set;

public class ConstantCollections {
    private static Set<UserRole> userRoles;
    private static Set<Permission> permissions;
    private static Set<TimeDiff> timeDiffs;
    private static Set<WantStatus> wantStatuses;


    public static Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public static UserRole getUserRoleByUserRoleId(String userRoleId) {
        Objects.requireNonNull(userRoleId, "userRoleId must not be null");
        for (UserRole userRole : userRoles) {
            if (userRole.getUserRoleId().equals(userRoleId))
                return userRole;
        }
        throw new IllegalArgumentException("no userRoleId " + userRoleId + " in collection");
    }

    public static String getUserRoleIdByUserRole(UserRole userRole) {
        Objects.requireNonNull(userRole, "userRole must not be null");
        for (UserRole role : userRoles) {
            if(role.equals(userRole)){
                return role.getUserRoleId();
            }
        }
        throw new IllegalArgumentException("no UserRole " + userRole + " in collection");
    }

    public static void setUserRoles(Set<UserRole> userRoles) {
        ConstantCollections.userRoles = userRoles;
    }

    public static Set<Permission> getPermissions() {
        return permissions;
    }

    public static void setPermissions(Set<Permission> permissions) {
        ConstantCollections.permissions = permissions;
    }

    public static Set<TimeDiff> getTimeDiffs() {
        return timeDiffs;
    }

    public static void setTimeDiffs(Set<TimeDiff> timeDiffs) {
        ConstantCollections.timeDiffs = timeDiffs;
    }

    public static Set<WantStatus> getWantStatuses() {
        return wantStatuses;
    }

    public static void setWantStatuses(Set<WantStatus> wantStatuses) {
        ConstantCollections.wantStatuses = wantStatuses;
    }
}
