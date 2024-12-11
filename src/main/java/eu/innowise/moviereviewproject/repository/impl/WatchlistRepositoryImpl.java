package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Watchlist;
import eu.innowise.moviereviewproject.repository.WatchlistRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.WATCHLIST_PAGE_SIZE;

@Slf4j
public class WatchlistRepositoryImpl implements WatchlistRepository {

    @Override
    public Watchlist save(Watchlist entity) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void update(Watchlist entity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(entity);
            return null;
        });
    }

    @Override
    public Optional<Watchlist> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT w FROM Watchlist w WHERE w.id = :id";
            Watchlist watchlist = entityManager.createQuery(jpql, Watchlist.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
            return Optional.ofNullable(watchlist);
        } catch (Exception e) {
            log.error("Error occurred while finding watchlist by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding watchlist", e);
        }
    }

    @Override
    public List<Watchlist> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT w FROM Watchlist w", Watchlist.class).getResultList();
        }
    }

    @Override
    public void deleteById(UUID id) {
        executeInTransaction(entityManager -> {
            Watchlist watchlist = entityManager.find(Watchlist.class, id);
            if (watchlist != null) {
                entityManager.remove(watchlist);
                log.info("Watchlist deleted successfully with ID: {}", id);
            } else {
                log.warn("Watchlist with ID: {} not found for deletion", id);
            }
            return null;
        });
    }

    @Override
    public Optional<Watchlist> findByUserIdAndMovieId(UUID userId, UUID movieId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT w FROM Watchlist w WHERE w.user.id = :userId AND w.movie.id = :movieId";
            Watchlist watchlist = entityManager.createQuery(jpql, Watchlist.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .getSingleResultOrNull();
            return Optional.ofNullable(watchlist);
        } catch (Exception e) {
            log.error("Error occurred while finding movie {} in watchlist for user: {}", movieId, userId);
            throw new RuntimeException("Error occurred while finding movie", e);
        }
    }

    @Override
    public List<Watchlist> findByUserId(int page, UUID userId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT w FROM Watchlist w LEFT JOIN FETCH w.movie WHERE w.user.id = :userId ORDER BY w.addedAt DESC";
            int firstResult = (page - 1) * WATCHLIST_PAGE_SIZE;
            return entityManager.createQuery(jpql, Watchlist.class)
                    .setParameter("userId", userId)
                    .setFirstResult(firstResult)
                    .setMaxResults(WATCHLIST_PAGE_SIZE)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error occurred while finding watchlist for user: {}", userId);
            throw new RuntimeException("Error occurred while finding watchlist", e);
        }
    }
}
