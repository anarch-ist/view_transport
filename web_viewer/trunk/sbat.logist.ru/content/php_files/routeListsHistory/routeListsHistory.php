<?php
ob_start();
include_once __DIR__ . '/../../../common_files/privilegedUser/PrivilegedUser.php';
$privUser = null;
try {
    $privUser = PrivilegedUser::getInstance();
    ?>
    <!DOCTYPE html>
    <html>
    <?php
    include_once __DIR__ . '/routeListsHistory.php';
    ?>
    </html>
    <?php
} catch (Exception $ex) {
    header("Location: login/?".$_SERVER['QUERY_STRING'], true, 303);
}
?>