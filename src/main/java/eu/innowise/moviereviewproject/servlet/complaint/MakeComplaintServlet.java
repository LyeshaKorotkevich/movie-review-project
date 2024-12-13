package eu.innowise.moviereviewproject.servlet.complaint;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.request.ComplaintRequest;
import eu.innowise.moviereviewproject.service.ComplaintService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.COMPLAINTS_URL;
import static eu.innowise.moviereviewproject.utils.ServletsUtil.getComplaintRequest;

@Slf4j
@WebServlet("/make-complaint")
public class MakeComplaintServlet extends HttpServlet {

    private ComplaintService complaintService;

    @Override
    public void init() throws ServletException {
        complaintService = ApplicationConfig.getComplaintService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ComplaintRequest complaintRequest = getComplaintRequest(req);
            complaintService.saveComplaint(complaintRequest);

            resp.sendRedirect(req.getContextPath() + COMPLAINTS_URL);
        } catch (Exception e) {
            log.error("Error processing complaint: {}", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/complaints.jsp").forward(req, resp);
        }
    }
}
