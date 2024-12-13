package eu.innowise.moviereviewproject.service.recommendation;

import eu.innowise.moviereviewproject.utils.Constants;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.List;

public class RecommendationEngine {

    public List<RecommendedItem> getRecommendations(long userId, int numberOfRecommendations) {
        try {
            DataModel model = new FileDataModel(new File(Constants.REVIEWS_CSV_FILE_PATH));
            UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);

            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            return recommender.recommend(userId, numberOfRecommendations);
        } catch (Exception e) {
            throw new RuntimeException("Error generating recommendations: " + e.getMessage(), e);
        }
    }
}
