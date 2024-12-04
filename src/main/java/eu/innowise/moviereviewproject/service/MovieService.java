package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.exceptions.movie.MovieNotFoundException;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApiService apiService;

    public MovieService(MovieRepository movieRepository, ApiService apiService) {
        this.movieRepository = movieRepository;
        this.apiService = apiService;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getAllMovies(int page, int typeNumber) {
        List<Movie> movies = movieRepository.findAll(page, typeNumber);
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

    public List<Movie> searchMovies(int page, String query) {

        String encodedQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);

        try {
            return apiService.fetchMoviesFromSearchFromApi(page, encodedQuery);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Movie getMovieById(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found"));
    }

    public List<Movie> getFilteredMovies(int page, Integer typeNumber, String genre, Integer startYear, Integer endYear, Integer minRating, Integer maxRating) {
        log.info("Fetching filtered movies: page={}, typeNumber={}, genre={}, startYear={}, endYear={}, minRating={}, maxRating={}",
                page, typeNumber, genre, startYear, endYear, minRating, maxRating);

        return movieRepository.findFilteredMovies(page, typeNumber, genre, startYear, endYear, minRating, maxRating);
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