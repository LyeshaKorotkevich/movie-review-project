package eu.innowise.moviereviewproject.service.recommendation;

import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.exceptions.movie.MovieNotFoundException;
import eu.innowise.moviereviewproject.mapper.IdMapper;
import eu.innowise.moviereviewproject.mapper.MovieMapper;
import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.ReviewRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.UserRepositoryImpl;
import eu.innowise.moviereviewproject.utils.Constants;
import eu.innowise.moviereviewproject.utils.CsvExporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class RecommendationService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final RecommendationEngine recommendationEngine;
    private final MovieMapper movieMapper;

    private final IdMapper<UUID> userIdMapper = new IdMapper<>();
    private final IdMapper<UUID> movieIdMapper = new IdMapper<>();

    private boolean isCsvUpdated = false;

    private RecommendationService(MovieRepository movieRepository, UserRepository userRepository, ReviewRepository reviewRepository,
                                 RecommendationEngine recommendationEngine) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.recommendationEngine = recommendationEngine;
        this.movieMapper = Mappers.getMapper(MovieMapper.class);
    }

    private static class SingletonHelper {
        private static final RecommendationService INSTANCE = new RecommendationService(
                MovieRepositoryImpl.getInstance(),
                UserRepositoryImpl.getInstance(),
                ReviewRepositoryImpl.getInstance(),
                RecommendationEngine.getInstance()
        );
    }

    public static RecommendationService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public List<MovieResponse> recommendForUser(UserResponse userResponse) {
        try {
            updateCsvIfNeeded();

            UUID userUuid = userResponse.id();
            long userId = userIdMapper.getOrAddId(userUuid);

            List<RecommendedItem> recommendations = recommendationEngine.getRecommendations(userId, 5);
            return recommendations.stream()
                    .map(rec -> movieRepository.findById(movieIdMapper.getOriginal(rec.getItemID()))
                            .map(movieMapper::toSummaryResponse)
                            .orElseThrow(() -> new MovieNotFoundException("Movie not found for ID: " + rec.getItemID())))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error generating recommendations: " + e.getMessage(), e);
        }
    }

    public void updateCsvIfNeeded() {
        //TODO update csv file once a day (at night for example)
        if (!isCsvUpdated) {
            exportDataForRecommendations();
            isCsvUpdated = true;
        }
    }

    private void exportDataForRecommendations() {
        List<String> csvData = prepareCsvData();
        CsvExporter.exportToCsv(csvData, Constants.REVIEWS_CSV_FILE_PATH);
    }

    private List<String> prepareCsvData() {
        List<String> csvData = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            long userId = userIdMapper.getOrAddId(user.getId());
            for (Review review : reviewRepository.findByUserId(user.getId())) {
                long movieId = movieIdMapper.getOrAddId(review.getMovie().getId());
                csvData.add(userId + "," + movieId + "," + review.getRate());
            }
        }
        return csvData;
    }
}
