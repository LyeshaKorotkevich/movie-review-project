package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.request.ComplaintRequest;
import eu.innowise.moviereviewproject.dto.response.ComplaintResponse;
import eu.innowise.moviereviewproject.exceptions.EntityNotFoundException;
import eu.innowise.moviereviewproject.mapper.ComplaintMapper;
import eu.innowise.moviereviewproject.model.Complaint;
import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.model.enums.ComplaintStatus;
import eu.innowise.moviereviewproject.repository.ComplaintRepository;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.repository.impl.ComplaintRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.ReviewRepositoryImpl;
import eu.innowise.moviereviewproject.repository.impl.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.COMPLAINT_NOT_FOUND_BY_ID;
import static eu.innowise.moviereviewproject.utils.Constants.REVIEW_NOT_FOUND_BY_ID;
import static eu.innowise.moviereviewproject.utils.Constants.USER_NOT_FOUND_BY_ID;

@Slf4j
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ComplaintMapper complaintMapper;

    private ComplaintService(ComplaintRepository complaintRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.complaintMapper = Mappers.getMapper(ComplaintMapper.class);
    }

    private static class SingletonHelper {
        private static final ComplaintService INSTANCE = new ComplaintService(
                ComplaintRepositoryImpl.getInstance(),
                UserRepositoryImpl.getInstance(),
                ReviewRepositoryImpl.getInstance()
        );
    }

    public static ComplaintService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void saveComplaint(ComplaintRequest complaintRequest) {
        User user = userRepository.findById(complaintRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, complaintRequest.userId())));

        Review review = reviewRepository.findById(complaintRequest.reviewId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(REVIEW_NOT_FOUND_BY_ID, complaintRequest.reviewId())));

        Complaint complaint = complaintMapper.toComplaint(complaintRequest);
        complaint.setUser(user);
        complaint.setReview(review);
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        complaintRepository.save(complaint);

        log.info("Complaint saved successfully.");
    }

    public List<ComplaintResponse> getComplaintsByStatus(String status) {
        List<ComplaintResponse> complaints = complaintRepository.findByStatus(status)
                .stream()
                .map(complaintMapper::toResponse)
                .toList();

        log.info("{} complaints with status '{}' loaded from the database.", complaints.size(), status);
        return complaints;
    }

    public void updateComplaintStatus(UUID complaintId, String status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(COMPLAINT_NOT_FOUND_BY_ID, complaintId)));

        complaint.setStatus(Enum.valueOf(ComplaintStatus.class, status));
        complaintRepository.update(complaint);

        log.info("Complaint status updated to '{}' for complaint ID: {}", status, complaintId);
    }

    public void deleteComplaint(UUID complaintId) {
        complaintRepository.deleteById(complaintId);
        log.info("Complaint with ID: {} deleted successfully.", complaintId);
    }
}
