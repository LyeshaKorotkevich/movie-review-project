package eu.innowise.moviereviewproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.impl.GenreRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;
import eu.innowise.moviereviewproject.service.ApiService;
import eu.innowise.moviereviewproject.service.MovieService;
import lombok.Getter;

import java.net.http.HttpClient;

public final class ApplicationConfig {

    private ApplicationConfig(){}

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final MovieRepository movieRepository = new MovieRepositoryImpl();
    private static final GenreRepository genreRepository = new GenreRepositoryImpl();

    @Getter
    private static final ApiService apiService = new ApiService(httpClient, objectMapper, movieRepository, genreRepository);

    @Getter
    private static final MovieService movieService = new MovieService(movieRepository, apiService);
}
