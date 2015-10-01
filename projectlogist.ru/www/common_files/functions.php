<?php

define("EMAIL_REGEX_PATTERN", '/^(?!(?:(?:\\x22?\\x5C[\\x00-\\x7E]\\x22?)|(?:\\x22?[^\\x5C\\x22]\\x22?)){255,})(?!(?:(?:\\x22?\\x5C[\\x00-\\x7E]\\x22?)|(?:\\x22?[^\\x5C\\x22]\\x22?)){65,}@)(?:(?:[\\x21\\x23-\\x27\\x2A\\x2B\\x2D\\x2F-\\x39\\x3D\\x3F\\x5E-\\x7E]+)|(?:\\x22(?:[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x21\\x23-\\x5B\\x5D-\\x7F]|(?:\\x5C[\\x00-\\x7F]))*\\x22))(?:\\.(?:(?:[\\x21\\x23-\\x27\\x2A\\x2B\\x2D\\x2F-\\x39\\x3D\\x3F\\x5E-\\x7E]+)|(?:\\x22(?:[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x21\\x23-\\x5B\\x5D-\\x7F]|(?:\\x5C[\\x00-\\x7F]))*\\x22)))*@(?:(?:(?!.*[^.]{64,})(?:(?:(?:xn--)?[a-z0-9]+(?:-+[a-z0-9]+)*\\.){1,126}){1,}(?:(?:[a-z][a-z0-9]*)|(?:(?:xn--)[a-z0-9]+))(?:-+[a-z0-9]+)*)|(?:\\[(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){7})|(?:(?!(?:.*[a-f0-9][:\\]]){7,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?)))|(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){5}:)|(?:(?!(?:.*[a-f0-9]:){5,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3}:)?)))?(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))(?:\\.(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))){3}))\\]))$/iD');

//return true if valid email
function emailValidation($emailString) {
	return (preg_match(EMAIL_REGEX_PATTERN, $emailString) === 1);
}

function getAccountType($num) {
	switch($num) {
		case 1: {
			return 'администратор';
		}
		case 2: {
			return 'торговый представитель';
		}
		case 3: {
			return 'диспетчер';
		}
		case 4: {
			return 'клиент';
		}
		default: {
			return "ошибка";
		}
	}
}
function request_url() {
	$result.= siteName();
	// А порт у нас по-умолчанию?
	if ($_SERVER["SERVER_PORT"] != $default_port) {
		// Если нет, то добавим порт в URL
		$result.= ":".$_SERVER["SERVER_PORT"];
	}
	// Последняя часть запроса (путь и GET-параметры).
	$result.= $_SERVER["REQUEST_URI"];
	// Уфф, вроде получилось!
	return $result;
}
function siteName() {
	$result = ""; // Пока результат пуст
	$default_port = 80; // Порт по-умолчанию

	// А не в защищенном-ли мы соединении?
	if (isset($_SERVER["HTTPS"]) && ($_SERVER["HTTPS"]=="on")) {
		// В защищенном! Добавим протокол...
		$result.= "https://";
		// ...и переназначим значение порта по-умолчанию
		$default_port = 443;
	} else {
		// Обычное соединение, обычный протокол
		$result.= "http://";
	}
	// Имя сервера, напр. site.com или www.site.com
	$result.= $_SERVER["SERVER_NAME"];
	return $result;
}
function makeMD5Encode($UID, $UTID) {
	return md5($UID.'4'.$UTID);
}
function md5IsEqual() {
	if (!isset($_COOKIE['authInfo'])||$_COOKIE['authInfo']!==makeMD5Encode($_COOKIE['UserID'],$_COOKIE['UserTypeID'])) {
		return false;
	}
	return true;
}
// $minIDs = array('UserID' => '100000000', 'PointID' => '110000000', 'RouteID' => '120000000', 'RoutePointID' => '130000000', 'InvoiceID' => '140000000', 'RequestID' => '150000000', 'InsideRequestID' => '160000000', 'StatusID' => '170000000');
function getMinID($str) {
	$minIDs = array('UserID' => '100000000', 'PointID' => '110000000', 'RouteID' => '120000000', 'RoutePointID' => '130000000', 'InvoiceID' => '140000000', 'RequestID' => '150000000', 'InsideRequestID' => '160000000', 'StatusID' => '170000000', 'RequizitID' => '180000000');
	return $minIDs[$str];
}
$startPage = siteName().'/auth/index.php';
$mysqlparams = array('server' => 'localhost', 'user' => 'andy', 'password' => 'andyandy', 'database' => 'requestdb');
?>