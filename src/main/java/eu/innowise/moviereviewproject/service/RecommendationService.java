package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.exceptions.movie.MovieNotFoundException;
import eu.innowise.moviereviewproject.mapper.MovieMapper;
import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.mapstruct.factory.Mappers;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RecommendationService {

    private static final String REVIEWS_CSV_FILE_PATH = "data/ratings.csv";

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    private final Map<UUID, Long> userUuidToLongMap = new HashMap<>();
    private final Map<UUID, Long> movieUuidToLongMap = new HashMap<>();
    private final Map<Long, UUID> longToMovieUuidMap = new HashMap<>();

    private long currentUserId = 1L;
    private long currentMovieId = 1L;

    public RecommendationService(UserRepository userRepository, ReviewRepository reviewRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.movieMapper = Mappers.getMapper(MovieMapper.class);
    }

    public List<MovieResponse> recommendForUser(UserResponse userResponse) {
        log.info("Starting recommendations for user: {}", userResponse.id());
        try {
            exportDataToCSV();

            UUID userUuid = userResponse.id();
            long userId = getUserLongId(userUuid);
            int numberOfRecommendations = 5;

            List<RecommendedItem> recommendations = recommendMovies(userId, numberOfRecommendations);

            log.info("Recommendations successfully generated for user: {}", userUuid);
            log.info(String.valueOf(recommendations.size()));
            return recommendations.stream()
                    .map(rec -> movieRepository.findById(getMovieUUID(rec.getItemID()))
                            .map(movieMapper::toSummaryResponse)
                            .orElseThrow(() -> {
                                log.error("Movie not found for ID: {}", rec.getItemID());
                                return new MovieNotFoundException("Movie not found for ID: " + rec.getItemID());
                            })
                    )
                    .toList();
        } catch (Exception e) {
            log.error("Error generating recommendations for user: {}", userResponse.id(), e);
            throw new RuntimeException("Error generating recommendations: " + e.getMessage(), e);
        }
    }

    private void exportDataToCSV() {
        log.info("Exporting data to CSV at path: {}", REVIEWS_CSV_FILE_PATH);
        try {
            File csvFile = new File(REVIEWS_CSV_FILE_PATH);
            File parentDir = csvFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.info("Created missing directory: {}", parentDir.getAbsolutePath());
                } else {
                    throw new RuntimeException("Failed to create directory: " + parentDir.getAbsolutePath());
                }
            }

            try (FileWriter writer = new FileWriter(csvFile)) {
                List<User> users = userRepository.findAll();
                for (User user : users) {
                    long userId = getUserLongId(user.getId());
                    List<Review> reviews = reviewRepository.findByUserId(user.getId());
                    for (Review review : reviews) {
                        long movieId = getMovieLongId(review.getMovie().getId());
                        writer.append(String.valueOf(userId))
                                .append(",")
                                .append(String.valueOf(movieId))
                                .append(",")
                                .append(String.valueOf(review.getRate()))
                                .append("\n");
                    }
                }
                log.info("Data successfully exported to CSV");
            }
        } catch (Exception e) {
            log.error("Error exporting data to CSV", e);
            throw new RuntimeException("Error exporting data to CSV: " + e.getMessage(), e);
        }
    }

    private List<RecommendedItem> recommendMovies(long userId, int numberOfRecommendations) {
        log.info("Generating recommendations for user ID: {}", userId);
        try {
            DataModel model = new FileDataModel(new File(REVIEWS_CSV_FILE_PATH));

            UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
            log.info("Neighbors for user ID {}: {}", userId, Arrays.toString(neighborhood.getUserNeighborhood(userId)));
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            log.info("Successfully created recommender for user ID: {}", userId);
            return recommender.recommend(userId, numberOfRecommendations);

        } catch (Exception e) {
            log.error("Error generating recommendations for user ID: {}", userId, e);
            throw new RuntimeException("Error generating recommendations: " + e.getMessage(), e);
        }
    }

    private long getUserLongId(UUID uuid) {
        return userUuidToLongMap.computeIfAbsent(uuid, k -> currentUserId++);
    }

    private long getMovieLongId(UUID uuid) {
        return movieUuidToLongMap.computeIfAbsent(uuid, k -> {
            long id = currentMovieId++;
            longToMovieUuidMap.putIfAbsent(id, uuid);
            return id;
        });
    }

    private UUID getMovieUUID(long id) {
        return longToMovieUuidMap.get(id);
    }
}
