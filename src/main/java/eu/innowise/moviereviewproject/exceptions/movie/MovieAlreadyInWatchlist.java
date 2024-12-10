package eu.innowise.moviereviewproject.exceptions.movie;

public class MovieAlreadyInWatchlist extends RuntimeException {
    public MovieAlreadyInWatchlist(String message) {
        super(message);
    }
}
