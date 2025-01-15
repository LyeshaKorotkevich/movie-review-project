package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends CrudRepository<Person, UUID> {

    Optional<Person> findByExternalId(Long externalId);

    List<Person> findAll();
}
