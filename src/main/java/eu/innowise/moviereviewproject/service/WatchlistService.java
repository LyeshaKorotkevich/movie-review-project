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
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.UserRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.WatchlistRepositoryImpl;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final WatchlistMapper watchlistMapper;

    private WatchlistService(WatchlistRepository watchlistRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.watchlistMapper = Mappers.getMapper(WatchlistMapper.class);
    }

    private static class SingletonHelper {
        private static final WatchlistService INSTANCE = new WatchlistService(
                WatchlistRepositoryImpl.getInstance(),
                UserRepositoryImpl.getInstance(),
                MovieRepositoryImpl.getInstance()
        );
    }

    public static WatchlistService getInstance() {
        return SingletonHelper.INSTANCE;
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

    public List<WatchlistResponse> getUserWatchlist(int page, UUID userId) {
        return watchlistRepository.findByUserId(page, userId)
                .stream()
                .map(watchlistMapper::toResponse)
                .toList();
    }

    public void removeMovieFromUserWatchlist(UUID userId, UUID movieId) {
        Watchlist watchlist = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new RuntimeException("Watchlist not found"));

        watchlistRepository.deleteById(watchlist.getId());
    }

    public void markAsWatched(UUID userId, UUID movieId) {
        Watchlist watchlist = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new RuntimeException("Watchlist not found"));

        watchlist.setWatched(true);
        watchlistRepository.update(watchlist);
    }

    public boolean checkIfWatchlistExists(UUID userId, UUID movieId) {
        return watchlistRepository.existsByUserIdAndMovieId(userId, movieId);
    }
}
