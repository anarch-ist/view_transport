$(document).ready(function () {
    window.localStorage.removeItem("USER_STATUSES");

    $("#loginButton").click(function () {
        var $loginInput = $("#loginInput");
        var login = $loginInput.val();
        var $passwordInput = $("#passwordInput");
        var password = calcMD5($passwordInput.val());

        // Checking for blank fields.
        if (login == "" || password == "") {
            var $inputs = $($loginInput, $passwordInput); // get array of inputs
            $inputs.addClass("login_error");
            $("#loginErrorContainer").text("Пожалуйста заполните все поля");
        } else {
            $.post("content/getData.php", {login: login, password: password},
                function (data) {
                    console.log(data);
                    var result = JSON.parse(data);

                    function handleInvalidLogin() {
                        $('input[type="text"]').css({"border": "2px solid red", "box-shadow": "0 0 3px red"});
                        $('input[type="password"]').css({
                            "border": "2px solid #00F5FF",
                            "box-shadow": "0 0 5px #00F5FF"
                        });
                        data = JSON.parse(data);
                        alert(data.responseCode);
                    }

                    function handleValidLogin() {
                        $("form")[0].reset();
                        $('input[type="text"],input[type="password"]').css({
                            "border": "2px solid #00F5FF",
                            "box-shadow": "0 0 5px #00F5FF"
                        });
                        window.localStorage.setItem("USER_STATUSES", JSON.stringify(result.statuses));
                        document.location = '/';
                    }

                    if (result.responseCode === "Ошибка авторизации - неверные имя пользователя или пароль") {
                        handleInvalidLogin();
                    } else if (result.responseCode === "") {
                        handleValidLogin();
                    } else {
                        alert(data);
                    }
                });
        }
    });
});