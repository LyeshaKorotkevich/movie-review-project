CREATE TABLE Genre (
                       id SERIAL,
                       name VARCHAR(100) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE Movie (
                       id UUID,
                       external_id BIGINT NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(2000),
                       poster_url VARCHAR(400),
                       rate_count INTEGER NOT NULL,
                       rate_sum INTEGER NOT NULL,
                       release_year INTEGER,
                       PRIMARY KEY (id)
);

CREATE TABLE movie_genre (
                             movie_id UUID NOT NULL,
                             genre_id INTEGER NOT NULL,
                             PRIMARY KEY (movie_id, genre_id)
);

CREATE TABLE users (
                       id UUID,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       created_at DATE,
                       user_role VARCHAR(30) NOT NULL CHECK (user_role IN ('USER', 'ADMIN')),
                       PRIMARY KEY (id)
);

CREATE TABLE Watchlist (
                           id UUID,
                           added_at TIMESTAMP(6) NOT NULL,
                           is_watched BOOLEAN NOT NULL,
                           movie_id UUID NOT NULL,
                           user_id UUID NOT NULL,
                           PRIMARY KEY (id)
);

CREATE TABLE Review (
                        id UUID,
                        content VARCHAR(3000),
                        rate INTEGER NOT NULL,
                        created_at TIMESTAMP(6),
                        updated_at TIMESTAMP(6),
                        movie_id UUID NOT NULL,
                        user_id UUID NOT NULL,
                        PRIMARY KEY (id)
);