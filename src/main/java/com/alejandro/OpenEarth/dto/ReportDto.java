package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Report;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.UserService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class ReportDto {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    private String comment;
    private Long reportedId;
    private Long reporterId;

    public Report fromDtoToEntity(ReportDto reportDto) {
        Report report = new Report();
        try{
            User reporter = userService.getUserById(reportDto.getReporterId());
            User reported = userService.getUserById(reportDto.getReportedId());
            report.setComment(reportDto.getComment());
            report.setReporter(reporter);
            report.setReported(reported);
            return report;
        }catch (RuntimeException rtex){
            throw new RuntimeException(rtex.getMessage());
        }

    }
}
