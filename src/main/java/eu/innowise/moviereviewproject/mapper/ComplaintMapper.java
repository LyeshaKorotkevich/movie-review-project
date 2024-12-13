package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.request.ComplaintRequest;
import eu.innowise.moviereviewproject.dto.response.ComplaintResponse;
import eu.innowise.moviereviewproject.model.Complaint;
import org.mapstruct.Mapper;

@Mapper
public interface ComplaintMapper {

    Complaint toComplaint(ComplaintRequest request);

    ComplaintResponse toResponse(Complaint complaint);
}
