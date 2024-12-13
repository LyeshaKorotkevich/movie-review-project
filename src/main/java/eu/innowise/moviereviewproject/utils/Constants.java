package eu.innowise.moviereviewproject.utils;

public final class Constants {
    private Constants() {
    }

    public static final String REVIEWS_CSV_FILE_PATH = "data/ratings.csv";

    public static final String API_URL = "https://api.kinopoisk.dev/v1.4/movie";
    public static final String API_KEY = "2GM61NX-6RV4E74-KGHP0K9-XEB3D4V";

    public static final int MOVIE_PAGE_SIZE = 12;
    public static final int REVIEW_PAGE_SIZE = 10;
    public static final int WATCHLIST_PAGE_SIZE = 20;

    // urls
    public static final String AUTH_LOGIN_URL = "/auth/login";
    public static final String MOVIES_URL = "/movies";
    public static final String COMPLAINTS_URL = "/complaints";
    public static final String WATCHLIST_URL = "/watchlist";

    // queries
    public static final String SELECT_COMPLAINTS = "SELECT c FROM Complaint c";
    public static final String SELECT_COMPLAINTS_BY_STATUS = "SELECT c FROM Complaint c WHERE c.status = :status";

    public static final String SELECT_GENRES = "SELECT g FROM Genre g";
    public static final String SELECT_GENRES_BY_NAME = "SELECT g FROM Genre g WHERE g.name = :name";

    public static final String SELECT_REVIEWS = "SELECT r FROM Review r";

    public static final String SELECT_USER_BY_ID = "SELECT u FROM User u WHERE u.id = :id";
    public static final String SELECT_USER_BY_USERNAME = "SELECT u FROM User u WHERE u.username = :username";
    public static final String SELECT_USERS = "SELECT u FROM User u";
}
