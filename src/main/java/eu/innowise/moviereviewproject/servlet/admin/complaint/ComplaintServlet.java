package eu.innowise.moviereviewproject.servlet.admin.complaint;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.service.ComplaintService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/complaints")
public class ComplaintServlet extends HttpServlet {

    private ComplaintService complaintService;

    @Override
    public void init() throws ServletException {
        complaintService = ApplicationConfig.getComplaintService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/complaints.jsp").forward(req, resp);
    }
}
