<?php
include_once "functions.php";
$currentSite=siteName();
echo"			<h1><a href=\"$currentSite\">".substr_replace($currentSite,"", 0,strpos($currentSite,"//")+2).'</a></h1>';
?>
	<style>
		div.droppedMenu {
			position: absolute;
			top: 26px;
			left: 0px;
			display: none;
			min-width: 100%;
		}
		li:hover div.droppedMenu {
			display: inline-block;
		}
		div.droppedMenu li {
			display: block;
			cursor: pointer;
		}
		#top-page div.droppedMenu ul {
			background-color: rgba(149, 223, 93, 0.95);
			border-top: none;
		}
		li.withDroppedMenu {
			position: relative;
		}
	</style>
<?php
echo"\n			<ul>\n";
if (isset($_COOKIE["UserID"])&&isset($_COOKIE["UserTypeID"])&&md5IsEqual()) {
	switch ($_COOKIE["UserTypeID"]) {
		case '0001': {
			?>
			<li class="withDroppedMenu">
				<a>создать</a>
				<div class="droppedMenu">
					<ul>
						<li><a href="<?php echo "$currentSite/admin/route_creating/"?>">маршрут</a></li>
						<li><a href="<?php echo "$currentSite/admin/new_user/"?>">пользователя</a></li>
						<li><a href="<?php echo "$currentSite/admin/point_creating/"?>">пункт</a></li>
					</ul>
				</div>
			<li class="withDroppedMenu">
				<a>редактировать</a>
				<div class="droppedMenu">
					<ul>
						<li><a href="<?php echo "$currentSite/admin/invoices/"?>">накладные</a></li>
						<li><a href="<?php echo "$currentSite/admin/routes/"?>">маршрут</a></li>
						<li><a href="<?php echo "$currentSite/admin/users/"?>">пользователя</a></li>
						<li><a href="<?php echo "$currentSite/admin/points/"?>">пункт</a></li>
					</ul>
				</div>
			</li>
			<?php
			break;
		}
		case '0002': {
			echo "				<li><a href=\"$currentSite/dispatcher/incoming/\">прибытие</a></li>\n";
			break;
		}
		case '0003': {
			echo "				<li><a href=\"$currentSite/marketman/invoices/\">работа с документами</a></li>\n";
			echo "				<li><a href=\"$currentSite/invoice_history/\">история</a></li>\n";
			break;
		}
		case '0004': {
			echo "				<li><a href=\"$currentSite/user/incoming/\">накладные в пути</a></li>\n";
			echo "				<li><a href=\"$currentSite/invoice_history/\">история</a></li>\n";
			break;
		}
	}
	echo "				<li><a href=\"$currentSite/profile\">учетная запись</a></li>\n";
	echo "				<li><a href=\"$currentSite/help\">помощь</a></li>\n";
	echo "				<li><a href=\"$currentSite/auth/logoff.php\">выход</a></li>\n";
}
else {
	echo"				<li><a href=\"$currentSite/auth/\">вход</a></li>\n";
}
echo"			</ul>";
?>