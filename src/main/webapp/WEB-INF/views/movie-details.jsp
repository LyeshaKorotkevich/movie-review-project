<%@ page import="eu.innowise.moviereviewproject.model.Movie" %>
<%@ page import="eu.innowise.moviereviewproject.model.Genre" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Детали фильма</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body class="bg-light">
<div class="container py-5">

    <%
        Movie movie = (Movie) request.getAttribute("movie");
        if (movie != null) {
    %>
    <h1 class="text-center mb-4"><%= movie.getTitle() %></h1>

    <div class="row">
        <div class="col-md-6">
            <img src="<%= movie.getPosterUrl() != null ? movie.getPosterUrl() : "https://via.placeholder.com/300x450" %>" class="img-fluid" alt="Постер фильма">
        </div>
        <div class="col-md-6">
            <p><strong>Год:</strong> <%= movie.getReleaseYear() %></p>
            <p><strong>Описание:</strong> <%= movie.getDescription() %></p>
            <p><strong>Рейтинг:</strong> <%= movie.getRating() %></p>
            <p><strong>Жанры:</strong>
                <%
                    if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                        for (Genre genre : movie.getGenres()) {
                %>
                <span class="badge badge-secondary"><%= genre.getName() %></span>
                <%
                    }
                } else {
                %>
                <span>Не указаны</span>
                <%
                    }
                %>
            </p>
        </div>
    </div>

    <a href="<%= request.getContextPath() + "/movies" %>" class="btn btn-primary mt-4">Назад к списку фильмов</a>

    <%
    } else {
    %>
    <p class="text-center">Фильм не найден.</p>
    <%
        }
    %>
</div>
</body>
</html>
