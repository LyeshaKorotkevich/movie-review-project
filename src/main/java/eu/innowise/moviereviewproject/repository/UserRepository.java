package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User> {
    boolean existsById(UUID id);

    Optional<User> findByUsername(String username);
}
