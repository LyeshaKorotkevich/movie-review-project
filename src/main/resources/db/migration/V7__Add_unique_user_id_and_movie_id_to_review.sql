ALTER TABLE review
    ADD CONSTRAINT unique_movie_user UNIQUE (movie_id, user_id);
