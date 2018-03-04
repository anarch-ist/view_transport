<?php
$img_dir = "img/";
$thumb_dir = "thumb/";
$file = $_FILES["fileToUpload"]["tmp_name"];
$thumb_file = $_FILES["thumbFile"]["tmp_name"];
$target_file = $img_dir . basename($_FILES["fileToUpload"]["name"]);
$target_thumb_file = $thumb_dir . basename($_FILES['thumbFile']["name"]);
$uploadOk = 1;
$imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
$thumbFileType = strtolower(pathinfo($target_thumb_file, PATHINFO_EXTENSION));
// Check if image file is a actual image or fake image
if(isset($_POST["submit"])) {
    $check = getimagesize($file);
    $checkThumb = getimagesize($thumb_file);
    if($check !== false && $checkThumb!==false) {
        echo "File is an image - " . $check["mime"] . ".";
        $uploadOk = 1;
    } else {
        echo "File is not an image.";
        $uploadOk = 0;
    }
}

// Check if file already exists
if (file_exists($target_file) || file_exists($target_thumb_file)) {
    echo "Файл уже существует";
    $uploadOk = 0;
}
// Check file size
if ($_FILES["fileToUpload"]["size"] > 500000 || $_FILES['thumbFile']['size'] > 500000) {
    echo "Не удалось загрузить файл: <br>Файл слишком тяжелый. Вес файла не должен превышать 50мб";
    $uploadOk = 0;
}
// Allow certain file formats
if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
    && $imageFileType != "gif" &&  $thumbFileType !="jpg" && $thumbFileType!="png" && $thumbFileType!="jpeg" && $thumbFileType!="gif") {
    echo "Не удалось загрузить файл: <br>Поддерживается только jpg, jpeg и png. ";
    $uploadOk = 0;
}
// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {
    echo "Не удалось загрузить файл: <br>Ошибка при загрузке файла. Пишите кодеру, пришлите ему ваш файл.";
} else {
// if everything is ok, try to upload file
    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {

        if(move_uploaded_file($_FILES["thumbFile"]["tmp_name"], $target_thumb_file)){
            echo "Файл ". basename( $_FILES["fileToUpload"]["name"]). " был загружен.<br>";
            echo "Файл ". basename( $_FILES["thumbFile"]["name"]). " был загружен.";
            echo "<a href='upload.html'>Назад</a>";
        }


    } else {
        echo "Не удалось загрузить файл: <br>Ошибка при загрузке файла. Пишите кодеру, пришлите ему ваш файл.";
    }
}
?>