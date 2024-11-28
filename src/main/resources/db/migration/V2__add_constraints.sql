ALTER TABLE users
    ADD CONSTRAINT UK_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT UK_username UNIQUE (username);

ALTER TABLE movie_genre
    ADD CONSTRAINT FK_movie_genre_genre
        FOREIGN KEY (genre_id) REFERENCES Genre;

ALTER TABLE movie_genre
    ADD CONSTRAINT FK_movie_genre_movie
        FOREIGN KEY (movie_id) REFERENCES Movie;

ALTER TABLE Review
    ADD CONSTRAINT FK_review_movie
        FOREIGN KEY (movie_id) REFERENCES Movie;

ALTER TABLE Review
    ADD CONSTRAINT FK_review_user
        FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE Watchlist
    ADD CONSTRAINT FK_watchlist_movie
        FOREIGN KEY (movie_id) REFERENCES Movie;

ALTER TABLE Watchlist
    ADD CONSTRAINT FK_watchlist_user
        FOREIGN KEY (user_id) REFERENCES users;
