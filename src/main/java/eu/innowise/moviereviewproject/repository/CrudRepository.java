package eu.innowise.moviereviewproject.repository;

import java.util.Optional;

public interface CrudRepository<T, K> {

    T save(T entity);

    void update(T entity);

    Optional<T> findById(K id);

    void deleteById(K id);
}
