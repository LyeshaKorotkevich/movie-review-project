<%@ page import="java.util.List" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.MovieResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.UserResponse" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Рекомендации</title>
    <link rel="stylesheet" href="<%= request.getContextPath() + "/resources/css/style.css" %>">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body class="bg-light">
<div class="container py-3">

    <div class="row">
        <%
            List<MovieResponse> recommendedMovies = (List<MovieResponse>) request.getAttribute("recommendations");
            if (recommendedMovies != null && !recommendedMovies.isEmpty()) {
                for (MovieResponse movie : recommendedMovies) {
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
        <p class="text-center">Нет доступных рекомендаций.</p>
        <%
            }
        %>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"></script>
</body>
</html>
