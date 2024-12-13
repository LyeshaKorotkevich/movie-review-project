package eu.innowise.moviereviewproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.innowise.moviereviewproject.repository.ComplaintRepository;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.PersonRepository;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.repository.WatchlistRepository;
import eu.innowise.moviereviewproject.repository.impl.ComplaintRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.GenreRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.PersonRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.ReviewRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.UserRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.WatchlistRepositoryImpl;
import eu.innowise.moviereviewproject.service.ApiService;
import eu.innowise.moviereviewproject.service.AuthenticationService;
import eu.innowise.moviereviewproject.service.ComplaintService;
import eu.innowise.moviereviewproject.service.GenreService;
import eu.innowise.moviereviewproject.service.MovieService;
import eu.innowise.moviereviewproject.service.ReviewService;
import eu.innowise.moviereviewproject.service.UserService;
import eu.innowise.moviereviewproject.service.WatchlistService;
import eu.innowise.moviereviewproject.service.recommendation.RecommendationEngine;
import eu.innowise.moviereviewproject.service.recommendation.RecommendationService;
import lombok.Getter;

import java.net.http.HttpClient;

public final class ApplicationConfig {

    private ApplicationConfig() {
    }

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final MovieRepository movieRepository = new MovieRepositoryImpl();
    private static final GenreRepository genreRepository = new GenreRepositoryImpl();
    private static final PersonRepository personRepository = new PersonRepositoryImpl();
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final ReviewRepository reviewRepository = new ReviewRepositoryImpl();
    private static final WatchlistRepository watchlistRepository = new WatchlistRepositoryImpl();
    private static final ComplaintRepository complaintRepository = new ComplaintRepositoryImpl();

    private static final RecommendationEngine recommendationEngine = new RecommendationEngine();

    @Getter
    private static final ApiService apiService = new ApiService(httpClient, objectMapper, movieRepository, personRepository, genreRepository);

    @Getter
    private static final MovieService movieService = new MovieService(movieRepository, apiService);

    @Getter
    private static final GenreService genreService = new GenreService(genreRepository);

    @Getter
    private static final UserService userService = new UserService(userRepository);

    @Getter
    private static final AuthenticationService authenticationService = new AuthenticationService(userRepository);

    @Getter
    private static final ReviewService reviewService = new ReviewService(reviewRepository, userRepository, movieRepository);

    @Getter
    private static final WatchlistService watchlistService = new WatchlistService(watchlistRepository, userRepository, movieRepository);

    @Getter
    private static final RecommendationService recommendationService = new RecommendationService(movieRepository, userRepository, reviewRepository, recommendationEngine);

    @Getter
    private static final ComplaintService complaintService = new ComplaintService(complaintRepository, userRepository, reviewRepository);
}
