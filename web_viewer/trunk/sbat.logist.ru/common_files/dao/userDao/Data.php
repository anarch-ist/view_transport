<?php

namespace DAO;
include_once __DIR__ . '/../entityData/EntityData.php';

class UserData extends EntityData
{
    function canAccessAdminPage() {
        $userRole = $this->getData('userRoleID');
        return !($userRole === "CLIENT_MANAGER" || $userRole === "TEMP_REMOVED");
    }
}

class PermissionsData extends EntityData
{

}