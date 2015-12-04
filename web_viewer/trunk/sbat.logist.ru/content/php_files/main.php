<?php
ob_start();
?>
<!DOCTYPE html>
<html>
<?php
include_once 'common_files/privilegedUser/PrivilegedUser.php';
try {
    $pUser = PrivilegedUser::getInstance();
    include_once 'body.php';
} catch (Exception $ex) {
    header("Location: login/", true, 303);
}
?>
</html>