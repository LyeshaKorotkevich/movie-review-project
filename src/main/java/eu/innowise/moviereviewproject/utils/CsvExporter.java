package eu.innowise.moviereviewproject.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public final class CsvExporter {

    private CsvExporter() {
    }

    public static void exportToCsv(List<String> data, String filePath) {
        try {
            File csvFile = new File(filePath);
            File parentDir = csvFile.getParentFile();
            if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + parentDir.getAbsolutePath());
            }

            try (FileWriter writer = new FileWriter(csvFile)) {
                for (String line : data) {
                    writer.write(line + "\n");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error exporting data to CSV: " + e.getMessage(), e);
        }
    }
}
