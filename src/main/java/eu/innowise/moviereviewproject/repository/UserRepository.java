package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    boolean existsById(UUID id);

    Optional<User> findByUsername(String username);

    List<User> findAll();
}
