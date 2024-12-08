<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Авторизация</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Войти</h4>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/auth/login" method="post">
                        <%
                            String userNotExists = (String) request.getAttribute("userNotExists");
                            if (userNotExists != null) {
                        %>
                        <div class="alert alert-danger mt-2">
                            <%= userNotExists %>
                        </div>
                        <%
                            }
                        %>
                        <div class="form-group">
                            <label for="username">Логин</label>
                            <input type="text" id="username" name="username" class="form-control" placeholder="Введите ваше имя пользователя" required
                                   value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>">
                            <%
                                Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
                                if (errors != null && errors.containsKey("username")) {
                            %>
                            <div class="alert alert-danger mt-2"><%= errors.get("username") %></div>
                            <%
                                }
                            %>
                        </div>

                        <div class="form-group">
                            <label for="password">Пароль</label>
                            <input type="password" id="password" name="password" class="form-control" placeholder="Введите пароль" required>
                            <%
                                if (errors != null && errors.containsKey("password")) {
                            %>
                            <div class="alert alert-danger mt-2"><%= errors.get("password") %></div>
                            <%
                                }
                            %>
                        </div>

                        <button type="submit" class="btn btn-primary btn-block">Войти</button>
                    </form>
                </div>
            </div>
            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/auth/register">Нет аккаунта? Зарегистрироваться</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
