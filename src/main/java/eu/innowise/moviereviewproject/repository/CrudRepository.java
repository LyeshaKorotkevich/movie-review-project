package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface CrudRepository<T, K> {

    T save(T entity);

    void update(T entity);

    Optional<T> findById(K id);

    List<T> findAll();

    void deleteById(K id);

    default <R> R executeInTransaction(Function<EntityManager, R> action) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            R result = action.apply(entityManager);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
