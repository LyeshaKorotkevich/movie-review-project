<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
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
                    <form action="${pageContext.request.contextPath}/auth/register" method="post">
                        <%
                            String userAlreadyExists = (String) request.getAttribute("userAlreadyExists");
                            if (userAlreadyExists != null) {
                        %>
                        <div class="alert alert-danger mt-2">
                            <%= userAlreadyExists %>
                        </div>
                        <%
                            }
                        %>

                        <div class="form-group">
                            <label for="username">Логин</label>
                            <input
                                    type="text"
                                    id="username"
                                    name="username"
                                    class="form-control"
                                    placeholder="Введите логин"
                                    value="<%= request.getAttribute("registrationDTO") != null
                                        ? ((eu.innowise.moviereviewproject.dto.RegistrationDTO) request.getAttribute("registrationDTO")).username()
                                        : "" %>">
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
                            <label for="email">Email</label>
                            <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    class="form-control"
                                    placeholder="Введите ваш email"
                                    value="<%= request.getAttribute("registrationDTO") != null
                                        ? ((eu.innowise.moviereviewproject.dto.RegistrationDTO) request.getAttribute("registrationDTO")).email()
                                        : "" %>">
                            <%
                                if (errors != null && errors.containsKey("email")) {
                            %>
                            <div class="alert alert-danger mt-2"><%= errors.get("email") %></div>
                            <%
                                }
                            %>
                        </div>

                        <div class="form-group">
                            <label for="password">Пароль</label>
                            <input
                                    type="password"
                                    id="password"
                                    name="password"
                                    class="form-control"
                                    placeholder="Введите пароль">
                            <%
                                if (errors != null && errors.containsKey("password")) {
                            %>
                            <div class="alert alert-danger mt-2"><%= errors.get("password") %></div>
                            <%
                                }
                            %>
                        </div>

                        <div class="form-group">
                            <label for="confirmPassword">Подтвердите пароль</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Подтвердите пароль" required>
                            <%
                                if (errors != null && errors.containsKey("confirmPassword")) {
                            %>
                            <div class="alert alert-danger mt-2"><%= errors.get("confirmPassword") %></div>
                            <%
                                }
                            %>
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
