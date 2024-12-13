package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Complaint;

import java.util.List;
import java.util.UUID;

public interface ComplaintRepository extends CrudRepository<Complaint, UUID> {

    List<Complaint> findByStatus(String status);
}
