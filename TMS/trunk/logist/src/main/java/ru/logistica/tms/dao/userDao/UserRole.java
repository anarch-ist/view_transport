package ru.logistica.tms.dao.userDao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_roles", schema = "public", catalog = "postgres")
public class UserRole {

    private UserRoles userRoleId;
    private Set<Permission> permissions;

    @Id
    @Column(name = "userroleid", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    public UserRoles getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(UserRoles userRoleId) {
        this.userRoleId = userRoleId;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "permissions_for_roles", schema = "public", catalog = "postgres",
            joinColumns = {@JoinColumn(name = "userroleid", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "permissionid", nullable = false, updatable = false)})
    public Set<Permission> getPermissions() {
        return permissions;
    }

    @Transient
    public Set<PermissionNames> getPermissionNames() {
        Set<PermissionNames> result = new HashSet<>();
        Set<Permission> permissions = getPermissions();
        for (Permission permission : permissions) {
            result.add(permission.getPermissionId());
        }
        return result;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        if (userRoleId != null ? !userRoleId.equals(userRole.userRoleId) : userRole.userRoleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return userRoleId != null ? userRoleId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleId=" + userRoleId +
                ", permissions=" + permissions +
                '}';
    }
}
