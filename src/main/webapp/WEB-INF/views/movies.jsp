<%@ page import="java.util.List" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.GenreResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.UserResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.MovieResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.GenreResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.MovieResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.UserResponse" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Список фильмов</title>
    <link rel="stylesheet" href="<%= request.getContextPath() + "/resources/css/style.css" %>">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body class="bg-light">
<div class="container py-3">
    <header class="mb-4">
        <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav mr-auto">
                    <%
                        String currentType = request.getParameter("typeNumber");
                        if(currentType == null) currentType = "0";
                    %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/recommendations">Для тебя</a>
                    </li>
                    <li class="nav-item <%= "0".equals(currentType) ? "active" : "" %>">
                        <a class="nav-link" href="?typeNumber=0">Все</a>
                    </li>
                    <li class="nav-item <%= "1".equals(currentType) ? "active" : "" %>">
                        <a class="nav-link" href="?typeNumber=1">Фильмы</a>
                    </li>
                    <li class="nav-item <%= "2".equals(currentType) ? "active" : "" %>">
                        <a class="nav-link" href="?typeNumber=2">Сериалы</a>
                    </li>
                    <li class="nav-item <%= "3".equals(currentType) ? "active" : "" %>">
                        <a class="nav-link" href="?typeNumber=3">Мультфильмы</a>
                    </li>
                    <li class="nav-item <%= "4".equals(currentType) ? "active" : "" %>">
                        <a class="nav-link" href="?typeNumber=4">Аниме</a>
                    </li>
                    <li class="nav-item <%= "5".equals(currentType) ? "active" : "" %>">
                        <a class="nav-link" href="?typeNumber=5">Анимационные сериалы</a>
                    </li>
                </ul>
                <form class="form-inline my-2 my-lg-0" action="<%= request.getContextPath() %>/movies/search" method="get">
                    <input class="form-control mr-sm-2" type="search" name="query" placeholder="Поиск фильмов..." aria-label="Search">
                    <button class="btn btn-outline-primary my-2 my-sm-0" type="submit">Искать</button>
                </form>

                <ul class="navbar-nav ml-auto">
                    <%
                        UserResponse user = (UserResponse) session.getAttribute("user");
                        if (user != null) {
                    %>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <%= user.username() %>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="<%= request.getContextPath() %>/watchlist">Список для просмотра</a>
                            <a class="dropdown-item" href="<%= request.getContextPath() %>/auth/logout">Выйти</a>
                        </div>
                    </li>
                    <%
                    } else {
                    %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/auth/login">Войти</a>
                    </li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </nav>
    </header>

    <div class="card mb-4">
        <div class="card-header bg-primary text-white">
            Фильтры поиска
        </div>
        <div class="card-body">
            <form class="form-inline d-flex flex-wrap justify-content-start" action="<%= request.getContextPath() %>/movies" method="get" style="gap: 15px;">
                <div class="form-group">
                    <label for="genre" class="mr-2">Жанр</label>
                    <select id="genre" class="form-control form-control-sm" name="genre">
                        <option value="">Все жанры</option>
                        <%
                            List<GenreResponse> genres = (List<GenreResponse>) request.getAttribute("genres");
                            if (genres != null) {
                                for (GenreResponse genre : genres) {
                        %>
                        <option value="<%= genre.name() %>"><%= genre.name() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label class="mr-2">Год выпуска</label>
                    <input type="number" class="form-control form-control-sm" name="startYear" placeholder="От" style="width: 80px;">
                    <input type="number" class="form-control form-control-sm" name="endYear" placeholder="До" style="width: 80px;">
                </div>

                <div class="form-group">
                    <label class="mr-2">Рейтинг</label>
                    <input type="number" class="form-control form-control-sm" name="minRating" placeholder="От" style="width: 80px;">
                    <input type="number" class="form-control form-control-sm" name="maxRating" placeholder="До" style="width: 80px;">
                </div>
                <input type="hidden" name="typeNumber" value="${param.typeNumber}"/>
                <button class="btn btn-primary btn-sm">Применить</button>
            </form>
        </div>
    </div>

    <div class="row">
        <%
            List<MovieResponse> movies = (List<MovieResponse>) request.getAttribute("movies");
            int currentPage = (int) request.getAttribute("currentPage");
            if (movies != null && !movies.isEmpty()) {
                for (MovieResponse movie : movies) {
        %>
        <div class="col-md-4 mb-4">
            <div class="card h-100">
                <a href="<%= request.getContextPath() + "/movies/" + movie.id() %>" class="movie-link">
                    <img src="<%= movie.posterUrl() != null ? movie.posterUrl() : "https://via.placeholder.com/300x450" %>" class="card-img-top" alt="Постер">
                    <div class="card-body">
                        <h5 class="card-title text-truncate"><%= movie.title() %></h5>
                        <p class="card-text">Год: <%= movie.releaseYear() %></p>
                        <small class="text-muted">Рейтинг: <%= movie.rating() %></small>
                    </div>
                </a>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p class="text-center">Нет доступных фильмов.</p>
        <%
            }
        %>
    </div>

    <nav aria-label="Навигация по страницам">

    <ul class="pagination justify-content-center">
        <%
            StringBuilder filterUrl = new StringBuilder();

            filterUrl.append("&typeNumber=").append(currentType);

            if (request.getParameter("genre") != null && !request.getParameter("genre").isEmpty()) {
                filterUrl.append("&genre=").append(request.getParameter("genre"));
            }
            if (request.getParameter("startYear") != null && !request.getParameter("startYear").isEmpty()) {
                filterUrl.append("&startYear=").append(request.getParameter("startYear"));
            }
            if (request.getParameter("endYear") != null && !request.getParameter("endYear").isEmpty()) {
                filterUrl.append("&endYear=").append(request.getParameter("endYear"));
            }
            if (request.getParameter("minRating") != null && !request.getParameter("minRating").isEmpty()) {
                filterUrl.append("&minRating=").append(request.getParameter("minRating"));
            }
            if (request.getParameter("maxRating") != null && !request.getParameter("maxRating").isEmpty()) {
                filterUrl.append("&maxRating=").append(request.getParameter("maxRating"));
            }
        %>
        <% if (currentPage > 1) { %>
        <li class="page-item">
            <a class="page-link" href="?page=<%= currentPage - 1 %><%= filterUrl%>">Предыдущая</a>
        </li>
        <% } %>
        <li class="page-item">
            <a class="page-link" href="?page=<%= currentPage + 1 %><%= filterUrl%>">Следующая</a>
        </li>
    </ul>

    </nav>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"></script>
</body>
</html>