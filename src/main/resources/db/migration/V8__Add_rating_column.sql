ALTER TABLE movie ADD COLUMN rating DOUBLE PRECISION DEFAULT 0;

UPDATE movie
    SET rating = rate_sum * 1.0 / NULLIF(rate_count, 0);

CREATE OR REPLACE FUNCTION update_rating_after_insert()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE movie
    SET
        rate_sum = rate_sum + NEW.rate,
        rate_count = rate_count + 1,
        rating = (rate_sum + NEW.rate) * 1.0 / NULLIF(rate_count + 1, 0)
    WHERE id = NEW.movie_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_rating_after_delete()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE movie
    SET
        rate_sum = rate_sum - OLD.rate,
        rate_count = rate_count - 1,
        rating = (rate_sum - OLD.rate) * 1.0 / NULLIF(rate_count - 1, 0)
    WHERE id = OLD.movie_id;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_rating_after_update()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE movie
    SET
        rate_sum = rate_sum - OLD.rate + NEW.rate,
        rating = (rate_sum - OLD.rate + NEW.rate) * 1.0 / NULLIF(rate_count, 0)
    WHERE id = NEW.movie_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
