<?php
	ob_start(); 
	$isAuth=false;
	if (isset($_COOKIE["UserTypeID"])||isset($_COOKIE["UserID"])) {
		$isAuth=true;
	}

?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>Помощь</title>
	<link rel="stylesheet" type="text/css" href="./style.css">
</head>
<body>
	<div id="container">
		<div id="top-page">
			<?php
				include_once "../common_files/top-page.php";
			?>
		</div>
		<?php
			if ($isAuth) {
				include_once "content.php";
			}
			else {
				include_once "../common_files/authorize.php";
			}
		?>
		<div id="foot-page"><?php include_once '../common_files/foot-page.php'; ?></div>
	</div>
</body>
</html>