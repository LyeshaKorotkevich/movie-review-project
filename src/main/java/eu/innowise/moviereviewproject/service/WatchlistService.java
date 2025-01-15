package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.response.WatchlistResponse;
import eu.innowise.moviereviewproject.exceptions.EntityNotFoundException;
import eu.innowise.moviereviewproject.exceptions.movie.MovieAlreadyInWatchlist;
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

import static eu.innowise.moviereviewproject.utils.Constants.MOVIE_ALREADY_IN_WATCHLIST;
import static eu.innowise.moviereviewproject.utils.Constants.MOVIE_NOT_FOUND_BY_ID;
import static eu.innowise.moviereviewproject.utils.Constants.MOVIE_NOT_IN_WATCHLIST;
import static eu.innowise.moviereviewproject.utils.Constants.USER_NOT_FOUND_BY_ID;

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
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId)));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MOVIE_NOT_FOUND_BY_ID, movieId)));

        if (watchlistRepository.findByUserIdAndMovieId(userId, movieId).isPresent()) {
            throw new MovieAlreadyInWatchlist(String.format(MOVIE_ALREADY_IN_WATCHLIST, movieId, userId));
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
                .orElseThrow(() -> new EntityNotFoundException(String.format(MOVIE_NOT_IN_WATCHLIST, movieId, userId)));

        watchlistRepository.deleteById(watchlist.getId());
    }

    public void markAsWatched(UUID userId, UUID movieId) {
        Watchlist watchlist = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MOVIE_NOT_IN_WATCHLIST, movieId, userId)));

        watchlist.setWatched(true);
        watchlistRepository.update(watchlist);
    }

    public boolean checkIfWatchlistExists(UUID userId, UUID movieId) {
        return watchlistRepository.existsByUserIdAndMovieId(userId, movieId);
    }
}
