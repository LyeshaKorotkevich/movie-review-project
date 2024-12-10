package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.response.WatchlistResponse;
import eu.innowise.moviereviewproject.exceptions.movie.MovieAlreadyInWatchlist;
import eu.innowise.moviereviewproject.exceptions.movie.MovieNotFoundException;
import eu.innowise.moviereviewproject.exceptions.user.UserNotFoundException;
import eu.innowise.moviereviewproject.mapper.WatchlistMapper;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.model.Watchlist;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.repository.WatchlistRepository;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final WatchlistMapper watchlistMapper;

    public WatchlistService(WatchlistRepository watchlistRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.watchlistMapper = Mappers.getMapper(WatchlistMapper.class);
    }

    public void addMovieToWatchlist(UUID userId, UUID movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        if (watchlistRepository.findByUserIdAndMovieId(userId, movieId).isPresent()) {
            throw new MovieAlreadyInWatchlist("Movie already in watchlist");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setMovie(movie);

        watchlistRepository.save(watchlist);
    }

    public List<WatchlistResponse> getUserWatchlist(UUID userId) {
        return watchlistRepository.findByUserId(userId)
                .stream()
                .map(watchlistMapper::toResponse)
                .toList();
    }

    public void removeMovieFromUserWatchlist(UUID userId, UUID movieId) {
        Watchlist watchlist = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new MovieAlreadyInWatchlist("Movie already in watchlist"));

        watchlistRepository.deleteById(watchlist.getId());
    }
}
