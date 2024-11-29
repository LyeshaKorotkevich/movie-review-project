<%@ page import="eu.innowise.moviereviewproject.model.Movie" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Список фильмов</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body class="bg-light">
<div class="container py-5">
    <h1 class="text-center mb-4">Список фильмов</h1>
    <div class="row">
        <%
            List<Movie> movies =
                    (List<Movie>) request.getAttribute("movies");
            int currentPage = (int) request.getAttribute("currentPage");

            if (movies != null && !movies.isEmpty()) {
                for (Movie movie : movies) {
        %>
        <div class="col-md-4 mb-4">
            <div class="card h-100">
                <img src="<%= movie.getPosterUrl() != null ? movie.getPosterUrl() : "https://via.placeholder.com/300x450" %>" class="card-img-top" alt="Постер">
                <div class="card-body">
                    <h5 class="card-title"><%= movie.getTitle() %></h5>
                    <p class="card-text">Год: <%= movie.getReleaseYear() %></p>
                </div>
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
            <% if (currentPage > 1) { %>
            <li class="page-item">
                <a class="page-link" href="?page=<%= currentPage - 1 %>">Предыдущая</a>
            </li>
            <% } %>
            <li class="page-item">
                <a class="page-link" href="?page=<%= currentPage + 1 %>">Следующая</a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>