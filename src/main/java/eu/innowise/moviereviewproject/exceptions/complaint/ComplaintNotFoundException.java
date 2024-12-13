package eu.innowise.moviereviewproject.exceptions.complaint;

public class ComplaintNotFoundException extends RuntimeException {
    public ComplaintNotFoundException(String message) {
        super(message);
    }
}
