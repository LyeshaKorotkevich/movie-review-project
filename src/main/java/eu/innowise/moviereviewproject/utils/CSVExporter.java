package eu.innowise.moviereviewproject.utils;

import eu.innowise.moviereviewproject.model.Review;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CSVExporter {

    public static void exportToCsv(List<Review> reviews, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Review review : reviews) {
                long userId = uuidToLong(review.getUser().getId());
                long movieId = uuidToLong(review.getMovie().getId());
                int rating = review.getRate();

                writer.write(userId + "," + movieId + "," + rating + "\n");
            }
        }
    }

    private static long uuidToLong(UUID uuid) {
        return uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
    }
}
