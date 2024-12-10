CREATE OR REPLACE FUNCTION update_rating_after_insert()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE movie
    SET
        rate_sum = rate_sum + NEW.rate,
        rate_count = rate_count + 1
    WHERE id = NEW.movie_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_review_insert
    AFTER INSERT ON review
    FOR EACH ROW
EXECUTE FUNCTION update_rating_after_insert();


CREATE OR REPLACE FUNCTION update_rating_after_delete()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE movie
    SET
        rate_sum = rate_sum - OLD.rate,
        rate_count = rate_count - 1
    WHERE id = OLD.movie_id;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_review_delete
    AFTER DELETE ON review
    FOR EACH ROW
EXECUTE FUNCTION update_rating_after_delete();


CREATE OR REPLACE FUNCTION update_rating_after_update()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE movie
    SET
        rate_sum = rate_sum - OLD.rate + NEW.rate
    WHERE id = NEW.movie_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_review_update
    AFTER UPDATE ON review
    FOR EACH ROW
EXECUTE FUNCTION update_rating_after_update();
