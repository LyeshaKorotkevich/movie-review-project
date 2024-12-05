<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script>
        function validateForm() {
            const username = document.getElementById("username").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;

            const passwordMismatch = document.getElementById("passwordMismatch");
            const passwordTooShort = document.getElementById("passwordTooShort");
            const usernameTooShort = document.getElementById("usernameTooShort");

            if (password.length < 8) {
                passwordTooShort.style.display = "block";
            } else {
                passwordTooShort.style.display = "none";
            }

            if (username.length < 3) {
                usernameTooShort.style.display = "block";
            } else {
                usernameTooShort.style.display = "none";
            }

            if (password !== confirmPassword) {
                passwordMismatch.style.display = "block";
                return false;
            } else {
                passwordMismatch.style.display = "none";
            }

            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(email)) {
                alert("Пожалуйста, введите корректный email.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Регистрация</h4>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/auth/register" method="post" onsubmit="return validateForm()">
                        <div class="form-group">
                            <label for="username">Логин</label>
                            <input type="text" id="username" name="username" class="form-control" placeholder="Введите логин" required>
                            <div class="alert alert-danger mt-2" id="usernameTooShort" style="display: none;">
                                Имя пользователя должно содержать минимум 3 символа.
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-control" placeholder="Введите ваш email" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Пароль</label>
                            <input type="password" id="password" name="password" class="form-control" placeholder="Введите пароль" required>
                            <div class="alert alert-danger mt-2" id="passwordTooShort" style="display: none;">
                                Пароль должен содержать минимум 8 символов.
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword">Подтвердите пароль</label>
                            <input type="password" id="confirmPassword" class="form-control" placeholder="Подтвердите пароль" required>
                        </div>
                        <div class="alert alert-danger mt-2" id="passwordMismatch" style="display: none;">
                            Пароли не совпадают. Пожалуйста, попробуйте снова.
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Зарегистрироваться</button>
                    </form>
                </div>
            </div>
            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/auth/login">Уже есть аккаунт? Войти</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
