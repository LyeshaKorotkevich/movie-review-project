package eu.innowise.moviereviewproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    private static final String API_URL = "https://api.kinopoisk.dev/v1.4/movie?page=1&limit=12&selectFields=id&selectFields=name&selectFields=description&selectFields=year&selectFields=poster&notNullFields=id&notNullFields=name&notNullFields=description&notNullFields=year&notNullFields=poster.url";
    private static final String API_KEY = "2GM61NX-6RV4E74-KGHP0K9-XEB3D4V";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MovieRepository movieRepository = new MovieRepositoryImpl();

    public List<Movie> fetchMoviesFromApi(int page) throws Exception {
        // FIX or just to concat
        String apiUrlWithPage = API_URL.replace("page=1", "page=" + page);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrlWithPage))
                .header("accept", "application/json")
                .header("X-API-KEY", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Ошибка API: статус " + response.statusCode());
        }

        JsonNode rootNode = objectMapper.readTree(response.body());
        JsonNode moviesNode = rootNode.get("docs");

        List<Movie> movies = new ArrayList<>();
        if (moviesNode != null && moviesNode.isArray()) {
            for (JsonNode movieNode : moviesNode) {
                Movie movie = new Movie();
                movie.setExternalId(movieNode.get("id").asLong());
                movie.setTitle(movieNode.get("name").asText());
                movie.setPosterUrl(movieNode.get("poster").get("url").asText());
                movie.setReleaseYear(movieNode.get("year").asInt());
                movie.setDescription(movieNode.get("description").asText());

                if (!movieRepository.existsByExternalId(movie.getExternalId())) {
                    movieRepository.save(movie);
                }

                movies.add(movie);
            }
        }

        return movies;
    }
}
