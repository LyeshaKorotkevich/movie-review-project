<%@ page import="java.util.List" %>
<%@ page import="eu.innowise.moviereviewproject.dto.response.WatchlistResponse" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Список для просмотра</title>
    <link rel="stylesheet" href="<%= request.getContextPath() + "/resources/css/style.css" %>">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body class="bg-light">

<%
    ResourceBundle bundle = ((ResourceBundle) request.getAttribute("bundle"));
%>

<div class="container py-5">
    <h1 class="text-center mb-4">Список для просмотра</h1>

    <%
        List<WatchlistResponse> watchlist = (List<WatchlistResponse>) request.getAttribute("watchlist");
        int currentPage = (int) request.getAttribute("page");
        if (watchlist != null && !watchlist.isEmpty()) {
    %>
    <ul class="list-group">
        <%
            for (WatchlistResponse watchlistMovie : watchlist) {
        %>
        <li class="list-group-item d-flex align-items-center">
            <img src="<%= watchlistMovie.movie().posterUrl() != null ? watchlistMovie.movie().posterUrl() : "https://via.placeholder.com/80x120" %>"
                 alt="Постер" class="img-thumbnail mr-3" style="width: 80px; height: 120px;">

            <div>
                <h5><%= watchlistMovie.movie().title() %></h5>
                <p class="mb-1"><%=bundle.getString("Year")%>: <%= watchlistMovie.movie().releaseYear() %></p>
                <small class="text-muted"><%=bundle.getString("Rating")%>: <%= watchlistMovie.movie().rating() %></small>
                <%
                    if (watchlistMovie.isWatched()) {
                %>
                <p class="text-success"><strong>Фильм просмотрен</strong></p>
                <%
                } else {
                %>
                <p class="text-warning"><strong>Фильм не просмотрен</strong></p>
                <%
                    }
                %>
            </div>

            <div class="ml-auto d-flex">
                <form action="<%= request.getContextPath() + "/watchlist/mark-watched" %>" method="post" style="display: <%= watchlistMovie.isWatched() ? "none" : "inline-block" %>;">
                    <input type="hidden" name="movieId" value="<%= watchlistMovie.movie().id() %>">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-check-circle"></i> Отметить как просмотренное
                    </button>
                </form>

                <form action="<%= request.getContextPath() + "/watchlist/remove" %>" method="post" class="ml-2">
                    <input type="hidden" name="movieId" value="<%= watchlistMovie.movie().id() %>">
                    <button type="submit" class="btn btn-danger">
                        <i class="fas fa-trash-alt"></i> Удалить
                    </button>
                </form>
            </div>

        </li>
        <%
            }
        %>
    </ul>

    <nav aria-label="Навигация по страницам">
        <ul class="pagination justify-content-center">
            <% if (currentPage > 1) { %>
            <li class="page-item">
                <a class="page-link" href="?page=<%= currentPage - 1 %>"><%=bundle.getString("Previous")%></a>
            </li>
            <% } %>
            <li class="page-item">
                <a class="page-link" href="?page=<%= currentPage + 1 %>"><%=bundle.getString("Next")%></a>
            </li>
        </ul>
    </nav>

    <%
    } else {
    %>
    <p class="text-center">Ваш список для просмотра пуст.</p>
    <%
        }
    %>
</div>
</body>
</html>
