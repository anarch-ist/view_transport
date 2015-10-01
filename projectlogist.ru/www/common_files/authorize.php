<div id="content">
	<div id="head-content">
		<h2>Авторизация</h2>
		<?php
		$URI=request_url();
		?>
	</div>
	<div id="authorize">
		<h2>Форма авторизации</h2>
		<p><i>Для получения доступа к содержимому страницы необходимо авторизоваться:</i></p>
		<div id="form">
			<form method="post" action="<?php siteName();?>/auth/login.php">
				<div>
					<input type="hidden" name="from" value="<?php echo $URI; ?>">
					<label for="FIO" class="label"><p>ФИО сотрудника: </p><input type="text" name="FIO" value="" placeholder="ФИО или логин"></label>
					<label for="Password" class="label"><p>пароль: </p><input type="password" name="Password" value="" placeholder="пароль"></label>
				</div>
				<div id="form-submit">
					<input type="submit" value="Войти">
				</div>
			</form>
		</div>
	</div>
	<div id="end-of-content">
	</div>
</div>