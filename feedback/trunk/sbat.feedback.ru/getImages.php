<?php

header("Cache-Control: no-store, no-cache, must-revalidate");
header("Expires: " . date("r"));
$files = array();
session_start();
if (!isset($_COOKIE['PHPSESSID'])) {
    $dir = "thumb";
    if (is_dir($dir)) {
        if ($dd = opendir($dir)) {
            while (($f = readdir($dd)) !== false)
                if ($f != "." && $f != "..")
                    $files[] = $f;
            closedir($dd);
        }
        shuffle($files);
		$_SESSION['files'] = $files;
    }
} else {
	$files = $_SESSION['files'];
}
$n = $_GET["n"];
$response = "";

for ($i = $n; $i < $n + 15; $i++) {
    $response .= $files[$i % count($files)] . ';';
}
echo $response;
?>

