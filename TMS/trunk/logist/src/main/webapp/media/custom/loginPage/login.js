$(document).ready(function() {
    "use strict";

    var MIN_PASSWORD_LENGTH = 6;
    var $loginInput = $("#userLoginId");
    var $loginError = $("#loginError");
    var $passwordInput = $("#userPasswordId");
    var $passwordError = $("#passwordError");
    var $inputs = $($loginInput).add($passwordInput);
    var $inputErrors = $loginError.add($passwordError);
    var $confirmLoginBtn = $('#buttonLogIn');

    // if enter pressed then submit
    $inputs.on("keypress", function (e) {
        if (e.which === 13) {
            $confirmLoginBtn.click();
        } else {
            $inputErrors.text("");
        }
    });

    $confirmLoginBtn.on("click", function (e) {
        e.preventDefault();
        if (checkFields()) {
            $.ajax({
                url: "login",
                method: "POST",
                data: {login: $loginInput.val(), password: calcMD5($passwordInput.val())},
                dataType: "json"
            }).done(function (data) {
                if (data.redirect) {
                    window.location.href = data.redirect;
                } else {
                    $loginError.text(data.loginErrorText);
                    $passwordError.text(data.passwordErrorText);
                }
            }).fail(function () {
                window.alert("error");
            });
        }
    });

    // return true or false
    function checkFields() {
        var loginValue = $loginInput.val();
        if (!loginValue) {
            $loginError.text("Введите логин");
            $loginInput.focus();
            return false;
        }
        var passwordValue = $passwordInput.val();
        if (!passwordValue) {
            $passwordError.text("Введите пароль");
            $passwordInput.focus();
            return false;
        } else if (passwordValue.length < MIN_PASSWORD_LENGTH) {
            $passwordError.text("Минимальная длина пароля - " + MIN_PASSWORD_LENGTH + " символов");
            $passwordInput.focus();
            return false;
        }
        return true;
    }

    $inputs.addClass("idleField");
    $inputs.focus(function() {
        $(this).removeClass("idleField").addClass("focusField");
        if (this.value === this.defaultValue){
            this.value = '';
        }
        if(this.value !== this.defaultValue){
            this.select();
        }
    });
    $inputs.blur(function() {
        $(this).removeClass("focusField").addClass("idleField");
    });

});