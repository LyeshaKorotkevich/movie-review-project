package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MovieService {

    private final MovieRepository movieRepository = new MovieRepositoryImpl();

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(UUID id) {
        return movieRepository.findById(id);
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