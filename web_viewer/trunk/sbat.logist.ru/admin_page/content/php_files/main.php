<?php
ob_start();
include_once __DIR__ . '/../../../common_files/privilegedUser/PrivilegedUser.php';
$privUser = null;
try {
    $privUser = PrivilegedUser::getInstance();
    if (!$privUser->getUserInfo()->canAccessAdminPage()) {
        header("Location: ../", true, 303);
        return;
    }
    ?>
    <!DOCTYPE html>
    <html>
    <?php
    include_once __DIR__ . '/bootstrapBody.php';
    ?>
    </html>
<?php
} catch (Exception $ex) {
    header("Location: ../login/", true, 303);
}
?>