<?php
ob_start();
setcookie('SESSION_CHECK_STRING',null,-1,'/');
session_unset();
setcookie(session_name(), session_id(),-1,'/');
?>
<!DOCTYPE html>
<html>
<?php
include_once __DIR__.'/body.php';
?>
</html>