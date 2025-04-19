package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.entity.Report;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.repository.ReportRepository;
import com.alejandro.OpenEarth.repository.UserRepository;
import com.alejandro.OpenEarth.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("reportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    @Qualifier("reportRepository")
    private ReportRepository reportRepository;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Override
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    @Override
    public Report getReportById(Long id) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isEmpty())
            throw new RuntimeException("Report not found");
        return report.get();
    }

    @Override
    public void deleteReportById(Long id) {
        try{
            Report report = this.getReportById(id);
            reportRepository.delete(report);
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Report fromDtoToEntity(long reportedId, User reporter, String comment) {
        Report report = new Report();
        User reported = userService.getUserById(reportedId);

        report.setComment(comment);
        report.setReporter(reporter);
        report.setReported(reported);
        report.setCreatedAt(LocalDateTime.now());

        return createReport(report);
    }
}
