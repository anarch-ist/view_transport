$(document).ready(function () {
    $("#loginButton").click(function () {
        var $loginInput = $("#loginInput");
        var login = $loginInput.val();
        var $passwordInput = $("#passwordInput");
        var password = $passwordInput.val();

        // Checking for blank fields.
        if (login == "" || password == "") {
            var $inputs = $($loginInput, $passwordInput); // get array of inputs
            $inputs.addClass("login_error");
            $("#loginErrorContainer").text("Пожалуйста заполните все поля");
        } else {
            $.post("login.php", {login: login, password: password},
                function (data) {
                    if (data == 'invalid_login') {
                        $('input[type="text"]').css({"border": "2px solid red", "box-shadow": "0 0 3px red"});
                        $('input[type="password"]').css({
                            "border": "2px solid #00F5FF",
                            "box-shadow": "0 0 5px #00F5FF"
                        });
                        alert(data);
                    } else if (data == 'Email or Password is wrong...!!!!') {
                        $('input[type="text"],input[type="password"]').css({
                            "border": "2px solid red",
                            "box-shadow": "0 0 3px red"
                        });
                        alert(data);
                    } else if (data == 'Successfully Logged in...') {
                        $("form")[0].reset();
                        $('input[type="text"],input[type="password"]').css({
                            "border": "2px solid #00F5FF",
                            "box-shadow": "0 0 5px #00F5FF"
                        });
                        alert(data);
                    } else {
                        alert(data);
                    }
                });
        }
    });
});
