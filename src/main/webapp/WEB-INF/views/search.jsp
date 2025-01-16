<%@ page import="java.util.List" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.MovieResponse" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Результаты поиска</title>
    <link rel="stylesheet" href="<%= request.getContextPath() + "/resources/css/style.css" %>">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body class="bg-light">

<%
    ResourceBundle bundle = ((ResourceBundle) request.getAttribute("bundle"));
%>

<div class="container py-5">
    <h1 class="text-center mb-4">Результаты поиска</h1>

    <%
        String query = request.getParameter("query");
        List<MovieResponse> movies = (List<MovieResponse>) request.getAttribute("movies");
        int currentPage = (int) request.getAttribute("currentPage");

        if (movies != null && !movies.isEmpty()) {
    %>
    <ul class="list-group">
        <%
            for (MovieResponse movie : movies) {
        %>
        <li class="list-group-item d-flex align-items-center">
            <img src="<%= movie.posterUrl() != null ? movie.posterUrl() : "https://via.placeholder.com/80x120" %>"
                 alt="Постер" class="img-thumbnail mr-3" style="width: 80px; height: 120px;">
            <div>
                <h5><%= movie.title() %></h5>
                <p class="mb-1"><%=bundle.getString("Year")%>: <%= movie.releaseYear() %></p>
                <small class="text-muted"><%=bundle.getString("Rating")%>: <%= movie.rating() %></small>
            </div>
            <a href="<%= request.getContextPath() + "/movies/" + movie.id() %>" class="btn btn-link ml-auto">Подробнее</a>
        </li>
        <%
            }
        %>
    </ul>

    <nav aria-label="Навигация по страницам">
        <ul class="pagination justify-content-center">
            <% if (currentPage > 1) { %>
            <li class="page-item">
                <a class="page-link" href="?page=<%= currentPage - 1 %>&query=<%= query %>"><%=bundle.getString("Previous")%></a>
            </li>
            <% } %>
            <li class="page-item">
                <a class="page-link" href="?page=<%= currentPage + 1 %>&query=<%= query %>"><%=bundle.getString("Next")%></a>
            </li>
        </ul>
    </nav>

    <%
    } else {
    %>
    <p class="text-center">По вашему запросу "<%= query %>" ничего не найдено.</p>
    <%
        }
    %>
</div>
</body>
</html>
