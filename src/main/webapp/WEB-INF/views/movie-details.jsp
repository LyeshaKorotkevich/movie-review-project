<%@ page import="eu.innowise.moviereviewproject.model.Movie" %>
<%@ page import="eu.innowise.moviereviewproject.model.Genre" %>
<%@ page import="eu.innowise.moviereviewproject.model.Person" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
        Movie movie = (Movie) request.getAttribute("movie");
        Map<String, List<Person>> filteredPersons = (Map<String, List<Person>>) request.getAttribute("filteredPersons");
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
            <%
                if (filteredPersons != null && !filteredPersons.isEmpty()) {
                    for (Map.Entry<String, List<Person>> entry : filteredPersons.entrySet()) {
                        String profession = entry.getKey();
                        List<Person> persons = entry.getValue();
            %>
            <div class="mb-4">
                <h5 class="text-primary"><%= profession != null ? profession : "Не указана профессия" %></h5>
                <div class="row">
                    <%
                        for (Person person : persons) {
                            String displayName = person.getName() != null ? person.getName() : person.getEnName();
                    %>
                    <div class="col-6 col-sm-4 col-md-3 mb-3 person-card">
                        <div class="card">
                            <img src="<%= person.getPhotoUrl() != null ? person.getPhotoUrl() : "https://via.placeholder.com/100" %>"
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
                    %>
                    <input type="radio" id="star-<%= i %>" name="rating" value="<%= i %>">
                    <label for="star-<%= i %>"><i class="fas fa-star"></i></label>
                    <%
                        }
                    %>
                </div>
            </div>
            <div class="form-group">
                <label for="review">Рецензия:</label>
                <textarea class="form-control" id="review" name="review" rows="5" placeholder="Напишите вашу рецензию здесь..."></textarea>
            </div>
<%--            <input type="hidden" name="movieId" value="<%= movie.getId() %>">--%>
            <button type="submit" class="btn btn-primary">Отправить</button>
        </form>
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
