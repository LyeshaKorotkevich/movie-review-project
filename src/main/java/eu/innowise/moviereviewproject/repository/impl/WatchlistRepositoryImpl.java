package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Watchlist;
import eu.innowise.moviereviewproject.repository.AbstractHibernateDao;
import eu.innowise.moviereviewproject.repository.WatchlistRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.WATCHLIST_PAGE_SIZE;

@Slf4j
public class WatchlistRepositoryImpl extends AbstractHibernateDao<Watchlist, UUID> implements WatchlistRepository {

    private WatchlistRepositoryImpl() {
        super(Watchlist.class);
    }

    private static class SingletonHelper {
        private static final WatchlistRepositoryImpl INSTANCE = new WatchlistRepositoryImpl();
    }

    public static WatchlistRepositoryImpl getInstance() {
        return SingletonHelper.INSTANCE;
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

    @Override
    public boolean existsByUserIdAndMovieId(UUID userId, UUID movieId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return (Boolean) entityManager
                    .createNativeQuery("SELECT EXISTS (SELECT 1 FROM watchlist WHERE user_id = :userId AND movie_id = :movieId)")
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .getSingleResult();
        } catch (Exception e) {
            log.error("Error occurred while checking if watchlist exists for user: {} and movie: {}", userId, movieId);
            throw new RuntimeException("Error occurred while checking if watchlist exists", e);
        }
    }
}
