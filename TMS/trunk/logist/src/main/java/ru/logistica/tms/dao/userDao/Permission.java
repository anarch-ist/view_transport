package ru.logistica.tms.dao.userDao;

import javax.persistence.*;

@Entity
@Table(name = "permissions", schema = "public", catalog = "postgres")
public class Permission {
    private PermissionNames permissionId;

    @Id
    @Column(name = "permissionid", nullable = false, length = 32, updatable = false, insertable = false)
    @Enumerated(EnumType.STRING)
    public PermissionNames getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(PermissionNames permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (permissionId != null ? !permissionId.equals(that.permissionId) : that.permissionId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return permissionId != null ? permissionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionId='" + permissionId + '\'' +
                '}';
    }
}
