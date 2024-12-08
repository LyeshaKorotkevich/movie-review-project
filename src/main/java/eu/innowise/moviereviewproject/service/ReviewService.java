package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.MovieDTO;
import eu.innowise.moviereviewproject.dto.UserDTO;
import eu.innowise.moviereviewproject.dto.request.ReviewRequest;
import eu.innowise.moviereviewproject.mapper.MovieMapper;
import eu.innowise.moviereviewproject.mapper.ReviewMapper;
import eu.innowise.moviereviewproject.mapper.UserMapper;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import org.mapstruct.factory.Mappers;

public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.reviewMapper = Mappers.getMapper(ReviewMapper.class);

    }

    public void saveReview(ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.userId()).orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(reviewRequest.movieId()).orElseThrow(() -> new RuntimeException("Movie not found"));

        Review review = reviewMapper.toReview(reviewRequest);
        review.setUser(user);
        review.setMovie(movie);

        reviewRepository.save(review);
    }
}
