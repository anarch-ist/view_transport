<?php

header("Cache-Control: no-store, no-cache, must-revalidate");
header("Expires: " . date("r"));


$dir = "thumb";
if (is_dir($dir)) {
    if ($dd = opendir($dir)) {
        while (($f = readdir($dd)) !== false)
            if ($f != "." && $f != "..")
                $files[] = $f;

        closedir($dd);
    }
    shuffle($files);


    $n = $_GET["n"];
    $response = "";

    for ($i = $n; $i < $n + 15; $i++) {
        $response = $response . $files[$i] . ';';
    }
    echo $response;
}
?>