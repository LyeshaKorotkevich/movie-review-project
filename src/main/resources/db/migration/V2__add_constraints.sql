ALTER TABLE Movie ADD CONSTRAINT UK_movie_external_id UNIQUE (external_id);
ALTER TABLE users ADD CONSTRAINT UK_users_email UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT UK_users_username UNIQUE (username);

ALTER TABLE movie_genre
    ADD CONSTRAINT FK_movie_genre_movie_id FOREIGN KEY (movie_id) REFERENCES Movie (id),
    ADD CONSTRAINT FK_movie_genre_genre_id FOREIGN KEY (genre_id) REFERENCES Genre (id);

ALTER TABLE Review
    ADD CONSTRAINT FK_review_movie_id FOREIGN KEY (movie_id) REFERENCES Movie (id),
    ADD CONSTRAINT FK_review_user_id FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE Watchlist
    ADD CONSTRAINT FK_watchlist_movie_id FOREIGN KEY (movie_id) REFERENCES Movie (id),
    ADD CONSTRAINT FK_watchlist_user_id FOREIGN KEY (user_id) REFERENCES users (id);