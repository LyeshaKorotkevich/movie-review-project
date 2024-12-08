package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class ReviewRepositoryImpl implements ReviewRepository {

    @Override
    public Review save(Review entity) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void update(Review entity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(entity);
            return null;
        });
    }

    @Override
    public Optional<Review> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(entityManager.find(Review.class, id));
        } catch (Exception e) {
            log.error("Error occurred while finding review by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding review by ID", e);
        }
    }

    @Override
    public List<Review> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT r FROM Review r", Review.class).getResultList();
        } catch (Exception e) {
            log.error("Error occurred while fetching all review", e);
            throw new RuntimeException("Error occurred while fetching all reviews", e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        executeInTransaction(entityManager -> {
            Review review = entityManager.find(Review.class, id);
            if (review != null) {
                entityManager.remove(review);
                log.info("Review deleted successfully with ID: {}", id);
            } else {
                log.warn("Review with ID: {} not found for deletion", id);
            }
            return null;
        });
    }
}
