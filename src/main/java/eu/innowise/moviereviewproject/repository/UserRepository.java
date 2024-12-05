package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
