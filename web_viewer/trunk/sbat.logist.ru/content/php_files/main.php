<?php
ob_start();
?>
<!DOCTYPE html>
<html>
<?php
include_once __DIR__ . '/../../common_files/privilegedUser/PrivilegedUser.php';
$privUser = '';
try {
    $privUser = PrivilegedUser::getInstance();
    include_once __DIR__ . '/body.php';
} catch (Exception $ex) {
    header("Location: login/", true, 303);
}
?>
</html>