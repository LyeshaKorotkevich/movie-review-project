package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class AbstractHibernateDao<T, K> implements CrudRepository<T, K> {

    private final Class<T> entityClass;

    protected AbstractHibernateDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T save(T entity) {
        return executeInTransactionWithReturn(entityManager -> {
            entityManager.persist(entity);
            log.info("Entity saved: {}", entity);
            return entity;
        });
    }

    @Override
    public void update(T entity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(entity);
            log.info("Entity updated: {}", entity);
        });
    }

    @Override
    public Optional<T> findById(K id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(entityManager.find(entityClass, id));
        } catch (Exception e) {
            log.error("Error occurred while finding complaint by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding complaint by ID", e);
        }
    }

    @Override
    public void deleteById(K id) {
        executeInTransaction(entityManager -> {
            T entity = entityManager.find(entityClass, id);
            if (entity != null) {
                entityManager.remove(entity);
                log.info("Entity deleted: {}", entity);
            } else {
                log.warn("Entity with ID {} not found for deletion", id);
            }
        });
    }

    protected void executeInTransaction(Consumer<EntityManager> action) {
        EntityTransaction transaction = null;

        try (EntityManager entityManager = JpaUtil.getEntityManager();) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Transaction failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    protected <R> R executeInTransactionWithReturn(Function<EntityManager, R> action) {
        EntityTransaction transaction = null;

        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            R result = action.apply(entityManager);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Transaction failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
