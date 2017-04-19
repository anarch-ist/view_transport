<?php
if (isset($_GET['clientId']) & isset($_GET['invoiceNumber'])){
    include_once __DIR__ . '/content/php_files/requestHistory.php';
} elseif (isset($_GET['reqIdExt'])) {
    include_once __DIR__ . '/content/php_files/documents.php';
} else  {
    include_once __DIR__ . '/content/php_files/main.php';
}
?>