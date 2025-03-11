package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Report;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
public class ReportDto {

    @NotBlank
    @Size(min = 10, max = 1000)
    private String comment;
    @NotBlank
    private Long reportedId;
    @NotBlank
    private Long reporterId;

    public Report fromDtoToEntity(ReportDto reportDto) {
        Report report = new Report();
        UserService userService = new UserService();

        User reporter = userService.getUserById(reportDto.getReporterId());
        User reported = userService.getUserById(reportDto.getReportedId());

        report.setComment(reportDto.getComment());
        report.setReporter(reporter);
        report.setReported(reported);

        return report;
    }
}
