package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.request.MovieFilterRequest;
import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.exceptions.EntityNotFoundException;
import eu.innowise.moviereviewproject.mapper.MovieMapper;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.enums.MovieType;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.MOVIE_NOT_FOUND_BY_ID;

@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApiService apiService;
    private final MovieMapper movieMapper;

    private MovieService(MovieRepository movieRepository, ApiService apiService) {
        this.movieRepository = movieRepository;
        this.apiService = apiService;
        this.movieMapper = Mappers.getMapper(MovieMapper.class);
    }

    private static class SingletonHelper {
        private static final MovieService INSTANCE = new MovieService(
                MovieRepositoryImpl.getInstance(),
                ApiService.getInstance()
        );
    }

    public static MovieService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public List<MovieResponse> getAllMovies(int page, int typeNumber) {
        MovieType movieType = MovieType.fromTypeNumber(typeNumber).orElse(null);
        List<MovieResponse> movies = movieRepository
                .findAll(page, movieType)
                .stream()
                .map(movieMapper::toSummaryResponse)
                .toList();
        if (!movies.isEmpty()) {
            log.info("Movies loaded from the database.");
            return movies;
        }

        log.info("No movies found in the database. Fetching from API...");
        try {
            movies = apiService.fetchMoviesFromApi(page, typeNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return movies;
    }

    public List<MovieResponse> searchMovies(int page, String query) {

        String encodedQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);

        try {
            return apiService.fetchMoviesFromSearchFromApi(page, encodedQuery);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MovieResponse getMovieById(UUID id) {
        return movieRepository
                .findById(id)
                .map(movieMapper::toDetailedResponse)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MOVIE_NOT_FOUND_BY_ID, id)));
    }

    public List<MovieResponse> getFilteredMovies(MovieFilterRequest movieFilterRequest) {
        return movieRepository
                .findFilteredMovies(movieFilterRequest)
                .stream()
                .map(movieMapper::toSummaryResponse)
                .toList();
    }

    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void updateMovie(Movie movie) {
        movieRepository.update(movie);
    }

    public void deleteMovie(UUID id) {
        movieRepository.deleteById(id);
    }
}