<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.MovieResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.PersonResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.GenreResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.ReviewResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.GenreResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.PersonResponse" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.MovieResponse" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Детали фильма</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="<%= request.getContextPath() + "/resources/css/style.css" %>">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-5">

    <%
        MovieResponse movie = (MovieResponse) request.getAttribute("movie");
        int currentPage = (int) request.getAttribute("page");
        ReviewResponse existingReview = (ReviewResponse) request.getAttribute("existingReview");
        Map<String, List<PersonResponse>> filteredPersons = (Map<String, List<PersonResponse>>) request.getAttribute("filteredPersons");
        if (movie != null) {
    %>
    <h1 class="text-center mb-4"><%= movie.title() %></h1>

    <div class="row">
        <div class="col-md-6">
            <div class="text-center">
                <img src="<%= movie.posterUrl() != null ? movie.posterUrl() : "https://via.placeholder.com/300x450" %>" class="img-fluid" alt="Постер фильма">
                <form action="<%= request.getContextPath() + "/watchlist" %>" method="post" class="mt-4">
                    <input type="hidden" name="movieId" value="<%= movie.id() %>">
                    <button type="submit" class="btn btn-success btn-lg w-100">
                        <i class="fas fa-plus-circle"></i> Добавить в список для просмотра
                    </button>
                </form>
            </div>
        </div>
        <div class="col-md-6">
            <p><strong>Год:</strong> <%= movie.releaseYear() %></p>
            <p><strong>Описание:</strong> <%= movie.description() %></p>
            <p><strong>Рейтинг:</strong> <%= movie.rating() %></p>
            <p><strong>Жанры:</strong>
                <%
                    if (movie.genres() != null && !movie.genres().isEmpty()) {
                        for (GenreResponse genre : movie.genres()) {
                %>
                <span class="badge badge-secondary"><%= genre.name() %></span>
                <%
                    }
                } else {
                %>
                <span>Не указаны</span>
                <%
                    }
                %>
            </p>
            <%
                if (filteredPersons != null && !filteredPersons.isEmpty()) {
                    for (Map.Entry<String, List<PersonResponse>> entry : filteredPersons.entrySet()) {
                        String profession = entry.getKey();
                        List<PersonResponse> persons = entry.getValue();
            %>
            <div class="mb-4">
                <h5 class="text-primary"><%= profession != null ? profession : "Не указана профессия" %></h5>
                <div class="row">
                    <%
                        for (PersonResponse person : persons) {
                            String displayName = person.name() != null ? person.name() : person.enName();
                    %>
                    <div class="col-6 col-sm-4 col-md-3 mb-3 person-card">
                        <div class="card">
                            <img src="<%= person.photoUrl() != null ? person.photoUrl() : "https://via.placeholder.com/100" %>"
                                 class="card-img-top rounded-circle"
                                 style="height: 100px; object-fit: cover;"
                                 alt="<%= displayName %>">
                            <div class="card-body">
                                <h6 class="card-title text-center"><%= displayName %></h6>
                            </div>
                        </div>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
            <%
                }
            } else {
            %>
            <p>Актеры и режиссеры не указаны.</p>
            <%
                }
            %>
        </div>
    </div>

    <div class="mt-5">
        <h3>Оставьте вашу рецензию</h3>
        <form action="<%= request.getContextPath() + "/review" %>" method="post">
            <div class="form-group">
                <label>Рейтинг:</label>
                <div class="star-rating">
                    <%
                        for (int i = 10; i >= 1; i--) {
                            boolean isChecked = (existingReview != null && existingReview.rate() == i);
                    %>
                    <input type="radio" id="star-<%= i %>" name="rating" value="<%= i %>" <%= isChecked ? "checked" : "" %> required>
                    <label for="star-<%= i %>"><i class="fas fa-star"></i></label>
                    <%
                        }
                    %>
                </div>
            </div>
            <div class="form-group">
                <label for="review">Рецензия:</label>
                <textarea class="form-control" id="review" name="review" rows="5" placeholder="Напишите вашу рецензию здесь..."><%= existingReview != null ? existingReview.content() : "" %></textarea>
            </div>
            <%
                if (existingReview != null) {
            %>
                <input type="hidden" name="reviewId" value="<%= existingReview.id() %>">
            <%
                }
            %>
            <input type="hidden" name="movieId" value="<%= movie.id() %>">
            <button type="submit" class="btn btn-primary">Отправить</button>
        </form>
    </div>

    <div class="mt-5">
        <h3>Рецензии:</h3>

        <%
            List<ReviewResponse> reviews = (List<ReviewResponse>) request.getAttribute("reviews");
            if (reviews != null && !reviews.isEmpty()) {
                for (ReviewResponse review : reviews) {
        %>
        <div class="review-item mb-4">
            <div class="card shadow-sm border-light rounded">
                <div class="card-body">
                    <h5 class="card-title d-flex align-items-center">
                        <strong class="text-warning me-3">Рейтинг:</strong>
                        <span class="badge bg-info fs-5"><%= review.rate() %> / 10</span>
                    </h5>

                    <p class="card-text mb-3">
                        <%= review.content() != null ? review.content() : "Нет текста рецензии" %>
                    </p>

                    <footer class="blockquote-footer text-end">
                        <small class="text-muted">Автор: <%= review.user().username() %></small>
                    </footer>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p>Рецензий еще нет.</p>
        <% } %>

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
