CREATE TABLE person (
                        id UUID PRIMARY KEY,
                        external_id BIGINT NOT NULL UNIQUE,
                        name VARCHAR(255),
                        en_name VARCHAR(255),
                        profession VARCHAR(255),
                        photo_url VARCHAR(500)
);

CREATE TABLE movie_person (
                              movie_id UUID NOT NULL,
                              person_id UUID NOT NULL,
                              PRIMARY KEY (movie_id, person_id),
                              CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE,
                              CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE
);