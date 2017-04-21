<?php
ob_start();
require_once __DIR__ . '/../../common_files/privilegedUser/PrivilegedUser.php';
$privUser = null;
try {
    $privUser = PrivilegedUser::getInstance();
    ?>
    <!DOCTYPE html>
    <html>
    <?php
    require_once __DIR__ . '/documentsBody.php';
    ?>
    </html>
    <?php
} catch (Exception $ex) {
    header("Location: login/", true, 303);
}
?>