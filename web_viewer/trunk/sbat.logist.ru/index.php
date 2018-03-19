<?php
if (isset($_GET['clientId']) & isset($_GET['invoiceNumber'])){
    include_once __DIR__ . '/content/php_files/requestHistory.php';
} elseif (isset($_GET['reqIdExt'])) {
    include_once __DIR__ . '/content/php_files/documents.php';
} elseif (isset($_GET['map'])) {
    include_once __DIR__ . '/content/php_files/map.php';
} elseif (isset($_GET['routeLists'])) {
    include_once __DIR__ . '/content/php_files/routeLists/routeLists.php';
} elseif (isset($_GET['routeListId'])) {
    include_once __DIR__ . '/content/php_files/routeList/routeList.php';
} elseif (isset($_GET['vMap'])) {
    include_once __DIR__ . '/content/php_files/vehiclesMap/vMap.php';
}elseif (isset($_GET['TCPage'])) {
    include_once __DIR__ . '/content/php_files/transportCompanyPage/transportCompanyPageBody.php';
} else  {
    include_once __DIR__ . '/content/php_files/main.php';
}
?>