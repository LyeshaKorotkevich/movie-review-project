package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.request.ReviewRequest;
import eu.innowise.moviereviewproject.dto.response.ReviewResponse;
import eu.innowise.moviereviewproject.exceptions.EntityNotFoundException;
import eu.innowise.moviereviewproject.mapper.ReviewMapper;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.ReviewRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.MOVIE_NOT_FOUND_BY_ID;
import static eu.innowise.moviereviewproject.utils.Constants.USER_NOT_FOUND_BY_ID;

@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewMapper reviewMapper;

    private ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.reviewMapper = Mappers.getMapper(ReviewMapper.class);

    }

    private static class SingletonHelper {
        private static final ReviewService INSTANCE = new ReviewService(
                ReviewRepositoryImpl.getInstance(),
                UserRepositoryImpl.getInstance(),
                MovieRepositoryImpl.getInstance()
        );
    }

    public static ReviewService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void saveReview(ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, reviewRequest.userId())));
        Movie movie = movieRepository.findById(reviewRequest.movieId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MOVIE_NOT_FOUND_BY_ID, reviewRequest.movieId())));

        Review review = reviewMapper.toReview(reviewRequest);
        review.setUser(user);
        review.setMovie(movie);

        reviewRepository.save(review);
    }

    public List<ReviewResponse> getAllReviews(int page, UUID movieId) {
        List<ReviewResponse> reviews = reviewRepository.findAll(page, movieId).stream().map(reviewMapper::toResponse).toList();
        if (!reviews.isEmpty()) {
            log.info("Reviews loaded from the database.");
            return reviews;
        }

        return reviews;
    }

    public ReviewResponse getReviewByUserAndMovie(UUID userId, UUID movieId) {
        return reviewRepository.findByUserIdAndMovieId(userId, movieId).map(reviewMapper::toResponse).orElse(null);
    }

    public void updateReview(ReviewRequest reviewRequest, UUID reviewId) {
        User user = userRepository.findById(reviewRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, reviewRequest.userId())));
        Movie movie = movieRepository.findById(reviewRequest.movieId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MOVIE_NOT_FOUND_BY_ID, reviewRequest.movieId())));

        Review review = reviewMapper.toReview(reviewRequest);
        review.setId(reviewId);
        review.setUser(user);
        review.setMovie(movie);
        reviewRepository.update(review);
    }
}
