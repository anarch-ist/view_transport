<?php
ob_start();
include 'E:\web\www\sbat.logist.ru\common_files\sessionAndCookieWork\SessionAndCookieWork.php';
$sacw = new SessionAndCookieWork\SessionAndCookieWork();
$sacw->startSession();
$_COOKIE['SESSION_CHECK_STRING'] = md5(session_id() . '1');
$sacw->setSessionParameter('UserID', '1');
$sacw->setSessionParameter('md5', md5('test'));
$_POST['status']='getAllUserRoles';
include 'E:\web\www\sbat.logist.ru\admin_page\content\php_files\getData.php';